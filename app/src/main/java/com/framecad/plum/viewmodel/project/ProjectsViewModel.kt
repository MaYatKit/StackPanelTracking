package com.framecad.plum.viewmodel.project

import android.util.Range
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.framecad.plum.data.ProjectsRepository
import com.framecad.plum.data.response.SearchProjectsResponse
import com.framecad.plum.data.response.SimpleProjectResponse
import com.framecad.plum.data.response.utils.ResponseUtils
import com.framecad.plum.viewmodel.base.BaseViewModel
import kotlinx.coroutines.launch
import okhttp3.ResponseBody

class ProjectsViewModel @ViewModelInject constructor(private val repository: ProjectsRepository) :
    BaseViewModel() {

    private val _projects = MutableLiveData<List<SimpleProjectResponse>>()

    val projects: LiveData<List<SimpleProjectResponse>>
        get() = _projects



    init {
        viewModelScope.launch {
            val response = repository.getProjectList()
            handleResponse(response) {
                _projects.value = response.body()?.projects
            }
        }
    }


    fun refresh() {
        viewModelScope.launch {
            val response = repository.getProjectList()

            handleResponse(response) {
                _projects.value = response.body()?.projects
            }
        }
    }

}