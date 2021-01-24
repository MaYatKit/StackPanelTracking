package com.framecad.plum.viewmodel.qrcode

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.framecad.plum.data.ProjectsRepository
import com.framecad.plum.data.model.ScanItem
import com.framecad.plum.utils.PreferenceUtils
import com.framecad.plum.utils.ScopedExecutor
import com.framecad.plum.viewmodel.base.BaseViewModel
import com.google.android.gms.tasks.TaskExecutors
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScanner
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.launch


class QrCodeViewModel @ViewModelInject constructor(
    private val context: Application,
    val preferenceUtils: PreferenceUtils,
    private val repository: ProjectsRepository
) :
    BaseViewModel() {

    private val _startCamera = MutableLiveData<Boolean>()

    val startCamera: LiveData<Boolean>
        get() = _startCamera

    private val _cameraProvider = MutableLiveData<ProcessCameraProvider>()


    val cameraProvider: LiveData<ProcessCameraProvider>
        get() = _cameraProvider


    private val _barCodeResult = MutableLiveData<String>()

    val barCodeResult: LiveData<String>
        get() = _barCodeResult


    private val _scanItem = MutableLiveData<ScanItem>()

    val scanItem: LiveData<ScanItem>
        get() = _scanItem


    private val executor = ScopedExecutor(TaskExecutors.MAIN_THREAD)
    private var isShutdown = false

    var shouldRequestCameraPermission = true

    lateinit var imageAnalysis: ImageAnalysis


    val cameraSelector: CameraSelector by lazy {
        CameraSelector.DEFAULT_BACK_CAMERA
    }

    private val scanner: BarcodeScanner =
        BarcodeScanning.getClient(
            BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                .build()
        )

    // ExperimentalGetImage is an annotation to suppress warning for
    // ImageProxy.getImage(), which is an experimental Api, and
    // seem we don't have other choices so far.
    // Ref: https://developer.android.com/reference/kotlin/androidx/camera/core/ExperimentalGetImage
    @ExperimentalGetImage
    private val imageAnalyzer = ImageAnalysis.Analyzer { imageProxy ->
        if (isShutdown) {
            return@Analyzer
        }
        val mediaImage = imageProxy.image
        val image = InputImage.fromMediaImage(mediaImage!!, imageProxy.imageInfo.rotationDegrees)
        val result = scanner.process(image)
            .addOnSuccessListener(executor) { barcodes: List<Barcode> ->
                for (barcode in barcodes) {
                    isShutdown = true
                    _barCodeResult.value = barcode.rawValue
                    break
                }
            }
            .addOnFailureListener(executor) { e: Exception ->
                e.printStackTrace()
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    }

    fun getItemByValidatingCode() {
        barCodeResult.value?.let {
            viewModelScope.launch {
                val response = repository.getItemByValidatingCode(it)
                handleResponse(response) {
                    response.body()?.let {
                        _scanItem.value = ScanItem(it.id, it.name, it.itemType)
                    }
                }
            }
        }
    }

    fun startCamera(needStart: Boolean) {
        _startCamera.value = needStart
    }

    fun pauseCamera() {
        isShutdown = true
        imageAnalysis.clearAnalyzer()
        _cameraProvider.value?.unbindAll()
    }


    fun destroyCamera() {
        executor.shutdown()
        isShutdown = true
        scanner.close()
    }


    fun resumeCamera() {
        isShutdown = false
        initCameraProvider()
    }


    private fun initCameraProvider() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        val builder = ImageAnalysis.Builder()
        preferenceUtils.getCameraXTargetResolution()?.let {
            builder.setTargetResolution(it)
        }
        builder.setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
        imageAnalysis = builder.build()
        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(context), imageAnalyzer)

        cameraProviderFuture.addListener({
            _cameraProvider.value = cameraProviderFuture.get()
        }, ContextCompat.getMainExecutor(context))
    }

}