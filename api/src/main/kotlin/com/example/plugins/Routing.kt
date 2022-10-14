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
    authenticate("auth-jwt") {
        route("/list") {
            // Получить список
            get {
                checkInternalServerError(call) {
                    val (userId, _, todoRevisionServer) = getUserInfo(call)
                    val todoList = LocalApi.getTodoList(userId)
                    call.respond(
                        TodoListResponse(
                            list = todoList,
                            revision = todoRevisionServer.revision,
                        )
                    )
                }
            }
            // Обновить список
            patch {
                checkInternalServerError(call) {
                    val (userId, deviceId, todoRevisionServer) = getUserInfo(call)
                    val lastKnownRevision = call.request.header("X-Last-Known-Revision")!!.toLong()
                    if (todoRevisionServer.revision != lastKnownRevision){
                        return@patch call.respond(
                            HttpStatusCode.BadRequest,
                            TodoListResponse(
                                message = "BadRequest: Ревизия не совпадает"
                            )
                        )
                    }
                    val incrementedTodoRevision =
                        LocalApi.setTodoRevision(TodoRevision(userId, deviceId, lastKnownRevision + 1))!!
                    val receivedTodoList = call.receive<List<TodoItem>>()
                    val todoList = LocalApi.updateTodoList(userId, receivedTodoList)
                    call.respond(
                        TodoListResponse(
                            list = todoList,
                            revision = incrementedTodoRevision.revision
                        )
                    )
                }
            }
            // Получить элемент
            get("{id?}") {
                checkInternalServerError(call) {
                    val (userId, _, todoRevisionServer) = getUserInfo(call)
                    val id = call.parameters["id"] ?: return@get call.respond(
                        HttpStatusCode.BadRequest,
                        TodoListResponse(
                            message = "BadRequest: id не предоставлен"
                        )
                    )
                    val todoItem = LocalApi.getTodoItem(userId, id.toLong()) ?: return@get call.respond(
                        HttpStatusCode.NotFound,
                        TodoListResponse(
                            message = "NotFound: Элемент с id: $id не существует"
                        )
                    )
                    call.respond(
                        TodoListResponse(
                            list = listOf(todoItem),
                            revision = todoRevisionServer.revision
                        )
                    )
                }
            }
            // Добавить элемент
            post {
                checkInternalServerError(call) {
                    val (userId, deviceId, todoRevisionServer) = getUserInfo(call)
                    val lastKnownRevision = call.request.header("X-Last-Known-Revision")!!.toLong()
                    if (todoRevisionServer.revision != lastKnownRevision){
                        return@post call.respond(
                            HttpStatusCode.BadRequest,
                            TodoListResponse(
                                message = "BadRequest: Ревизия не совпадает"
                            )
                        )
                    }
                    val newTodoRevision =
                        LocalApi.setTodoRevision(TodoRevision(userId, deviceId, lastKnownRevision + 1))!!
                    val todoItem = call.receiveNullable<TodoItem>()
                    todoItem ?: return@post call.respond(
                        HttpStatusCode.BadRequest,
                        TodoListResponse(
                            message = "BadRequest: не предоставлен элемент TodoItem"
                        )
                    )
                    val addedTodoItem = LocalApi.addTodoItem(userId, todoItem)!!
                    call.respond(
                        TodoListResponse(
                            list = listOf(addedTodoItem),
                            revision = newTodoRevision.revision
                        )
                    )
                }
            }
            // Обновить элемент
            put("{id?}") {
                checkInternalServerError(call) {
                    val (userId, deviceId, todoRevisionServer) = getUserInfo(call)
                    val lastKnownRevision = call.request.header("X-Last-Known-Revision")!!.toLong()
                    if (todoRevisionServer.revision != lastKnownRevision){
                        return@put call.respond(
                            HttpStatusCode.BadRequest,
                            TodoListResponse(
                                message = "BadRequest: Ревизия не совпадает"
                            )
                        )
                    }
                    val newTodoRevision =
                        LocalApi.setTodoRevision(TodoRevision(userId, deviceId, lastKnownRevision + 1))!!
                    val id = call.parameters["id"] ?: return@put call.respond(
                        HttpStatusCode.BadRequest,
                        TodoListResponse(
                            message = "BadRequest: id не предоставлен"
                        )
                    )
                    val todoItem = call.receiveNullable<TodoItem>()
                    todoItem ?: return@put call.respond(
                        HttpStatusCode.BadRequest,
                        TodoListResponse(
                            message = "BadRequest: не предоставлен элемент TodoItem"
                        )
                    )
                    val updatedTodoItem = LocalApi.updateTodoItem(userId, todoItem.copy(id = id.toLong()))
                    updatedTodoItem ?: return@put call.respond(
                        HttpStatusCode.NotFound,
                        TodoListResponse(
                            message = "NotFound: Элемент с id: $id не существует"
                        )
                    )
                    call.respond(
                        TodoListResponse(
                            list = listOf(updatedTodoItem),
                            revision = newTodoRevision.revision
                        )
                    )
                }
            }
            // Удалить элемент
            delete("{id?}") {
                checkInternalServerError(call) {
                    val (userId, deviceId, todoRevisionServer) = getUserInfo(call)
                    val lastKnownRevision = call.request.header("X-Last-Known-Revision")!!.toLong()
                    if (todoRevisionServer.revision != lastKnownRevision){
                        return@delete call.respond(
                            HttpStatusCode.BadRequest,
                            TodoListResponse(
                                message = "BadRequest: Ревизия не совпадает"
                            )
                        )
                    }
                    val newTodoRevision =
                        LocalApi.setTodoRevision(TodoRevision(userId, deviceId, lastKnownRevision + 1))!!
                    val id = call.parameters["id"] ?: return@delete call.respond(
                        HttpStatusCode.BadRequest,
                        TodoListResponse(
                            message = "BadRequest: id не предоставлен"
                        )
                    )

                    val deletedTodoItem = LocalApi.removeTodoItem(userId, id.toLong())
                    deletedTodoItem ?: return@delete call.respond(
                        HttpStatusCode.NotFound,
                        TodoListResponse(
                            message = "NotFound: Элемент с id: $id не существует"
                        )
                    )
                    call.respond(
                        TodoListResponse(
                            list = listOf(deletedTodoItem),
                            revision = newTodoRevision.revision
                        )
                    )
                }
            }
        }
    }
}

private suspend fun getTodoRevision(userId: Long, deviceId: Long): TodoRevision {
    var todoRevision = LocalApi.getTodoRevision(userId, deviceId)
    if (todoRevision == null) {
        todoRevision = LocalApi.setTodoRevision(TodoRevision(userId, deviceId, 1))
    }
    return todoRevision!!
}
data class UserInfo(val userId: Long,val deviceId: Long, val todoRevision: TodoRevision)

private suspend fun getUserInfo(call: ApplicationCall): UserInfo {
    val principal = call.principal<JWTPrincipal>()
    val userId = principal!!.payload.getClaim("userId").asString().toLong()
    val deviceId = principal.payload.getClaim("deviceId").asString().toLong()
    val todoRevisionServer = getTodoRevision(userId, deviceId)
    return UserInfo(userId, deviceId, todoRevisionServer)
}
private suspend inline fun checkInternalServerError(call: ApplicationCall, block: ()->Unit) {
    try {
        block()
    } catch (e: Exception) {
        call.respond(
            HttpStatusCode.InternalServerError,
            TodoListResponse(
                message = "InternalServerError"
            )
        )
    }
}