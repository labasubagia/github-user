package com.example.consumerapp.ui.favorite

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.consumerapp.model.UserDetail
import com.example.consumerapp.repository.FavoriteUserRepository


class FavoriteViewModel(context: Context) : ViewModel() {

    private val repository = FavoriteUserRepository(context)
    val users = MutableLiveData<ArrayList<UserDetail>>()

    fun setUser() {
        users.postValue(repository.getAll())
    }
}
