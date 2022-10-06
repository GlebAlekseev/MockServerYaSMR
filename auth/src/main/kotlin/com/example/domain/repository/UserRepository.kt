package com.example.domain.repository

import com.example.domain.entity.User

interface UserRepository {
    suspend fun getUserList(): List<User>
    suspend fun getUser(id: Long): User?
    suspend fun addUser(user: User): User?
    suspend fun updateUser(user: User): User?
    suspend fun removeUser(id: Long): User?
}