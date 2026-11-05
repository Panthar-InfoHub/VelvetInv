package org.sharad.velvetinvestment.features.bundles.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.utils.networking.getUrl
import org.sharad.velvetinvestment.utils.networking.safeRequest
import org.sharad.velvetinvestment.features.bundles.data.remote.mapper.toDomain
import org.sharad.velvetinvestment.features.bundles.data.remote.model.AllBundlesDto
import org.sharad.velvetinvestment.features.bundles.data.remote.model.BundleDetailsDto
import org.sharad.velvetinvestment.features.bundles.domain.models.BundleDetailsDomain
import org.sharad.velvetinvestment.features.bundles.domain.models.BundleSummaryDomain
import org.sharad.velvetinvestment.features.bundles.domain.repository.BundlesRepository

class BundlesRepo(
    private val client: HttpClient
) : BundlesRepository {

    override suspend fun getAllBundles(): NetworkResponse<List<BundleSummaryDomain>, ErrorDomain> {
        val response = safeRequest<AllBundlesDto> {
            client.get(getUrl("/bundles"))
        }

        return when (response) {
            is NetworkResponse.Success -> NetworkResponse.Success(response.data.toDomain())
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
        }
    }

    override suspend fun getBundleDetails(
        bundleId: String
    ): NetworkResponse<BundleDetailsDomain, ErrorDomain> {
        val response = safeRequest<BundleDetailsDto> {
            client.get(getUrl("/bundles/$bundleId"))
        }

        return when (response) {
            is NetworkResponse.Success -> NetworkResponse.Success(response.data.toDomain())
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
        }
    }

}
