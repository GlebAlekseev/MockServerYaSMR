package com.example.server

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.server.plugins.configureRouting
import com.example.server.plugins.configureSerialization

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureRouting()
        configureSerialization()
    }.start(wait = true)
}
