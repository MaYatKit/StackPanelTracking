package com.framecad.plum.view.viewdetail

import android.content.Intent
import android.os.Bundle
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.framecad.plum.R
import com.framecad.plum.adapters.DetailPageListAdapter
import com.framecad.plum.adapters.PageType
import com.framecad.plum.data.model.ScanItem
import com.framecad.plum.data.model.SvgItem
import com.framecad.plum.databinding.ActivityViewDetailBinding
import com.framecad.plum.view.base.BaseActivity
import com.framecad.plum.view.drawing.SVGDrawingActivity
import com.framecad.plum.viewmodel.base.BaseViewModel
import com.framecad.plum.viewmodel.viewdetail.ViewDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ViewDetailActivity : BaseActivity() {

    private val adapter = DetailPageListAdapter(PageType.ITEM_DETAIL_PAGE)

    private val viewDetailViewModel: ViewDetailViewModel by lazy {
        ViewModelProvider(this).get(ViewDetailViewModel::class.java)
    }

    override val baseViewModel: BaseViewModel by lazy {
        viewDetailViewModel
    }

    private lateinit var binding: ActivityViewDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_detail)
        binding.lifecycleOwner = this
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
                    val intent = Intent(this, SVGDrawingActivity::class.java)
                    intent.putExtra(getString(R.string.svg_drawing_page_item), SvgItem(scanItem.id, scanItem.name, SvgItem.SvgItemType.PANEL))
                    startActivity(intent)
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