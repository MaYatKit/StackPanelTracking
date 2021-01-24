package com.framecad.plum.data.response

import com.framecad.plum.data.model.ItemDetailPage


interface DetailResponse {
    fun getDetailId() : String
    fun getDetailName(): String
    fun getDetailItemType(): ItemDetailPage.ItemDetailPageType
    fun getDetailPlanUrl(): String
    fun getDetailProperties(): List<PropertyResponse>
}