package com.example

import io.ktor.server.application.*
import com.example.plugins.*

import com.example.plugins.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

fun main(){
    val port = System.getenv()["PORT"]
    val mongoUrl = System.getenv()["MONGO_URL"]
    val authApiUrl = System.getenv()["AUTH_API_URL"]
    println("Получены переменные окружения: \n\tport=$port\n\tmongoUrl=$mongoUrl\n\tauthApiUrl=$authApiUrl")
    embeddedServer(Netty, port = port?.toInt() ?: 8889) {
        configureRouting()
        configureSerialization()
    }.start(wait = true)
}
