package com.example.githubsearch.ui.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubsearch.model.User
import com.example.githubsearch.model.UserDetail
import com.example.githubsearch.network.NetworkConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel : ViewModel() {

    private val user = MutableLiveData<UserDetail>()
    private val userFollowers = MutableLiveData<ArrayList<User>>()
    private val userFollowing = MutableLiveData<ArrayList<User>>()
    private val errorMessage = MutableLiveData<String>()

    // request user's detail information
    private fun setUserDetail(username: String) {
        NetworkConfig().api().userDetail(username).enqueue(object : Callback<UserDetail> {
            override fun onFailure(call: Call<UserDetail>, t: Throwable) {
                errorMessage.value = t.message.toString()
            }

            override fun onResponse(call: Call<UserDetail>, response: Response<UserDetail>) {
                if (response.isSuccessful) {
                    errorMessage.value = null
                    user.value = response.body()
                } else {
                    errorMessage.value = "Error API ${response.code()}: ${response.message()}"
                }
            }

        })
    }

    // request user's followers
    private fun setUserFollower(username: String) {
        NetworkConfig().api().userFollowers(username).enqueue(object : Callback<ArrayList<User>> {
            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                errorMessage.value = t.message.toString()
            }

            override fun onResponse(
                call: Call<ArrayList<User>>,
                response: Response<ArrayList<User>>
            ) {
                if (response.isSuccessful) {
                    errorMessage.value = null
                    userFollowers.value = response.body()
                } else {
                    errorMessage.value = "Error API ${response.code()}: ${response.message()}"
                }
            }
        })
    }

    // request user's following
    private fun setUserFollowing(username: String) {
        NetworkConfig().api().userFollowing(username).enqueue(object : Callback<ArrayList<User>> {
            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                errorMessage.value = t.message.toString()
            }

            override fun onResponse(
                call: Call<ArrayList<User>>,
                response: Response<ArrayList<User>>
            ) {
                if (response.isSuccessful) {
                    errorMessage.value = null
                    userFollowing.value = response.body()
                } else {
                    errorMessage.value = "Error API ${response.code()}: ${response.message()}"
                }
            }
        })
    }

    // set user
    fun setUser(username: String) {
        setUserDetail(username)
        setUserFollower(username)
        setUserFollowing(username)
    }

    fun getUserDetail() = user

    fun getUserFollowers() = userFollowers

    fun getUserFollowing() = userFollowing

    fun getErrorMessage() = errorMessage
}
