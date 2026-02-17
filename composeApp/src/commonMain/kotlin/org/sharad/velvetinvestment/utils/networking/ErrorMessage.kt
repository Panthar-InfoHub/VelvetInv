package org.sharad.velvetinvestment.utils.networking

import com.sharad.surakshakawachneo.utils.Networking.NetworkError

fun NetworkError.toMessage(): String {
    return when (this) {

        NetworkError.REQUEST_TIMEOUT ->
            "The request timed out. Please try again."

        NetworkError.UNAUTHORIZED ->
            "You are not authorized. Please login again."

        NetworkError.CONFLICT ->
            "A conflict occurred. Please refresh and try again."

        NetworkError.TOO_MANY_REQUESTS ->
            "Too many requests. Please wait and try again."

        NetworkError.NO_INTERNET ->
            "No internet connection. Please check your network."

        NetworkError.PAYLOAD_TOO_LARGE ->
            "The data size is too large."

        NetworkError.SERVER_ERROR ->
            "Server error. Please try again later."

        NetworkError.SERIALIZATION ->
            "Unexpected data format received."

        NetworkError.INVALID_REQUEST ->
            "Invalid request. Please check your input."

        NetworkError.INVALID_OTP ->
            "Invalid OTP. Please try again."

        NetworkError.UNKNOWN ->
            "Something went wrong. Please try again."
    }
}
