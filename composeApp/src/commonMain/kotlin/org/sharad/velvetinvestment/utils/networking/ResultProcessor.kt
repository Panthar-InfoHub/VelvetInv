package org.sharad.velvetinvestment.utils.networking

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException

import kotlinx.serialization.SerializationException
import org.sharad.velvetinvestment.data.remote.model.ServerError.ServerError

suspend inline fun <reified T> safeRequest(
    execute: () -> HttpResponse
): NetworkResponse<T, ErrorDomain> {

    val response = try {
        execute()
    } catch (e: UnresolvedAddressException) {
        return NetworkResponse.Error(
            ErrorDomain(
                code = -1,
                message = "No internet connection",
                type = "NO_INTERNET"
            )
        )
    } catch (e: SerializationException) {
        return NetworkResponse.Error(
            ErrorDomain(
                code = -1,
                message = "Serialization error",
                type = "SERIALIZATION"
            )
        )
    } catch (e: Exception) {
        return NetworkResponse.Error(
            ErrorDomain(
                code = -1,
                message = "Unknown network error",
                type = "UNKNOWN"
            )
        )
    }

    return if (response.status.value in 200..299) {

        try {
            NetworkResponse.Success(response.body())
        } catch (e: SerializationException) {
            NetworkResponse.Error(
                ErrorDomain(
                    code = response.status.value,
                    message = "Serialization error",
                    type = "SERIALIZATION"
                )
            )
        }

    } else {
        parseServerError(response)
    }
}

suspend inline fun safeUnitRequest(
    execute: () -> HttpResponse
): NetworkResponse<Unit, ErrorDomain> {

    val response = try {
        execute()
    } catch (e: UnresolvedAddressException) {
        return NetworkResponse.Error(
            ErrorDomain(
                code = -1,
                message = "No internet connection",
                type = "NO_INTERNET"
            )
        )
    } catch (e: SerializationException) {
        return NetworkResponse.Error(
            ErrorDomain(
                code = -1,
                message = "Serialization error",
                type = "SERIALIZATION"
            )
        )
    } catch (e: Exception) {
        return NetworkResponse.Error(
            ErrorDomain(
                code = -1,
                message = "Unknown network error",
                type = "UNKNOWN"
            )
        )
    }

    return if (response.status.value in 200..299) {
        NetworkResponse.Success(Unit)
    } else {
        parseServerError(response)
    }
}

suspend fun parseServerError(
    response: HttpResponse
): NetworkResponse.Error<ErrorDomain> {

    return try {

        val serverError = response.body<ServerError>()

        NetworkResponse.Error(
            ErrorDomain(
                code = response.status.value,
                message = serverError.error.message,
                type = serverError.error.type
            )
        )

    } catch (e: Exception) {

        NetworkResponse.Error(
            ErrorDomain(
                code = response.status.value,
                message = "Unknown error",
                type = "UNKNOWN"
            )
        )
    }
}