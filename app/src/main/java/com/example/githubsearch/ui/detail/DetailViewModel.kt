package com.example.githubsearch.ui.detail

import androidx.lifecycle.ViewModel
import com.example.githubsearch.repository.UserRepository

class DetailViewModel : ViewModel() {
    private val repository = UserRepository.instance

    val error = repository.detailError
    fun getDetail(username: String) = repository.getDetail(username)
}
