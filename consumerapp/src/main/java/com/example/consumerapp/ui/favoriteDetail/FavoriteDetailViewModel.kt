package com.example.consumerapp.ui.favoriteDetail

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.consumerapp.model.UserDetail
import com.example.consumerapp.repository.FavoriteUserRepository

class FavoriteDetailViewModel(context: Context) : ViewModel() {

    // local repo
    private val repository = FavoriteUserRepository(context)
    val isDeleteSuccess = MutableLiveData<Boolean>()
    var userDetail = MutableLiveData<UserDetail>()


    fun delete(user: UserDetail) {
        val status = repository.delete(user.login)
        isDeleteSuccess.postValue(status > 0)
    }

    fun search(username: String) {
        userDetail.postValue(repository.searchByUsername(username))
    }
}
