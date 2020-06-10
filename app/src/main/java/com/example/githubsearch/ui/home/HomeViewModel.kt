package com.example.githubsearch.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.githubsearch.model.CustomError
import com.example.githubsearch.repository.UserRepository

class HomeViewModel : ViewModel() {

    private val repository = UserRepository.instance

    private val username = MutableLiveData<String>()

    val found = Transformations.switchMap(username) {
        repository.getFound(it)
    }
    val error: LiveData<CustomError> = repository.foundError

    fun search(username: String) {
        this.username.value = username
    }
}
