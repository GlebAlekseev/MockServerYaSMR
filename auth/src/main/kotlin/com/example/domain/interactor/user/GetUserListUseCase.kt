package com.example.domain.interactor.user

import com.example.domain.entity.User
import com.example.domain.repository.UserRepository

class GetUserListUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(): List<User> = userRepository.getUserList()
}