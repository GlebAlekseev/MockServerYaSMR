package com.example.plugins

import com.auth0.jwk.JwkProviderBuilder
import com.example.server.response.TodoListResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import java.util.concurrent.TimeUnit

fun Application.configureJwt() {
    val issuer = environment.config.property("jwt.issuer").getString()

    val jwkProvider = JwkProviderBuilder(issuer)
        .cached(10, 24, TimeUnit.HOURS)
        .rateLimited(10, 1, TimeUnit.MINUTES)
        .build()

    install(Authentication) {
        jwt("auth-jwt") {
            verifier(jwkProvider, issuer) {
                acceptLeeway(3)
            }
            validate { credential ->
                with(credential.payload) {
                    if (getClaim("userId").toString() != "" && getClaim("deviceId").toString() != "") {
                        JWTPrincipal(this)
                    } else {
                        null
                    }
                }
            }
            challenge { _, _ ->
                return@challenge call.respond(
                    HttpStatusCode.Unauthorized,
                    TodoListResponse(
                        message = "Unauthorized: access_token не валиден"
                    )
                )
            }
        }
    }
}