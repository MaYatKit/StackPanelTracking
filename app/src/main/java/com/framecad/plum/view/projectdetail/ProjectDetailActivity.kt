package com.framecad.plum.view.projectdetail

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.framecad.plum.R
import com.framecad.plum.adapters.DetailPageListAdapter
import com.framecad.plum.adapters.PageType
import com.framecad.plum.databinding.ActivityProjectDetailBinding
import com.framecad.plum.view.base.BaseActivity
import com.framecad.plum.viewmodel.base.BaseViewModel
import com.framecad.plum.viewmodel.projectdetail.ProjectDetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ProjectDetailActivity : BaseActivity() {

    private val adapter = DetailPageListAdapter(PageType.PROJECT_DETAIL_PAGE)

    private val projectDetailViewModel: ProjectDetailViewModel by lazy {
        ViewModelProvider(this).get(ProjectDetailViewModel::class.java)
    }

    override val baseViewModel: BaseViewModel by lazy {
        projectDetailViewModel
    }

    private lateinit var binding: ActivityProjectDetailBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_project_detail)
        binding.lifecycleOwner = this
        binding.projectDetailList.adapter = adapter
        binding.vm = projectDetailViewModel
        binding.backClickListener = View.OnClickListener { finish() }

        startLoad()
        initView()
        subscribeUI()
        loadProjects(intent.getLongExtra(resources.getString(R.string.project_id), 40))

    }


    private fun initView() {
        val layoutManager = object : LinearLayoutManager(this) {
            override fun onLayoutCompleted(state: RecyclerView.State?) {
                super.onLayoutCompleted(state)
                val firstVisibleItemPosition = findFirstVisibleItemPosition()
                val lastVisibleItemPosition = findLastVisibleItemPosition()
                val itemsShown = lastVisibleItemPosition - firstVisibleItemPosition
                if (adapter.itemCount > itemsShown) {
                    stopLoad()
                }
            }
        }
        binding.projectDetailList.layoutManager = layoutManager


        binding.swipeRefreshLayout.setOnRefreshListener {
            loadProjects(intent.getLongExtra(resources.getString(R.string.project_id), 40))
        }
    }

    private fun subscribeUI() {
        projectDetailViewModel.items.observe(this, { it ->
            if (it == null) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.loading_error_msg),
                    Toast.LENGTH_LONG
                ).show()
                finish()
            }
            binding.swipeRefreshLayout.isRefreshing = false
            adapter.submitList(it)
        })
        projectDetailViewModel.errorResponseMsg.observe(this, {
            binding.swipeRefreshLayout.isRefreshing = false
        })

        projectDetailViewModel.serverError.observe(this, {
            binding.swipeRefreshLayout.isRefreshing = false
        })
    }


    private fun loadProjects(projectId: Long) {
        lifecycleScope.launch {
            projectDetailViewModel.getProjectList(projectId)
        }
    }


}