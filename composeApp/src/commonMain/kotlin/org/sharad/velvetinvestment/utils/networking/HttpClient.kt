package org.sharad.velvetinvestment.utils.networking

import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.sharad.velvetinvestment.utils.Log
import org.sharad.velvetinvestment.utils.storage.AuthPrefs

fun getHttpClient(
    authPrefs: AuthPrefs
): HttpClient{
    return HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
        install(Logging) {
            level = LogLevel.ALL
            logger = object : Logger {
                override fun log(message: String) {
                    Log("Ktor Response", message)
                }
            }
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 30_000
            connectTimeoutMillis = 15_000
        }
        install(Auth) {
            bearer {
                loadTokens {
                    val bearerToken= authPrefs.getBearerToken()?:""
                    val refresh= authPrefs.getRefreshToken()
                    BearerTokens(bearerToken, refresh)
                }
//                refreshTokens {
//
//                }
            }
        }

        defaultRequest {
            contentType(ContentType.Application.Json)
        }
    }
}