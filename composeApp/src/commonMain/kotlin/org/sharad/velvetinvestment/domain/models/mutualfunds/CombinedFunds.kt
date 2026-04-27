package org.sharad.velvetinvestment.domain.models.mutualfunds

import org.sharad.velvetinvestment.presentation.mutualfund.CategoryMutualFundDomain

data class CombinedFundsDomain(
    val bundleFunds: List<BundledMutualFundDomain> = emptyList(),
    val categoryMutualFundDomain: List<CategoryMutualFundDomain> = emptyList()
)
