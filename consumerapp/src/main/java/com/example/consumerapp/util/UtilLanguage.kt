package com.example.consumerapp.util

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import androidx.core.os.ConfigurationCompat
import com.example.consumerapp.R
import java.util.*

object UtilLanguage {
    // set app language
    fun setLanguage(context: Context, language: String) {
        with(context) {
            // select code based on language
            val languageCode = when (language) {
                getString(R.string.language_indonesia) ->
                    getString(R.string.language_indonesia_code)
                else ->
                    getString(R.string.language_english_code)
            }

            // set locale
            val locale = Locale(languageCode)
            Locale.setDefault(locale)

            // add locale to config
            val config = Configuration()
            config.setLocale(locale)

            // update configuration, change language
            // NOTE: Still looking for better solution
            @Suppress("DEPRECATION")
            resources.updateConfiguration(config, resources.displayMetrics)
        }
    }

    // get current system language
    fun getDefaultLanguage(context: Context): String {
        with(context) {
            // get system locales
            val currentSystemLanguage =
                ConfigurationCompat.getLocales(Resources.getSystem().configuration)

            // select string based on current locale
            return when (currentSystemLanguage.get(0).language) {
                getString(R.string.language_indonesia_code) ->
                    getString(R.string.language_indonesia)
                else ->
                    getString(R.string.language_english)
            }
        }
    }
}