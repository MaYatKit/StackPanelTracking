package com.framecad.plum.view.viewdetail

import android.content.Intent
import android.os.Bundle
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.framecad.plum.R
import com.framecad.plum.StackerWebView
import com.framecad.plum.adapters.DetailPageListAdapter
import com.framecad.plum.adapters.PageType
import com.framecad.plum.data.model.ScanItem
import com.framecad.plum.data.model.SvgItem
import com.framecad.plum.databinding.ActivityViewDetailBinding
import com.framecad.plum.utils.WarningDialogHelper
import com.framecad.plum.view.base.BaseActivity
import com.framecad.plum.view.drawing.SVGDrawingActivity
import com.framecad.plum.view.drawing.StackDrawingActivity
import com.framecad.plum.viewmodel.base.BaseViewModel
import com.framecad.plum.viewmodel.viewdetail.ViewDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ViewDetailActivity : BaseActivity() {

    private lateinit var adapter: DetailPageListAdapter

    private val viewDetailViewModel: ViewDetailViewModel by lazy {
        ViewModelProvider(this).get(ViewDetailViewModel::class.java)
    }

    override val baseViewModel: BaseViewModel by lazy {
        viewDetailViewModel
    }

    private lateinit var binding: ActivityViewDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val projectId = intent.getLongExtra(getString(R.string.project_id), 40)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_detail)
        binding.lifecycleOwner = this
        adapter = DetailPageListAdapter(projectId, PageType.ITEM_DETAIL_PAGE)
        binding.viewDetailList.adapter = adapter
        binding.vm = viewDetailViewModel
        binding.setBackClickListener {
            finish()
        }

        startLoad()
        initView()
        subscribeUI()
        intent.getParcelableExtra<ScanItem>(getString(R.string.view_detail_page_item))
            ?.let { scanItem ->
                viewDetailViewModel.setViewItem(scanItem)

                binding.setShowPlanClickListener {
                    val itemType = when (scanItem.itemType){
                        ScanItem.ScanItemType.PANEL -> SvgItem.SvgItemType.PANEL
                        ScanItem.ScanItemType.PLAN -> SvgItem.SvgItemType.PLAN
                        ScanItem.ScanItemType.STACK -> SvgItem.SvgItemType.STACK
                        else -> SvgItem.SvgItemType.UNKNOWN
                    }
                    val intent = when(itemType) {
                        SvgItem.SvgItemType.STACK -> Intent(this, StackDrawingActivity::class.java)
                        else -> Intent(this, SVGDrawingActivity::class.java)
                    }
                    intent.putExtra(getString(R.string.svg_drawing_page_item), SvgItem(projectId, scanItem.id, scanItem.name, itemType))

                    if (StackerWebView.getInstance(this).is3dRenderingNotSupported && itemType == SvgItem.SvgItemType.STACK) {
                        WarningDialogHelper.create(this, getString(R.string.svg_drawing_unsupported_msg))
                    } else {
                        startActivity(intent)
                    }
                }
            }
    }

    private fun initView() {
        binding.viewDetailList.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (adapter.itemCount > 0) {
                    stopLoad()
                    binding.viewDetailList
                        .viewTreeObserver
                        .removeOnGlobalLayoutListener(this)
                }
            }

        })


        binding.swipeRefreshLayout.setOnRefreshListener {
            lifecycleScope.launch {
                viewDetailViewModel.getViewDetail()
            }
        }
    }

    private fun subscribeUI() {
        viewDetailViewModel.items.observe(this, { it ->
            binding.swipeRefreshLayout.isRefreshing = false
            adapter.submitList(it)
        })


        viewDetailViewModel.viewDetail.observe(this, {
            if (it == null) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.loading_error_msg),
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
        })


        viewDetailViewModel.errorResponseMsg.observe(this, {
            binding.swipeRefreshLayout.isRefreshing = false
        })


        viewDetailViewModel.serverError.observe(this, {
            binding.swipeRefreshLayout.isRefreshing = false
        })
    }

}