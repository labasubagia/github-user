package com.example.githubsearch.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubsearch.model.CustomError
import com.example.githubsearch.model.UserDetail
import com.example.githubsearch.network.NetworkConfig
import com.example.githubsearch.util.Util.getClientError
import com.example.githubsearch.util.Util.getServerError
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {

    private val user = MutableLiveData<UserDetail>()
    private val error = MutableLiveData<CustomError>()

    // request user's detail information
    fun setUserDetail(username: String) {
        NetworkConfig().api().userDetail(username).enqueue(object : Callback<UserDetail> {
            override fun onFailure(call: Call<UserDetail>, t: Throwable) {
                error.value = getClientError()
            }

            override fun onResponse(call: Call<UserDetail>, response: Response<UserDetail>) {
                if (response.isSuccessful) {
                    error.value = null
                    user.value = response.body()
                } else {
                    error.value = getServerError()
                }
            }

        })
    }

    fun getUserDetail() = user

    fun getError() = error
}
