package com.example.data.repository.local

import com.example.domain.entity.TodoItem
import com.example.domain.repository.TodoItemsRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.awaitFirst
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

@OptIn(DelicateCoroutinesApi::class)
class TodoItemsRepositoryImpl(private val dataBase: CoroutineDatabase): TodoItemsRepository {
    init {
        GlobalScope.launch {
            val col = dataBase.getCollection<TodoItem>()
            if (col.collection.countDocuments().awaitFirst() == 0L){
                col.insertMany(TodoItem.DEFAULT_LIST)
            }
        }
    }
    private val collectionTodoItem = dataBase.getCollection<TodoItem>()
    override suspend fun getTodoList(): List<TodoItem> {
        return collectionTodoItem.find().toList()
    }

    override suspend fun updateTodoList(list: List<TodoItem>): List<TodoItem> {
        collectionTodoItem.deleteMany()
        collectionTodoItem.insertMany(list)
        collectionTodoItem.distinct(TodoItem::id)
        return collectionTodoItem.find().toList()
    }

    override suspend fun getTodoItem(id: String): TodoItem? {
        return collectionTodoItem.findOne(TodoItem::id eq id)
    }

    override suspend fun removeTodoItem(id: String): TodoItem? {
        return collectionTodoItem.findOneAndDelete(TodoItem::id eq id)
    }

    override suspend fun addTodoItem(todoItem: TodoItem): TodoItem? {
        collectionTodoItem.insertOne(todoItem)
        collectionTodoItem.distinct(TodoItem::id)
        return collectionTodoItem.findOne(TodoItem::id eq todoItem.id)
    }

    override suspend fun updateTodoItem(todoItem: TodoItem): TodoItem? {
        collectionTodoItem.updateOne(TodoItem::id eq todoItem.id,todoItem)
        return collectionTodoItem.findOne(TodoItem::id eq todoItem.id)
    }
}