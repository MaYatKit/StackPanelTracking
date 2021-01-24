package com.framecad.plum.utils

import android.content.Context
import android.preference.PreferenceManager
import android.util.Log
import android.util.Size
import com.framecad.plum.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferenceUtils @Inject constructor(@ApplicationContext val context: Context) {

    fun getCameraXTargetResolution(): Size? {
        val prefKey = context.getString(R.string.pref_key_camerax_target_resolution)
        val sharedPreferences = context.getSharedPreferences(
            getDefaultSharedPreferencesName(context),
            getDefaultSharedPreferencesMode()
        )
        return try {
            Size.parseSize(sharedPreferences.getString(prefKey, null))
        } catch (e: Exception) {
            null
        }
    }

    fun getAccessToken(): String {
        val prefKey = context.getString(R.string.pref_key_access_token)
        val sharedPreferences = context.getSharedPreferences(
            getDefaultSharedPreferencesName(context),
            getDefaultSharedPreferencesMode()
        )
        return sharedPreferences.getString(prefKey, "") ?: ""
    }

    fun setAccessToken(newAccessToken: String) {
        val prefKey = context.getString(R.string.pref_key_access_token)
        val sharedPreferences = context.getSharedPreferences(
            getDefaultSharedPreferencesName(context),
            getDefaultSharedPreferencesMode()
        )
        sharedPreferences.edit().putString(prefKey, newAccessToken).apply()
    }

    fun clearAccessToken() {
        val prefKey = context.getString(R.string.pref_key_access_token)
        val sharedPreferences = context.getSharedPreferences(
            getDefaultSharedPreferencesName(context),
            getDefaultSharedPreferencesMode()
        )
        sharedPreferences.edit().remove(prefKey).apply()
    }

    fun getUserName(): String? {
        val prefKey = context.getString(R.string.pref_key_user_name)
        val sharedPreferences = context.getSharedPreferences(
            getDefaultSharedPreferencesName(context),
            getDefaultSharedPreferencesMode()
        )
        return sharedPreferences.getString(prefKey, null)
    }

    fun setUserName(userName: String) {
        val prefKey = context.getString(R.string.pref_key_user_name)
        val sharedPreferences = context.getSharedPreferences(
            getDefaultSharedPreferencesName(context),
            getDefaultSharedPreferencesMode()
        )
        sharedPreferences.edit().putString(prefKey, userName).apply()
    }

    private fun getDefaultSharedPreferencesName(context: Context): String {
        return context.packageName + "_preferences"
    }

    private fun getDefaultSharedPreferencesMode(): Int {
        return Context.MODE_PRIVATE
    }
}