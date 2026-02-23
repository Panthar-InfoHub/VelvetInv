package org.sharad.velvetinvestment.domain.models.explore

data class MutualFundTopPicksDomain(
    val id:String,
    val icon: String,
    val name: String,
    val metadata: String,
    val returnYears: Int,
    val percentage: Double
)