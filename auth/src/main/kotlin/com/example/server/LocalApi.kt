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
    suspend fun removeUser(id: String): User? = removeUserUseCase(id)
    suspend fun getUser(id: String): User? = getUserUseCase(id)
    suspend fun getUserList(): List<User> = getUserListUseCase()

    suspend fun addUserToken(userToken: UserToken): UserToken? = addUserTokenUseCase(userToken)
    suspend fun updateUserToken(userToken: UserToken): UserToken? = updateUserTokenUseCase(userToken)
    suspend fun removeUserToken(id: String): UserToken? = removeUserTokenUseCase(id)
    suspend fun getUserToken(id: String): UserToken? = getUserTokenUseCase(id)
    suspend fun getUserTokenList(): List<UserToken> = getUserTokenListUseCase()

    suspend fun getWithYandex(yandexId: String): User?{
        return getUserList().lastOrNull { it.yandexId == yandexId }
    }
    suspend fun getNewDeviceIdForUser(id: String): String{
        return getUserTokenList().filter { it.id == id }.maxOfOrNull { it.deviceId.toInt() }.let { if (it != null) (it + 1).toString() else "1" }
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