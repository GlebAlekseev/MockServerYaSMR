package com.example.domain.entity

import com.example.domain.entity.serializer.TimeStampAsLong
import kotlinx.serialization.Serializable
import java.sql.Timestamp

@Serializable
data class TodoItem(
    val id: String,
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
){
    companion object{
        @Serializable
        enum class Importance{
            LOW,
            BASIC,
            IMPORTANT
        }

        val DEFAULT_LIST = listOf(
            TodoItem(
                "1",
                "Задача 1",
                Importance.IMPORTANT,
                Timestamp(System.currentTimeMillis()),
                false,
                "#EEFFFF",
                Timestamp(System.currentTimeMillis()),
                Timestamp(System.currentTimeMillis()),
                4322345
            ),
            TodoItem(
                "2",
                "Задача 2",
                Importance.BASIC,
                Timestamp(System.currentTimeMillis()),
                true,
                "#65FF55",
                Timestamp(System.currentTimeMillis()),
                Timestamp(System.currentTimeMillis()),
                56567556
            )
        )

    }
}