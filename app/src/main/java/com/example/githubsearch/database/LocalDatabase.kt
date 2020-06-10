package com.example.githubsearch.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.githubsearch.model.UserDetail

@Database(entities = [UserDetail::class], version = 1)
abstract class LocalDatabase : RoomDatabase() {
    abstract fun favoriteUserDao(): FavoriteUserDao

    companion object {

        // make singleton
        @Volatile
        private var INSTANCE: LocalDatabase? = null

        // get singleton database
        fun getDatabase(context: Context): LocalDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    LocalDatabase::class.java,
                    "github_user_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}