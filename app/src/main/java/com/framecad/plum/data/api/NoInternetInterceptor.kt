package com.framecad.plum.data.api

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.widget.Toast
import com.framecad.plum.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull

class NoInternetInterceptor(val context: Context, private val errorCode: Int, private val msg: String): Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isConnected()) {
            //Handle no internet
//            if (msg.isNotEmpty()){
//                MainScope().launch {
//                    withContext(Dispatchers.Main){
//                        Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
//                    }
//                }
//            }

            val emptyResponseBody = ResponseBody.create(
                "application/json".toMediaTypeOrNull(),
                ("{\n" +
                        "  \"error_code\": $errorCode,\n" +
                        "  \"error_message\": \"$msg\"\n" +
                        "}\n").toByteArray())
            return okhttp3.Response.Builder()
                .code(400)
                .body(emptyResponseBody)
                .protocol(Protocol.HTTP_2)
                .message(msg)
                .request(chain.request())
                .build()
        }
        return chain.proceed(chain.request())
    }

    private fun isConnected(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            val nwInfo = connectivityManager.activeNetworkInfo ?: return false
            return nwInfo.isConnected
        } else {
            val nw = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                //for other device how are able to connect with Ethernet
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                //for check internet over Bluetooth
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
        }
    }
}