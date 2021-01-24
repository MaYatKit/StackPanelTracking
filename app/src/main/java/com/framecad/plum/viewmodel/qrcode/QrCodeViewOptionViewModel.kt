package com.framecad.plum.viewmodel.qrcode

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.framecad.plum.data.ProjectsRepository
import com.framecad.plum.data.model.ProjectDetailPage
import com.framecad.plum.data.model.ScanItem
import com.framecad.plum.viewmodel.base.BaseViewModel
import kotlinx.coroutines.launch

class QrCodeViewOptionViewModel @ViewModelInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
) :
    BaseViewModel() {

    private val scanItemText = "item"

    val viewItem: LiveData<ScanItem> = savedStateHandle.getLiveData(scanItemText)


    fun setScamItem(item: ScanItem) {
        savedStateHandle[scanItemText] = item
    }


}