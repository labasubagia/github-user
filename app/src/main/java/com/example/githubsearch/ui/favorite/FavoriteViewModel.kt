package com.example.githubsearch.ui.favorite

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.githubsearch.database.LocalDatabase
import com.example.githubsearch.model.UserDetail
import com.example.githubsearch.repository.LocalFavoriteUserRepository

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    // local data
    private val localFavoriteUserRepository: LocalFavoriteUserRepository
    val favorites: LiveData<List<UserDetail>>

    // init room
    init {
        LocalDatabase.getDatabase(application).favoriteUserDao().apply {
            localFavoriteUserRepository = LocalFavoriteUserRepository(this)
        }
        favorites = localFavoriteUserRepository.favorites
    }
}
