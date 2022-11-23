package com.example.domain.entity

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class YandexUser(
    val id: String,
    val login: String,
    @SerialName("client_id") val clientId: String,
    @SerialName("display_name") val displayName: String,
    @SerialName("real_name") val realName: String,
    @SerialName("first_name") val firstName: String,
    @SerialName("last_name") val lastName: String,
    val sex: String?,
    @SerialName("default_avatar_id") val defaultAvatarId: String,
    @SerialName("is_avatar_empty") val isAvatarEmpty: Boolean,
    val psuid: String,
)