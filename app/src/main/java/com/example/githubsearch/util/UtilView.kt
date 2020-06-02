package com.example.githubsearch.util

import android.view.View
import com.bumptech.glide.Glide
import com.example.githubsearch.R
import com.example.githubsearch.model.CustomError
import com.example.githubsearch.util.Util.REQUEST_SERVER_ERROR
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

    // set view layout_info.xml for error view
    fun setInfoViewAsErrorView(view: View, error: CustomError) {

        // set error image on info view
        val imageResource =
            if (error.type == REQUEST_SERVER_ERROR)
                R.drawable.ic_undraw_server_down
            else
                R.drawable.ic_undraw_warning

        setInfoView(view, imageResource, error.messageResource)
    }
}