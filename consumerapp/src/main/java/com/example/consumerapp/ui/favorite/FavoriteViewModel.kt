package com.example.consumerapp.ui.favorite

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.consumerapp.model.UserDetail
import com.example.consumerapp.repository.FavoriteUserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class FavoriteViewModel(context: Context) : ViewModel() {

    private val repository = FavoriteUserRepository(context)

    val users = MutableLiveData<ArrayList<UserDetail>>()

    fun setUsers() =
        viewModelScope.launch(Dispatchers.IO) {
            users.postValue(repository.getAll())
        }
}
