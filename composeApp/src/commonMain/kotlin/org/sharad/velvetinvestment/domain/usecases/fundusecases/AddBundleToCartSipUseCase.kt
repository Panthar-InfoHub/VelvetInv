package org.sharad.velvetinvestment.domain.usecases.fundusecases
import org.sharad.velvetinvestment.domain.repository.MutualFundRepository
class AddBundleToCartSipUseCase(
    private val repository: MutualFundRepository
) {
    suspend operator fun invoke(
        request: org.sharad.velvetinvestment.data.remote.model.bundlecart.AddBundleSipRequest
    ) = repository.addBundleToCartSip(request)
}
