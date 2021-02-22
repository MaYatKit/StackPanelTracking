package com.framecad.plum.view.login

import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.framecad.plum.R
import com.framecad.plum.databinding.ActivityForgotPasswordBinding
import com.framecad.plum.utils.WarningDialogHelper
import com.framecad.plum.view.base.BaseActivity
import com.framecad.plum.viewmodel.base.BaseViewModel
import com.framecad.plum.viewmodel.login.ForgotPasswordViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ForgotPasswordActivity : BaseActivity() {
    private val forgotPasswordViewModel: ForgotPasswordViewModel by lazy {
        ViewModelProvider(this).get(ForgotPasswordViewModel::class.java)
    }

    override val baseViewModel: BaseViewModel by lazy {
        forgotPasswordViewModel
    }
    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_forgot_password)
        binding.lifecycleOwner = this
        initView()
        subscribeUI()
    }


    private fun initView() {
        binding.setSubmitClickListener {
            startLoad()
            forgotPasswordViewModel.forgotPassword(binding.email.text.toString())
        }

        binding.setBackClickListener {
            finish()
        }

    }


    private fun subscribeUI() {
        forgotPasswordViewModel.forgotFailed.observe(this, {
            stopLoad()
            if (it) {
                WarningDialogHelper.create(this, resources.getString(R.string.forgot_fail))
            }
        })

        forgotPasswordViewModel.forgotSuccess.observe(this, {
            stopLoad()
            if (it) {
                WarningDialogHelper.create(this, resources.getString(R.string.forgot_success))
            }
        })

        forgotPasswordViewModel.invalidEmail.observe(this, {
            stopLoad()
            if (it) {
                WarningDialogHelper.create(this, resources.getString(R.string.forgot_invalid_email))
            }
        })

    }


}