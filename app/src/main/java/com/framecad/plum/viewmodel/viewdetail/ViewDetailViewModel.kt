package com.framecad.plum.viewmodel.viewdetail

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.framecad.plum.data.model.ListItem
import com.framecad.plum.data.ProjectsRepository
import com.framecad.plum.data.model.ScanItem
import com.framecad.plum.data.model.ItemDetailPage
import com.framecad.plum.data.response.utils.ResponseUtils
import com.framecad.plum.viewmodel.base.BaseViewModel
import kotlinx.coroutines.launch

class ViewDetailViewModel @ViewModelInject constructor(
    private val repository: ProjectsRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {
    val scanItem: LiveData<ScanItem>
        get() = savedStateHandle.getLiveData("scan_item")

    val viewDetail: LiveData<ItemDetailPage?>
        get() = savedStateHandle.getLiveData("view_detail")

    val items: LiveData<List<ListItem>>
        get() = savedStateHandle.getLiveData("items")


    suspend fun getViewDetail() {
        scanItem.value?.apply {

            val response = if (this.itemType == ScanItem.ScanItemType.PANEL) {
                repository.getPanelDetail(id.toLong())
            } else {
                repository.getStackDetail(id.toLong())
            }

            handleResponse(response) {
                response.body()?.let {
                    savedStateHandle["view_detail"] = ItemDetailPage(it)
                }
            }

            val viewDetail = viewDetail.value
            val items = ArrayList<ListItem>()

            viewDetail?.let {
                items.addAll(it.getProperties())
                val subList = it.getSubList()
                if (subList == null) {
                    // Unknown item type (except stack or panel)
                    savedStateHandle["view_detail"] = null
                } else {
                    items.add(subList)
                    savedStateHandle["items"] = items
                }
            }
        }
    }

    fun setViewItem(item: ScanItem) {
        savedStateHandle["scan_item"] = item
        viewModelScope.launch {
            getViewDetail()
        }
    }

}