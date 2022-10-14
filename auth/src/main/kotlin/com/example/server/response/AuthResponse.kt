package com.example.server.response

import com.example.plugins.TokenPair
import io.ktor.http.*
import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val message: String = "OK",
    val data: TokenPair? = null
)

