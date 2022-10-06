package com.example.domain.interactor.user

import com.example.domain.entity.User
import com.example.domain.repository.UserRepository

class RemoveUserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(id: Long): User? = userRepository.removeUser(id)
}