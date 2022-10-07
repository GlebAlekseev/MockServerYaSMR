package com.example.domain.interactor.usertoken

import com.example.domain.entity.UserToken
import com.example.domain.repository.UserTokenRepository

class GetUserTokenUseCase(private val userTokenRepository: UserTokenRepository) {
    suspend operator fun invoke(userId: Long, deviceId: Long): UserToken? = userTokenRepository.getUserToken(userId, deviceId)
}