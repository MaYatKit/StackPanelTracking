package com.framecad.plum.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StackStatusResponse(
    @field:SerializedName("id") var id: String,
    @field:SerializedName("item_type") var itemType: String,
    @field:SerializedName("status") var status: String,
    @field:SerializedName("stacked") var stacked: Boolean,
    @field:SerializedName("note") var note: String,
    @field:SerializedName("longitude") var longitude: Double,
    @field:SerializedName("latitude") var latitude: Double,
):Parcelable