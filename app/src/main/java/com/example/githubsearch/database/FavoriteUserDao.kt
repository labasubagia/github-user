package com.example.githubsearch.database

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.githubsearch.model.UserDetail
import com.example.githubsearch.model.UserDetail.Companion.LOGIN
import com.example.githubsearch.model.UserDetail.Companion.TABLE_NAME

@Dao
interface FavoriteUserDao {
    @Query("SELECT * FROM $TABLE_NAME")
    fun getAll(): LiveData<List<UserDetail>>

    @Query("SELECT * FROM $TABLE_NAME")
    fun getAllCursor(): Cursor

    @Query("SELECT * FROM $TABLE_NAME WHERE $LOGIN = :username")
    fun getByUsername(username: String): UserDetail

    @Query("SELECT * FROM $TABLE_NAME WHERE $LOGIN = :username")
    fun getByUsernameCursor(username: String): Cursor

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: UserDetail): Long

    @Query("DELETE FROM $TABLE_NAME WHERE $LOGIN = :username")
    fun deleteByUsername(username: String): Int
}