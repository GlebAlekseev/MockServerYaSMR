package com.example.domain.interactor.todoitem

import com.example.domain.entity.TodoItem
import com.example.domain.repository.TodoItemRepository

class UpdateTodoItemUseCase(private val todoItemRepository: TodoItemRepository) {
    suspend operator fun invoke(userId:String, todoItem: TodoItem): TodoItem? = todoItemRepository.updateTodoItem(userId,todoItem)
}