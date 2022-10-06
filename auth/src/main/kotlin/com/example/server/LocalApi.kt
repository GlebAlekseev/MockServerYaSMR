package com.example.server

import com.example.data.repository.local.UserRepositoryImpl
import com.example.data.repository.local.UserTokenRepositoryImpl
import com.example.domain.entity.User
import com.example.domain.entity.UserToken
import com.example.domain.interactor.user.*
import com.example.domain.interactor.usertoken.*
import com.example.plugins.RefreshToken
import org.litote.kmongo.reactivestreams.*
import org.litote.kmongo.coroutine.*

object LocalApi {
    private val database = getApiDataBase()

    private val userRepositoryImpl = UserRepositoryImpl(database)
    private val addUserUseCase = AddUserUseCase(userRepositoryImpl)
    private val updateUserUseCase = UpdateUserUseCase(userRepositoryImpl)
    private val removeUserUseCase = RemoveUserUseCase(userRepositoryImpl)
    private val getUserUseCase = GetUserUseCase(userRepositoryImpl)
    private val getUserListUseCase = GetUserListUseCase(userRepositoryImpl)

    private val userTokenRepositoryImpl = UserTokenRepositoryImpl(database)
    private val addUserTokenUseCase = AddUserTokenUseCase(userTokenRepositoryImpl)
    private val updateUserTokenUseCase = UpdateUserTokenUseCase(userTokenRepositoryImpl)
    private val removeUserTokenUseCase = RemoveUserTokenUseCase(userTokenRepositoryImpl)
    private val getUserTokenUseCase = GetUserTokenUseCase(userTokenRepositoryImpl)
    private val getUserTokenListUseCase = GetUserTokenListUseCase(userTokenRepositoryImpl)

    suspend fun addUser(user: User): User? = addUserUseCase(user)
    suspend fun updateUser(user: User): User? = updateUserUseCase(user)
    suspend fun removeUser(id: Long): User? = removeUserUseCase(id)
    suspend fun getUser(id: Long): User? = getUserUseCase(id)
    suspend fun getUserList(): List<User> = getUserListUseCase()

    suspend fun addUserToken(userToken: UserToken): UserToken? = addUserTokenUseCase(userToken)
    suspend fun updateUserToken(userToken: UserToken): UserToken? = updateUserTokenUseCase(userToken)
    suspend fun removeUserToken(id: Long): UserToken? = removeUserTokenUseCase(id)
    suspend fun getUserToken(id: Long): UserToken? = getUserTokenUseCase(id)
    suspend fun getUserTokenList(): List<UserToken> = getUserTokenListUseCase()

    suspend fun getWithYandex(yandexId: Long): User?{
        return getUserList().lastOrNull { it.yandexId == yandexId }
    }
    suspend fun getNewDeviceIdForUser(id: Long): Long{
        return getUserTokenList().filter { it.id == id }.maxOfOrNull { it.deviceId }.let { if (it != null) it + 1 else 1 }
    }

    suspend fun getUserTokenWithRefreshToken(refreshToken: String): UserToken? {
         return getUserTokenList().lastOrNull { it.refreshToken == refreshToken }
    }

    private fun getApiDataBase(): CoroutineDatabase {
        KMongo.createClient()
        val client = KMongo.createClient(System.getenv()["MONGO_URL"]!!).coroutine
        return client.getDatabase("auth")
    }
}