package com.example

import io.ktor.server.application.*
import com.example.plugins.*

import com.example.plugins.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module(){
    val port = System.getenv()["PORT"]
    val mongoUrl = System.getenv()["MONGO_URL"]
    val apiUrl = System.getenv()["API_URL"]
    println("Получены переменные окружения: \n\tport=$port\n\tmongoUrl=$mongoUrl\n\tapiUrl=$apiUrl")
    configureRouting()
    configureSerialization()
}