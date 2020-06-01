package com.example.githubsearch.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubsearch.model.UserDetail
import com.example.githubsearch.network.NetworkConfig
import com.example.githubsearch.util.Util.REQUEST_ERROR_API_PROBLEM
import com.example.githubsearch.util.Util.REQUEST_ERROR_NETWORK_FAILURE
import com.example.githubsearch.util.Util.getRequestErrorResourceInt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {

    private val user = MutableLiveData<UserDetail>()
    private val errorMessageInt = MutableLiveData<Int>()

    // request user's detail information
    fun setUserDetail(username: String) {
        NetworkConfig().api().userDetail(username).enqueue(object : Callback<UserDetail> {
            override fun onFailure(call: Call<UserDetail>, t: Throwable) {
                errorMessageInt.value = getRequestErrorResourceInt(REQUEST_ERROR_NETWORK_FAILURE)
            }

            override fun onResponse(call: Call<UserDetail>, response: Response<UserDetail>) {
                if (response.isSuccessful) {
                    errorMessageInt.value = null
                    user.value = response.body()
                } else {
                    errorMessageInt.value = getRequestErrorResourceInt(REQUEST_ERROR_API_PROBLEM)
                }
            }

        })
    }

    fun getUserDetail() = user

    fun getErrorMessageInt() = errorMessageInt
}
