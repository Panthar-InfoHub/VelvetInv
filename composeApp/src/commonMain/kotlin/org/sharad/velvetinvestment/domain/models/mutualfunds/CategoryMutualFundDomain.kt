package org.sharad.velvetinvestment.domain.models.mutualfunds

data class CategoryMutualFundDomain(
    val categoryName:String,
    val categorySearchReference:String,
    val mutualFunds:List<MutualFundDomain>
)
