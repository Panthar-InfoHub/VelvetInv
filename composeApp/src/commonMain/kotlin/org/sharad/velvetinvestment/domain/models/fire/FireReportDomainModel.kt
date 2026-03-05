package org.sharad.velvetinvestment.domain.models.fire

data class FireReportDomainModel(
    val fireNumber: Long,
    val fireNumberAnnualGrowth: Double,
    val fireProgress:Double,
    val fireProgressThisYear: Double,
    val yearsToFire:Int,
    val yearsToFirePercentage:Double,
    val currentNetWorth:Long,
    val fireInsights:List<String>,
    val trend:List<QuarterlyTrendDomain>,
    val netWorthGrowth:Double,
    val progressIncrement:Double,
    val projectionList:List<FireProjections>,
    val targetProjection: TargetProjection
)

data class TargetProjection(
    val targetYear:Int,
    val projectedPortfolio:Long,
    val fireProgress: Double
)