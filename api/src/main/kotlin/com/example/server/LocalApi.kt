package com.example.server

import com.example.data.repository.local.TodoItemRepositoryImpl
import com.example.data.repository.local.TodoRevisionRepositoryImpl
import com.example.domain.entity.TodoItem
import com.example.domain.entity.TodoRevision
import com.example.domain.interactor.todoitem.*
import com.example.domain.interactor.todorevision.GetTodoRevisionUseCase
import com.example.domain.interactor.todorevision.SetTodoRevisionUseCase
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

object LocalApi {
    private val database = getApiDataBase()

    private val todoItemRepositoryImpl = TodoItemRepositoryImpl(database)
    private val addTodoItemUseCase = AddTodoItemUseCase(todoItemRepositoryImpl)
    private val updateTodoItemUseCase = UpdateTodoItemUseCase(todoItemRepositoryImpl)
    private val removeTodoItemUseCase = RemoveTodoItemUseCase(todoItemRepositoryImpl)
    private val getTodoItemUseCase = GetTodoItemUseCase(todoItemRepositoryImpl)
    private val getTodoListUseCase = GetTodoListUseCase(todoItemRepositoryImpl)
    private val updateTodoListUseCase = UpdateTodoListUseCase(todoItemRepositoryImpl)

    private val todoRevisionRepositoryImpl = TodoRevisionRepositoryImpl(database)
    private val getTodoRevisionUseCase = GetTodoRevisionUseCase(todoRevisionRepositoryImpl)
    private val setTodoRevisionUseCase = SetTodoRevisionUseCase(todoRevisionRepositoryImpl)


    suspend fun addTodoItem(userId: String, todoItem: TodoItem): TodoItem? = addTodoItemUseCase(userId,todoItem)
    suspend fun updateTodoItem(userId: String,todoItem: TodoItem): TodoItem? = updateTodoItemUseCase(userId,todoItem)
    suspend fun removeTodoItem(userId: String,id: String): TodoItem? = removeTodoItemUseCase(userId,id)
    suspend fun getTodoItem(userId: String,id: String): TodoItem? = getTodoItemUseCase(userId,id)
    suspend fun getTodoList(userId: String,): List<TodoItem> = getTodoListUseCase(userId)
    suspend fun updateTodoList(userId: String,todoList: List<TodoItem>): List<TodoItem> = updateTodoListUseCase(userId,todoList)

    suspend fun getTodoRevision(userId: String, deviceId: String): TodoRevision? =
        getTodoRevisionUseCase(userId, deviceId)

    suspend fun setTodoRevision(todoRevision: TodoRevision): TodoRevision? = setTodoRevisionUseCase(todoRevision)


    private fun getApiDataBase(): CoroutineDatabase {
        KMongo.createClient()
        val client = KMongo.createClient(System.getenv()["MONGO_URL"]!!).coroutine
        return client.getDatabase("api_v1")
    }
}