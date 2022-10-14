package com.example.plugins

import com.auth0.jwk.JwkProvider
import com.auth0.jwk.JwkProviderBuilder
import com.example.server.response.AuthResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import java.util.concurrent.TimeUnit

fun AuthenticationConfig.configureJwt(issuer: String, jwkProvider: JwkProvider) {
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
        challenge { _, _ ->
            return@challenge call.respond(
                HttpStatusCode.Unauthorized,
                AuthResponse(
                    message = "Unauthorized: access_token не валиден"
                )
            )
        }
    }
}