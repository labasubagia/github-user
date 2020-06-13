package com.example.githubsearch.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.example.githubsearch.database.LocalDatabase
import com.example.githubsearch.model.UserDetail
import com.example.githubsearch.repository.LocalFavoriteUserRepository
import com.example.githubsearch.widget.FavoriteUserWidget


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

        // Local Repository
        private lateinit var repository: LocalFavoriteUserRepository

        init {
            uriMatcher.addURI(AUTHORITY, TABLE_NAME, USER)
            uriMatcher.addURI(AUTHORITY, "$TABLE_NAME/*", USER_NAME)
        }
    }

    override fun onCreate(): Boolean {

        // Init Repository
        context?.let {
            LocalDatabase.getDatabase(it).favoriteUserDao()
        }?.let {
            repository = LocalFavoriteUserRepository(it)
        }

        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {

        var cursor: Cursor? = null

        // Create Thread
        // Because this error when call from widget
        val thread = Thread(Runnable {
            cursor = when (uriMatcher.match(uri)) {
                USER -> repository.getAllCursor()
                USER_NAME -> repository.getByUsernameCursor(uri.lastPathSegment.toString())
                else -> null
            }
        })

        // Run thread
        try {
            thread.start()
            thread.join()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        cursor?.setNotificationUri(context?.contentResolver, uri)
        return cursor
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

        // Delete
        val deleted = when (uriMatcher.match(uri)) {
            USER_NAME -> repository.deleteByUsername(uri.lastPathSegment.toString())
            else -> 0
        }
        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        // Refresh Widget
        FavoriteUserWidget.sendRefreshBroadcast(context as Context)

        return deleted
    }
}
