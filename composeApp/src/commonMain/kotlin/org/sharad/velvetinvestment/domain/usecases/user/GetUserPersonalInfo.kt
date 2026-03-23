package org.sharad.velvetinvestment.domain.usecases.user

import org.sharad.velvetinvestment.domain.models.user.UserPersonalInfoDomain
import org.sharad.velvetinvestment.domain.repository.UserAuth
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class GetUserPersonalInfo(
    private val userAuth: UserAuth
) {
    suspend operator fun invoke(): NetworkResponse<UserPersonalInfoDomain, ErrorDomain> {

        return when (val response = userAuth.getUserData()) {

            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }

            is NetworkResponse.Success -> {
                val dto = response.data.data

                val domain = UserPersonalInfoDomain(
                    name = dto.full_name,
                    dob = dto.dob,
                    phone = dto.phone_no,
                    city = dto.city,
                    email = dto.email
                )
                NetworkResponse.Success(domain)
            }
        }
    }
}