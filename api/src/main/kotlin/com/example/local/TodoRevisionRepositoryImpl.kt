package com.example.data.repository.local

import com.example.domain.entity.TodoItem
import com.example.domain.entity.TodoRevision
import com.example.domain.repository.TodoItemRepository
import com.example.domain.repository.TodoRevisionRepository
import com.mongodb.client.model.Filters
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.awaitFirst
import org.litote.kmongo.coroutine.CoroutineDatabase
import org.litote.kmongo.eq

@OptIn(DelicateCoroutinesApi::class)
class TodoRevisionRepositoryImpl(private val dataBase: CoroutineDatabase) : TodoRevisionRepository {
    private val collectionTodoRevision = dataBase.getCollection<TodoRevision>()

    override suspend fun getTodoRevision(userId: String, deviceId: String): TodoRevision? {
        val filter = Filters.and(
            Filters.eq("userId",userId),
            Filters.eq("deviceId",deviceId)
        )
        return collectionTodoRevision.findOne(filter)
    }

    override suspend fun setTodoRevision(todoRevision: TodoRevision): TodoRevision? {
        val filter = Filters.and(
            Filters.eq("userId",todoRevision.userId),
            Filters.eq("deviceId",todoRevision.deviceId)
        )
        val item = collectionTodoRevision.find(filter).first()
        if (item == null){
            collectionTodoRevision.insertOne(todoRevision)
        }else{
            collectionTodoRevision.updateOne(filter,todoRevision)
        }
        return todoRevision
    }
}