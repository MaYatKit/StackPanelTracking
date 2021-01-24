package com.framecad.plum.utils

import android.app.Dialog
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.Gravity
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import com.framecad.plum.R

object LoadingDialogHelper {

    //Set a minimum loading time to avoid flicker
    private const val minLoadingTime = 800
    var startTime: Long = 0

    fun create(context: Context, msg: String? = null): Dialog {
        val v = View.inflate(context, R.layout.dialog_loading, null)
        val layout = v.findViewById<View>(R.id.dialog_loading_view) as LinearLayout
        val tipTextView = v.findViewById<View>(R.id.tipTextView) as TextView
        if (msg == null) {
            tipTextView.text = context.getString(R.string.loading)
        } else {
            tipTextView.text = msg
        }

        val loadingDialog = Dialog(context, R.style.LoadingDialogStyle)
        loadingDialog.setCancelable(true)
        loadingDialog.setCanceledOnTouchOutside(false)
        loadingDialog.setContentView(
            layout, LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
        )
        val window = loadingDialog.window
        val lp = window!!.attributes
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT
        window.setGravity(Gravity.CENTER)
        window.attributes = lp
        window.setWindowAnimations(R.style.LoadingDialogStyle)
        loadingDialog.show()
        startTime = System.currentTimeMillis()

        return loadingDialog
    }

    fun close(loadingDialog: Dialog?) {
        val haveLoadedTime = System.currentTimeMillis() - startTime
        if (haveLoadedTime < minLoadingTime) {
            // Some time when the activity is being finish in the minLoadingTime time interval
            // there will throw a log saying activity has been leaked, but actually the
            // activity will be recycled at the end of minLoadingTime time interval.
            val looper = Looper.getMainLooper()
            val handler = Handler(looper)
            handler.postDelayed({
                if (loadingDialog?.isShowing == true) {
                    loadingDialog.dismiss()
                }
            }, minLoadingTime - haveLoadedTime)
        } else {
            if (loadingDialog?.isShowing == true) {
                loadingDialog.dismiss()
            }
        }
    }
}