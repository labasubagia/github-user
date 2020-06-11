package com.example.consumerapp.model

data class UserDetail(
    val name: String? = null,
    val login: String,
    val avatar_url: String? = null,
    val company: String? = null,
    val location: String? = null,
    val public_repos: Int = 0,
    val followers: Int = 0,
    val following: Int = 0
) {
    companion object {
        const val TABLE_NAME = "favorite_user"

        const val NAME = "name"
        const val LOGIN = "login"
        const val AVATAR_URL = "avatar_url"
        const val COMPANY = "company"
        const val LOCATION = "location"
        const val PUBLIC_REPOS = "public_repos"
        const val FOLLOWERS = "followers"
        const val FOLLOWING = "following"
    }
}