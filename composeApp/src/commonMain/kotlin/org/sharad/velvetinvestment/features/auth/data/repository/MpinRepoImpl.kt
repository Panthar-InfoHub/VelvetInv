package org.sharad.velvetinvestment.features.auth.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.utils.networking.getUrl
import org.sharad.velvetinvestment.utils.networking.safeUnitRequest
import org.sharad.velvetinvestment.features.auth.data.remote.model.UpdateMpinBodyDto
import org.sharad.velvetinvestment.features.auth.data.remote.model.VerifyMpinBodyDto
import org.sharad.velvetinvestment.features.auth.domain.repository.MpinRepo

class MpinRepoImpl(
    private val client: HttpClient
) : MpinRepo {

    override suspend fun updateMpin(mpin: String, status: Boolean): NetworkResponse<Unit, ErrorDomain> {
        return safeUnitRequest {
            client.patch(getUrl("/user/")) {
                setBody(UpdateMpinBodyDto(mpin = mpin, mpin_enabled = status))
            }
        }
    }

    override suspend fun updateMpinStatus(enabled: Boolean): NetworkResponse<Unit, ErrorDomain> {
        return safeUnitRequest {
            client.patch(getUrl("/user/")) {
                setBody(UpdateMpinBodyDto(mpin_enabled = enabled))
            }
        }
    }

    override suspend fun verifyMpin(mpin: String): NetworkResponse<Unit, ErrorDomain> {
        return safeUnitRequest {
            client.post(getUrl("/user/verify-mpin")) {
                setBody(VerifyMpinBodyDto(mpin = mpin))
            }
        }
    }
}
