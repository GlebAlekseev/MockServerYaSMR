package com.example.server

import com.example.data.repository.local.TodoItemsRepositoryImpl
import com.example.domain.entity.TodoItem
import com.example.domain.interactor.*
import org.litote.kmongo.reactivestreams.*
import org.litote.kmongo.coroutine.*

object LocalApi {
    private val database = getApiDataBase()
    private val repositoryImpl = TodoItemsRepositoryImpl(database)
    private val addTodoItemUseCase = AddTodoItemUseCase(repositoryImpl)
    private val updateTodoItemUseCase = UpdateTodoItemUseCase(repositoryImpl)
    private val removeTodoItemUseCase = RemoveTodoItemUseCase(repositoryImpl)
    private val getTodoItemUseCase = GetTodoItemUseCase(repositoryImpl)
    private val getTodoListUseCase = GetTodoListUseCase(repositoryImpl)
    private val updateTodoListUseCase = UpdateTodoListUseCase(repositoryImpl)

    suspend fun addTodoItem(todoItem: TodoItem): TodoItem? = addTodoItemUseCase(todoItem)
    suspend fun updateTodoItem(todoItem: TodoItem): TodoItem? = updateTodoItemUseCase(todoItem)
    suspend fun removeTodoItem(id: String): TodoItem? = removeTodoItemUseCase(id)
    suspend fun getTodoItem(id: String): TodoItem? = getTodoItemUseCase(id)
    suspend fun getTodoList(): List<TodoItem> = getTodoListUseCase()
    suspend fun updateTodoList(todoList: List<TodoItem>): List<TodoItem> = updateTodoListUseCase(todoList)

    private fun getApiDataBase(): CoroutineDatabase {
        KMongo.createClient()
        val client = KMongo.createClient(System.getenv()["MONGO_URL"]!!).coroutine
        return client.getDatabase("api")
    }
}