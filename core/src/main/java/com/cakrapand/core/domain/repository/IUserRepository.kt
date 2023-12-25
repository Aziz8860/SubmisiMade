package com.cakrapand.core.domain.repository

import com.cakrapand.core.data.Resource
import com.cakrapand.core.domain.model.Users
import kotlinx.coroutines.flow.Flow

interface IUserRepository {

    fun getUsersByUsername(username: String): Flow<Resource<List<Users>>>

    fun getUserDetail(username: String): Flow<Resource<Users>>

    fun getUserFollowing(username: String): Flow<Resource<List<Users>>>

    fun getUserFollowers(username: String): Flow<Resource<List<Users>>>

    fun getAllUserFavorite(): Flow<List<Users>>

    fun getFavoriteIsExists(username: String): Flow<Boolean>

    suspend fun insertUserFavorite(users: Users)

    suspend fun deleteUserFavorite(users: Users)
}