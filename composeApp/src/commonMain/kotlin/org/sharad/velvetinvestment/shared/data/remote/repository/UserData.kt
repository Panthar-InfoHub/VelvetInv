package org.sharad.velvetinvestment.shared.data.remote.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.utils.networking.getUrl
import org.sharad.velvetinvestment.utils.networking.safeRequest
import org.sharad.velvetinvestment.shared.data.local.mapper.toDomain
import org.sharad.velvetinvestment.shared.data.remote.model.userdata.UserDataDto
import org.sharad.velvetinvestment.shared.domain.models.UserDataDomain
import org.sharad.velvetinvestment.shared.domain.repository.UserDataRepo

class UserDataRepoImpl(
    private val client: HttpClient
): UserDataRepo {
    override suspend fun getUserData(): NetworkResponse<UserDataDomain, ErrorDomain> {
        val response= safeRequest<UserDataDto> {
            client.get(
                getUrl("/user/")
            )
        }
        return when(response){
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
            is NetworkResponse.Success -> {
                NetworkResponse.Success(response.data.toDomain())
            }
        }
    }
}