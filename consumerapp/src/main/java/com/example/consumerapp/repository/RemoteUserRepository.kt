package com.example.consumerapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.consumerapp.model.CustomError
import com.example.consumerapp.model.User
import com.example.consumerapp.network.NetworkConfig
import com.example.consumerapp.util.UtilError.getClientError
import com.example.consumerapp.util.UtilError.getServerError
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RemoteUserRepository private constructor() {

    // make singleton
    companion object {
        val instance by lazy { RemoteUserRepository() }
    }

    // retrofit
    private val webservice = NetworkConfig().api()

    val followers = MutableLiveData<ArrayList<User>>()
    val followersError = MutableLiveData<CustomError>()

    val following = MutableLiveData<ArrayList<User>>()
    val followingError = MutableLiveData<CustomError>()

    // follower by username (login field)
    fun getFollowers(username: String): LiveData<ArrayList<User>> {
        followersError.value = null
        followers.value = null
        webservice.userFollowers(username).enqueue(object : Callback<ArrayList<User>> {
            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                followersError.value = getClientError()
            }

            override fun onResponse(
                call: Call<ArrayList<User>>,
                response: Response<ArrayList<User>>
            ) {
                if (response.isSuccessful) {
                    followers.value = response.body()
                } else {
                    followersError.value = getServerError()
                }
            }
        })
        return followers
    }

    // following by username (login field)
    fun getFollowing(username: String): LiveData<ArrayList<User>> {
        following.value = null
        followingError.value = null
        webservice.userFollowing(username).enqueue(object : Callback<ArrayList<User>> {
            override fun onFailure(call: Call<ArrayList<User>>, t: Throwable) {
                followingError.value = getClientError()
            }

            override fun onResponse(
                call: Call<ArrayList<User>>,
                response: Response<ArrayList<User>>
            ) {
                if (response.isSuccessful) {
                    following.value = response.body()
                } else {
                    followingError.value = getServerError()
                }
            }
        })
        return following
    }

}