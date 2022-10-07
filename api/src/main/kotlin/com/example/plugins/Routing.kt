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
    get("/") {
        println("test get /")
        call.respondText {
            "OK API"
        }
    }
    authenticate("auth-jwt") {
        route("/list") {
            // Получить список
            get {
                checkInternalServerError(call) {
                    val (userId, _, todoRevision) = getUserInfo(call)
                    val todoList = LocalApi.getTodoList(userId)
                    call.respond(
                        TodoListResponse(
                            list = todoList,
                            revision = todoRevision.revision,
                        )
                    )
                }
            }
            // Обновить список
            patch {
                checkInternalServerError(call) {
                    val (userId, deviceId, todoRevision) = getUserInfo(call)
                    val incrementedTodoRevision =
                        LocalApi.setTodoRevision(TodoRevision(userId, deviceId, todoRevision.revision + 1))!!
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
                    val (userId, _, todoRevision) = getUserInfo(call)
                    val id = call.parameters["id"] ?: return@get call.respond(
                        TodoListResponse(
                            status = HttpStatusCode.BadRequest.value,
                            message = "BadRequest: id не предоставлен"
                        )
                    )
                    val todoItem = LocalApi.getTodoItem(userId, id) ?: return@get call.respond(
                        TodoListResponse(
                            status = HttpStatusCode.NotFound.value,
                            message = "NotFound: Элемент с id: $id не существует"
                        )
                    )
                    call.respond(
                        TodoListResponse(
                            list = listOf(todoItem),
                            revision = todoRevision.revision
                        )
                    )
                }
            }
            // Добавить элемент
            post {
                checkInternalServerError(call) {
                    val (userId, deviceId, todoRevision) = getUserInfo(call)
                    val lastRevision = LocalApi.getTodoRevision(userId, deviceId)
                    if (todoRevision != lastRevision){
                        return@post call.respond(
                            TodoListResponse(
                                status = HttpStatusCode.BadRequest.value,
                                message = "BadRequest: Ревизия не совпадает"
                            )
                        )
                    }
                    val newTodoRevision =
                        LocalApi.setTodoRevision(TodoRevision(userId, deviceId, todoRevision.revision + 1))!!
                    val todoItem = call.receiveNullable<TodoItem>()
                    todoItem ?: return@post call.respond(
                        TodoListResponse(
                            status = HttpStatusCode.BadRequest.value,
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
                    val (userId, deviceId, todoRevision) = getUserInfo(call)
                    val lastRevision = LocalApi.getTodoRevision(userId, deviceId)
                    if (todoRevision != lastRevision){
                        return@put call.respond(
                            TodoListResponse(
                                status = HttpStatusCode.BadRequest.value,
                                message = "BadRequest: Ревизия не совпадает"
                            )
                        )
                    }
                    val newTodoRevision =
                        LocalApi.setTodoRevision(TodoRevision(userId, deviceId, todoRevision.revision + 1))!!
                    val id = call.parameters["id"] ?: return@put call.respond(
                        TodoListResponse(
                            status = HttpStatusCode.BadRequest.value,
                            message = "BadRequest: id не предоставлен"
                        )
                    )
                    val todoItem = call.receiveNullable<TodoItem>()
                    todoItem ?: return@put call.respond(
                        TodoListResponse(
                            status = HttpStatusCode.BadRequest.value,
                            message = "BadRequest: не предоставлен элемент TodoItem"
                        )
                    )
                    val updatedTodoItem = LocalApi.updateTodoItem(userId, todoItem)
                    updatedTodoItem ?: return@put call.respond(
                        TodoListResponse(
                            status = HttpStatusCode.NotFound.value,
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
                    val (userId, deviceId, todoRevision) = getUserInfo(call)
                    val lastRevision = LocalApi.getTodoRevision(userId, deviceId)
                    if (todoRevision != lastRevision){
                        return@delete call.respond(
                            TodoListResponse(
                                status = HttpStatusCode.BadRequest.value,
                                message = "BadRequest: Ревизия не совпадает"
                            )
                        )
                    }
                    val newTodoRevision =
                        LocalApi.setTodoRevision(TodoRevision(userId, deviceId, todoRevision.revision + 1))!!
                    val id = call.parameters["id"] ?: return@delete call.respond(
                        TodoListResponse(
                            status = HttpStatusCode.BadRequest.value,
                            message = "BadRequest: id не предоставлен"
                        )
                    )

                    val deletedTodoItem = LocalApi.removeTodoItem(userId, id)
                    deletedTodoItem ?: return@delete call.respond(
                        TodoListResponse(
                            status = HttpStatusCode.NotFound.value,
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

private suspend fun getTodoRevision(userId: String, deviceId:String): TodoRevision {
    var todoRevision = LocalApi.getTodoRevision(userId, deviceId)
    if (todoRevision == null) {
        todoRevision = LocalApi.setTodoRevision(TodoRevision(userId, deviceId, 1))
    }
    return todoRevision!!
}
data class UserInfo(val userId: String,val deviceId: String, val todoRevision: TodoRevision)

private suspend fun getUserInfo(call: ApplicationCall): UserInfo {
    val principal = call.principal<JWTPrincipal>()
    val userId = principal!!.payload.getClaim("userId").asString()
    val deviceId = principal.payload.getClaim("deviceId").asString()
    val todoRevision = getTodoRevision(userId, deviceId)
    return UserInfo(userId, deviceId, todoRevision)
}
private suspend inline fun checkInternalServerError(call: ApplicationCall, block: ()->Unit) {
    try {
        block()
    } catch (e: Exception) {
        call.respond(
            TodoListResponse(
                status = HttpStatusCode.InternalServerError.value,
                list = emptyList(),
                revision = 0,
                message = "InternalServerError"
            )
        )
    }
}