package com.example.domain.entity

@kotlinx.serialization.Serializable
data class UserToken(
    val id: Long,
    val deviceId: Long,
    val login: String,
    val displayName: String,
    val accessToken: String,
    val refreshToken: String,
    val refreshTokenExpireAt: Long,
) {
    companion object {
        const val DAY_MILLIS = 86400000
    }
}