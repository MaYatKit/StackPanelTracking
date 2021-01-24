package com.framecad.plum.view.home

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.framecad.plum.R
import com.framecad.plum.databinding.ActivityMainBinding
import com.framecad.plum.view.base.BaseActivity
import com.framecad.plum.view.login.LoginActivity
import com.framecad.plum.view.login.LoginActivity.Companion.resultCodeLogoutFail
import com.framecad.plum.view.login.LoginActivity.Companion.resultCodeLogoutSuccess
import com.framecad.plum.view.login.LoginActivity.Companion.resultCodePressBack
import com.framecad.plum.view.project.ProjectsFragment
import com.framecad.plum.view.project.ProjectsFragmentDirections
import com.framecad.plum.view.projectdetail.ProjectDetailActivity
import com.framecad.plum.view.qrcode.QrCodeFragment
import com.framecad.plum.view.qrcode.QrCodeFragmentDirections
import com.framecad.plum.viewmodel.base.BaseViewModel
import com.framecad.plum.viewmodel.home.HomeViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*



@AndroidEntryPoint
class HomeActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProvider(this).get(HomeViewModel::class.java)
    }

    override val baseViewModel: BaseViewModel by lazy {
        homeViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this
        initView(savedInstanceState)
        subscribeUI()
    }


    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("topAppBar_title", binding.topAppBar.title.toString())
    }

    private fun initView(savedInstanceState: Bundle?) {
        savedInstanceState?.let {
            topAppBar.title = it.getString("topAppBar_title", resources.getString(R.string.qr_page_tile))
        }

        bottom_navigation_view.setOnNavigationItemSelectedListener { item ->
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
            var currentFragment: Fragment? = null

            navHostFragment?.let {
                currentFragment = it.childFragmentManager.primaryNavigationFragment
            }

            when (item.itemId) {
                R.id.navQrcode -> {
                    if (currentFragment is QrCodeFragment) {
                        false
                    } else {
                        val direction =
                            ProjectsFragmentDirections.actionProjectsFragmentToQrcodeFragment()
                        findNavController(R.id.fragment_container).navigate(direction)
                        topAppBar.title = resources.getString(R.string.qr_page_tile)
                        true
                    }
                }
                R.id.navProjects -> {
                    if (currentFragment is ProjectsFragment) {
                        false
                    } else {
                        val direction =
                            QrCodeFragmentDirections.actionQrcodeFragmentToProjectsFragment(0)
                        findNavController(R.id.fragment_container).navigate(direction)
                        topAppBar.title = resources.getString(R.string.projects_page_tile)
                        true
                    }
                }
                else -> false
            }
        }

        binding.topAppBar.setOnMenuItemClickListener { item ->
            if (item?.itemId == R.id.logout) {
                homeViewModel.logout()
            }
            true
        }
    }

    private fun subscribeUI() {
        homeViewModel.logoutResult.observe(this, {
            val intent = Intent(this, LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            if (it) {
                intent.putExtra(LoginActivity.sStatusExtras, resultCodeLogoutSuccess)
            } else {
                intent.putExtra(LoginActivity.sStatusExtras, resultCodeLogoutFail)
            }
            startActivity(intent)
            finish()
        })
    }


    override fun onBackPressed() {
        setResult(resultCodePressBack)
        finish()
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }


}