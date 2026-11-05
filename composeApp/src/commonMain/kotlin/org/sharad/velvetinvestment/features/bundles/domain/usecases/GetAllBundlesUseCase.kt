package org.sharad.velvetinvestment.features.bundles.domain.usecases

import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.features.bundles.domain.models.BundleSummaryDomain
import org.sharad.velvetinvestment.features.bundles.domain.repository.BundlesRepository

class GetAllBundlesUseCase(
    private val repository: BundlesRepository
) {
    suspend operator fun invoke(): NetworkResponse<List<BundleSummaryDomain>, ErrorDomain> =
        repository.getAllBundles()
}
