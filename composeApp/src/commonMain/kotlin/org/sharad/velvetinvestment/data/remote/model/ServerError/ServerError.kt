package org.sharad.velvetinvestment.data.remote.model.ServerError

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

/**
 * Default error envelope returned by the server, e.g.
 * `{"success": false, "message": "OTP validation failed", "data": null, "errorCode": "OTP_VALIDATION_FAILED"}`.
 */
@Serializable
data class ServerError(
    val success: Boolean = false,
    val message: String? = null,
    val data: JsonElement? = null,
    val errorCode: String? = null
)
