package com.example.githubsearch.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.githubsearch.R
import com.example.githubsearch.model.UserDetail
import kotlinx.android.synthetic.main.layout_item_user.view.*

class FavoriteListAdapter : RecyclerView.Adapter<FavoriteListAdapter.FavoriteListViewHolder>() {

    private var onItemClickCallback: OnItemClickCallback? = null
    private val users = ArrayList<UserDetail>()

    inner class FavoriteListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // load data in layout
        fun bind(user: UserDetail) {
            with(itemView) {
                Glide.with(itemView)
                    .asBitmap()
                    .apply(RequestOptions().override(80, 80))
                    .load(user.avatar_url)
                    .placeholder(R.drawable.ic_undraw_profile_pic)
                    .error(R.drawable.ic_undraw_profile_pic)
                    .into(img_avatar)
                tv_username.text = user.login
                itemView.setOnClickListener { onItemClickCallback?.onItemClicked(user) }
            }
        }
    }

    // setter
    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteListViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_item_user, parent, false)
        return FavoriteListViewHolder(view)
    }

    override fun getItemCount(): Int = users.size

    override fun onBindViewHolder(holder: FavoriteListViewHolder, position: Int) {
        holder.bind(users[position])
    }

    // set found users
    fun setUsers(users: ArrayList<UserDetail>) {
        this.users.clear()
        this.users.addAll(users)
        notifyDataSetChanged()
    }

    interface OnItemClickCallback {
        fun onItemClicked(user: UserDetail)
    }
}