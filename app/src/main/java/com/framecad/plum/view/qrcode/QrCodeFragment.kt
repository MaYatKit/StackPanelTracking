package com.framecad.plum.view.qrcode

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.camera.core.Preview
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.framecad.plum.R
import com.framecad.plum.databinding.FragmentQrcodeBinding
import com.framecad.plum.utils.PreferenceUtils
import com.framecad.plum.view.base.BaseActivity
import com.framecad.plum.view.base.BaseFragment
import com.framecad.plum.viewmodel.base.BaseViewModel
import com.framecad.plum.viewmodel.qrcode.QrCodeViewModel
import com.google.mlkit.vision.barcode.Barcode
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_qrcode.*
import java.util.concurrent.ExecutorService
import javax.inject.Inject


@AndroidEntryPoint
class QrCodeFragment : BaseFragment() {


    @Inject
    lateinit var preferenceUtils: PreferenceUtils

    private val qrCodeViewModel: QrCodeViewModel by lazy {
        ViewModelProvider(this).get(QrCodeViewModel::class.java)
    }

    override val baseViewModel: BaseViewModel by lazy {
        qrCodeViewModel
    }

    private lateinit var cameraProviderFuture: ExecutorService

    private val barcodeScannerOptions = BarcodeScannerOptions.Builder()
        .setBarcodeFormats(
            Barcode.FORMAT_QR_CODE
        ).build()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val binding = DataBindingUtil.inflate<FragmentQrcodeBinding>(
            inflater,
            R.layout.fragment_qrcode,
            container,
            false
        )

        binding.viewModel = qrCodeViewModel
        binding.lifecycleOwner = this

        subscribeCamera()
        return binding.root
    }


    override fun onDestroy() {
        super.onDestroy()
        qrCodeViewModel.destroyCamera()
    }


    override fun onPause() {
        super.onPause()
        qrCodeViewModel.pauseCamera()
    }

    override fun onResume() {
        super.onResume()
        qrCodeViewModel.resumeCamera()
    }


    private fun subscribeCamera() {
        qrCodeViewModel.errorResponseMsg.observe(viewLifecycleOwner,{
            qrCodeViewModel.resumeCamera()
        })

        qrCodeViewModel.serverError.observe(viewLifecycleOwner,{
            qrCodeViewModel.resumeCamera()
        })

        qrCodeViewModel.barCodeResult.observe(viewLifecycleOwner, {
            (activity as BaseActivity).startLoad()
            qrCodeViewModel.getItemByValidatingCode()
        })

        qrCodeViewModel.scanItem.observe(viewLifecycleOwner) {
            val intent = Intent(context, QrCodeViewOptionActivity::class.java)
            intent.putExtra(getString(R.string.view_detail_page_item), it)
            startActivity(intent)
            (activity as BaseActivity).stopLoad()
        }

        qrCodeViewModel.cameraProvider.observe(viewLifecycleOwner) {
            // CameraProvider init finish, request camera permissions
            if (cameraPermissionsGranted()) {
                qrCodeViewModel.startCamera(true)
            } else if (qrCodeViewModel.shouldRequestCameraPermission) {
                requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
            }
        }

        qrCodeViewModel.startCamera.observe(viewLifecycleOwner) {
            val cameraProvider = qrCodeViewModel.cameraProvider.value
            if (it && cameraProvider != null) {

                // Must generate preview every time
                val previewBuilder = Preview.Builder()
                preferenceUtils.getCameraXTargetResolution()?.let { size ->
                    previewBuilder.setTargetResolution(size)
                }
                val preview = previewBuilder.build()
                preview.setSurfaceProvider(preview_view.createSurfaceProvider())

                try {
                    // Must unbind all use cases before starting
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        this, qrCodeViewModel.cameraSelector, preview, qrCodeViewModel.imageAnalysis
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            } else if (!qrCodeViewModel.shouldRequestCameraPermission) {
                Toast.makeText(
                    requireContext(),
                    getString(R.string.warn_camera_disable),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults:
        IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (cameraPermissionsGranted()) {
                qrCodeViewModel.startCamera(true)
            } else {
                qrCodeViewModel.shouldRequestCameraPermission = false
//                    shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)
                qrCodeViewModel.startCamera(false)
            }
        }
    }


    companion object {
        const val TAG = "QrCodeFragment"
        private const val REQUEST_CODE_PERMISSIONS = 10
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }


    private fun cameraPermissionsGranted() = ContextCompat.checkSelfPermission(requireContext(), REQUIRED_PERMISSIONS[0]) == PackageManager.PERMISSION_GRANTED



}