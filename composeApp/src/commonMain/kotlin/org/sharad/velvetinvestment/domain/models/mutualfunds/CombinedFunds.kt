package org.sharad.velvetinvestment.domain.models.mutualfunds

import org.sharad.velvetinvestment.presentation.mutualfund.CategoryMutualFundDomain

data class CombinedFundsDomain(
    val categoryMutualFundDomain: List<CategoryMutualFundDomain> = emptyList()
)
