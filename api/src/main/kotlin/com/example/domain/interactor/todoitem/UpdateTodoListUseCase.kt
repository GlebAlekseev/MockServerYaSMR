package com.example.domain.interactor.todoitem

import com.example.domain.entity.TodoItem
import com.example.domain.repository.TodoItemRepository

class UpdateTodoListUseCase(private val todoItemRepository: TodoItemRepository) {
    suspend operator fun invoke(userId:String, todoList: List<TodoItem>): List<TodoItem> = todoItemRepository.updateTodoList(userId,todoList)
}