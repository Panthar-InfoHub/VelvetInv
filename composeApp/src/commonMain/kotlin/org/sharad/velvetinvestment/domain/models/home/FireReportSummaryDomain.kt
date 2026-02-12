package org.sharad.velvetinvestment.domain.models.home

data class FireReportSummaryDomain(
    val annualGrowth: Double,
    val fireNumber:Double,
    val setupStep:Int,
    val totalStep: Int=8,
)
