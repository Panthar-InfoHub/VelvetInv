package org.sharad.velvetinvestment.features.mutualfund.domain.usecases

import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.features.core.domain.models.PaginatedData
import org.sharad.velvetinvestment.features.mutualfund.domain.models.MutualFundDomain
import org.sharad.velvetinvestment.features.mutualfund.domain.repository.MutualFundRepository

/**
 * The fund list behind the search-results screen: `GET /mf/funds`.
 *
 * [tag] is the sub-category section, [category] the asset-class chip and [amountType] the
 * minimum-installment chip. Leaving one null means "do not filter on it" — the server's own
 * defaults then apply, so a bare call returns the popular list.
 */
class GetMutualFundSearchResultUseCase(
    private val repository: MutualFundRepository
) {

    suspend operator fun invoke(
        search: String? = null,
        tag: String? = null,
        category: String? = null,
        amountType: String? = null,
        investmentMode: String? = null,
        page: Int? = 1,
        limit: Int? = 20
    ): NetworkResponse<PaginatedData<MutualFundDomain>, ErrorDomain> {

        return repository.getFunds(
            tag = tag,
            category = category,
            amountType = amountType,
            investmentMode=investmentMode,
            search = search,
            page = page,
            limit = limit
        )
    }
}
