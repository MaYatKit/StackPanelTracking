package com.framecad.plum.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PropertyResponse(
    @field:SerializedName("name") var name: String,
    @field:SerializedName("value") var value: String,
    @field:SerializedName("type") val type: String,
): Parcelable