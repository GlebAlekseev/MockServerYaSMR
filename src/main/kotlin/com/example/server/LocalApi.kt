package com.example.server

import com.example.data.repository.local.TodoItemsRepositoryImpl
import com.example.domain.entity.TodoItem
import com.example.domain.interactor.*

object LocalApi {
    private val repositoryImpl = TodoItemsRepositoryImpl()
    private val addTodoItemUseCase = AddTodoItemUseCase(repositoryImpl)
    private val updateTodoItemUseCase = UpdateTodoItemUseCase(repositoryImpl)
    private val removeTodoItemUseCase = RemoveTodoItemUseCase(repositoryImpl)
    private val getTodoItemUseCase = GetTodoItemUseCase(repositoryImpl)
    private val getTodoListUseCase = GetTodoListUseCase(repositoryImpl)
    private val updateTodoListUseCase = UpdateTodoListUseCase(repositoryImpl)

    fun addTodoItem(todoItem: TodoItem): TodoItem = addTodoItemUseCase(todoItem)
    fun updateTodoItem(todoItem: TodoItem): TodoItem = updateTodoItemUseCase(todoItem)
    fun removeTodoItem(id: String): TodoItem? = removeTodoItemUseCase(id)
    fun getTodoItem(id: String): TodoItem? = getTodoItemUseCase(id)
    fun getTodoList(): List<TodoItem> = getTodoListUseCase()
    fun updateTodoList(todoList: List<TodoItem>): List<TodoItem> = updateTodoListUseCase(todoList)
}