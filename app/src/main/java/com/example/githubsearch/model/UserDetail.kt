package com.example.githubsearch.model

data class UserDetail(
    val name: String? = null,
    val login: String? = null,
    val avatar_url: String? = null,
    val company: String? = null,
    val location: String? = null,
    val public_repos: Int = 0,
    val followers: Int = 0,
    val following: Int = 0
)