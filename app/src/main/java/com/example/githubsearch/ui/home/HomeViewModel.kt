package com.example.githubsearch.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.githubsearch.model.CustomError
import com.example.githubsearch.repository.RemoteUserRepository

class HomeViewModel : ViewModel() {

    // remote data
    private val remoteUserRepository = RemoteUserRepository.instance
    private val username = MutableLiveData<String>()


    // found search
    val found = Transformations.switchMap(username) {
        remoteUserRepository.getFound(it)
    }

    // error
    val error: LiveData<CustomError> = remoteUserRepository.foundError

    // search
    fun search(username: String) {
        this.username.value = username
    }
}
