package com.framecad.plum.data.response

import android.os.Parcelable
import com.framecad.plum.data.model.ItemDetailPage
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class PanelDetailResponse(
    @field:SerializedName("id")
    var id: String,

    @field:SerializedName("name")
    var name: String,

    @field:SerializedName("item_type")
    var itemType: ItemDetailPage.ItemDetailPageType,

    @field:SerializedName("plan_url")
    var planUrl: String,

    @field:SerializedName("properties")
    var properties: List<PropertyResponse>,

    @field:SerializedName("sticks")
    val sticks: List<SubListItemResponse>
) : Parcelable, DetailResponse {
    override fun getDetailId(): String {
        return id
    }

    override fun getDetailName(): String {
        return name
    }

    override fun getDetailItemType(): ItemDetailPage.ItemDetailPageType {
        return itemType
    }

    override fun getDetailPlanUrl(): String {
        return planUrl
    }

    override fun getDetailProperties(): List<PropertyResponse> {
        return properties
    }

}