package com.example.domain.interactor.user

import com.example.domain.entity.User
import com.example.domain.repository.UserRepository

class GetUserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(id: Long): User? = userRepository.getUser(id)
}