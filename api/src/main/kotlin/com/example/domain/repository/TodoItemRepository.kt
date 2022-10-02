package com.example.domain.repository

import com.example.domain.entity.TodoItem

interface TodoItemRepository {
    suspend fun getTodoList(userId: String): List<TodoItem>
    suspend fun updateTodoList(userId: String, list: List<TodoItem>): List<TodoItem>
    suspend fun getTodoItem(userId: String, id: String): TodoItem?
    suspend fun removeTodoItem(userId: String, id: String): TodoItem?
    suspend fun addTodoItem(userId: String, todoItem: TodoItem): TodoItem?
    suspend fun updateTodoItem(userId: String, todoItem: TodoItem): TodoItem?
}