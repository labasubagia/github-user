package com.example.githubsearch.util

import androidx.fragment.app.Fragment
import com.example.githubsearch.activity.main.MainActivity

object UtilFragment {

    // ActionBar Show BackMenu
    fun showBackToHomeOptionMenu(fragment: Fragment, isShow: Boolean = true) {
        with(fragment) {
            val activity = activity as? MainActivity
            activity?.supportActionBar?.setDisplayHomeAsUpEnabled(isShow)
            setHasOptionsMenu(isShow)
        }
    }
}