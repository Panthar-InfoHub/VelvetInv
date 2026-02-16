package com.sharad.surakshakawachneo.utils.Networking

sealed class NetworkResponse<out T, out E> {
    data class Success<out T>(val data: T) : NetworkResponse<T, Nothing>()
    data class Error<out E>(val error: E) : NetworkResponse<Nothing, E>()
}

interface Error

enum class NetworkError: Error {
    REQUEST_TIMEOUT,
    UNAUTHORIZED,
    CONFLICT,
    TOO_MANY_REQUESTS,
    NO_INTERNET,
    PAYLOAD_TOO_LARGE,
    SERVER_ERROR,
    SERIALIZATION,
    UNKNOWN,
    INVALID_REQUEST,
    INVALID_OTP
}