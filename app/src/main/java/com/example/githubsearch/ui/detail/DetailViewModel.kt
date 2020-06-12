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

    // Remote Repository
    private val remoteUserRepository = RemoteUserRepository.instance

    val error = remoteUserRepository.detailError

    fun getRemoteDetail(username: String) =
        remoteUserRepository.getDetail(username)


    // Local Repository
    private val localFavoriteUserRepository: LocalFavoriteUserRepository

    val userFavorite = MutableLiveData<UserDetail>()
    val isInserted = MutableLiveData<Boolean>()

    fun addLocalFavorite(user: UserDetail) =
        viewModelScope.launch(Dispatchers.IO) {
            val insert = localFavoriteUserRepository.insert(user)
            isInserted.postValue(insert > 0)
        }

    fun checkLocalFavorite(username: String) =
        viewModelScope.launch(Dispatchers.IO) {
            userFavorite.postValue(localFavoriteUserRepository.getByUsername(username))
        }


    // Init Room For Local Repository
    init {
        val favoriteUserDao = LocalDatabase.getDatabase(application).favoriteUserDao()
        localFavoriteUserRepository = LocalFavoriteUserRepository(favoriteUserDao)
    }
}
