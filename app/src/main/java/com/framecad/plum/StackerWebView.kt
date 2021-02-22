package com.framecad.plum

import android.content.Context
import android.os.Build
import android.webkit.*

class StackerWebView private constructor(context: Context) : WebView(context) {

    var is3dRenderingNotSupported = false

    companion object {
        @Volatile
        private var INSTANCE: StackerWebView? = null

        @Synchronized
        fun getInstance(context: Context): StackerWebView {
            return INSTANCE ?: run {
                val stackerWebView = StackerWebView(context)
                stackerWebView.setInitialScale(0)
                stackerWebView.settings.javaScriptEnabled = true
                stackerWebView.settings.builtInZoomControls = true
                stackerWebView.settings.displayZoomControls = false
                stackerWebView.settings.domStorageEnabled = true
                if ((Build.MODEL.contains("Moto") || Build.MODEL.contains("moto")) && Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
                    stackerWebView.is3dRenderingNotSupported = true
                } else {
                    stackerWebView.loadUrl(BuildConfig.STACKER_URL)
                }
                INSTANCE = stackerWebView
                return stackerWebView
            }
        }
    }

    @Synchronized
    fun releaseInstance() {
        INSTANCE = null
    }
}