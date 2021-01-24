package com.framecad.plum.data.response

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PanelStatusResponse(
    @Expose
    @field:SerializedName("id") var id: String,
    @Expose
    @field:SerializedName("item_type") var itemType: String,
    @Expose
    @field:SerializedName("status") var status: String,
    @Expose
    @field:SerializedName("broken_out") var brokenOut: Boolean,
    @Expose
    @field:SerializedName("note") var note: String,
    @Expose
    @field:SerializedName("longitude") var longitude: Double,
    @Expose
    @field:SerializedName("latitude") var latitude: Double,
) : Parcelable