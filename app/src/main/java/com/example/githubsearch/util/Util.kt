package com.example.githubsearch.util

import android.view.View
import com.example.githubsearch.R

object Util {

    // error type
    const val REQUEST_ERROR_NETWORK_FAILURE = "request_failure"
    const val REQUEST_ERROR_API_PROBLEM = "request_api_problem"


    // function to make views visible or gone
    fun showView(views: ArrayList<View>, isShow: Boolean = true) {
        views.forEach {
            it.visibility = if (isShow) View.VISIBLE else View.GONE
        }
    }

    // function to give request error a specific message
    fun getRequestErrorResourceInt(requestErrorType: String): Int {
        return when (requestErrorType) {
            REQUEST_ERROR_NETWORK_FAILURE -> R.string.request_network_failure
            REQUEST_ERROR_API_PROBLEM -> R.string.request_api_problem
            else -> R.string.request_unknown_fail
        }
    }
}
