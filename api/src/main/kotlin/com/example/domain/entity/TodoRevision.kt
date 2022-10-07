package com.example.domain.entity

@kotlinx.serialization.Serializable
data class TodoRevision(
    val userId: Long,
    val deviceId: Long,
    val revision: Long,
)