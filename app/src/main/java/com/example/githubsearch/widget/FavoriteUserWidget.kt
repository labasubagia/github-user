package com.example.githubsearch.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import androidx.core.net.toUri
import com.example.githubsearch.R


class FavoriteUserWidget : AppWidgetProvider() {

    companion object {

        const val EXTRA_ITEM = "com.example.githubsearch.EXTRA_ITEM"

        fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            // Set Service
            val intent = Intent(context, StackWidgetService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()

            // Set layout
            val views = RemoteViews(context.packageName, R.layout.widget_favorite_user)
            views.setRemoteAdapter(R.id.stack_view, intent)
            views.setEmptyView(R.id.stack_view, R.id.empty_view)

            // Update
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        /*
        * Send Broadcast to refresh widget
        * Implementation -> Database onInsert, onDelete
        * */
        fun sendRefreshBroadcast(context: Context) {
            val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
            intent.component = ComponentName(context, FavoriteUserWidget::class.java)
            context.sendBroadcast(intent)
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {

            // Update Broadcast Received
            AppWidgetManager.ACTION_APPWIDGET_UPDATE -> {
                val manager = AppWidgetManager.getInstance(context)
                val component = ComponentName(context, FavoriteUserWidget::class.java)
                manager.notifyAppWidgetViewDataChanged(
                    manager.getAppWidgetIds(component),
                    R.id.stack_view
                )
            }
        }
        super.onReceive(context, intent)
    }
}
