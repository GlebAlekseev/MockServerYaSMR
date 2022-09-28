package com.example.plugins

import com.auth0.jwk.*
import com.auth0.jwt.*
import com.auth0.jwt.algorithms.*
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.http.content.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import java.io.File
import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.util.*
import java.util.concurrent.TimeUnit
import com.auth0.jwk.*
import com.auth0.jwt.*
import com.auth0.jwt.algorithms.*
import com.example.model.UserInfo
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.http.content.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.sessions.*
import kotlinx.serialization.Serializable
import java.io.*
import java.security.*
import java.security.interfaces.*
import java.security.spec.*
import java.util.*
import java.util.concurrent.*

fun Application.configureRouting(applicationHttpClient: HttpClient) {
    // Приватный ключ RSA
    val privateKeyString = environment.config.property("jwt.privateKey").getString()
    // Изготовитель токена
    val issuer = environment.config.property("jwt.issuer").getString()
    // Для кого предназначен токен
    val audience = environment.config.property("jwt.audience").getString()

    // Получаю доступ к JWKS, который хранит публичные ключи
    // jwks.json является статическим файлом, чтобы другие пользователи могли использовать эти публичные ключи,
    // для проверки токена на валидность
    // указываю URI, где находится .well-known/jwks.json
    val jwkProvider = JwkProviderBuilder(issuer)
        .cached(10, 24, TimeUnit.HOURS)
        .rateLimited(10, 1, TimeUnit.MINUTES)
        .build()

    routing {
        // Применяю OAuth авторизацию к запросам ниже
        authenticate("auth-oauth-google") {
            // Используется клиентом, чтобы зарегистрироваться/войти
            // Далее он будет жить на jwt
            // Если jwt иссякнет, то опять /login
            get("/login") {
                // Редирект на страницу Яндекса
            }

            get("/callback") {
                // Получаю токены от Яндекса
                val principal: OAuthAccessTokenResponse.OAuth2? = call.principal()

                // Заправшиваю информацию от Яндекса
                val userInfo: UserInfo = applicationHttpClient.get("https://login.yandex.ru/info?format=json") {
                    headers {
                        append(HttpHeaders.Authorization, "Bearer ${principal?.accessToken}")
                    }
                }.body()

                println("Вошел пользователь: $userInfo")

                // Проверяю в бд наличие юзера, иначе создаю запись


                // Генерирую токен с id пользователя
                val publicKey = jwkProvider.get("test_key").publicKey
                val keySpecPKCS8 = PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyString))
                val privateKey = KeyFactory.getInstance("RSA").generatePrivate(keySpecPKCS8)
                val token = JWT.create()
                    .withAudience(audience)
                    .withIssuer(issuer)
                    .withClaim("username", 111)
                    .withExpiresAt(Date(System.currentTimeMillis() + 60000))
                    .sign(Algorithm.RSA256(publicKey as RSAPublicKey, privateKey as RSAPrivateKey))
                call.respond(hashMapOf("token" to token))
            }
        }

        get("/refreshToken") {
            // Продление токена
        }

        // Делаю папку certs статикой
        static(".well-known") {
            staticRootFolder = File("certs")
            file("jwks.json")
        }
    }
}
