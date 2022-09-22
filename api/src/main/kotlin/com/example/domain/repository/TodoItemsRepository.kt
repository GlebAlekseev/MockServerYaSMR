package com.example.domain.repository

import com.example.domain.entity.TodoItem

interface TodoItemsRepository {
    suspend fun getTodoList(): List<TodoItem>
    suspend fun updateTodoList(list: List<TodoItem>): List<TodoItem>
    suspend fun getTodoItem(id: String): TodoItem?
    suspend fun removeTodoItem(id: String): TodoItem?
    suspend fun addTodoItem(todoItem: TodoItem): TodoItem?
    suspend fun updateTodoItem(todoItem: TodoItem): TodoItem?
}