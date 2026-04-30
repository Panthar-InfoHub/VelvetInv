package org.sharad.velvetinvestment.domain.usecases.fundusecases
import org.sharad.velvetinvestment.data.remote.model.bundlecart.AddBundleSipRequest
import org.sharad.velvetinvestment.domain.repository.MutualFundRepository
class AddBundleToCartSipUseCase(
    private val repository: MutualFundRepository
) {
    suspend operator fun invoke(
        request: AddBundleSipRequest
    ) = repository.addBundleToCartSip(request)
}
