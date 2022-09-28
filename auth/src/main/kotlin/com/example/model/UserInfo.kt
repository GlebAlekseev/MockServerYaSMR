package com.example.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserInfo(
    val id:String,
    val login:String,
    @SerialName("client_id") val clientId:String,
    @SerialName("display_name") val displayName:String,
    @SerialName("real_name") val realName:String,
    @SerialName("first_name") val firstName:String,
    @SerialName("last_name") val lastName:String,
    val sex: String,
    val psuid:String
)