package com.example.githubsearch.broadcast

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.navigation.NavDeepLinkBuilder
import com.example.githubsearch.R
import com.example.githubsearch.activity.main.MainActivity
import java.util.*

class ReminderReceiver : BroadcastReceiver() {

    companion object {
        private const val REMINDER_ID = 100
    }

    override fun onReceive(context: Context, intent: Intent) {
        showReminderNotification(context)
    }

    /*
    * Show Notification
    * */
    private fun showReminderNotification(context: Context) {

        // Channel Info
        val channelId = "channel_1"
        val channelName = "daily_reminder"

        // Text Info
        val title = context.getString(R.string.notification_title)
        val message = context.getString(R.string.notification_message)

        // Sound & Vibrate
        val sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
        val vibrate = longArrayOf(1000, 1000, 1000, 1000, 1000)

        // Click Notification Go To App (HomeFragment)
        val pendingIntent = NavDeepLinkBuilder(context)
            .setComponentName(MainActivity::class.java)
            .setGraph(R.navigation.main_navigation)
            .setDestination(R.id.homeFragment)
            .createPendingIntent()

        // Notification Builder
        val builder = NotificationCompat.Builder(context, channelId)
            .apply {
                setContentIntent(pendingIntent)
                setSmallIcon(R.drawable.ic_notifications_black_24dp)
                setContentTitle(title)
                setContentText(message)
                setVibrate(vibrate)
                setSound(sound)
                setAutoCancel(true)
            }

        // Notification Manager
        val notificationManagerCompat =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        // Set Channel for Android Oreo and Up
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            channel.enableVibration(true)
            channel.vibrationPattern = vibrate
            builder.setChannelId(channelId)
            notificationManagerCompat.createNotificationChannel(channel)
        }

        // Show
        val notification = builder.build()
        notificationManagerCompat.notify(REMINDER_ID, notification)
    }

    /*
    * Activate Reminder
    * Time 9.00 AM
    * */
    private fun activateReminder(context: Context) {

        // Do Nothing when reminder already Active
        if (isReminderActive(context)) return

        // Activate Time
        val calendar = Calendar.getInstance()
        val activateTime = calendar.timeInMillis

        // Reminder Time
        calendar.apply {
            set(Calendar.HOUR_OF_DAY, 9)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
        }
        var reminderTime = calendar.timeInMillis

        // Time has past
        // Set time to Tomorrow
        if (reminderTime < activateTime) {
            reminderTime += AlarmManager.INTERVAL_DAY + 1
        }

        // Set Reminder
        val intent = Intent(context, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, REMINDER_ID, intent, 0)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            reminderTime,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    /*
    * Cancel Reminder
    * */
    private fun cancelReminder(context: Context) {

        // Do Nothing when reminder already Non-Active
        if (!isReminderActive(context)) return

        val intent = Intent(context, ReminderReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, REMINDER_ID, intent, 0)
        pendingIntent.cancel()
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }

    /*
    * Check Reminder isActive
    * */
    private fun isReminderActive(context: Context): Boolean {
        val intent = Intent(context, ReminderReceiver::class.java)
        return PendingIntent.getBroadcast(
            context,
            REMINDER_ID,
            intent,
            PendingIntent.FLAG_NO_CREATE
        ) != null
    }

    /*
    * Function to Set Reminder
    * */
    fun setReminder(context: Context, setActive: Boolean = true): String {
        return if (setActive) {
            activateReminder(context)
            context.getString(R.string.message_reminder_change_active)
        } else {
            cancelReminder(context)
            context.getString(R.string.message_reminder_change_deactivate)
        }
    }
}
