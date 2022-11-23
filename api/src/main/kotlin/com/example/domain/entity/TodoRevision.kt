package com.example.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class TodoRevision(
    val userId: Long,
    val deviceId: Long,
    val revision: Long,
)