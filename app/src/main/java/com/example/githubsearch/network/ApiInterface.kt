package com.example.githubsearch.network

import com.example.githubsearch.model.Search
import com.example.githubsearch.model.User
import com.example.githubsearch.model.UserDetail
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET("search/users")
    fun userSearch(@Query("q") username: String): Call<Search>

    @GET("users/{username}")
    fun userDetail(@Path("username") username: String): Call<UserDetail>

    @GET("users/{username}/followers")
    fun userFollowers(@Path("username") username: String): Call<ArrayList<User>>

    @GET("users/{username}/following")
    fun userFollowing(@Path("username") username: String): Call<ArrayList<User>>
}