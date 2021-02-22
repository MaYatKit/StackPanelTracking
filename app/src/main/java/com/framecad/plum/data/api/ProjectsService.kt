package com.framecad.plum.data.api

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import com.framecad.plum.BuildConfig
import com.framecad.plum.R
import com.framecad.plum.data.response.*
import com.framecad.plum.utils.PreferenceUtils
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface ProjectsService {

    @GET("projects")
    suspend fun getProjects(): Response<List<SimpleProjectResponse>>

    @GET("projects")
    suspend fun getProjectsByUserId(): Response<SearchProjectsResponse>


    @GET("project")
    suspend fun getProjectById(@Query("project_id") id: Long): Response<ProjectDetailResponse>


    @GET("stack")
    suspend fun getStackById(@Query("stack_id") id: Long): Response<StackDetailResponse>

    @GET("panel")
    suspend fun getPanelById(@Query("panel_id") id: Long): Response<PanelDetailResponse>


    @GET("stack-status")
    suspend fun getStackStatusById(@Query("stack_id") id: Long): Response<StackStatusResponse>

    @GET("panel-status")
    suspend fun getPanelStatusById(@Query("panel_id") id: Long): Response<PanelStatusResponse>


    @PUT("panel-status")
    suspend fun setPanelStatus(@Body panel: PanelStatusResponse): Response<GeneralPutResponse>


    @PUT("stack-status")
    suspend fun setStackStatus(@Body stack: StackStatusResponse): Response<GeneralPutResponse>


    @GET("panel-drawing")
    suspend fun getPanelDrawingById(@Query("panel_id") id: Long): Response<ResponseBody>

    @GET("plan-drawing")
    suspend fun getPlanDrawingById(@Query("plan_id") id: Long): Response<ResponseBody>

    @GET("validate-item")
    suspend fun getItemByValidatingCode(@Query("qr_code") encodedCode: String): Response<ValidateItemResponse>



    companion object {
        private const val BASE_URL = BuildConfig.SERVER_URL


        lateinit var retrofit: Retrofit

        fun create(context: Context, preferenceUtils: PreferenceUtils): ProjectsService {

            val cookieJar =
                PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(context))
            val client = OkHttpClient.Builder()
                .cookieJar(cookieJar)
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(NoInternetInterceptor(context,400, context.getString(
                    R.string.error_msg_no_internet)))
                .addInterceptor(object : Interceptor {
                    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                        // Put access token into header
                        val accessToken = preferenceUtils.getAccessToken()
                        val request: Request = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer $accessToken")
                            .build()
                        Log.d("ProjectsService", "access_token = $accessToken")
                        return chain.proceed(request)
                    }
                }).build()

            retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(ProjectsService::class.java)
        }
    }
}