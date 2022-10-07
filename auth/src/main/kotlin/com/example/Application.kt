package com.example

import io.ktor.server.application.*
import com.example.plugins.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.netty.*

fun main(args: Array<String>): Unit = EngineMain.main(args)

val applicationHttpClient = HttpClient(CIO) {
    install(ContentNegotiation) {
        json()
    }
}
fun Application.module(){
    checkEnv()
    configureJwt()
    configureOAuthYandex(applicationHttpClient)
    configureRouting(applicationHttpClient)
    configureSerialization()
}

fun checkEnv(){
    val port = System.getenv()["PORT"] ?: throw RuntimeException("ENV PORT is not exist")
    val mongoUrl = System.getenv()["MONGO_URL"] ?: throw RuntimeException("ENV MONGO_URL is not exist")
    val apiUrl = System.getenv()["API_URL"] ?: throw RuntimeException("ENV API_URL is not exist")
    val clientId = System.getenv()["YANDEX_CLIENT_ID"] ?: throw RuntimeException("ENV YANDEX_CLIENT_ID is not exist")
    val clientSecret = System.getenv()["YANDEX_CLIENT_SECRET"] ?: throw RuntimeException("ENV YANDEX_CLIENT_SECRET is not exist")
    val yandexCallback = System.getenv()["YANDEX_CALLBACK"] ?: throw RuntimeException("ENV YANDEX_CALLBACK is not exist")
    println("Получены переменные окружения: " +
            "\n\tport=$port" +
            "\n\tmongoUrl=$mongoUrl" +
            "\n\tapiUrl=$apiUrl" +
            "\n\tclientId=$clientId" +
            "\n\tclientSecret=$clientSecret" +
            "\n\tyandexCallback=$yandexCallback"
    )
}