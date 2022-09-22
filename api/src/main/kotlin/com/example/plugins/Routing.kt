package com.example.plugins

import com.example.domain.entity.TodoItem
import com.example.server.LocalApi
import com.example.server.response.TodoItemResponse
import com.example.server.response.TodoListResponse
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*


fun Application.configureRouting() {
    routing {
        customRouting()
    }
}

fun Route.customRouting(){
    route("/list"){
        // Получить список
        get{
            val response = LocalApi.getTodoList()
            call.respond(TodoListResponse(list = response))
        }
        // Обновить список
        patch {
            val list = call.receive<List<TodoItem>>()
            val response = LocalApi.updateTodoList(list)
            call.respond(TodoListResponse(list = response))
        }
        // Получить элемент
        get("{id?}"){
            val id = call.parameters["id"] ?:
            return@get call.respond(TodoItemResponse(status = HttpStatusCode.BadRequest.value, message = "Missing id"))
            val response = LocalApi.getTodoItem(id) ?:
            return@get call.respond(TodoItemResponse(status = HttpStatusCode.NotFound.value, message = "No todo with id $id"))
            call.respond(TodoItemResponse(status = HttpStatusCode.OK.value, item = response))
        }
        // Добавить элемент
        post {
            val item = call.receive<TodoItem>()
            val response = LocalApi.addTodoItem(item)
            call.respond(TodoItemResponse(item = response))
        }
        // Обновить элемент
        put("{id?}"){
            val id = call.parameters["id"] ?:
            return@put call.respond(TodoItemResponse(status = HttpStatusCode.BadRequest.value, message = "Missing id"))
            val item = call.receive<TodoItem>().copy(id = id)
            val response = LocalApi.updateTodoItem(item)
            call.respond(TodoItemResponse(item = response))
        }
        // Удалить элемент
        delete("{id?}"){
            val id = call.parameters["id"] ?:
            return@delete call.respond(TodoItemResponse(status = HttpStatusCode.BadRequest.value, message = "Missing id"))
            val response = LocalApi.removeTodoItem(id)
            call.respond(TodoItemResponse(item = response))
        }
    }
}