package com.cakrapand.core.domain.model

data class Users(
    val name: String?,
    val login: String?,
    val avatarUrl: String?,
    val following: Int?,
    val followers: Int?,
)
