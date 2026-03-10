package org.sharad.velvetinvestment.utils.networking

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

import kotlinx.serialization.SerializationException
import org.sharad.velvetinvestment.data.remote.data.ServerError.ServerError

suspend inline fun <reified T> safeRequest(
    execute: () -> HttpResponse
): NetworkResponse<T, Error> {

    val response = try {
        execute()
    } catch (e: Exception) {
        return NetworkResponse.Error(
            ErrorDomain(
                code = -1,
                message = "Network error",
                type = "NETWORK"
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
): NetworkResponse<Unit, Error> {

    val response = try {
        execute()
    } catch (e: Exception) {
        return NetworkResponse.Error(
            ErrorDomain(
                code = -1,
                message = "Network error",
                type = "NETWORK"
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
): NetworkResponse.Error<Error> {

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