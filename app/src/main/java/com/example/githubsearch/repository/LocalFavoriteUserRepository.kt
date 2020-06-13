package com.example.githubsearch.repository

import com.example.githubsearch.database.FavoriteUserDao
import com.example.githubsearch.model.UserDetail

class LocalFavoriteUserRepository(private val favoriteUserDao: FavoriteUserDao) {

    var favorites = favoriteUserDao.getAll()

    fun insert(user: UserDetail) =
        favoriteUserDao.insert(user)

    fun getByUsername(username: String) =
        favoriteUserDao.getByUsername(username)

    fun deleteByUsername(username: String) =
        favoriteUserDao.deleteByUsername(username)


    // This Below for mostly content provider
    // Some Content Provider method need cursor

    fun getAllCursor() = favoriteUserDao.getAllCursor()

    fun getByUsernameCursor(username: String) =
        favoriteUserDao.getByUsernameCursor(username)

}