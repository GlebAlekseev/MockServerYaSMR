package com.example.plugins

import io.ktor.client.*
import io.ktor.http.*
import io.ktor.server.auth.*


fun AuthenticationConfig.configureOAuthYandex(applicationHttpClient: HttpClient, issuer: String) {
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

