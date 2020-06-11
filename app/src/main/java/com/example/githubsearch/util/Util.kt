package com.example.githubsearch.util

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import androidx.core.os.ConfigurationCompat
import com.example.githubsearch.R
import com.example.githubsearch.model.CustomError
import java.text.NumberFormat
import java.util.*

object Util {

    // error type
    private const val REQUEST_CLIENT_ERROR = "request_client_error"
    const val REQUEST_SERVER_ERROR = "request_server_error"


    // function to give request error a specific message
    private fun getErrorRequestResourceString(requestErrorType: String): Int {
        return when (requestErrorType) {
            REQUEST_CLIENT_ERROR -> R.string.request_network_failure
            REQUEST_SERVER_ERROR -> R.string.request_api_problem
            else -> R.string.request_unknown_fail
        }
    }

    // function client error
    fun getClientError() = CustomError(
        REQUEST_CLIENT_ERROR,
        getErrorRequestResourceString(REQUEST_CLIENT_ERROR)
    )

    // function server error
    fun getServerError() = CustomError(
        REQUEST_SERVER_ERROR,
        getErrorRequestResourceString(REQUEST_SERVER_ERROR)
    )

    // function to format thousand number string
    fun numberFormat(number: Int): String {
        // use comma separator
        val formatter = NumberFormat.getInstance(Locale.US)
        return formatter.format(number)
    }


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
            // still looking for better solution
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
