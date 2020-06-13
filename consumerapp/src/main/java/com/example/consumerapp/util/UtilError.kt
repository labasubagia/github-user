package com.example.consumerapp.util

import com.example.consumerapp.R
import com.example.consumerapp.model.CustomError

object UtilError {
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
}