package com.example.githubsearch.util

import android.view.View

// function to make views visible or gone
fun showView(views: ArrayList<View>, isShow: Boolean = true) {
    views.forEach {
        it.visibility = if (isShow) View.VISIBLE else View.GONE
    }
}
