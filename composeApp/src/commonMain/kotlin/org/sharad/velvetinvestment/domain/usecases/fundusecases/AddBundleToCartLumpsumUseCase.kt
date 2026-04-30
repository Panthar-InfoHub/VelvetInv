package org.sharad.velvetinvestment.domain.usecases.fundusecases
import org.sharad.velvetinvestment.domain.repository.MutualFundRepository
class AddBundleToCartLumpsumUseCase(
    private val repository: MutualFundRepository
) {
    suspend operator fun invoke(
        bundleId: String,
        amount: Long
    ) = repository.addBundleToCartLumpsum(
        bundleId = bundleId,
        amount = amount
    )
}
