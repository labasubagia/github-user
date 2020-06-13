package com.example.consumerapp.util

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.example.consumerapp.R
import com.example.consumerapp.util.UtilLanguage.getDefaultLanguage
import com.example.consumerapp.util.UtilLanguage.setLanguage

object UtilSharedPreference {

    /*
    * Configure Language Based on Shared Preference
    * Default: Current System Language
    * */
    private fun settingLanguage(context: Context, sharedPreferences: SharedPreferences) {
        // read from shared preference
        // if not set, use system language
        val language = sharedPreferences.getString(
            context.getString(R.string.preference_language_key),
            getDefaultLanguage(context)
        )
        setLanguage(context, language as String)
    }

    /*
    * SharedPreference Load Settings
    * Settings:
    * - Language
    * - Reminder
    * */
    fun loadPreferenceSettings(context: Context) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        settingLanguage(context, sharedPreferences)
    }

}