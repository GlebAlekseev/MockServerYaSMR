package com.example.domain.entity

import com.example.domain.entity.serializer.TimeStampAsLong
import kotlinx.serialization.Serializable
import java.sql.Timestamp

@kotlinx.serialization.Serializable
data class UserToken(
    // Для одного юзера может быть несколько устройств
    val id: String,
    val deviceId:String,
    // Токены auth, для разных устройств свой токен.
    val accessToken: String,
    val refreshToken: String,
    @Serializable(with = TimeStampAsLong::class)
    val refreshTokenExpireAt: Timestamp,
)