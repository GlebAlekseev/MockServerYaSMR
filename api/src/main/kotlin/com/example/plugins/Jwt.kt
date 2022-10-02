package com.example.plugins

import com.auth0.jwk.JwkProviderBuilder
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import java.util.concurrent.TimeUnit

fun Application.configureJwt() {
    var issuer = environment.config.property("jwt.issuer").getString()

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
                    if (getClaim("userId").asString() != "" && getClaim("deviceId").asString() != "") {
                        JWTPrincipal(this)
                    } else {
                        null
                    }
                }
            }
            challenge { defaultScheme, realm ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }
}