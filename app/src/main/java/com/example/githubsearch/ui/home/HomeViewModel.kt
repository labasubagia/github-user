package com.example.githubsearch.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubsearch.model.Search
import com.example.githubsearch.network.NetworkConfig
import com.example.githubsearch.util.Util.REQUEST_ERROR_API_PROBLEM
import com.example.githubsearch.util.Util.REQUEST_ERROR_NETWORK_FAILURE
import com.example.githubsearch.util.Util.getRequestErrorResourceInt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val foundUsers = MutableLiveData<Search>()
    private val errorMessageInt = MutableLiveData<Int>()

    fun searchUsers(username: String) {

        NetworkConfig().api().userSearch(username).enqueue(object : Callback<Search> {

            override fun onFailure(call: Call<Search>, t: Throwable) {
                errorMessageInt.value = getRequestErrorResourceInt(REQUEST_ERROR_NETWORK_FAILURE)
            }

            override fun onResponse(call: Call<Search>, response: Response<Search>) {
                if (response.isSuccessful) {
                    errorMessageInt.value = null
                    foundUsers.value = response.body()
                } else {
                    errorMessageInt.value = getRequestErrorResourceInt(REQUEST_ERROR_API_PROBLEM)
                }
            }
        })
    }

    fun getFoundUsers() = foundUsers

    fun getErrorMessageInt() = errorMessageInt
}
