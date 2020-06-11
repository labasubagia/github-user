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
import com.example.githubsearch.activity.main.MainActivity
import com.example.githubsearch.util.Util.getDefaultLanguage
import com.example.githubsearch.util.Util.setLanguage
import com.google.android.material.snackbar.Snackbar


class PreferenceFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var LANGUAGE: String
    private lateinit var REMINDER: String

    private lateinit var languagePreference: ListPreference
    private lateinit var reminderPreference: CheckBoxPreference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {

        val activity = activity as? MainActivity

        // change action bar title
        activity?.title = getString(R.string.page_preference)

        // show back to home
        activity?.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)

        // xml
        addPreferencesFromResource(R.xml.preferences)

        init()
        setValues()
    }

    override fun onDestroyView() {

        val activity = activity as? MainActivity

        // remove back to home
        activity?.supportActionBar?.setDisplayHomeAsUpEnabled(false)
        setHasOptionsMenu(false)

        super.onDestroyView()
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
        REMINDER = getString(R.string.preference_reminder_key)
        LANGUAGE = getString(R.string.preference_language_key)

        languagePreference = findPreference<ListPreference>(LANGUAGE) as ListPreference
        reminderPreference = findPreference<CheckBoxPreference>(REMINDER) as CheckBoxPreference
    }

    private fun setValues() {
        val sharedPreferences = preferenceManager.sharedPreferences

        val language = sharedPreferences.getString(LANGUAGE, getDefaultLanguage(requireContext()))
        languagePreference.value = language
        setLanguage(requireContext(), language as String)

        reminderPreference.isChecked = sharedPreferences.getBoolean(REMINDER, true)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
        if (key == LANGUAGE) {
            val language =
                sharedPreferences.getString(LANGUAGE, getDefaultLanguage(requireContext()))
            languagePreference.value = language

            // change app language
            setLanguage(requireContext(), language as String)

            refreshAfterLanguageChange()

            // snack bar
            showSnackBar(getString(R.string.message_language_change, language))
        }
        if (key == REMINDER) {
            val isReminderActive = sharedPreferences.getBoolean(REMINDER, true)
            reminderPreference.isChecked = isReminderActive

            // snack bar
            val message =
                getString(
                    if (isReminderActive)
                        R.string.message_reminder_change_active
                    else
                        R.string.message_reminder_change_deactivate
                )
            showSnackBar(message)
        }
    }

    private fun refreshAfterLanguageChange() {
        // just refresh some text in layout
        activity?.title = getString(R.string.page_preference)
        languagePreference.title = getString(R.string.preference_language_text)
        reminderPreference.title = getString(R.string.preference_reminder_text)
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(
            view as View,
            message,
            Snackbar.LENGTH_LONG
        ).show()
    }
}