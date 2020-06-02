package com.example.githubsearch.util

import com.example.githubsearch.R
import java.text.NumberFormat
import java.util.*

object Util {

    // error type
    const val REQUEST_ERROR_NETWORK_FAILURE = "request_failure"
    const val REQUEST_ERROR_API_PROBLEM = "request_api_problem"


    // function to give request error a specific message
    fun getRequestErrorResourceInt(requestErrorType: String): Int {
        return when (requestErrorType) {
            REQUEST_ERROR_NETWORK_FAILURE -> R.string.request_network_failure
            REQUEST_ERROR_API_PROBLEM -> R.string.request_api_problem
            else -> R.string.request_unknown_fail
        }
    }

    // function to format thousand number string
    fun numberFormat(number: Int): String {
        // use comma separator
        val formatter = NumberFormat.getInstance(Locale.US)
        return formatter.format(number)
    }
}
