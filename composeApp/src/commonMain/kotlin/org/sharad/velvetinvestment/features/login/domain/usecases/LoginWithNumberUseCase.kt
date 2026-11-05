package org.sharad.velvetinvestment.features.login.domain.usecases

import org.sharad.velvetinvestment.features.login.domain.repository.UserAuth

class LoginWithNumberUseCase(private val repository: UserAuth) {
    suspend operator fun invoke(number: String) = repository.loginWithNumber(number)
}
