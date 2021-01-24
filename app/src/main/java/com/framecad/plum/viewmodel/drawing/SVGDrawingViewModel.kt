package com.framecad.plum.viewmodel.drawing

import android.content.Context
import android.util.Size
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.framecad.plum.data.ProjectsRepository
import com.framecad.plum.data.model.SvgItem
import com.framecad.plum.data.response.utils.ResponseUtils
import com.framecad.plum.viewmodel.base.BaseViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import okhttp3.ResponseBody

class SVGDrawingViewModel @ViewModelInject constructor(
    private val repository: ProjectsRepository,
    @Assisted private val savedStateHandle: SavedStateHandle,
    @ApplicationContext val context: Context
) : BaseViewModel() {

    private val svgStringText = "svg_string"
    private val scanItemText = "scan_item"
    private val webViewSizeText = "web_view_size"

    val svgHTMLString: LiveData<String>
        get() = savedStateHandle.getLiveData(svgStringText)

    val scanItem: LiveData<SvgItem>
        get() = savedStateHandle.getLiveData(scanItemText)

    val webViewSize: LiveData<Size>
        get() = savedStateHandle.getLiveData(webViewSizeText)


    init {
        webViewSize.observeForever {
            viewModelScope.launch {
                getSVG()
            }
        }
    }

    fun setViewItem(item: SvgItem) {
        savedStateHandle[scanItemText] = item
    }

    fun setWebViewSize(size: Size) {
        savedStateHandle[webViewSizeText] = size
    }


    suspend fun getSVG() {
        scanItem.value?.apply {
            val response =
                when (this.itemType) {
                    SvgItem.SvgItemType.PANEL -> repository.getPanelDrawing(this.id.toLong())
                    SvgItem.SvgItemType.PLAN -> repository.getPlanDrawing(this.id.toLong())
                    else -> null
                }

            response?.let { res ->
                handleResponse<ResponseBody>(res) {
                    res.body()?.let {
                        savedStateHandle[svgStringText] = it.string()
                    }
                }


            }


        }
    }

}