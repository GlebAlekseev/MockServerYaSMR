package com.example.domain.interactor.usertoken

import com.example.domain.entity.UserToken
import com.example.domain.repository.UserTokenRepository

class GetUserTokenUseCase(private val userTokenRepository: UserTokenRepository) {
    suspend operator fun invoke(id: String): UserToken? = userTokenRepository.getUserToken(id)
}