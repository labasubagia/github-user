package com.example.githubsearch.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.githubsearch.model.CustomError
import com.example.githubsearch.model.Search
import com.example.githubsearch.model.User
import com.example.githubsearch.network.NetworkConfig
import com.example.githubsearch.util.UtilError.getClientError
import com.example.githubsearch.util.UtilError.getServerError
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.githubsearch.model.UserDetail as Detail

class RemoteUserRepository private constructor() {

    // make singleton
    companion object {
        val instance by lazy { RemoteUserRepository() }
    }

    // retrofit
    private val webservice = NetworkConfig().api()


    // data & error
    val detail = MutableLiveData<Detail>()
    val detailError = MutableLiveData<CustomError>()

    val followers = MutableLiveData<ArrayList<User>>()
    val followersError = MutableLiveData<CustomError>()

    val following = MutableLiveData<ArrayList<User>>()
    val followingError = MutableLiveData<CustomError>()

    val found = MutableLiveData<Search>()
    val foundError = MutableLiveData<CustomError>()


    // get detail of user by username (login field)
    fun getDetail(username: String): LiveData<Detail> {
        detailError.value = null
        detail.value = null

        webservice.userDetail(username).enqueue(object : Callback<Detail> {
            override fun onFailure(call: Call<Detail>, t: Throwable) {
                detailError.value = getClientError()
            }

            override fun onResponse(call: Call<Detail>, response: Response<Detail>) {
                if (response.isSuccessful) {
                    detail.value = response.body()
                } else {
                    detailError.value = getServerError()
                }
            }
        })
        return detail
    }

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

    // search user by username (login field)
    fun getFound(username: String): LiveData<Search> {
        foundError.value = null
        found.value = null
        webservice.userSearch(username).enqueue(object : Callback<Search> {
            override fun onFailure(call: Call<Search>, t: Throwable) {
                foundError.value = getClientError()
            }

            override fun onResponse(call: Call<Search>, response: Response<Search>) {
                if (response.isSuccessful) {
                    found.value = response.body()
                } else {
                    foundError.value = getServerError()
                }
            }
        })
        return found
    }
}