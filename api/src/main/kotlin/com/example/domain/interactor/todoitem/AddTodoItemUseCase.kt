package com.example.domain.interactor.todoitem

import com.example.domain.entity.TodoItem
import com.example.domain.repository.TodoItemRepository

class AddTodoItemUseCase(private val todoItemRepository: TodoItemRepository) {
    suspend operator fun invoke(todoItem: TodoItem): TodoItem? = todoItemRepository.addTodoItem(todoItem)
}