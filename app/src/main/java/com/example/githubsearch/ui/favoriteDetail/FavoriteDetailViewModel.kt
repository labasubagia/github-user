package com.example.githubsearch.ui.favoriteDetail

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.githubsearch.database.LocalDatabase
import com.example.githubsearch.model.UserDetail
import com.example.githubsearch.repository.LocalFavoriteUserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteDetailViewModel(application: Application) : AndroidViewModel(application) {

    // local repo
    private val localFavoriteUserRepo: LocalFavoriteUserRepository
    val isDeleteSuccess = MutableLiveData<Boolean>()
    var userDetail = MutableLiveData<UserDetail>()

    init {
        val favoriteUserDao = LocalDatabase.getDatabase(application).favoriteUserDao()
        localFavoriteUserRepo = LocalFavoriteUserRepository(favoriteUserDao)
    }

    fun deleteFavorite(user: UserDetail) = viewModelScope.launch(Dispatchers.IO) {
        try {
            localFavoriteUserRepo.delete(user)
            isDeleteSuccess.postValue(true)
        } catch (e: Throwable) {
            isDeleteSuccess.postValue(false)
        }
    }

    // local
    fun searchDetail(username: String) = viewModelScope.launch(Dispatchers.IO) {
        userDetail.postValue(localFavoriteUserRepo.searchByUsername(username))
    }
}
