package com.example.consumerapp.network

import com.example.consumerapp.model.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {

    @GET("users/{username}/followers")
    fun userFollowers(@Path("username") username: String): Call<ArrayList<User>>

    @GET("users/{username}/following")
    fun userFollowing(@Path("username") username: String): Call<ArrayList<User>>
}