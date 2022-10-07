package com.example.domain.interactor.todoitem

import com.example.domain.entity.TodoItem
import com.example.domain.repository.TodoItemRepository

class GetTodoItemUseCase(private val todoItemRepository: TodoItemRepository) {
    suspend operator fun invoke(userId: Long, id: Long): TodoItem? = todoItemRepository.getTodoItem(userId,id)
}