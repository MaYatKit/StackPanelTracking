package com.framecad.plum.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Represent a response from the Login endpoint
 */
@Parcelize
data class LoginResponse(
    @field:SerializedName("access_token")
    var accessToken: String,

    @field:SerializedName("account_id")
    var accountId: String?,
) : Parcelable