package com.example

import com.example.plugins.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*



fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module(){
    val port = System.getenv()["PORT"]
    val mongoUrl = System.getenv()["MONGO_URL"]
    val authApiUrl = System.getenv()["AUTH_API_URL"]
    println("Получены переменные окружения: \n\tport=$port\n\tmongoUrl=$mongoUrl\n\tauthApiUrl=$authApiUrl")
    configureRouting()
    configureSerialization()
}
