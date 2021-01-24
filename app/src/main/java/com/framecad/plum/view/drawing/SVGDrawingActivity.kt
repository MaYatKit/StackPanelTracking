package com.framecad.plum.view.drawing

import android.os.Bundle
import android.util.Size
import android.webkit.WebChromeClient
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.framecad.plum.R
import com.framecad.plum.data.model.SvgItem
import com.framecad.plum.databinding.ActivitySvgDrawingBinding
import com.framecad.plum.view.base.BaseActivity
import com.framecad.plum.viewmodel.base.BaseViewModel
import com.framecad.plum.viewmodel.drawing.SVGDrawingViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SVGDrawingActivity : BaseActivity() {

    private val baseUrl = "file:///android_asset/web/"

    private val svgDrawingViewModel: SVGDrawingViewModel by lazy {
        ViewModelProvider(this).get(SVGDrawingViewModel::class.java)
    }

    override val baseViewModel: BaseViewModel by lazy {
        svgDrawingViewModel
    }

    private lateinit var binding: ActivitySvgDrawingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_svg_drawing)
        binding.lifecycleOwner = this
        binding.vm = svgDrawingViewModel
        binding.setBackClickListener {
            finish()
        }

        startLoad()
        initView()
        subscribeUI()
        intent.getParcelableExtra<SvgItem>(getString(R.string.svg_drawing_page_item))
            ?.let { svgDrawingViewModel.setViewItem(it) }
    }


    private fun subscribeUI() {
        svgDrawingViewModel.svgHTMLString.observe(this, { it ->
            stopLoad()
            binding.swipeRefreshLayout.isRefreshing = false
            binding.webView.loadDataWithBaseURL(
                baseUrl, it, "text/html",
                "UTF-8", null
            )
        })

    }


    private fun initView() {
        binding.webView.setInitialScale(100)
        binding.webView.settings.builtInZoomControls = true
        binding.webView.settings.displayZoomControls = false
        binding.webView.settings.domStorageEnabled = true
        binding.webView.webChromeClient = WebChromeClient()

        binding.webView.addOnLayoutChangeListener { v, _, _, _, _, _, _, _, _ ->
            svgDrawingViewModel.setWebViewSize(Size(v.width, v.height))
        }


        binding.swipeRefreshLayout.setOnRefreshListener {
            lifecycleScope.launch {
                svgDrawingViewModel.getSVG()
            }
        }

        svgDrawingViewModel.errorResponseMsg.observe(this, {
            binding.swipeRefreshLayout.isRefreshing = false
        })

        svgDrawingViewModel.serverError.observe(this, {
            binding.swipeRefreshLayout.isRefreshing = false
        })
    }


}