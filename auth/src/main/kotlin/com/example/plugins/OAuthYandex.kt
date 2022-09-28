package com.example.plugins

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*


fun Application.configureOAuthYandex(applicationHttpClient: HttpClient){
    install(Authentication) {
        oauth("auth-oauth-google") {
            urlProvider = { "http://localhost:8080/callback" }
            providerLookup = {
                OAuthServerSettings.OAuth2ServerSettings(
                    name = "yandex",
                    authorizeUrl = "https://oauth.yandex.ru/authorize",
                    accessTokenUrl = "https://oauth.yandex.ru/token",
                    requestMethod = HttpMethod.Post,
                    clientId = System.getenv()["YANDEX_CLIENT_ID"]!!,
                    clientSecret = System.getenv()["YANDEX_CLIENT_SECRET"]!!,
                )
            }
            client = applicationHttpClient
        }
    }
}