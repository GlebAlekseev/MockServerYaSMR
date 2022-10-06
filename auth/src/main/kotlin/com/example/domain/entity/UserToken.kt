package com.example.domain.entity

import kotlinx.serialization.Serializable
import java.sql.Timestamp

@kotlinx.serialization.Serializable
data class UserToken(
    // Для одного юзера может быть несколько устройств
    val id: Long,
    val deviceId: Long,
    // Токены auth, для разных устройств свой токен.
    val accessToken: String,
    val refreshToken: String,
    val refreshTokenExpireAt: Long,
){
    companion object{
        const val DAY_MILLIS = 86400000
    }
}