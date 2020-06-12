package com.example.githubsearch.util

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.example.githubsearch.R
import com.example.githubsearch.broadcast.ReminderReceiver
import com.example.githubsearch.ui.preference.PreferenceFragment

object UtilSharedPreference {

    /*
    * Configure Language Based on Shared Preference
    * Default: Current System Language
    * */
    private fun settingLanguage(context: Context, sharedPreferences: SharedPreferences) {
        // read from shared preference
        // if not set, use system language
        val language = sharedPreferences.getString(
            context.getString(R.string.preference_language_key),
            UtilLanguage.getDefaultLanguage(context)
        )
        UtilLanguage.setLanguage(context, language as String)
    }

    /*
    * Configure Reminder Based on Shared Preference
    * Default: False
    * */
    private fun settingReminder(context: Context, sharedPreferences: SharedPreferences) {
        val reminderReceiver = ReminderReceiver()
        val isReminderActive = sharedPreferences.getBoolean(
            context.getString(R.string.preference_reminder_key),
            PreferenceFragment.DEFAULT_REMINDER
        )
        reminderReceiver.setReminder(context, isReminderActive)
    }

    /*
    * SharedPreference Load Settings
    * Settings:
    * - Language
    * - Reminder
    * */
    fun loadPreferenceSettings(context: Context) {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        settingLanguage(context, sharedPreferences)
        settingReminder(context, sharedPreferences)
    }

}