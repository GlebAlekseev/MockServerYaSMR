package com.example.domain.interactor.usertoken

import com.example.domain.entity.UserToken
import com.example.domain.repository.UserTokenRepository

class RemoveUserTokenUseCase(private val uerTokenRepository: UserTokenRepository) {
    suspend operator fun invoke(id: String): UserToken? = uerTokenRepository.removeUserToken(id)
}