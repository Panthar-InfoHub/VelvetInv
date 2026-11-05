package org.sharad.velvetinvestment.shared.domain.usecase

import org.sharad.velvetinvestment.shared.domain.models.UserDataDomain
import org.sharad.velvetinvestment.shared.domain.repository.UserDataRepo
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class GetUserDataUseCase(
    private val repository: UserDataRepo
) {
    suspend operator fun invoke(): NetworkResponse<UserDataDomain, ErrorDomain> {
        return repository.getUserData()
    }
}
