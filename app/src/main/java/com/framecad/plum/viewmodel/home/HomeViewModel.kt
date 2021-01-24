package com.framecad.plum.viewmodel.home

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.framecad.plum.data.LoginRepository
import com.framecad.plum.utils.PreferenceUtils
import com.framecad.plum.viewmodel.base.BaseViewModel
import kotlinx.coroutines.launch

class HomeViewModel @ViewModelInject constructor(
    val repository: LoginRepository,
    @Assisted private val savedStateHandle: SavedStateHandle,
    val preferenceUtils: PreferenceUtils
) : BaseViewModel() {

    private val _logoutResult = MutableLiveData<Boolean>()
    val logoutResult: LiveData<Boolean> = _logoutResult


    fun logout() {
        viewModelScope.launch {
            val response = repository.logout()
            handleResponse(response) {
                _logoutResult.value = true
                preferenceUtils.clearAccessToken()
            }
        }
    }
}