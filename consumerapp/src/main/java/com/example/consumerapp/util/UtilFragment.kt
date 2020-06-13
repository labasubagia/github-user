package com.example.consumerapp.util

import androidx.fragment.app.Fragment
import com.example.consumerapp.activity.main.MainActivity

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