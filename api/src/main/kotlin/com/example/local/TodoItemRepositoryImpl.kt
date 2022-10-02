package com.example.data.repository.local

import com.example.domain.entity.TodoItem
import com.example.domain.repository.TodoItemRepository
import com.mongodb.client.model.Filters
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.awaitFirst
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

@OptIn(DelicateCoroutinesApi::class)
class TodoItemRepositoryImpl(private val dataBase: CoroutineDatabase) : TodoItemRepository {
    private val collectionTodoItem = dataBase.getCollection<TodoItem>()
    override suspend fun getTodoList(userId:String): List<TodoItem> {
        return collectionTodoItem.find(TodoItem::userId eq userId).toList()
    }

    override suspend fun updateTodoList(userId:String, list: List<TodoItem>): List<TodoItem> {
        collectionTodoItem.deleteMany(TodoItem::userId eq userId)
        var id = 1
        list.forEach {item->
            collectionTodoItem.insertOne(item.copy(userId=userId,id = id.toString()))
            id++
        }
        return collectionTodoItem.find(TodoItem::userId eq userId).toList()
    }

    override suspend fun getTodoItem(userId: String, id: String): TodoItem? {
        val filter = Filters.and(
            Filters.eq("userId",userId),
            Filters.eq("id",id)
        )
        return collectionTodoItem.findOne(filter)
    }

    override suspend fun removeTodoItem(userId:String, id: String): TodoItem? {
        val filter = Filters.and(
            Filters.eq("userId",userId),
            Filters.eq("id",id)
        )
        return collectionTodoItem.findOneAndDelete(filter)
    }

    override suspend fun addTodoItem(userId:String, todoItem: TodoItem): TodoItem? {
        val maxId = collectionTodoItem.find(TodoItem::userId eq userId).toList().maxBy { it.id }.id
        collectionTodoItem.insertOne(todoItem.copy(userId=userId, id = (maxId.toInt()+1).toString()))
        val filter = Filters.and(
            Filters.eq("userId",userId),
            Filters.eq("id",todoItem.id)
        )
        return collectionTodoItem.findOne(filter)
    }

    override suspend fun updateTodoItem(userId:String, todoItem: TodoItem): TodoItem? {
        val filter = Filters.and(
            Filters.eq("userId",userId),
            Filters.eq("id",todoItem.id)
        )
        collectionTodoItem.updateOne(filter, todoItem)
        return collectionTodoItem.findOne(filter)
    }
}