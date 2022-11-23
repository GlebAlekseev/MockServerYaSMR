package com.example.data.repository.local

import com.example.domain.entity.TodoItem
import com.example.domain.entity.TodoItem.Companion.UNDEFINED
import com.example.domain.repository.TodoItemRepository
import com.mongodb.client.model.Filters
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.deleteOne
import org.litote.kmongo.eq

class TodoItemRepositoryImpl(dataBase: CoroutineDatabase) : TodoItemRepository {
    private val collectionTodoItem = dataBase.getCollection<TodoItem>()
    override suspend fun getTodoList(userId: Long): List<TodoItem> {
        return collectionTodoItem.find(TodoItem::userId eq userId).toList()
    }

    override suspend fun deleteTodoList(userId: Long): List<TodoItem> {
        val result = collectionTodoItem.find(TodoItem::userId eq userId).toList()
        collectionTodoItem.deleteMany(TodoItem::userId eq userId)
        return result
    }

    override suspend fun updateTodoList(userId: Long, list: List<TodoItem>): List<TodoItem> {
        list.forEach {
            val filter = Filters.and(
                Filters.eq("userId", userId),
                Filters.eq("id", it.id)
            )
            collectionTodoItem.deleteOne(filter)
            collectionTodoItem.insertOne(it.copy())
        }
        return collectionTodoItem.find(TodoItem::userId eq userId).toList()
    }

    override suspend fun getTodoItem(userId: Long, id: Long): TodoItem? {
        val filter = Filters.and(
            Filters.eq("userId", userId),
            Filters.eq("id", id)
        )
        return collectionTodoItem.findOne(filter)
    }

    override suspend fun removeTodoItem(userId: Long, id: Long): TodoItem? {
        val filter = Filters.and(
            Filters.eq("userId", userId),
            Filters.eq("id", id)
        )
        return collectionTodoItem.findOneAndDelete(filter)
    }

    override suspend fun addTodoItem(userId: Long, todoItem: TodoItem): TodoItem? {
        val count = collectionTodoItem.countDocuments()
        val maxId = if (count != 0L) {
            collectionTodoItem.find(TodoItem::userId eq userId).toList().maxBy { it.id }.id
        } else {
            0
        }
        collectionTodoItem.insertOne(todoItem.copy(userId = userId, id = maxId + 1))

        val filter = Filters.and(
            Filters.eq("userId", userId),
            Filters.eq("id", maxId + 1)
        )

        return collectionTodoItem.findOne(filter)
    }

    override suspend fun updateTodoItem(userId: Long, todoItem: TodoItem): TodoItem? {
        val filter = Filters.and(
            Filters.eq("userId", userId),
            Filters.eq("id", todoItem.id)
        )
        collectionTodoItem.updateOne(filter, todoItem)
        return collectionTodoItem.findOne(filter)
    }
}