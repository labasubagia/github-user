package com.example.githubsearch.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = UserDetail.TABLE_NAME)
data class UserDetail(

    @ColumnInfo(name = NAME)
    val name: String? = null,

    @PrimaryKey
    @ColumnInfo(name = LOGIN)
    val login: String,

    @ColumnInfo(name = AVATAR_URL)
    val avatar_url: String? = null,

    @ColumnInfo(name = COMPANY)
    val company: String? = null,

    @ColumnInfo(name = LOCATION)
    val location: String? = null,

    @ColumnInfo(name = PUBLIC_REPOS)
    val public_repos: Int = 0,

    @ColumnInfo(name = FOLLOWERS)
    val followers: Int = 0,

    @ColumnInfo(name = FOLLOWING)
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