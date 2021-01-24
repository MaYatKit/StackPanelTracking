package com.framecad.plum.viewmodel.login

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*

import com.framecad.plum.data.LoginRepository
import com.framecad.plum.data.api.ForgotBody
import com.framecad.plum.data.api.ResponseStatus.unauthenticatedCodes
import com.framecad.plum.data.response.utils.ResponseUtils
import com.framecad.plum.viewmodel.base.BaseViewModel
import kotlinx.coroutines.launch

class ForgotPasswordViewModel @ViewModelInject constructor(
    private val repository: LoginRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {


    private val _forgotSuccess = MutableLiveData<Boolean>()
    val forgotSuccess: LiveData<Boolean> = _forgotSuccess

    private val _forgotFailed = MutableLiveData<Boolean>()
    val forgotFailed: LiveData<Boolean> = _forgotFailed


    fun forgotPassword(email: String) {
        viewModelScope.launch {
            val response = repository.forgotPassword(ForgotBody(email))
            val responseCode = response.code()

            if (responseCode == unauthenticatedCodes) {
                _forgotFailed.value = true
                return@launch
            }

            handleResponse(response) {
                _forgotSuccess.value = true
            }
        }
    }


}