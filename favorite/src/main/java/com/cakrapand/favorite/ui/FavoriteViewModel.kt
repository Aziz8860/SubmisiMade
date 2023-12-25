package com.cakrapand.favorite.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.cakrapand.core.domain.usecase.UserUseCase


class FavoriteViewModel(
    private val userUseCase: UserUseCase
) : ViewModel() {

    fun getAllUserFavorite() = userUseCase.getAllUserFavorite().asLiveData()
}