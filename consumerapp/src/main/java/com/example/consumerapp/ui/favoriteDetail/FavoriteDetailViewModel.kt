package com.example.consumerapp.ui.favoriteDetail

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.consumerapp.model.UserDetail
import com.example.consumerapp.repository.FavoriteUserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteDetailViewModel(context: Context) : ViewModel() {

    private val repository = FavoriteUserRepository(context)

    val isDeleted = MutableLiveData<Boolean>()
    var userDetail = MutableLiveData<UserDetail>()

    fun delete(user: UserDetail) =
        viewModelScope.launch(Dispatchers.IO) {
            val status = repository.delete(user.login)
            isDeleted.postValue(status > 0)
        }

    fun search(username: String) =
        viewModelScope.launch(Dispatchers.IO) {
            val user = repository.getByUsername(username)
            userDetail.postValue(user)
        }
}
