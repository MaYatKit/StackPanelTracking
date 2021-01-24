package com.framecad.plum.viewmodel.login

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*

import com.framecad.plum.data.LoginRepository
import com.framecad.plum.data.api.LoginBody
import com.framecad.plum.data.api.ResponseStatus.unauthenticatedCodes
import com.framecad.plum.data.response.utils.ResponseUtils
import com.framecad.plum.utils.PreferenceUtils
import com.framecad.plum.viewmodel.base.BaseViewModel
import kotlinx.coroutines.launch

class LoginViewModel @ViewModelInject constructor(
    private val repository: LoginRepository,
    @Assisted private val savedStateHandle: SavedStateHandle,
    private val preferenceUtils: PreferenceUtils
) : BaseViewModel() {

    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean> = _loginSuccess


    private val _unauthenticated = MutableLiveData<Boolean>()
    val unauthenticated: LiveData<Boolean> = _unauthenticated

    private val _tokenExist = MutableLiveData<Boolean>()
    val tokenExist: LiveData<Boolean> = _tokenExist

    init {
        _tokenExist.value = preferenceUtils.getAccessToken().isNotEmpty()
    }

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val response = repository.login(LoginBody(username, password))
            if (response.code() == unauthenticatedCodes) {
                _unauthenticated.value = true
                return@launch
            }
            handleResponse(response) {
                _loginSuccess.value = true
            }
        }
    }


    fun logout() {
        viewModelScope.launch {
            val response = repository.logout()
        }
    }

}