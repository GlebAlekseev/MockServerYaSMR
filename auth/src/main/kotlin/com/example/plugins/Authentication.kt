package com.example.plugins

import com.auth0.jwk.JwkProviderBuilder
import io.ktor.client.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import java.util.concurrent.TimeUnit

fun Application.configureAuthentication(applicationHttpClient: HttpClient) {
    //JWT
    var issuerJWT = environment.config.property("jwt.issuer").getString()
    val jwkProvider = JwkProviderBuilder(issuerJWT)
        .cached(10, 24, TimeUnit.HOURS)
        .rateLimited(10, 1, TimeUnit.MINUTES)
        .build()

    // OAuth
    var issuerOAuth = "http://localhost:8080/callback"
    System.getenv()["YANDEX_CALLBACK"]?.let {
        issuerOAuth = it
    }

    install(Authentication) {
        configureOAuthYandex(applicationHttpClient, issuerOAuth)
        configureJwt(issuerJWT, jwkProvider)
    }
}