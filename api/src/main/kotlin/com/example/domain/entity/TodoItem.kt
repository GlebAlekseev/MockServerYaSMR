package com.example.domain.entity

import com.example.domain.entity.serializer.TimeStampAsLong
import kotlinx.serialization.Serializable
import java.sql.Timestamp

@Serializable
data class TodoItem(
    val userId: String,
    val id: String = UNDEFINED,
    val text: String,
    val importance: Importance,
    @Serializable(with = TimeStampAsLong::class)
    val deadline: Timestamp,
    val done: Boolean,
    val color: String,
    @Serializable(with = TimeStampAsLong::class)
    val createdAt: Timestamp,
    @Serializable(with = TimeStampAsLong::class)
    val changedAt: Timestamp,
    val lastUpdatedBy: Int
) {
    companion object {
        const val UNDEFINED = "-1"
        @Serializable
        enum class Importance {
            LOW,
            BASIC,
            IMPORTANT
        }
    }
}