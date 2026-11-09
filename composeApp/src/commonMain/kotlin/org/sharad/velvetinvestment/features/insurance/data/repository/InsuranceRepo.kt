package org.sharad.velvetinvestment.features.insurance.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.sharad.velvetinvestment.features.insurance.data.remote.model.RequestConnectionDto
import org.sharad.velvetinvestment.features.insurance.domain.repository.InsuranceRepository
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.utils.networking.getUrl
import org.sharad.velvetinvestment.utils.networking.safeRequest

class InsuranceRepo(
    private val client: HttpClient
) : InsuranceRepository {

    override suspend fun requestConnection(
        type: String,
        message: String
    ): NetworkResponse<Unit, ErrorDomain> {
        return safeRequest<Unit> {
            client.post(getUrl("/frontend/request-connection")) {
                setBody(RequestConnectionDto(type, message))
            }
        }
    }
}
