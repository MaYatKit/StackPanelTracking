package com.framecad.plum.view.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider

import com.framecad.plum.R
import com.framecad.plum.databinding.ActivityLoginBinding
import com.framecad.plum.utils.PreferenceUtils
import com.framecad.plum.utils.WarningDialogHelper
import com.framecad.plum.view.base.BaseActivity
import com.framecad.plum.view.home.*
import com.framecad.plum.viewmodel.base.BaseViewModel
import com.framecad.plum.viewmodel.login.LoginViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class LoginActivity : BaseActivity() {

    private val loginViewModel: LoginViewModel by lazy {
        ViewModelProvider(this).get(LoginViewModel::class.java)
    }


    override val baseViewModel: BaseViewModel? by lazy {
        loginViewModel
    }

    private lateinit var binding: ActivityLoginBinding

    @Inject
    lateinit var preferenceUtils: PreferenceUtils


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.lifecycleOwner = this
        initView()
        substanceUI()
        startLoad()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let {
            val statusCode = it.getIntExtra(sStatusExtras, 0)
            if (statusCode != 0) {
                dealWithStatus(statusCode)
            }
        }
    }


    private fun initView() {
        binding.setForgotClickListener {
            val intent = Intent(this, ForgotPasswordActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

        binding.setLoginClickListener {
            startLoad()
            loginViewModel.login(binding.username.text.toString(), binding.password.text.toString())
        }

        preferenceUtils.getUserName()?.let {
            binding.username.setText(it)
        }
    }

    private fun substanceUI() {
        loginViewModel.unauthenticated.observe(this, {
            stopLoad()
            if (it) {
                WarningDialogHelper.create(this, resources.getString(R.string.login_fail))
            }
        })


        loginViewModel.loginSuccess.observe(this, {
            stopLoad()
            if (it) {
                val intent = Intent(this, HomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivityForResult(intent, sRequestCode)
            }
        })


        loginViewModel.tokenExist.observe(this, {
            if (it) {
                val intent = Intent(this, HomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivityForResult(intent, sRequestCode)
            }
            stopLoad()
        })
    }


    private fun dealWithStatus(statusCode: Int) {
        when (statusCode) {
            resultCodeLogoutSuccess -> {
                binding.password.text.clear()
                PreferenceUtils(this).clearAccessToken()
                WarningDialogHelper.create(
                    this,
                    resources.getString(R.string.logout_success)
                )
            }
            resultCodeLogoutFail -> WarningDialogHelper.create(
                this,
                resources.getString(R.string.logout_fail)
            )
            resultCodeTokenExpiry -> {
                binding.password.text.clear()
                PreferenceUtils(this).clearAccessToken()
                WarningDialogHelper.create(this, resources.getString(R.string.login_expiry))
            }
            resultCodePressBack -> finish()
            else -> {
            }

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == sRequestCode) {
            when (resultCode) {
                resultCodePressBack -> finish()
                else -> {
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }


    companion object {
        const val sStatusExtras = "statusExtras"
        const val sRequestCode = 1
        const val resultCodeLogoutSuccess = 1
        const val resultCodeLogoutFail = 2
        const val resultCodeTokenExpiry = 3
        const val resultCodePressBack = 4
    }
}

