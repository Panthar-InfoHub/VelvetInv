package org.sharad.velvetinvestment.domain.usecases.home


import org.sharad.velvetinvestment.domain.models.home.UserWorthCardDomain
import org.sharad.velvetinvestment.domain.repository.HomeRepository

class GetUserWorthCardUseCase(
    private val repository: HomeRepository
) {
    suspend operator fun invoke(): UserWorthCardDomain {
        return repository.getUserWorthCard()
    }
}
