package com.framecad.plum.data.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LoginBody(
    @field:SerializedName("username")
    var username: String,

    @field:SerializedName("password")
    var password: String
) : Parcelable