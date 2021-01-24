package com.framecad.plum.view.qrcode

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.framecad.plum.R
import com.framecad.plum.data.model.ScanItem
import com.framecad.plum.databinding.ActivityQrcodeViewOptionBinding
import com.framecad.plum.view.base.BaseActivity
import com.framecad.plum.view.viewdetail.ViewDetailActivity
import com.framecad.plum.view.viewupdate.ViewUpdateActivity
import com.framecad.plum.viewmodel.qrcode.QrCodeViewOptionViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class QrCodeViewOptionActivity : BaseActivity() {

    private lateinit var binding: ActivityQrcodeViewOptionBinding

    private val viewModel: QrCodeViewOptionViewModel by lazy {
        ViewModelProvider(this).get(QrCodeViewOptionViewModel::class.java)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_qrcode_view_option)
        binding.lifecycleOwner = this
        binding.vm = viewModel

        startLoad()
        initView()
        subscribeUI()


        intent.getParcelableExtra<ScanItem>(getString(R.string.view_detail_page_item))
            ?.let { viewModel.setScamItem(it) }
    }


    private fun initView() {
        binding.setBackClickListener { finish() }
        binding.setViewDetailClickListener {
            val intent = Intent(this, ViewDetailActivity::class.java)
            intent.putExtra(getString(R.string.view_detail_page_item), viewModel.viewItem.value)
            startActivity(intent)
        }
        binding.setUpdateDetailClickListener {
            val intent = Intent(this, ViewUpdateActivity::class.java)
            intent.putExtra(getString(R.string.view_detail_page_item), viewModel.viewItem.value)
            startActivity(intent)
        }
    }


    private fun subscribeUI() {
        viewModel.viewItem.observe(this, {
            if (it != null) {
                stopLoad()
            }
        })

    }


}