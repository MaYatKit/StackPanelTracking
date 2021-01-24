package com.framecad.plum

import android.app.Application
import androidx.camera.camera2.Camera2Config
import androidx.camera.core.CameraXConfig
import dagger.Provides
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MainApplication: Application(), CameraXConfig.Provider {
    override fun getCameraXConfig(): CameraXConfig {
        return Camera2Config.defaultConfig()
    }
}