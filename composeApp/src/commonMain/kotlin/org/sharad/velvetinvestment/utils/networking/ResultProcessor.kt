package org.sharad.velvetinvestment.utils.networking

import io.ktor.client.call.body
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.CancellationException
import kotlinx.io.IOException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

import kotlinx.serialization.SerializationException
import org.sharad.velvetinvestment.utils.Log

suspend inline fun <reified T> safeRequest(
    execute: () -> HttpResponse
): NetworkResponse<T, ErrorDomain> {

    val response = try {
        execute()
    } catch (e: Exception) {
        return NetworkResponse.Error(e.toRequestFailure())
    }

    return if (response.status.value in 200..299) {

        try {
            NetworkResponse.Success(response.body())
        } catch (e: Exception) {
            if (e is CancellationException) throw e
            // Ktor wraps kotlinx MissingFieldException etc. in JsonConvertException (not a
            // SerializationException), so catch broadly — a DTO mismatch must not crash the app.
            Log("SerializationError", e.message.toString())
            // A 2xx whose body is not the expected shape may still carry the server's reason.
            NetworkResponse.Error(
                ErrorDomain(
                    code = response.status.value,
                    message = response.readServerError().message ?: SERIALIZATION_ERROR_MESSAGE,
                    type = ErrorType.SERIALIZATION
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
    } catch (e: Exception) {
        return NetworkResponse.Error(e.toRequestFailure())
    }

    return if (response.status.value in 200..299) {
        NetworkResponse.Success(Unit)
    } else {
        parseServerError(response)
    }
}

const val NO_INTERNET_MESSAGE = "No internet connection"
const val CONNECTION_ERROR_MESSAGE =
    "Unable to connect to the server. Please check your internet connection and try again."
const val TIMEOUT_MESSAGE = "The request timed out. Please try again."
const val SERIALIZATION_ERROR_MESSAGE = "Serialization error"

/** Shown only for a request failure that matches none of the known causes below. */
const val UNKNOWN_NETWORK_ERROR_MESSAGE = "Unknown network error"

/**
 * Maps an exception thrown before any HTTP response arrived. Cancellation is rethrown so a
 * cancelled screen neither swallows it nor shows an error for it.
 *
 * Offline on Android surfaces as `UnknownHostException` / `ConnectException` and on iOS as
 * `DarwinHttpRequestException`; all are [IOException]s, so they read as a connection problem
 * rather than an unknown one.
 */
fun Throwable.toRequestFailure(): ErrorDomain {
    // Checked first: HttpTimeout's exceptions are reported as timeouts, never as cancellation.
    val (message, type) = when (this) {
        is HttpRequestTimeoutException,
        is ConnectTimeoutException,
        is SocketTimeoutException -> TIMEOUT_MESSAGE to ErrorType.TIMEOUT

        is CancellationException -> throw this
        is UnresolvedAddressException -> NO_INTERNET_MESSAGE to ErrorType.NO_INTERNET
        is IOException -> CONNECTION_ERROR_MESSAGE to ErrorType.NO_INTERNET
        is SerializationException -> SERIALIZATION_ERROR_MESSAGE to ErrorType.SERIALIZATION
        else -> UNKNOWN_NETWORK_ERROR_MESSAGE to ErrorType.UNKNOWN
    }
    return ErrorDomain(code = -1, message = message, type = type)
}

suspend fun parseServerError(
    response: HttpResponse
): NetworkResponse.Error<ErrorDomain> {

    val serverError = response.readServerError()

    return NetworkResponse.Error(
        ErrorDomain(
            code = response.status.value,
            message = serverError.message ?: UNKNOWN_ERROR_MESSAGE,
            type = if (serverError.message == null && serverError.type == null) {
                ErrorType.UNKNOWN
            } else {
                serverError.type.toErrorType()
            }
        )
    )
}

/** Shown only when an error response carries no readable message at all. */
const val UNKNOWN_ERROR_MESSAGE = "Unknown error"

/** What could be read from an error response; either field is `null` when it was not there. */
data class ParsedServerError(
    val message: String?,
    val type: String?
)

/**
 * Reads an error body without requiring it to match [org.sharad.velvetinvestment.data.remote.model.ServerError.ServerError] exactly, so a server message is
 * never swapped for [UNKNOWN_ERROR_MESSAGE] just because `success` was missing, `type` was null, or
 * the response was not served as JSON. Looks in `error.message`, a string `error`, then a
 * top-level `message` or `detail`; `type` comes from `error.type` or a top-level `type`.
 */
suspend fun HttpResponse.readServerError(): ParsedServerError {
    val root = try {
        Json.parseToJsonElement(bodyAsText()) as? JsonObject
    } catch (e: Exception) {
        null
    } ?: return ParsedServerError(message = null, type = null)

    val error = root["error"]
    val errorObject = error as? JsonObject

    return ParsedServerError(
        message = errorObject?.nonBlankString("message")
            ?: (error as? JsonPrimitive)?.nonBlankString()
            ?: root.nonBlankString("message")
            ?: root.nonBlankString("detail"),
        type = errorObject?.nonBlankString("type")
            ?: root.nonBlankString("type")
    )
}

private fun JsonObject.nonBlankString(key: String): String? =
    (this[key] as? JsonPrimitive)?.nonBlankString()

private fun JsonPrimitive.nonBlankString(): String? =
    takeIf { it.isString }?.content?.takeIf { it.isNotBlank() }

fun String?.toErrorType(): ErrorType {
    return when (this) {
        "MF_KYC_REQUIRED" -> ErrorType.MF_KYC_REQUIRED
        "TRADING_KYC_REQUIRED" -> ErrorType.MF_TRADING_ACCOUNT_REQUIRED
        else -> ErrorType.SERVER
    }
}
