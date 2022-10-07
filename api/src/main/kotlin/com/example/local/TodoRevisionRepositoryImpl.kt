package com.example.data.repository.local

import com.example.domain.entity.TodoRevision
import com.example.domain.repository.TodoRevisionRepository
import com.mongodb.client.model.Filters
import org.litote.kmongo.coroutine.CoroutineDatabase

class TodoRevisionRepositoryImpl(dataBase: CoroutineDatabase) : TodoRevisionRepository {
    private val collectionTodoRevision = dataBase.getCollection<TodoRevision>()

    override suspend fun getTodoRevision(userId: Long, deviceId: Long): TodoRevision? {
        val filter = Filters.and(
            Filters.eq("userId",userId),
            Filters.eq("deviceId",deviceId)
        )
        return collectionTodoRevision.findOne(filter)
    }

    override suspend fun setTodoRevision(todoRevision: TodoRevision): TodoRevision {
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