package org.sharad.velvetinvestment.presentation.firereport.uimodels

data class FireCombinedUIState(
    val emiIncluded: FireReportUIModel,
    val emiExcluded: FireReportUIModel,
)

data class FireReportUIModel(
    val fireNumber: Long,
    val fireNumberAnnualGrowth: Double,
    val fireProgress: Double,
    val fireProgressThisYear: Double,
    val yearsToFire: Int,
    val yearsToFirePercentage: Double,
    val currentNetWorth: Long,
    val fireInsights: List<String>,
    val trend: List<QuarterlyTrendUI>,
    val netWorthGrowth: Double,
    val progressIncrement: Double,
    val projectionList: List<FireProjectionUI>,
    val targetProjection: TargetProjectionUi
)

data class TargetProjectionUi(
    val targetYear:Int,
    val projectedPortfolio:Long,
    val fireProgress: Double
)

data class QuarterlyTrendUI(
    val quarter: String,
    val netWorth: Long,
    val fireProgress: Double
)

data class FireProjectionUI(
    val extended: Boolean=false,
    val year: Int,
    val age: Int,
    val firePercent: Double,
    val currentPortfolio: Long,
    val netOutflow: Long,
    val goals: Long,
    val fireNumber: Long
)