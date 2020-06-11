package com.example.consumerapp.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.consumerapp.ui.favorite.FavoriteViewModel
import com.example.consumerapp.ui.favoriteDetail.FavoriteDetailViewModel


class FavoriteViewModelFactory(private val context: Context) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(context) as T
        }
        if (modelClass.isAssignableFrom(FavoriteDetailViewModel::class.java)) {
            return FavoriteDetailViewModel(context) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}