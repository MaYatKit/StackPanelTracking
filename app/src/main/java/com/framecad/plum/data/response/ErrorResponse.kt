package com.framecad.plum.data.response

import com.google.gson.annotations.SerializedName


class ErrorResponse(
    @field:SerializedName("error_code")
    var errorCode: String,

    @field:SerializedName("error_message")
    var errorMessage: String
)
