package com.framecad.plum.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProjectDetailResponse(
    @field:SerializedName("id") var id: String,
    @field:SerializedName("name") var name: String,
    @field:SerializedName("percentage") var percentage: String?,
    @field:SerializedName("item_type") var itemType: String?,
    @field:SerializedName("status") var status: String?,
    @field:SerializedName("timing") var timing: String?,
    @field:SerializedName("properties") var properties: List<PropertyResponse>,
    @field:SerializedName("panels") var panels: List<SubListItemResponse>,
    @field:SerializedName("stacks") var stacks: List<SubListItemResponse>,
    @field:SerializedName("plans") var plans: List<SubListItemResponse>,
): Parcelable