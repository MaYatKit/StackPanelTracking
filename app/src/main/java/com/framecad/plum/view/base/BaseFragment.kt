package com.framecad.plum.view.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.framecad.plum.R
import com.framecad.plum.utils.WarningDialogHelper
import com.framecad.plum.view.login.LoginActivity
import com.framecad.plum.viewmodel.base.BaseViewModel

open class BaseFragment : Fragment() {


    open val baseViewModel: BaseViewModel? = null

    lateinit var parentActivity: BaseActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        parentActivity = activity as BaseActivity
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        subscribeUI()
        return super.onCreateView(inflater, container, savedInstanceState)
    }


    private fun subscribeUI() {
        baseViewModel?.loginExpires?.observe(viewLifecycleOwner, {
            if (it) {
                val intent = Intent(parentActivity, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.putExtra(
                    LoginActivity.sStatusExtras,
                    LoginActivity.resultCodeTokenExpiry
                )
                startActivity(intent)
                parentActivity.finish()
            }
        })


        baseViewModel?.errorResponseMsg?.observe(viewLifecycleOwner, {
            (activity as BaseActivity).stopLoad()
            if (it.isNotEmpty()) {
                WarningDialogHelper.create(parentActivity, it)
            } else {
                WarningDialogHelper.create(parentActivity, getString(R.string.server_error_msg))
            }
        })

        baseViewModel?.serverError?.observe(viewLifecycleOwner, {
            (activity as BaseActivity).stopLoad()
            if (it) {
                WarningDialogHelper.create(parentActivity, getString(R.string.server_error_msg))
            }
        })

    }


}