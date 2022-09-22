package com.example.domain.interactor

import com.example.domain.entity.TodoItem
import com.example.domain.repository.TodoItemsRepository

class GetTodoListUseCase(private val todoItemsRepository: TodoItemsRepository) {
    suspend operator fun invoke(): List<TodoItem> = todoItemsRepository.getTodoList()
}