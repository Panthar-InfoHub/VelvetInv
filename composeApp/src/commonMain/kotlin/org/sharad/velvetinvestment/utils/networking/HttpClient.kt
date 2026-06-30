package org.sharad.velvetinvestment.utils.networking

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.encodedPath
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.sharad.velvetinvestment.data.remote.model.auth.tokens.RefreshTokenBody
import org.sharad.velvetinvestment.data.remote.model.auth.tokens.RefreshTokenDto
import org.sharad.velvetinvestment.utils.AppEventsController
import org.sharad.velvetinvestment.utils.Log
import org.sharad.velvetinvestment.utils.storage.AuthPrefs

fun getHttpClient(
    authPrefs: AuthPrefs
): HttpClient {
    return HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
                encodeDefaults = true
            })
        }
        install(Logging) {
            level = LogLevel.ALL

            logger = object : Logger {
                override fun log(message: String) {
                    Log("Ktor Response", message)
                }
            }

            filter { request ->
                !request.url.encodedPath.contains("/api/v1/fire-report/pdf")
            }
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 40_000
            connectTimeoutMillis = 35_000
            socketTimeoutMillis = 40_000
        }
        install(Auth) {
            bearer {
                loadTokens {
//                    val token = authPrefs.getBearerToken()
                    val token = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpZCI6IjVmOWMzOWRiLWIyNTAtNDViMy05NGFlLWU4YmM0NWM0NjMzNCIsInBob25lX25vIjoiODkzMDQ5MjA5NiIsInVzciI6Ijg5MzA0OTIwOTYiLCJpbnZfaWQiOjU5LCJwd2QiOiIzOTI1MTMiLCJpYXQiOjE3ODI0ODE4NTcsImV4cCI6MTc4MzA4NjY1N30.51QJHx9FGhjUdCUaO5qA_EyBYJawE_amlCdTrRThZEE"
                    val refresh = authPrefs.getRefreshToken()
                    if (token.isNullOrEmpty()) return@loadTokens null
                    BearerTokens(token, refresh ?: "")
                }
                refreshTokens {
                    val token = authPrefs.getRefreshToken()
                        ?: return@refreshTokens null
                    try {
                        val response: RefreshTokenDto =
                            client.post(getUrl("/auth/refresh-token")) {
                                markAsRefreshTokenRequest()
                                setBody(RefreshTokenBody(token))
                            }.body()
                        val tokens = response.data
                        authPrefs.setBearerToken(tokens.token)
                        authPrefs.setRefreshToken(tokens.refresh_token)
                        BearerTokens(
                            tokens.token,
                            tokens.refresh_token
                        )
                    } catch (e: Exception) {
                        Log("Logout", e.message ?: "")
                        AppEventsController.logout()
                        null
                    }
                }

            }
        }

        defaultRequest {
            contentType(ContentType.Application.Json)
        }
    }
}