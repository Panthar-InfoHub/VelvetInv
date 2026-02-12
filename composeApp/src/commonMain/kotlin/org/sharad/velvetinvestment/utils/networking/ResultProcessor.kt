package com.sharad.surakshakawachneo.utils.Networking

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

import kotlinx.serialization.SerializationException

suspend inline fun <reified T> handleResponse(
    response: HttpResponse
): NetworkResponse<T, NetworkError> {
    return when (response.status.value) {
        in 200..299 -> {
            try {
                NetworkResponse.Success(response.body())
            } catch (e: SerializationException) {
                NetworkResponse.Error(NetworkError.SERIALIZATION)
            }
        }
        401 -> NetworkResponse.Error(NetworkError.UNAUTHORIZED)
        408 -> NetworkResponse.Error(NetworkError.REQUEST_TIMEOUT)
        409 -> NetworkResponse.Error(NetworkError.CONFLICT)
        413 -> NetworkResponse.Error(NetworkError.PAYLOAD_TOO_LARGE)
        429 -> NetworkResponse.Error(NetworkError.TOO_MANY_REQUESTS)
        in 500..599 -> NetworkResponse.Error(NetworkError.SERVER_ERROR)
        else -> NetworkResponse.Error(NetworkError.UNKNOWN)
    }
}

suspend inline fun handleUnitResponse(
    response: HttpResponse
): NetworkResponse<Unit, NetworkError> {
    return when (response.status.value) {
        in 200..299 -> NetworkResponse.Success(Unit)
        401 -> NetworkResponse.Error(NetworkError.UNAUTHORIZED)
        408 -> NetworkResponse.Error(NetworkError.REQUEST_TIMEOUT)
        409 -> NetworkResponse.Error(NetworkError.CONFLICT)
        413 -> NetworkResponse.Error(NetworkError.PAYLOAD_TOO_LARGE)
        429 -> NetworkResponse.Error(NetworkError.TOO_MANY_REQUESTS)
        in 500..599 -> NetworkResponse.Error(NetworkError.SERVER_ERROR)
        else -> NetworkResponse.Error(NetworkError.UNKNOWN)
    }
}
