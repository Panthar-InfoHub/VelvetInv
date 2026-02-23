package org.sharad.velvetinvestment.domain.models.mutualfunds

data class MutualFundGraphDomain(
    val graphPoints: List<MutualFundGraphPointsDomain>
)


data class MutualFundGraphPointsDomain(
    val navValue: Double,
    val date: String
)
