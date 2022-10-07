package com.example.domain.interactor.todoitem

import com.example.domain.entity.TodoItem
import com.example.domain.repository.TodoItemRepository

class GetTodoListUseCase(private val todoItemRepository: TodoItemRepository) {
    suspend operator fun invoke(userId: Long): List<TodoItem> = todoItemRepository.getTodoList(userId)
}