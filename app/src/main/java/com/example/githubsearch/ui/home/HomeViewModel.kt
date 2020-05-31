package com.example.githubsearch.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubsearch.model.Search
import com.example.githubsearch.network.NetworkConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val foundUsers = MutableLiveData<Search>()
    private val errorMessage = MutableLiveData<String>()

    fun searchUsers(username: String) {

        NetworkConfig().api().userSearch(username).enqueue(object : Callback<Search> {

            override fun onFailure(call: Call<Search>, t: Throwable) {
                errorMessage.value = t.message.toString()
            }

            override fun onResponse(call: Call<Search>, response: Response<Search>) {
                if (response.isSuccessful) {
                    errorMessage.value = null
                    foundUsers.value = response.body()
                } else {
                    errorMessage.value = "Error API ${response.code()}: ${response.message()}"
                }
            }
        })
    }

    fun getFoundUsers() = foundUsers

    fun getErrorMessage() = errorMessage
}
