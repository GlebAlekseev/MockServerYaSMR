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


    suspend fun addTodoItem(todoItem: TodoItem): TodoItem? = addTodoItemUseCase(todoItem)
    suspend fun updateTodoItem(todoItem: TodoItem): TodoItem? = updateTodoItemUseCase(todoItem)
    suspend fun removeTodoItem(id: String): TodoItem? = removeTodoItemUseCase(id)
    suspend fun getTodoItem(id: String): TodoItem? = getTodoItemUseCase(id)
    suspend fun getTodoList(): List<TodoItem> = getTodoListUseCase()
    suspend fun updateTodoList(todoList: List<TodoItem>): List<TodoItem> = updateTodoListUseCase(todoList)

    suspend fun getTodoRevision(userId: String, deviceId: String): TodoRevision? =
        getTodoRevisionUseCase(userId, deviceId)

    suspend fun setTodoRevision(todoRevision: TodoRevision): TodoRevision? = setTodoRevisionUseCase(todoRevision)


    private fun getApiDataBase(): CoroutineDatabase {
        KMongo.createClient()
        val client = KMongo.createClient(System.getenv()["MONGO_URL"]!!).coroutine
        return client.getDatabase("api")
    }
}