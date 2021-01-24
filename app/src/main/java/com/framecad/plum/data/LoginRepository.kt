package com.framecad.plum.data

import android.content.Context
import com.framecad.plum.data.api.ForgotBody
import com.framecad.plum.data.api.LoginBody
import com.framecad.plum.data.api.LoginService
import com.framecad.plum.data.api.ResponseStatus.successCodes
import com.framecad.plum.data.response.GeneralPutResponse
import com.framecad.plum.data.response.LoginResponse
import com.framecad.plum.utils.PreferenceUtils
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.ResponseBody
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginRepository @Inject constructor(private val service: LoginService,
                                          @ApplicationContext val context: Context, private val preferenceUtils: PreferenceUtils) {



    suspend fun login(loginBody: LoginBody): Response<LoginResponse> {
        val response = service.login(loginBody)
        if (successCodes.any { it == response.code() }) {
            response.body()?.let {
                preferenceUtils.setAccessToken(it.accessToken)
            }
            preferenceUtils.setUserName(loginBody.username)
            return response
        }
        return response
    }


    suspend fun logout(): Response<GeneralPutResponse> {
        return service.logout()
    }

    suspend fun forgotPassword(forgotBody: ForgotBody): Response<ResponseBody> {
        return service.forgotPassword(forgotBody)
    }


}