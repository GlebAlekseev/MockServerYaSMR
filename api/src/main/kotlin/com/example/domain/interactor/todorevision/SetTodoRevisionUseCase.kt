package com.example.domain.interactor.todorevision

import com.example.domain.entity.TodoRevision
import com.example.domain.repository.TodoRevisionRepository

class SetTodoRevisionUseCase(private val todoRevisionRepository: TodoRevisionRepository) {
    suspend operator fun invoke(todoRevision: TodoRevision): TodoRevision? =
        todoRevisionRepository.setTodoRevision(todoRevision)
}