package com.example.githubsearch.ui.detail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.githubsearch.database.LocalDatabase
import com.example.githubsearch.model.UserDetail
import com.example.githubsearch.repository.LocalFavoriteUserRepository
import com.example.githubsearch.repository.RemoteUserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel(application: Application) : AndroidViewModel(application) {

    // local repository and local data
    private val localFavoriteUserRepo: LocalFavoriteUserRepository
    val foundUserFavorite = MutableLiveData<UserDetail>()
    val isInsertSuccess = MutableLiveData<Boolean>()

    // remote repository and remote data
    private val remoteUserRepo = RemoteUserRepository.instance
    val error = remoteUserRepo.detailError

    // init room
    init {
        val favoriteUserDao = LocalDatabase.getDatabase(application).favoriteUserDao()
        localFavoriteUserRepo = LocalFavoriteUserRepository(favoriteUserDao)
    }

    // remote detail data
    fun getRemoteDetail(username: String) = remoteUserRepo.getDetail(username)


    // local
    fun addLocalFavorite(user: UserDetail) = viewModelScope.launch(Dispatchers.IO) {
        try {
            localFavoriteUserRepo.insert(user)
            isInsertSuccess.postValue(true)
        } catch (e: Throwable) {
            isInsertSuccess.postValue(false)
        }
    }

    // local
    fun checkLocalFavorite(username: String) = viewModelScope.launch(Dispatchers.IO) {
        foundUserFavorite.postValue(localFavoriteUserRepo.searchByUsername(username))
    }
}
