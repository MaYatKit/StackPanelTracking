package com.framecad.plum.utils

import android.app.Dialog
import android.content.Context
import com.framecad.plum.R
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object WarningDialogHelper {

    fun create(context: Context, msg: String, callback: (() -> Unit)? = null): Dialog {

        return MaterialAlertDialogBuilder(context, R.style.AlertDialogTheme)
            .setTitle(msg)
            .setPositiveButton("OK") { _, _ ->
                if (callback != null) {
                    callback()
                }
            }.show()
    }
}