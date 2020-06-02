package com.example.githubsearch.ui.follow

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubsearch.model.CustomError
import com.example.githubsearch.model.User
import com.example.githubsearch.network.NetworkConfig
import com.example.githubsearch.util.Util.getClientError
import com.example.githubsearch.util.Util.getServerError
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowViewModel : ViewModel() {


    private val userFollowers = MutableLiveData<ArrayList<User>>()
    private val userFollowing = MutableLiveData<ArrayList<User>>()
    private val error = MutableLiveData<CustomError>()

    // request user's followers
    fun setUserFollower(username: String) {
        NetworkConfig().api().userFollowers(username).enqueue(object : Callback<ArrayList<User>> {
            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                error.value = getClientError()
            }

            override fun onResponse(
                call: Call<ArrayList<User>>,
                response: Response<ArrayList<User>>
            ) {
                if (response.isSuccessful) {
                    error.value = null
                    userFollowers.value = response.body()
                } else {
                    error.value = getServerError()
                }
            }
        })
    }

    // request user's following
    fun setUserFollowing(username: String) {
        NetworkConfig().api().userFollowing(username).enqueue(object : Callback<ArrayList<User>> {
            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                error.value = getClientError()
            }

            override fun onResponse(
                call: Call<ArrayList<User>>,
                response: Response<ArrayList<User>>
            ) {
                if (response.isSuccessful) {
                    error.value = null
                    userFollowing.value = response.body()
                } else {
                    error.value = getServerError()
                }
            }
        })
    }

    fun getUserFollowers() = userFollowers

    fun getUserFollowing() = userFollowing

    fun getError() = error
}
