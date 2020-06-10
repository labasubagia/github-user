package com.example.githubsearch.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_user")
data class UserDetail(

    @ColumnInfo(name = "name")
    val name: String? = null,

    @PrimaryKey
    @ColumnInfo(name = "login")
    val login: String,

    @ColumnInfo(name = "avatar_url")
    val avatar_url: String? = null,

    @ColumnInfo(name = "company")
    val company: String? = null,

    @ColumnInfo(name = "location")
    val location: String? = null,

    @ColumnInfo(name = "public_repos")
    val public_repos: Int = 0,

    @ColumnInfo(name = "followers")
    val followers: Int = 0,

    @ColumnInfo(name = "following")
    val following: Int = 0
)