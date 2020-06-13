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

    private val localFavoriteUserRepository: LocalFavoriteUserRepository

    val isDeleted = MutableLiveData<Boolean>()
    var userDetail = MutableLiveData<UserDetail>()

    fun deleteFavorite(username: String) =
        viewModelScope.launch(Dispatchers.IO) {
            val deleted = localFavoriteUserRepository.deleteByUsername(username)
            isDeleted.postValue(deleted > 0)
        }

    fun getDetail(username: String) =
        viewModelScope.launch(Dispatchers.IO) {
            userDetail.postValue(localFavoriteUserRepository.getByUsername(username))
        }

    init {
        val favoriteUserDao = LocalDatabase.getDatabase(application).favoriteUserDao()
        localFavoriteUserRepository = LocalFavoriteUserRepository(favoriteUserDao)
    }
}
