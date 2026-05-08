package org.sharad.velvetinvestment.data.remote.mapper

import org.sharad.velvetinvestment.data.remote.model.investmentratedto.InvestingTrend
import org.sharad.velvetinvestment.data.remote.model.investmentratedto.InvestmentRateDto
import org.sharad.velvetinvestment.data.remote.model.investmentratedto.SpendingCategories
import org.sharad.velvetinvestment.domain.models.user.InvestmentRateDomain
import org.sharad.velvetinvestment.domain.models.user.SavingTrendsDomain
import org.sharad.velvetinvestment.domain.models.user.SpendingCategoriesDomain
import org.sharad.velvetinvestment.domain.models.user.SpendingChartData

fun InvestmentRateDto.toDomain(): InvestmentRateDomain {
    val data= this.data
    return InvestmentRateDomain(
        currentSavingPercentage = data.average_savings_pattern.current_savings_percent,
        previousMonthSavingPercentage = data.average_savings_pattern.previous_month_savings_percent,
        percentDelta = data.average_savings_pattern.month_over_month_delta,
        savingDelta = data.average_savings_pattern.total_saved_vs_prev_month,
        trends = data.investing_trend.map {
            it.toDomain()
        },
        spendingCategories = data.spending_categories.toDomain()
    )
}

fun InvestingTrend.toDomain(): SavingTrendsDomain{
    return SavingTrendsDomain(
        monthText = month,
        savings = savings,
        investments = investments
    )
}

fun SpendingCategories.toDomain(): SpendingCategoriesDomain{
    return SpendingCategoriesDomain(
        savings = SpendingChartData(
            amount = savings.amount,
            percent = savings.percent
        ),
        investments = SpendingChartData(
            amount = investments.amount,
            percent = investments.percent
        ),
        essentials = SpendingChartData(
            amount = essentials.amount,
            percent = essentials.percent
        )
    )
}