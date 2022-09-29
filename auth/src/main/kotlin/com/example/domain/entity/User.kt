package com.example.domain.entity


@kotlinx.serialization.Serializable
data class User(
    val id: String = "0",
    val displayName:String,
    // Yandex единственный для всех устройств пользователя
    val yandexId: String,
    val accessTokenYandex: String,
    val refreshTokenYandex: String,
)