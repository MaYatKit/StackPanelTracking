package com.framecad.plum.viewmodel.viewupdate

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.framecad.plum.data.ProjectsRepository
import com.framecad.plum.data.model.PanelStatusPage
import com.framecad.plum.data.model.ScanItem
import com.framecad.plum.data.model.StackStatusPage
import com.framecad.plum.data.model.StatusPage
import com.framecad.plum.data.response.PanelStatusResponse
import com.framecad.plum.data.response.StackStatusResponse
import com.framecad.plum.data.response.utils.ResponseUtils
import com.framecad.plum.view.viewupdate.ViewUpdateActivity
import com.framecad.plum.view.viewupdate.ViewUpdateActivity.Companion.REQUIRED_PERMISSIONS
import com.framecad.plum.viewmodel.base.BaseViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import com.framecad.plum.R


class ViewUpdateViewModel @ViewModelInject constructor(
    private val repository: ProjectsRepository,
    @Assisted private val savedStateHandle: SavedStateHandle,
    @ApplicationContext val context: Context
) : BaseViewModel() {

    private val updateResponseText = "response"
    private val statusObjectText = "status_object"
    private val scanItemText = "scan_item"
    private val switchText = "switch"
    private val permissionText = "permission"
    private val inInventoryText = "In Inventory"
    private val onSiteText = "On Site"
    private val currentLocationText = "currentLocation"

    val statusPageItemObject: LiveData<StatusPage>
        get() = savedStateHandle.getLiveData(statusObjectText)

    private val scanItem: LiveData<ScanItem>
        get() = savedStateHandle.getLiveData(scanItemText)

    val isUpdateSuccess: LiveData<Boolean>
        get() = savedStateHandle.getLiveData(updateResponseText)

    val switchChangeable: LiveData<Boolean>
        get() = savedStateHandle.getLiveData(switchText)


    val hasLocationPermission: LiveData<Boolean>
        get() = savedStateHandle.getLiveData(permissionText)


    val currentLocation: LiveData<Location>
        get() = savedStateHandle.getLiveData(currentLocationText)


    init {
        isUpdateSuccess.observeForever {
            if (it) {
                savedStateHandle[statusObjectText] = statusPageItemObject.value
            } else {
                Log.d("response", "response = $it")
            }
        }

    }


    suspend fun getViewStatus() {
        scanItem.value?.apply {
            val response = if (itemType == ScanItem.ScanItemType.STACK) {
                repository.getStackStatus(id.toLong())
            } else {
                repository.getPanelStatus(id.toLong())
            }


            handleResponse(response) {
                response.body()?.let { statusResponse ->
                    if (itemType == ScanItem.ScanItemType.STACK) {
                        savedStateHandle[statusObjectText] =
                            StackStatusPage(statusResponse as StackStatusResponse)
                    } else {
                        savedStateHandle[statusObjectText] =
                            PanelStatusPage(statusResponse as PanelStatusResponse)
                    }
                }
            }

        }
    }


    /**
     * Update switch status
     */
    fun setSwitch(statusText: String) {
        when (statusText) {
            context.resources.getStringArray(R.array.status_array)[2] -> savedStateHandle[switchText] =
                true
            context.resources.getStringArray(R.array.status_array)[3] -> savedStateHandle[switchText] =
                true
            context.resources.getStringArray(R.array.status_array)[4] -> savedStateHandle[switchText] =
                true
            else -> savedStateHandle[switchText] = false
        }
    }

    fun setViewItem(item: ScanItem) {
        savedStateHandle[scanItemText] = item
        viewModelScope.launch {
            getViewStatus()
        }
    }

    fun setStatusBoolean(status: Boolean) {
        statusPageItemObject.value?.setStatusBoolean(status)
    }

    fun setStatusText(text: String) {
        statusPageItemObject.value?.setStatusText(text)
    }

    fun setNoteText(note: String) {
        statusPageItemObject.value?.setStatusNote(note)
    }


    fun updateStatus() {
        viewModelScope.launch {
            val statusPage = statusPageItemObject.value
            if (statusPage?.getStatusText() == inInventoryText || statusPage?.getStatusText() == onSiteText) {
                currentLocation.value?.let {
                    statusPage.setStatusLongitude(it.longitude)
                    statusPage.setStatusLatitude(it.latitude)
                }
            }

            val response = when (statusPage) {
                is PanelStatusPage -> {
                    repository.setPanelStatus(statusPage.panelStatusResponse)
                }
                is StackStatusPage -> {
                    repository.setStackStatus(statusPage.stackStatusResponse)
                }
                else -> {
                    null
                }
            }



            response?.let {
                handleResponse(response) {
                    savedStateHandle[updateResponseText] = true
                }
            }

        }
    }

    fun setHasLocationPermission(hasLocationPermission: Boolean) {
        savedStateHandle[permissionText] = hasLocationPermission
    }

    @SuppressLint("MissingPermission")
    fun retrieveLocation(fusedLocationClient: FusedLocationProviderClient) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !cameraPermissionsGranted()) {
            savedStateHandle[permissionText] = false
        } else {
            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(result: LocationResult?) {
                    result?.let { locationResult ->
                        for (loc in locationResult.locations) {
                            savedStateHandle[currentLocationText] = loc
                            val latitude = loc.latitude
                            val longitude = loc.longitude
                            Log.d(
                                ViewUpdateActivity.TAG,
                                "latitude = $latitude, longitude = $longitude"
                            )
                        }
                    }
                }
            }
            fusedLocationClient.removeLocationUpdates(locationCallback)
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                if (location != null) {
                    savedStateHandle[currentLocationText] = location
                    val latitude = location.latitude
                    val longitude = location.longitude
                    Log.d(ViewUpdateActivity.TAG, "latitude = $latitude, longitude = $longitude")
                } else {
                    val locationRequest = LocationRequest.create()
                    locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
                    locationRequest.interval = 5000
                    fusedLocationClient.requestLocationUpdates(
                        locationRequest,
                        locationCallback,
                        null
                    )
                }
            }
        }
    }


    private fun cameraPermissionsGranted() = ContextCompat.checkSelfPermission(
        context,
        REQUIRED_PERMISSIONS[0]
    ) == PackageManager.PERMISSION_GRANTED

}