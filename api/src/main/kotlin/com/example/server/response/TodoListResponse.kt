package com.example.server.response

import com.example.domain.entity.TodoItem
import io.ktor.http.*
import kotlinx.serialization.Serializable

@Serializable
data class TodoListResponse(
    val status: Int = HttpStatusCode.OK.value,
    val list: List<TodoItem>? = emptyList(),
    val revision: Long = 0,
    val message: String = "OK",
)