package com.example.githubsearch.ui.follow

import androidx.lifecycle.ViewModel
import com.example.githubsearch.repository.UserRepository

class FollowViewModel : ViewModel() {
    private val repository = UserRepository.instance

    fun follow(username: String, type: String) =
        if (type == FollowFragment.TYPE_FOLLOWERS) {
            repository.getFollowers(username)
        } else repository.getFollowing(username)

    fun error(type: String) =
        if (type == FollowFragment.TYPE_FOLLOWERS) {
            repository.followersError
        } else repository.followingError
}
