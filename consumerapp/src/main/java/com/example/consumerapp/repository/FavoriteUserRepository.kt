package com.example.consumerapp.repository

import android.content.Context
import android.content.UriMatcher
import android.net.Uri
import com.example.consumerapp.database.MappingHelper
import com.example.consumerapp.model.UserDetail


class FavoriteUserRepository(private val context: Context) {

    companion object {
        private const val AUTHORITY = "com.example.githubsearch"
        private const val SCHEME = "content"
        private const val TABLE_NAME = UserDetail.TABLE_NAME

        private const val USER = 1
        private const val USER_NAME = 2

        val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
            .authority(AUTHORITY)
            .appendPath(TABLE_NAME)
            .build()
        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)


        init {
            uriMatcher.addURI(AUTHORITY, TABLE_NAME, USER)
            uriMatcher.addURI(AUTHORITY, "$TABLE_NAME/*", USER_NAME)
        }
    }

    fun getAll(): ArrayList<UserDetail> {
        val list = ArrayList<UserDetail>()
        val cursor = context.contentResolver.query(
            CONTENT_URI,
            null,
            null,
            null,
            null
        )
        cursor?.let {
            list.addAll(MappingHelper.mapCursorToArrayList(cursor))
        }
        return list
    }

    fun delete(username: String): Int {
        val uriWithUsername = Uri.parse("$CONTENT_URI/$username")
        return context.contentResolver.delete(uriWithUsername, null, null)
    }

    fun searchByUsername(username: String): UserDetail? {
        val uriWithUsername = Uri.parse("$CONTENT_URI/$username")
        val cursor = context.contentResolver.query(
            uriWithUsername,
            null,
            null,
            null,
            null
        )
        cursor?.let {
            return MappingHelper.mapCursorToObject(cursor)
        }
        return null
    }

}