package com.cakrapand.submisimade.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.cakrapand.core.domain.usecase.UserUseCase

class MainViewModel(
    private val userUseCase: UserUseCase,
) : ViewModel() {

    fun getUsersByUsername(username: String) = userUseCase.getUsersByUsername(username).asLiveData()
}