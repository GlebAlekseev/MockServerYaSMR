package com.example.data.repository.local

import com.example.domain.entity.User
import com.example.domain.repository.UserRepository
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq


class UserRepositoryImpl(dataBase: CoroutineDatabase): UserRepository {
    private val collectionUser = dataBase.getCollection<User>()

    override suspend fun getUserList(): List<User> {
        return collectionUser.find().toList()
    }

    override suspend fun getUser(id: String): User? {
        return collectionUser.findOne(User::id eq id)
    }

    override suspend fun addUser(user: User): User? {
        val lastId = getUserList().lastOrNull()?.id ?: "1"
        collectionUser.insertOne(user.copy(id = lastId))
        return collectionUser.findOne(User::id eq lastId)
    }

    override suspend fun updateUser(user: User): User? {
        collectionUser.updateOne(User::id eq user.id,user)
        return collectionUser.findOne(User::id eq user.id)
    }

    override suspend fun removeUser(id: String): User? {
        return collectionUser.findOneAndDelete(User::id eq id)
    }
}