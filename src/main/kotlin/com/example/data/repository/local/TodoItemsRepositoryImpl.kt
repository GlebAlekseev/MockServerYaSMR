package com.example.data.repository.local

import com.example.domain.entity.TodoItem
import com.example.domain.repository.TodoItemsRepository

class TodoItemsRepositoryImpl: TodoItemsRepository {
    private var todoList: MutableList<TodoItem> = TodoItem.DEFAULT_LIST.toMutableList()

    override fun getTodoList(): List<TodoItem> {
        return todoList
    }

    override fun updateTodoList(list: List<TodoItem>): List<TodoItem> {
        todoList = list.toMutableList()
        return todoList
    }

    override fun getTodoItem(id: String): TodoItem? {
        return todoList.firstOrNull { it.id == id }
    }

    override fun removeTodoItem(id: String): TodoItem? {
        val item = todoList.firstOrNull { it.id == id }
        todoList.remove(item)
        return item
    }

    override fun addTodoItem(todoItem: TodoItem): TodoItem {
        todoList.add(todoItem)
        return todoItem
    }

    override fun updateTodoItem(todoItem: TodoItem): TodoItem {
        todoList.replaceAll { todoItem }
        return todoItem
    }
}