package com.example.consumerapp.database

import android.database.Cursor
import com.example.consumerapp.model.UserDetail
import com.example.consumerapp.model.UserDetail.Companion.AVATAR_URL
import com.example.consumerapp.model.UserDetail.Companion.COMPANY
import com.example.consumerapp.model.UserDetail.Companion.FOLLOWERS
import com.example.consumerapp.model.UserDetail.Companion.FOLLOWING
import com.example.consumerapp.model.UserDetail.Companion.LOCATION
import com.example.consumerapp.model.UserDetail.Companion.LOGIN
import com.example.consumerapp.model.UserDetail.Companion.NAME
import com.example.consumerapp.model.UserDetail.Companion.PUBLIC_REPOS

object MappingHelper {

    fun mapCursorToArrayList(usersCursor: Cursor?): ArrayList<UserDetail> {
        val list = ArrayList<UserDetail>()
        usersCursor?.apply {
            while (moveToNext()) {
                val user = UserDetail(
                    name = getString(getColumnIndexOrThrow(NAME)),
                    login = getString(getColumnIndexOrThrow(LOGIN)),
                    avatar_url = getString(getColumnIndexOrThrow(AVATAR_URL)),
                    company = getString(getColumnIndexOrThrow(COMPANY)),
                    location = getString(getColumnIndexOrThrow(LOCATION)),
                    public_repos = getInt(getColumnIndexOrThrow(PUBLIC_REPOS)),
                    followers = getInt(getColumnIndexOrThrow(FOLLOWERS)),
                    following = getInt(getColumnIndexOrThrow(FOLLOWING))
                )
                list.add(user)
            }
        }
        return list
    }

    fun mapCursorToObject(userCursor: Cursor?): UserDetail {
        var user = UserDetail(login = "")
        userCursor?.apply {
            moveToFirst()
            user = UserDetail(
                name = getString(getColumnIndexOrThrow(NAME)),
                login = getString(getColumnIndexOrThrow(LOGIN)),
                avatar_url = getString(getColumnIndexOrThrow(AVATAR_URL)),
                company = getString(getColumnIndexOrThrow(COMPANY)),
                location = getString(getColumnIndexOrThrow(LOCATION)),
                public_repos = getInt(getColumnIndexOrThrow(PUBLIC_REPOS)),
                followers = getInt(getColumnIndexOrThrow(FOLLOWERS)),
                following = getInt(getColumnIndexOrThrow(FOLLOWING))
            )
        }
        return user
    }
}