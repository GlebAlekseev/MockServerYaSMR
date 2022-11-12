package com.example.plugins

import com.auth0.jwk.JwkProviderBuilder
import io.ktor.client.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import java.util.concurrent.TimeUnit

fun Application.configureAuthentication(applicationHttpClient: HttpClient) {
    // OAuth
    var issuerOAuth = "http://localhost:8080/callback"
    System.getenv()["YANDEX_CALLBACK"]?.let {
        issuerOAuth = it
    }

    install(Authentication) {
        configureOAuthYandex(applicationHttpClient, issuerOAuth)
    }
}