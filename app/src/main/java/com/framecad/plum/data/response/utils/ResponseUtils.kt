package com.framecad.plum.data.response.utils

import com.framecad.plum.data.api.LoginService
import com.framecad.plum.data.response.ErrorResponse
import okhttp3.ResponseBody
import java.lang.Exception

object ResponseUtils  {

    fun errorResponseParser(body: ResponseBody): ErrorResponse? {
        val converter = LoginService.retrofit.responseBodyConverter<ErrorResponse>(ErrorResponse::class.java, arrayOfNulls<Annotation>(0))
        return try {
            converter.convert(body)
        }catch (e: Exception){
            ErrorResponse("400", "Something went wrong, please try again later")
        }
    }


}