package com.framecad.plum.data.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ForgotBody(
    @field:SerializedName("email")
    var email: String,
) : Parcelable