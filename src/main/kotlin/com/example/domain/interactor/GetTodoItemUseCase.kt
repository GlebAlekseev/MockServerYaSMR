package com.example.domain.interactor

import com.example.domain.entity.TodoItem
import com.example.domain.repository.TodoItemsRepository

class GetTodoItemUseCase(private val todoItemsRepository: TodoItemsRepository) {
    operator fun invoke(id: String): TodoItem? = todoItemsRepository.getTodoItem(id)
}