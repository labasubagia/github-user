package com.example.githubsearch.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.githubsearch.model.UserDetail

@Dao
interface FavoriteUserDao {
    @Query("SELECT * FROM favorite_user")
    fun getAll(): LiveData<List<UserDetail>>

    @Query("SELECT * FROM favorite_user WHERE login = :username")
    fun getByUsername(username: String): UserDetail

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: UserDetail)

    @Delete
    fun delete(user: UserDetail)
}