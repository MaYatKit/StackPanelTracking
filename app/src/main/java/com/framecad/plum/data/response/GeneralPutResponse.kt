package com.framecad.plum.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Represent a response from a PUT endpoint
 */
@Parcelize
data class GeneralPutResponse(
    @field:SerializedName("Id") var id: Long,
    @field:SerializedName("ResponseMessage") var responseMessage: String,
) : Parcelable