package com.example.domain.interactor.usertoken

import com.example.domain.entity.UserToken
import com.example.domain.repository.UserTokenRepository

class GetUserTokenListUseCase(private val userTokenRepository: UserTokenRepository) {
    suspend operator fun invoke(): List<UserToken> = userTokenRepository.getUserTokenList()
}