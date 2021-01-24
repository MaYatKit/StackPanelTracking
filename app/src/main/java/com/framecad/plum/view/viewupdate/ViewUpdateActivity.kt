package com.framecad.plum.view.viewupdate

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import android.widget.Toast.*
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.framecad.plum.R
import com.framecad.plum.adapters.ProjectStatus
import com.framecad.plum.adapters.StatusSpinnerAdapter
import com.framecad.plum.data.model.ScanItem
import com.framecad.plum.databinding.ActivityViewUpdateBinding
import com.framecad.plum.utils.WarningDialogHelper
import com.framecad.plum.view.base.BaseActivity
import com.framecad.plum.view.home.HomeActivity
import com.framecad.plum.viewmodel.base.BaseViewModel
import com.framecad.plum.viewmodel.viewupdate.ViewUpdateViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_view_update.view.*
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ViewUpdateActivity : BaseActivity() {

    private val viewUpdateViewModel: ViewUpdateViewModel by lazy {
        ViewModelProvider(this).get(ViewUpdateViewModel::class.java)
    }

    override val baseViewModel: BaseViewModel by lazy {
        viewUpdateViewModel
    }

    private lateinit var binding: ActivityViewUpdateBinding

    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_update)
        binding.lifecycleOwner = this
        binding.vm = viewUpdateViewModel
        binding.stateSelector = ProjectStatus()

        startLoad()
        initView()
        subscribeUI()
        intent.getParcelableExtra<ScanItem>(getString(R.string.view_detail_page_item))?.let {
            binding.scanItem = it
            viewUpdateViewModel.setViewItem(it)
        }
    }


    @SuppressLint("ClickableViewAccessibility")
    private fun initView() {
        viewUpdateViewModel.retrieveLocation(fusedLocationClient)

        binding.setSubmitClickListener {
            startLoad()
            viewUpdateViewModel.setNoteText(binding.editText.text.toString())
            viewUpdateViewModel.updateStatus()
        }

        binding.setBackClickListener {
            finish()
        }

        binding.switcher.setOnTouchListener { v, _ ->
            if (v?.isClickable == false) {
                Snackbar.make(
                    binding.noteLayout,
                    resources.getString(R.string.view_update_page_switch_failed),
                    Snackbar.LENGTH_LONG
                ).show()
            }
            false
        }

        binding.setCheckedChangeListener { _, isChecked ->
            viewUpdateViewModel.setStatusBoolean(isChecked)
        }

        binding.spinner.adapter =
            StatusSpinnerAdapter(listOf(*resources.getStringArray(R.array.status_array)))
        binding.spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val statusText = parent?.getItemAtPosition(position) as String
                viewUpdateViewModel.statusPageItemObject.value?.let {
                    if (!it.getStatusBoolean() && when (statusText) {
                            resources.getStringArray(R.array.status_array)[2] -> true
                            resources.getStringArray(R.array.status_array)[3] -> true
                            resources.getStringArray(R.array.status_array)[4] -> true
                            else -> false
                        }
                    ) {
                        binding.switchLayout.visibility = View.VISIBLE
                        binding.switchTitle.visibility = View.VISIBLE
                    } else {
                        binding.switchLayout.visibility = View.INVISIBLE
                        binding.switchTitle.visibility = View.GONE
                    }
                }

                viewUpdateViewModel.setSwitch(statusText)
                viewUpdateViewModel.setStatusText(statusText)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }


        binding.swipeRefreshLayout.setOnRefreshListener {
            lifecycleScope.launch {
                viewUpdateViewModel.getViewStatus()
            }
        }
    }


    private fun subscribeUI() {
        viewUpdateViewModel.statusPageItemObject.observe(this, {
            if (it == null) {
                val intent = Intent(this, HomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(intent)
                return@observe
            }
            binding.swipeRefreshLayout.isRefreshing = false
            viewUpdateViewModel.currentLocation.value?.let {
                stopLoad()
            }
            val statusArray = resources.getStringArray(R.array.status_array)
            if ((it.getStatusText() == statusArray[2] || it.getStatusText() == statusArray[3] || it.getStatusText() == statusArray[4]) && !it.getStatusBoolean()) {
                binding.switchTitle.visibility = View.VISIBLE
                binding.switchLayout.visibility = View.VISIBLE
                binding.switcher.isChecked = it.getStatusBoolean()
            }
            val currentSpinnerIndex =
                listOf(*resources.getStringArray(R.array.status_array)).indexOf(it.getStatusText())
            if (currentSpinnerIndex != -1) {
                binding.spinner.setSelection(currentSpinnerIndex)
            }
        })
        viewUpdateViewModel.isUpdateSuccess.observe(this, {
            stopLoad()
            if (it) {
                val intent = Intent(this@ViewUpdateActivity, HomeActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(intent)
                finish()
            }
        })

        viewUpdateViewModel.hasLocationPermission.observe(this, {
            if (it) {
                viewUpdateViewModel.retrieveLocation(fusedLocationClient)
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                        requestPermissions(REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
                    } else {
                        stopLoad()
                        WarningDialogHelper.create(
                            this,
                            resources.getString(R.string.warn_location_disable)
                        )
                    }
                }
            }
        })

        viewUpdateViewModel.currentLocation.observe(this, {
            viewUpdateViewModel.statusPageItemObject.value?.let {
                stopLoad()
            }
        })


        viewUpdateViewModel.errorResponseMsg.observe(this, {
            binding.swipeRefreshLayout.isRefreshing = false
        })

        viewUpdateViewModel.serverError.observe(this, {
            binding.swipeRefreshLayout.isRefreshing = false
        })
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLoad()
                viewUpdateViewModel.setHasLocationPermission(true)
            } else {
                stopLoad()
                makeText(
                    this,
                    resources.getString(R.string.warn_location_disable),
                    LENGTH_LONG
                ).show()
            }
        }
    }

    companion object {
        const val TAG = "ViewUpdateActivity"
        const val REQUEST_CODE_PERMISSIONS = 10
        val REQUIRED_PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }


}