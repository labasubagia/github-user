package com.example.githubsearch.widget

import android.content.Context
import android.content.Intent
import android.os.Binder
import android.widget.AdapterView
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.example.githubsearch.R
import com.example.githubsearch.database.MappingHelper.mapCursorToArrayList
import com.example.githubsearch.model.UserDetail
import com.example.githubsearch.provider.FavoriteUserProvider

class StackRemoteViewsFactory(private val context: Context) :
    RemoteViewsService.RemoteViewsFactory {

    private val users = ArrayList<UserDetail>()

    override fun onCreate() {
        init()
    }

    override fun onDestroy() {
    }

    override fun onDataSetChanged() {
        val token = Binder.clearCallingIdentity()
        init()
        Binder.restoreCallingIdentity(token)
    }

    override fun getViewAt(position: Int): RemoteViews? {

        // Break When
        if (
        // Data Empty
            users.isEmpty()

            // Position Invalid
            || position == AdapterView.INVALID_POSITION

            // Cannot Get Data
            || null == users.getOrNull(position)
        )
            return null


        // Continue When User data Valid

        val user = users[position]

        val bitmap = Glide.with(context)
            .asBitmap()
            .load(user.avatar_url)
            .submit()
            .get()

        // Set View
        val remoteViews = RemoteViews(context.packageName, R.layout.widget_favorite_user_item)
        remoteViews.apply {
            setImageViewBitmap(R.id.img_profile, bitmap)
            setTextViewText(R.id.tv_username, user.login)
            setTextViewText(R.id.tv_name, user.name ?: context.getString(R.string.not_applicable))
        }

        val extras = bundleOf(
            FavoriteUserWidget.EXTRA_ITEM to position
        )
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        remoteViews.setOnClickFillInIntent(R.id.img_profile, fillInIntent)
        return remoteViews
    }

    override fun getCount() = users.size

    override fun getItemId(position: Int) = position.toLong()

    override fun getLoadingView(): RemoteViews? = null

    override fun hasStableIds(): Boolean = true

    override fun getViewTypeCount() = 1

    private fun init() {
        users.clear()
        val cursor = context.contentResolver.query(
            FavoriteUserProvider.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        cursor?.let {
            users.addAll(mapCursorToArrayList(it))
        }
        cursor?.close()
    }
}