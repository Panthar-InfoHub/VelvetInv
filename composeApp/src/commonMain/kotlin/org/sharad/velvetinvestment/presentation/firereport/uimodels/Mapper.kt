package org.sharad.velvetinvestment.presentation.firereport.uimodels

import org.sharad.velvetinvestment.domain.models.fire.FireCombinedDomainModel
import org.sharad.velvetinvestment.domain.models.fire.FireReportDomainModel
import org.sharad.velvetinvestment.domain.models.fire.TargetProjection

fun FireReportDomainModel.toUI(): FireReportUIModel {
    return FireReportUIModel(
        fireNumber = fireNumber,
        fireNumberAnnualGrowth = fireNumberAnnualGrowth,
        fireProgress = fireProgress,
        fireProgressThisYear = fireProgressThisYear,
        yearsToFire = yearsToFire,
        yearsToFirePercentage = yearsToFirePercentage,
        currentNetWorth = currentNetWorth,
        fireInsights = fireInsights,
        trend = trend.map {
            QuarterlyTrendUI(
                quarter = it.quarter,
                netWorth = it.netWorth,
                fireProgress = it.fireProgress
            )
        },
        netWorthGrowth = netWorthGrowth,
        progressIncrement = progressIncrement,
        projectionList = projectionList.map {
            FireProjectionUI(
                year = it.year,
                age = it.age,
                firePercent = it.firePercent,
                currentPortfolio = it.currentPortFolio,
                netOutflow = it.netOutflow,
                goals = it.goals,
                fireNumber = it.fireNumber
            )
        },
        targetProjection = targetProjection.toUI()
    )
}

fun FireCombinedDomainModel.toUI(): FireCombinedUIState {
    return FireCombinedUIState(
        emiIncluded = emiIncluded.toUI(),
        emiExcluded = emiExcluded.toUI()
    )
}

fun TargetProjection.toUI(): TargetProjectionUi {
    return TargetProjectionUi(
        targetYear = targetYear,
        projectedPortfolio = projectedPortfolio,
        fireProgress = fireProgress
    )
}