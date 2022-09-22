package com.example.domain.interactor

import com.example.domain.entity.TodoItem
import com.example.domain.repository.TodoItemsRepository

class RemoveTodoItemUseCase(private val todoItemsRepository: TodoItemsRepository) {
    suspend operator fun invoke(id: String): TodoItem? = todoItemsRepository.removeTodoItem(id)
}