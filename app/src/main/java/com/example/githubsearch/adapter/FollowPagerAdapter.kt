package com.example.githubsearch.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.example.githubsearch.R
import com.example.githubsearch.ui.follow.FollowFragment

class FollowPagerAdapter(private val context: Context, fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    companion object {
        private val TAB_TITLES = intArrayOf(R.string.label_followers, R.string.label_following)
    }

    private var username: String? = null

    fun setUsername(username: String) {
        this.username = username
    }

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> FollowFragment.newInstance(FollowFragment.TYPE_FOLLOWERS, username.toString())
            1 -> FollowFragment.newInstance(FollowFragment.TYPE_FOLLOWING, username.toString())
            else -> null
        } as Fragment
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int = TAB_TITLES.size
}