package com.example.data.repository.local

import com.example.domain.entity.UserToken
import com.example.domain.repository.UserTokenRepository
import com.mongodb.client.model.Filters
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq


class UserTokenRepositoryImpl(dataBase: CoroutineDatabase): UserTokenRepository {
    private val collectionUserToken = dataBase.getCollection<UserToken>()

    override suspend fun getUserTokenList(): List<UserToken> {
        return collectionUserToken.find().toList()
    }

    override suspend fun getUserToken(userId: Long, deviceId: Long): UserToken? {
        return collectionUserToken.findOne(UserToken::deviceId eq deviceId)
    }

    override suspend fun addUserToken(userToken: UserToken): UserToken? {
        collectionUserToken.insertOne(userToken)
        collectionUserToken.distinct(UserToken::deviceId)
        return collectionUserToken.findOne(UserToken::deviceId eq userToken.deviceId)
    }

    override suspend fun updateUserToken(userToken: UserToken): UserToken? {
        collectionUserToken.updateOne(UserToken::deviceId eq userToken.deviceId, userToken)
        return collectionUserToken.findOne(UserToken::deviceId eq userToken.deviceId)
    }

    override suspend fun removeUserToken(userId: Long, deviceId: Long): UserToken? {
        val filter = Filters.and(
            Filters.eq("userId", userId),
            Filters.eq("deviceId", deviceId)
        )
        return collectionUserToken.findOneAndDelete(filter)
    }
}