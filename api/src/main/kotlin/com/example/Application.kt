package com.example

import com.example.plugins.configureJwt
import com.example.plugins.configureRouting
import com.example.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.netty.*


fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module(){
    configureSerialization()
    checkEnv()
    configureJwt()
    configureRouting()
}

fun checkEnv(){
    val port = System.getenv()["PORT"] ?: throw RuntimeException("ENV PORT is not exist")
    val mongoUrl = System.getenv()["MONGO_URL"] ?: throw RuntimeException("ENV MONGO_URL is not exist")
    val authUrl = System.getenv()["AUTH_API_URL"] ?: throw RuntimeException("ENV API_URL is not exist")
    println("Получены переменные окружения: \n\tport=$port\n\tmongoUrl=$mongoUrl\n\tauthUrl=$authUrl\n\t")
}