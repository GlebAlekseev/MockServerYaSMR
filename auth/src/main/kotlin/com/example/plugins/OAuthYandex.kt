package com.example.plugins

import io.ktor.client.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*


fun Application.configureOAuthYandex(applicationHttpClient: HttpClient) {
    var issuer = "http://localhost:8080/callback"
    System.getenv()["YANDEX_CALLBACK"]?.let {
        issuer = it
    }
    install(Authentication) {
        oauth("auth-oauth-yandex") {
            urlProvider = { issuer }
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