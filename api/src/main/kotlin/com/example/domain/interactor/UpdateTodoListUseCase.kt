package com.example.domain.interactor

import com.example.domain.entity.TodoItem
import com.example.domain.repository.TodoItemsRepository

class UpdateTodoListUseCase(private val todoItemsRepository: TodoItemsRepository) {
    suspend operator fun invoke(todoList: List<TodoItem>): List<TodoItem> = todoItemsRepository.updateTodoList(todoList)
}