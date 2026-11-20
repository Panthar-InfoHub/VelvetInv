package org.sharad.velvetinvestment.utils.networking

import io.ktor.client.call.body
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.CancellationException
import kotlinx.io.IOException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull

import kotlinx.serialization.SerializationException
import org.sharad.velvetinvestment.data.remote.model.ServerError.ServerError
import org.sharad.velvetinvestment.utils.Log

suspend inline fun <reified T> safeRequest(
    execute: () -> HttpResponse
): NetworkResponse<T, ErrorDomain> {

    val response = try {
        execute()
    } catch (e: Exception) {
        return NetworkResponse.Error(e.toRequestFailure())
    }

    if (response.status.value !in 200..299) return parseServerError(response)

    // A 2xx can still be a failure envelope; surface it before trying to read it as [T].
    // Kept as a plain if/return (not `?.let { return it }`): Kotlin/Native miscompiled that
    // non-local return inside this reified suspend inline, handing callers a Success's payload
    // as `Error.error` (ClassCastException ... cannot be cast to ErrorDomain on iOS only).
    val failedEnvelope: NetworkResponse.Error<ErrorDomain>? = response.failedEnvelopeOrNull()
    if (failedEnvelope != null) return failedEnvelope

    return try {
        val body: T = response.body()
        NetworkResponse.Success(body)
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        // Ktor wraps kotlinx MissingFieldException etc. in JsonConvertException (not a
        // SerializationException), so catch broadly — a DTO mismatch must not crash the app.
        Log("SerializationError", e.message.toString())
        // A 2xx whose body is not the expected shape may still carry the server's reason.
        val serverError = response.readServerError()
        NetworkResponse.Error(
            ErrorDomain(
                code = response.status.value,
                message = serverError.message ?: SERIALIZATION_ERROR_MESSAGE,
                type = ErrorType.SERIALIZATION
            )
        )
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
        response.failedEnvelopeOrNull() ?: NetworkResponse.Success(Unit)
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
            // The server's message is shown whatever the code; no code at all reads as UNKNOWN.
            message = serverError.message ?: UNKNOWN_ERROR_MESSAGE,
            type = serverError.type?.toErrorType() ?: ErrorType.UNKNOWN
        )
    )
}

/**
 * Returns the server's error when a 2xx JSON body is a [ServerError] envelope with
 * `"success": false`, so its `message` and `errorCode` reach the caller instead of being lost to a
 * DTO that happens to deserialize anyway. `null` for anything else, including non-JSON bodies.
 */
suspend fun HttpResponse.failedEnvelopeOrNull(): NetworkResponse.Error<ErrorDomain>? {
    if (contentType()?.match(ContentType.Application.Json) != true) return null
    val root = try {
        Json.parseToJsonElement(bodyAsText()) as? JsonObject
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        null
    } ?: return null
    if ((root["success"] as? JsonPrimitive)?.booleanOrNull != false) return null
    return parseServerError(this)
}

/** Shown only when an error response carries no readable message at all. */
const val UNKNOWN_ERROR_MESSAGE = "Unknown error"

/**
 * What could be read from an error response; either field is `null` when it was not there.
 * [type] is the server's `errorCode` (or a legacy `error.type` / `type`).
 */
data class ParsedServerError(
    val message: String?,
    val type: String?
)

/** Tolerant decoder for [ServerError]: extra keys or missing optional fields never fail the read. */
private val serverErrorJson = Json {
    ignoreUnknownKeys = true
    explicitNulls = false
}

/**
 * Reads an error body, the [ServerError] envelope (`message`, `errorCode`) first, without requiring
 * it to match exactly, so a server message is never swapped for [UNKNOWN_ERROR_MESSAGE] just
 * because a field was missing or the response was not served as JSON. Falls back to
 * `error.message`, a string `error`, then a top-level `detail`; the type falls back to
 * `error.type` or a top-level `type`.
 */
suspend fun HttpResponse.readServerError(): ParsedServerError {
    val root = try {
        Json.parseToJsonElement(bodyAsText()) as? JsonObject
    } catch (e: Exception) {
        if (e is CancellationException) throw e
        null
    } ?: return ParsedServerError(message = null, type = null)

    val envelope = try {
        serverErrorJson.decodeFromJsonElement(ServerError.serializer(), root)
    } catch (e: Exception) {
        null
    }

    val error = root["error"]
    val errorObject = error as? JsonObject

    return ParsedServerError(
        message = envelope?.message?.takeIf { it.isNotBlank() }
            ?: errorObject?.nonBlankString("message")
            ?: (error as? JsonPrimitive)?.nonBlankString()
            ?: root.nonBlankString("detail"),
        type = envelope?.errorCode?.takeIf { it.isNotBlank() }
            ?: errorObject?.nonBlankString("type")
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
