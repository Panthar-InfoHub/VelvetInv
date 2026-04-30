package org.sharad.velvetinvestment.domain.usecases.user

import org.sharad.velvetinvestment.data.remote.model.onboarding.OnBoardingBodyDto
import org.sharad.velvetinvestment.domain.repository.UserAuth
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class OnBoardUserUseCase(
    private val userAuth: UserAuth
) {
    suspend operator fun invoke(data: OnBoardingBodyDto): NetworkResponse<Unit, ErrorDomain> {
        return userAuth.onBoardUser(data)
    }
}