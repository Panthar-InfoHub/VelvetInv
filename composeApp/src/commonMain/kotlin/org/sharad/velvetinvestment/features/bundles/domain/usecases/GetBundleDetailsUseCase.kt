package org.sharad.velvetinvestment.features.bundles.domain.usecases

import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.features.bundles.domain.models.BundleDetailsDomain
import org.sharad.velvetinvestment.features.bundles.domain.repository.BundlesRepository

class GetBundleDetailsUseCase(
    private val repository: BundlesRepository
) {
    suspend operator fun invoke(bundleId: String): NetworkResponse<BundleDetailsDomain, ErrorDomain> =
        repository.getBundleDetails(bundleId)
}
