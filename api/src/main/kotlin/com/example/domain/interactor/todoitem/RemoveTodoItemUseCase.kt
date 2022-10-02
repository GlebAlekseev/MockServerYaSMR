package com.example.domain.interactor.todoitem

import com.example.domain.entity.TodoItem
import com.example.domain.repository.TodoItemRepository

class RemoveTodoItemUseCase(private val todoItemRepository: TodoItemRepository) {
    suspend operator fun invoke(userId:String, id: String): TodoItem? = todoItemRepository.removeTodoItem(userId,id)
}