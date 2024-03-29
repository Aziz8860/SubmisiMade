package com.cakrapand.submisimade.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.cakrapand.core.domain.model.Users
import com.cakrapand.core.domain.usecase.UserUseCase
import kotlinx.coroutines.launch

class DetailViewModel(
    private val userUseCase: UserUseCase,
) : ViewModel() {

    fun getUserDetail(username: String) = userUseCase.getUserDetail(username).asLiveData()

    fun getUserFollowing(username: String) = userUseCase.getUserFollowing(username).asLiveData()

    fun getUserFollowers(username: String) = userUseCase.getUserFollowers(username).asLiveData()

    fun insertUserFavorite(user: Users) {
        viewModelScope.launch {
            userUseCase.insertUserFavorite(user)
        }
    }

    fun deleteUserFavorite(users: Users) {
        viewModelScope.launch {
            userUseCase.deleteUserFavorite(users)
        }
    }

    fun getFavoriteIsExists(username: String) =
        userUseCase.getFavoriteIsExists(username).asLiveData()
}