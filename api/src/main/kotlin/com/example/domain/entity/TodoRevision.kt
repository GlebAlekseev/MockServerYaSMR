package com.example.domain.entity

@kotlinx.serialization.Serializable
data class TodoRevision(
    val userId: String,
    val deviceId: String,
    val revision: Long,
)