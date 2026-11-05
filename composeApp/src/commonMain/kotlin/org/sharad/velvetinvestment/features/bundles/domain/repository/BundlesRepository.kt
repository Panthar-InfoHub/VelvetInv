package org.sharad.velvetinvestment.features.bundles.domain.repository

import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.features.bundles.domain.models.BundleDetailsDomain
import org.sharad.velvetinvestment.features.bundles.domain.models.BundleSummaryDomain

interface BundlesRepository {

    /** `GET /bundles`. */
    suspend fun getAllBundles(): NetworkResponse<List<BundleSummaryDomain>, ErrorDomain>

    /** `GET /bundles/{id}`. */
    suspend fun getBundleDetails(bundleId: String): NetworkResponse<BundleDetailsDomain, ErrorDomain>
}
