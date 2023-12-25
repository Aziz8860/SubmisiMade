package com.cakrapand.submisimade.di

import com.cakrapand.core.domain.usecase.UserUseCase
import com.cakrapand.core.domain.usecase.UsersInteractor
import com.cakrapand.submisimade.ui.detail.DetailViewModel
import com.cakrapand.submisimade.ui.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val useCaseModule = module {
    factory<UserUseCase> { UsersInteractor(get()) }
}

val viewModelModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { DetailViewModel(get()) }
}