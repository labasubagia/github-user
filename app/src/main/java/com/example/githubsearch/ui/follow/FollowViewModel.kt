package com.example.githubsearch.ui.follow

import androidx.lifecycle.ViewModel
import com.example.githubsearch.repository.RemoteUserRepository

class FollowViewModel : ViewModel() {
    private val remoteUserRepository = RemoteUserRepository.instance

    fun follow(username: String, type: String) =
        if (type == FollowFragment.TYPE_FOLLOWERS) {
            remoteUserRepository.getFollowers(username)
        } else remoteUserRepository.getFollowing(username)

    fun error(type: String) =
        if (type == FollowFragment.TYPE_FOLLOWERS) {
            remoteUserRepository.followersError
        } else remoteUserRepository.followingError
}
