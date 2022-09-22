package com.example.domain.interactor

import com.example.domain.entity.TodoItem
import com.example.domain.repository.TodoItemsRepository

class UpdateTodoItemUseCase(private val todoItemsRepository: TodoItemsRepository) {
    suspend operator fun invoke(todoItem: TodoItem): TodoItem? = todoItemsRepository.updateTodoItem(todoItem)
}