package com.example.domain.repository

import com.example.domain.entity.UserToken

interface UserTokenRepository {
    suspend fun getUserTokenList(): List<UserToken>
    suspend fun getUserToken(id: Long): UserToken?
    suspend fun addUserToken(userToken: UserToken): UserToken?
    suspend fun updateUserToken(userToken: UserToken): UserToken?
    suspend fun removeUserToken(id: Long): UserToken?
}