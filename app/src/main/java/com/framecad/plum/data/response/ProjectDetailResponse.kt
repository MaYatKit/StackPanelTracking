package com.framecad.plum.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ProjectDetailResponse(
    @field:SerializedName("id") val id: String,
    @field:SerializedName("name") val name: String,
    @field:SerializedName("percentage") val percentage: String,
    @field:SerializedName("item_type") val itemType: String,
    @field:SerializedName("status") val status: String,
    @field:SerializedName("timing") val timing: String,
    @field:SerializedName("properties") val properties: List<PropertyResponse>,
    @field:SerializedName("panels") val panels: List<SubListItemResponse>,
    @field:SerializedName("stacks") val stacks: List<SubListItemResponse>,
    @field:SerializedName("plans") val plans: List<SubListItemResponse>,
): Parcelable