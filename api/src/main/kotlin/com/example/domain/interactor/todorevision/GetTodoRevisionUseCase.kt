package com.example.domain.interactor.todorevision

import com.example.domain.entity.TodoRevision
import com.example.domain.repository.TodoRevisionRepository

class GetTodoRevisionUseCase(private val todoRevisionRepository: TodoRevisionRepository) {
    suspend operator fun invoke(userId: Long, deviceId: Long): TodoRevision? =
        todoRevisionRepository.getTodoRevision(userId, deviceId)
}