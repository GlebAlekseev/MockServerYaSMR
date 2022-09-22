package com.example

import com.example.plugins.configureRouting
import com.example.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.netty.*


fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.module(){
    System.getenv()["PORT"] ?: throw RuntimeException("ENV PORT is not exist")
    System.getenv()["MONGO_URL"] ?: throw RuntimeException("ENV MONGO_URL is not exist")
    System.getenv()["AUTH_API_URL"] ?: throw RuntimeException("ENV AUTH_API_URL is not exist")
    configureRouting()
    configureSerialization()
}