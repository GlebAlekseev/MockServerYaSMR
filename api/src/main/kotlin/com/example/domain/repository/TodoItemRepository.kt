package com.example.domain.repository

import com.example.domain.entity.TodoItem

interface TodoItemRepository {
    suspend fun getTodoList(userId: Long): List<TodoItem>
    suspend fun updateTodoList(userId: Long, list: List<TodoItem>): List<TodoItem>
    suspend fun getTodoItem(userId: Long, id: Long): TodoItem?
    suspend fun removeTodoItem(userId: Long, id: Long): TodoItem?
    suspend fun addTodoItem(userId: Long, todoItem: TodoItem): TodoItem?
    suspend fun updateTodoItem(userId: Long, todoItem: TodoItem): TodoItem?
}