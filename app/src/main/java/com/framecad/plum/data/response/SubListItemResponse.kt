package com.framecad.plum.data.response

import android.os.Parcelable
import android.util.Log
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Response type for a list of subItem, E.g., panels, stacks, sticks
 */
@Parcelize
data class SubListItemResponse(
    @field:SerializedName("id")
    var id: String,

    @field:SerializedName("name")
    var name: String,

    @field:SerializedName("status")
    var status: String?,

    @field:SerializedName("item_type")
    var itemType: String?,

    @field:SerializedName("properties")
    val properties: List<PropertyResponse>?,

    @field:SerializedName("url")
    var url: String?
) :Parcelable