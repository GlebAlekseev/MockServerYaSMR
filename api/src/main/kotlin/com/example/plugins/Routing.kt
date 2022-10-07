package com.example.plugins

import com.example.domain.entity.TodoItem
import com.example.domain.entity.TodoRevision
import com.example.server.LocalApi
import com.example.server.response.TodoListResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*


fun Application.configureRouting() {
    routing {
        customRouting()
    }
}

fun Route.customRouting() {
    get("/"){
        println("test get /")
        call.respondText {

            "OK API"
        }
    }
    authenticate("auth-jwt") {
        route("/list") {
            // Получить список
            get {
                val principal = call.principal<JWTPrincipal>()
                val (userId,deviceId,todoRevision) = getUserInfo(principal)
                val list = LocalApi.getTodoList(userId)
                call.respond(
                    TodoListResponse(
                        list = list,
                        revision = todoRevision.revision,
                        message = "OK"
                    )
                )
            }
            // Обновить список
            patch {
                val principal = call.principal<JWTPrincipal>()
                val (userId,deviceId,todoRevision) = getUserInfo(principal)
                val newTodoRevision = LocalApi.setTodoRevision(TodoRevision(userId,deviceId,todoRevision.revision + 1))
                val list = call.receive<List<TodoItem>>()
                val response = LocalApi.updateTodoList(userId,list)
                call.respond(TodoListResponse(list = response, revision = newTodoRevision!!.revision))
            }
            // Получить элемент
            get("{id?}") {
                val principal = call.principal<JWTPrincipal>()
                val (userId,deviceId,todoRevision) = getUserInfo(principal)
                val id = call.parameters["id"] ?: return@get call.respond(
                    TodoListResponse(
                        status = HttpStatusCode.BadRequest.value,
                        message = "Missing id"
                    )
                )
                val response = LocalApi.getTodoItem(userId,id) ?: return@get call.respond(
                    TodoListResponse(
                        status = HttpStatusCode.NotFound.value,
                        message = "No todo with id $id"
                    )
                )
                call.respond(TodoListResponse(status = HttpStatusCode.OK.value, list = listOf(response), revision = todoRevision.revision))
            }
            // Добавить элемент
            post {
                val principal = call.principal<JWTPrincipal>()
                val (userId,deviceId,todoRevision) = getUserInfo(principal)
                val newTodoRevision = LocalApi.setTodoRevision(TodoRevision(userId,deviceId,todoRevision.revision + 1))
                val item = call.receive<TodoItem>()
                val response = LocalApi.addTodoItem(userId,item)
                call.respond(TodoListResponse(list = if (response == null) emptyList() else listOf(response), revision = newTodoRevision!!.revision))
            }
            // Обновить элемент
            put("{id?}") {
                val principal = call.principal<JWTPrincipal>()
                val (userId,deviceId,todoRevision) = getUserInfo(principal)
                val newTodoRevision = LocalApi.setTodoRevision(TodoRevision(userId,deviceId,todoRevision.revision + 1))

                val id = call.parameters["id"] ?: return@put call.respond(
                    TodoListResponse(
                        status = HttpStatusCode.BadRequest.value,
                        message = "Missing id"
                    )
                )
                val item = call.receive<TodoItem>().copy(id = id)
                val response = LocalApi.updateTodoItem(userId,item)
                call.respond(TodoListResponse(list = if (response == null) emptyList() else listOf(response), revision = newTodoRevision!!.revision))
            }
            // Удалить элемент
            delete("{id?}") {
                val principal = call.principal<JWTPrincipal>()
                val (userId,deviceId,todoRevision) = getUserInfo(principal)
                val newTodoRevision = LocalApi.setTodoRevision(TodoRevision(userId,deviceId,todoRevision.revision + 1))
                val id = call.parameters["id"] ?: return@delete call.respond(
                    TodoListResponse(
                        status = HttpStatusCode.BadRequest.value,
                        message = "Missing id"
                    )
                )
                val response = LocalApi.removeTodoItem(userId,id)
                call.respond(TodoListResponse(list = if (response == null) emptyList() else listOf(response), revision = newTodoRevision!!.revision))
            }
        }
    }
}

private suspend fun getTodoRevision(userId: String, deviceId:String): TodoRevision{
    var todoRevision = LocalApi.getTodoRevision(userId,deviceId)
    if (todoRevision == null){
        todoRevision = LocalApi.setTodoRevision(TodoRevision(userId,deviceId,1))
    }
    return todoRevision!!
}
data class UserInfo(val userId: String,val deviceId: String, val todoRevision: TodoRevision)

private suspend fun getUserInfo(principal: JWTPrincipal?): UserInfo{
    val userId = principal!!.payload.getClaim("userId").asString()
    val deviceId = principal.payload.getClaim("deviceId").asString()
    val todoRevision = getTodoRevision(userId,deviceId)
    return UserInfo(userId,deviceId,todoRevision)
}
