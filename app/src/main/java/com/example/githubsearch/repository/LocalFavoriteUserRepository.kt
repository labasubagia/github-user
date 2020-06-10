package com.example.githubsearch.repository

import androidx.lifecycle.LiveData
import com.example.githubsearch.database.FavoriteUserDao
import com.example.githubsearch.model.UserDetail

class LocalFavoriteUserRepository(private val favoriteUserDao: FavoriteUserDao) {

    val favorites: LiveData<List<UserDetail>> = favoriteUserDao.getAll()

    fun insert(user: UserDetail) {
        favoriteUserDao.insert(user)
    }

    fun delete(user: UserDetail) {
        favoriteUserDao.delete(user)
    }

    fun searchByUsername(username: String): UserDetail {
        return favoriteUserDao.getByUsername(username)
    }
}