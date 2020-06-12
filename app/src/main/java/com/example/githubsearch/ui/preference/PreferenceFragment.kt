package com.example.githubsearch.ui.preference

import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.preference.CheckBoxPreference
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.example.githubsearch.R
import com.example.githubsearch.broadcast.ReminderReceiver
import com.example.githubsearch.util.UtilFragment.showBackToHomeOptionMenu
import com.example.githubsearch.util.UtilLanguage.getDefaultLanguage
import com.example.githubsearch.util.UtilLanguage.setLanguage
import com.google.android.material.snackbar.Snackbar


class PreferenceFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    // Reminder
    private val reminderReceiver = ReminderReceiver()

    companion object {
        val DEFAULT_REMINDER = false
    }

    private lateinit var languageKey: String
    private lateinit var reminderKey: String

    private lateinit var languagePreference: ListPreference
    private lateinit var reminderPreference: CheckBoxPreference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        activity?.title = getString(R.string.page_preference)
        showBackToHomeOptionMenu(this)
        addPreferencesFromResource(R.xml.preferences)

        init()
        setValues()
    }

    override fun onDestroy() {
        showBackToHomeOptionMenu(this, false)
        super.onDestroy()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            // back to home
            android.R.id.home -> {
                findNavController().navigateUp()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    private fun init() {
        reminderKey = getString(R.string.preference_reminder_key)
        languageKey = getString(R.string.preference_language_key)

        languagePreference = findPreference<ListPreference>(languageKey) as ListPreference
        reminderPreference = findPreference<CheckBoxPreference>(reminderKey) as CheckBoxPreference
    }

    private fun setValues() {
        val sharedPreferences = preferenceManager.sharedPreferences

        val language =
            sharedPreferences.getString(languageKey, getDefaultLanguage(requireContext()))
        languagePreference.value = language
        setLanguage(requireContext(), language as String)

        val isReminderActive = sharedPreferences.getBoolean(reminderKey, DEFAULT_REMINDER)
        reminderPreference.isChecked = isReminderActive
        reminderReceiver.setReminder(requireContext(), isReminderActive)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {

        if (key == languageKey) {
            val language =
                sharedPreferences.getString(languageKey, getDefaultLanguage(requireContext()))
            languagePreference.value = language

            // change app language
            setLanguage(requireContext(), language as String)

            refreshAfterLanguageChange()

            // snack bar
            showSnackBar(getString(R.string.message_language_change, language))
        }

        if (key == reminderKey) {
            val isReminderActive = sharedPreferences.getBoolean(reminderKey, DEFAULT_REMINDER)

            reminderPreference.isChecked = isReminderActive

            val message = reminderReceiver.setReminder(requireContext(), isReminderActive)

            showSnackBar(message)
        }
    }

    /*
    * Refresh text on UI
    * NOTE: Must run this after change language */
    private fun refreshAfterLanguageChange() {
        activity?.title = getString(R.string.page_preference)
        languagePreference.title = getString(R.string.preference_language_text)
        reminderPreference.title = getString(R.string.preference_reminder_text)
    }

    /*
    * Show SnackBar
    * */
    private fun showSnackBar(message: String) {
        Snackbar.make(
            view as View,
            message,
            Snackbar.LENGTH_LONG
        ).show()
    }
}