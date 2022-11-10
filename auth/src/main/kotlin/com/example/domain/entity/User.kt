package com.example.domain.entity

@kotlinx.serialization.Serializable
data class User(
    val id: Long = UNDEFINED,
    val displayName: String,
    val yandexId: Long,
    val accessTokenYandex: String,
    val refreshTokenYandex: String,
) {
    companion object {
        const val UNDEFINED = 0L
    }
}