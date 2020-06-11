package com.example.githubsearch.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.githubsearch.database.LocalDatabase
import com.example.githubsearch.model.UserDetail
import com.example.githubsearch.repository.LocalFavoriteUserRepository

class FavoriteUserProvider : ContentProvider() {

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

        val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var favoriteUserRepo: LocalFavoriteUserRepository

        init {
            uriMatcher.addURI(AUTHORITY, TABLE_NAME, USER)
            uriMatcher.addURI(AUTHORITY, "$TABLE_NAME/*", USER_NAME)
        }
    }

    override fun onCreate(): Boolean {
        val favoriteUserDao = context?.let { LocalDatabase.getDatabase(it).favoriteUserDao() }
        favoriteUserDao?.let {
            favoriteUserRepo = LocalFavoriteUserRepository(favoriteUserDao)
        }
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        return when (uriMatcher.match(uri)) {
            USER -> favoriteUserRepo.getAllCursor()
            USER_NAME -> favoriteUserRepo.searchByUsernameCursor(uri.lastPathSegment.toString())
            else -> null
        }
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        return 0
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val deleted = when (uriMatcher.match(uri)) {
            USER_NAME -> favoriteUserRepo.deleteByUsername(uri.lastPathSegment.toString())
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)
        return deleted
    }
}
