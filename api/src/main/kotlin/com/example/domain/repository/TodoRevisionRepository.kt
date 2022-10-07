package com.example.domain.repository

import com.example.domain.entity.TodoRevision

interface TodoRevisionRepository {
    suspend fun getTodoRevision(userId: Long, deviceId: Long): TodoRevision?
    suspend fun setTodoRevision(todoRevision: TodoRevision): TodoRevision?
}