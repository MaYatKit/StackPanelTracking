package com.framecad.plum.view.base

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.framecad.plum.R
import com.framecad.plum.utils.LoadingDialogHelper
import com.framecad.plum.utils.WarningDialogHelper
import com.framecad.plum.view.login.LoginActivity
import com.framecad.plum.view.login.LoginActivity.Companion.resultCodeTokenExpiry
import com.framecad.plum.view.login.LoginActivity.Companion.sStatusExtras
import com.framecad.plum.viewmodel.base.BaseViewModel
import java.lang.ref.WeakReference

open class BaseActivity : AppCompatActivity() {

    var mLoadingDialog: Dialog? = null

    open val baseViewModel: BaseViewModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subscribeUI()
    }

    override fun onStart() {
        super.onStart()
    }


    fun startLoad() {
        if (mLoadingDialog == null) {
            mLoadingDialog = LoadingDialogHelper.create(this)
        }
    }


    fun stopLoad() {
        if (mLoadingDialog != null) {
            LoadingDialogHelper.close(mLoadingDialog)
            mLoadingDialog = null
        }
    }


    private fun subscribeUI() {
        baseViewModel?.loginExpires?.observe(this, {
            if (it) {
                val intent = Intent(this, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.putExtra(sStatusExtras, resultCodeTokenExpiry)
                startActivity(intent)
                finish()
            }
        })

        baseViewModel?.errorResponseMsg?.observe(this, {
            stopLoad()
            if (it.isNotEmpty()) {
                WarningDialogHelper.create(this, it)
            } else {
                WarningDialogHelper.create(this, getString(R.string.server_error_msg))
            }
        })

    }


}