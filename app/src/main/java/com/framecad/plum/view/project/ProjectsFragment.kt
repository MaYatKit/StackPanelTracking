package com.framecad.plum.view.project

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import com.framecad.plum.adapters.ProjectsListAdapter
import com.framecad.plum.databinding.FragmentProjectsBinding
import com.framecad.plum.view.base.BaseActivity
import com.framecad.plum.view.base.BaseFragment
import com.framecad.plum.view.home.HomeActivity
import com.framecad.plum.view.login.LoginActivity.Companion.resultCodeTokenExpiry
import com.framecad.plum.viewmodel.base.BaseViewModel
import com.framecad.plum.viewmodel.project.ProjectsViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class ProjectsFragment : BaseFragment() {

    private val adapter: ProjectsListAdapter by lazy {
        ProjectsListAdapter(activity as HomeActivity)
    }

    private val args: ProjectsFragmentArgs by navArgs()

    private lateinit var binding: FragmentProjectsBinding

    private val projectsViewModel: ProjectsViewModel by lazy {
        ViewModelProvider(this).get(ProjectsViewModel::class.java)
    }

    override val baseViewModel: BaseViewModel by lazy {
        projectsViewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        binding = FragmentProjectsBinding.inflate(inflater, container, false)

        binding.projectList.adapter = adapter
        binding.projectsViewModel = projectsViewModel
        binding.lifecycleOwner = this
        (activity as BaseActivity).startLoad()
        initView()
        subscribeUI()

        return binding.root
    }


    private fun initView() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            projectsViewModel.refresh()
        }
    }


    private fun subscribeUI() {
        projectsViewModel.projects.observe(viewLifecycleOwner, {
            binding.swipeRefreshLayout.isRefreshing = false
            adapter.submitList(it)
            (activity as BaseActivity).stopLoad()
        })


        projectsViewModel.errorResponseMsg.observe(viewLifecycleOwner, {
            binding.swipeRefreshLayout.isRefreshing = false
        })

        projectsViewModel.serverError.observe(viewLifecycleOwner, {
            binding.swipeRefreshLayout.isRefreshing = false
        })


    }


}