package com.example.domain.entity

data class TodoRevision(
    val userId: String,
    val deviceId: String,
    val revision: Long,
)