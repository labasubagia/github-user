package com.example.githubsearch.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubsearch.model.CustomError
import com.example.githubsearch.model.Search
import com.example.githubsearch.network.NetworkConfig
import com.example.githubsearch.util.Util.getClientError
import com.example.githubsearch.util.Util.getServerError
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val foundUsers = MutableLiveData<Search>()
    private val error = MutableLiveData<CustomError>()

    fun searchUsers(username: String) {

        NetworkConfig().api().userSearch(username).enqueue(object : Callback<Search> {

            override fun onFailure(call: Call<Search>, t: Throwable) {
                error.value = getClientError()
            }

            override fun onResponse(call: Call<Search>, response: Response<Search>) {
                if (response.isSuccessful) {
                    error.value = null
                    foundUsers.value = response.body()
                } else {
                    error.value = getServerError()
                }
            }
        })
    }

    fun getFoundUsers() = foundUsers

    fun getError() = error
}
