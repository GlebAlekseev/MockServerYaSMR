package com.example.plugins

import io.ktor.client.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*


fun Application.configureOAuthYandex(applicationHttpClient: HttpClient) {
    var issuer = "http://localhost:8080/callback"
    if (System.getenv()["YANDEX_CALLBACK"] != null) {
        issuer = System.getenv()["YANDEX_CALLBACK"]!!
    }
    install(Authentication) {
        oauth("auth-oauth-google") {
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