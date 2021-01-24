package com.framecad.plum.data.api

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import android.widget.Toast
import com.framecad.plum.BuildConfig
import com.framecad.plum.R
import com.framecad.plum.data.response.GeneralPutResponse
import com.framecad.plum.data.response.LoginResponse
import com.framecad.plum.utils.PreferenceUtils
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import javax.inject.Inject

interface LoginService {

    @POST("forgot-password")
    suspend fun forgotPassword(@Body forgotBody: ForgotBody): Response<ResponseBody>


    @POST("logout")
    suspend fun logout(): Response<GeneralPutResponse>


    @POST("login")
    suspend fun login(@Body loginBody: LoginBody): Response<LoginResponse>


    companion object {
        private const val BASE_URL = BuildConfig.AUTHORISE_URL

        lateinit var retrofit: Retrofit


        fun create(context: Context, preferenceUtils: PreferenceUtils): LoginService {

            val cookieJar =
                PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(context))
            val client = OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .addInterceptor(NoInternetInterceptor(context,400 ,context.getString(R.string.error_msg_no_internet)))
                .addInterceptor(object : Interceptor {
                    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                        val accessToken = preferenceUtils.getAccessToken()
                        val request: Request = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer $accessToken")
                            .build()

                        Log.d("LoginService", "access_token = $accessToken")
                        return chain.proceed(request)
                    }
                }).build()


            retrofit =  Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(LoginService::class.java)

        }
    }
}