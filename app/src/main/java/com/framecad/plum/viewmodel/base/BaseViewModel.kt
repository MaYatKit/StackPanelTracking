package com.framecad.plum.viewmodel.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.framecad.plum.data.api.ResponseStatus.expiresCodes
import com.framecad.plum.data.api.ResponseStatus.serverErrorCodesRange
import com.framecad.plum.data.api.ResponseStatus.successCodes
import com.framecad.plum.data.response.utils.ResponseUtils
import retrofit2.Response

open class BaseViewModel : ViewModel() {
    val errorResponseMsg = MutableLiveData<String>()

    val loginExpires = MutableLiveData<Boolean>()

    val serverError = MutableLiveData<Boolean>()


    fun <T> handleResponse(res: Response<T>, unit: () -> Unit) {
        if (res.code() == expiresCodes) loginExpires.value = true
        else if (res.code() in serverErrorCodesRange) serverError.value = true
        else {
            if (successCodes.any { it == res.code() }) {
                unit()
            } else {
                res.errorBody()?.let {
                    val errorResponse = ResponseUtils.errorResponseParser(it)
                    errorResponseMsg.value = errorResponse?.errorMessage
                }
            }
        }
    }
}