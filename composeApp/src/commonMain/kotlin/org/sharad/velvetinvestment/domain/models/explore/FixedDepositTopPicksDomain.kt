package org.sharad.velvetinvestment.domain.models.explore

data class FixedDepositTopPicksDomain(
    val icon: String,
    val name: String,
    val metadata: String,
    val returnYears: String,
    val percentage: Double,
    val id: String
)