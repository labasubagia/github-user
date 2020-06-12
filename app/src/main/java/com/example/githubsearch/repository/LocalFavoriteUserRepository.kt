package com.example.githubsearch.repository

import com.example.githubsearch.database.FavoriteUserDao
import com.example.githubsearch.model.UserDetail

class LocalFavoriteUserRepository(private val favoriteUserDao: FavoriteUserDao) {

    var favorites = favoriteUserDao.getAll()

    fun insert(user: UserDetail) =
        favoriteUserDao.insert(user)

    fun delete(user: UserDetail) =
        favoriteUserDao.delete(user)

    fun getByUsername(username: String) =
        favoriteUserDao.getByUsername(username)


    // For Content Provider

    fun getAllCursor() = favoriteUserDao.getAllCursor()

    fun searchByUsernameCursor(username: String) =
        favoriteUserDao.getByUsernameCursor(username)

    fun deleteByUsername(username: String) =
        favoriteUserDao.deleteByUsername(username)
}