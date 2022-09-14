package com.example.domain.repository

import com.example.domain.entity.TodoItem

interface TodoItemsRepository {
    fun getTodoList(): List<TodoItem>
    fun updateTodoList(todoList: List<TodoItem>): List<TodoItem>
    fun getTodoItem(id: String): TodoItem?
    fun removeTodoItem(id: String): TodoItem?
    fun addTodoItem(todoItem: TodoItem): TodoItem
    fun updateTodoItem(todoItem: TodoItem): TodoItem
}