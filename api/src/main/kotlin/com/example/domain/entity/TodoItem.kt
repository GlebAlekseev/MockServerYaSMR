package com.example.domain.entity

import kotlinx.serialization.Serializable

@Serializable
data class TodoItem(
    val userId: Long,
    val id: Long,
    val text: String,
    val importance: Importance,
    val deadline: Long?,
    val done: Boolean,
    val color: String,
    val createdAt: Long,
    val changedAt: Long?,
    val lastUpdatedBy: Long
) {
    companion object {
        @Serializable
        enum class Importance {
            LOW,
            BASIC,
            IMPORTANT
        }
    }
}