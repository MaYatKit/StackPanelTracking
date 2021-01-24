package com.framecad.plum.data.api

import android.util.Range

object ResponseStatus {
    val successCodes = intArrayOf(200, 203, 201, 204, 205)
    val unauthenticatedCodes = 401
    val expiresCodes = 403
    val serverErrorCodesRange = Range(500, 599)
}