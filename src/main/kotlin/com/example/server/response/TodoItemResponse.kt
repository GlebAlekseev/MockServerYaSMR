package com.example.server.response

import com.example.domain.entity.TodoItem
import io.ktor.http.*
import kotlinx.serialization.Serializable

@Serializable
data class TodoItemResponse(
    val status: Int = HttpStatusCode.OK.value,
    val item: TodoItem? = null,
    val revision: Long = 0,
    val message: String= "",
)