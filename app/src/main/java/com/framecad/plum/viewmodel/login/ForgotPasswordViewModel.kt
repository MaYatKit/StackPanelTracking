package com.framecad.plum.viewmodel.login

import android.util.Patterns
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*

import com.framecad.plum.data.LoginRepository
import com.framecad.plum.data.api.ForgotBody
import com.framecad.plum.data.api.ResponseStatus.unauthenticatedCodes
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

    private val _invalidEmail = MutableLiveData<Boolean>()
    val invalidEmail: LiveData<Boolean> = _invalidEmail


    fun forgotPassword(email: String) {
        if (email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
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
        } else {
            _invalidEmail.value = true
        }
    }


}