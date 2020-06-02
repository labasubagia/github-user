package com.example.githubsearch.util

import android.view.View
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.layout_info.view.*
import java.util.*

object UtilView {

    // function to make views visible or gone
    fun showView(views: ArrayList<View>, isShow: Boolean = true) {
        views.forEach {
            it.visibility = if (isShow) View.VISIBLE else View.GONE
        }
    }

    // set view for layout_info.xml
    fun setInfoView(view: View, imageResource: Int, messageResource: Int) {
        with(view) {
            Glide.with(view)
                .load(imageResource)
                .into(img_info)
            tv_info_message.text = resources.getString(messageResource)
        }
    }
}