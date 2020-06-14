package com.example.consumerapp.ui.preference

import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.navigation.fragment.findNavController
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import com.example.consumerapp.R
import com.example.consumerapp.util.UtilFragment.showBackToHomeOptionMenu
import com.example.consumerapp.util.UtilLanguage.getDefaultLanguage
import com.example.consumerapp.util.UtilLanguage.setLanguage
import com.google.android.material.snackbar.Snackbar


class PreferenceFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var languageKey: String

    private lateinit var languagePreference: ListPreference

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
        languageKey = getString(R.string.preference_language_key)
        languagePreference = findPreference<ListPreference>(languageKey) as ListPreference
    }

    private fun setValues() {
        val sharedPreferences = preferenceManager.sharedPreferences

        val language =
            sharedPreferences.getString(languageKey, getDefaultLanguage(requireContext()))
        languagePreference.value = language
        setLanguage(requireContext(), language as String)
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
    }

    /*
    * Refresh text on UI
    * NOTE: Must run this after change language */
    private fun refreshAfterLanguageChange() {
        activity?.title = getString(R.string.page_preference)
        languagePreference.title = getString(R.string.preference_language_text)
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