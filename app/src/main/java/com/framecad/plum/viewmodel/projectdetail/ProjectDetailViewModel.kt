package com.framecad.plum.viewmodel.projectdetail

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.framecad.plum.data.model.ListItem
import com.framecad.plum.data.ProjectsRepository
import com.framecad.plum.data.model.ProjectDetailPage
import com.framecad.plum.viewmodel.base.BaseViewModel

class ProjectDetailViewModel @ViewModelInject constructor(
    private val repository: ProjectsRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    private val projectDetailText = "project_detail"

    val projectDetail: LiveData<ProjectDetailPage>?
        get() = savedStateHandle.getLiveData(projectDetailText)

    private val _items: MutableLiveData<List<ListItem>> = MutableLiveData()

    val items: LiveData<List<ListItem>>
        get() = _items


    suspend fun getProjectList(projectId: Long) {
        val items = ArrayList<ListItem>()
        val response = repository.getProjectDetail(projectId)

        handleResponse(response) {
            response.body()?.let {
                savedStateHandle[projectDetailText] = ProjectDetailPage(it)
            }
            projectDetail?.value?.let { projectDetail ->
                items.addAll(projectDetail.getProperties())
                items.addAll(projectDetail.getSubLists())
                _items.value = items
            }
        }

    }

}
