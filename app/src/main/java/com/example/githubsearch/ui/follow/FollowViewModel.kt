package com.example.githubsearch.ui.follow

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubsearch.model.User
import com.example.githubsearch.network.NetworkConfig
import com.example.githubsearch.util.Util.REQUEST_ERROR_API_PROBLEM
import com.example.githubsearch.util.Util.REQUEST_ERROR_NETWORK_FAILURE
import com.example.githubsearch.util.Util.getRequestErrorResourceInt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowViewModel : ViewModel() {


    private val userFollowers = MutableLiveData<ArrayList<User>>()
    private val userFollowing = MutableLiveData<ArrayList<User>>()
    private val errorMessageInt = MutableLiveData<Int>()

    // request user's followers
    fun setUserFollower(username: String) {
        NetworkConfig().api().userFollowers(username).enqueue(object : Callback<ArrayList<User>> {
            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                errorMessageInt.value = getRequestErrorResourceInt(REQUEST_ERROR_NETWORK_FAILURE)
            }

            override fun onResponse(
                call: Call<ArrayList<User>>,
                response: Response<ArrayList<User>>
            ) {
                if (response.isSuccessful) {
                    errorMessageInt.value = null
                    userFollowers.value = response.body()
                } else {
                    errorMessageInt.value = getRequestErrorResourceInt(REQUEST_ERROR_API_PROBLEM)
                }
            }
        })
    }

    // request user's following
    fun setUserFollowing(username: String) {
        NetworkConfig().api().userFollowing(username).enqueue(object : Callback<ArrayList<User>> {
            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                errorMessageInt.value = getRequestErrorResourceInt(REQUEST_ERROR_NETWORK_FAILURE)
            }

            override fun onResponse(
                call: Call<ArrayList<User>>,
                response: Response<ArrayList<User>>
            ) {
                if (response.isSuccessful) {
                    errorMessageInt.value = null
                    userFollowing.value = response.body()
                } else {
                    errorMessageInt.value = getRequestErrorResourceInt(REQUEST_ERROR_API_PROBLEM)
                }
            }
        })
    }

    fun getUserFollowers() = userFollowers

    fun getUserFollowing() = userFollowing

    fun getErrorMessageInt() = errorMessageInt
}
