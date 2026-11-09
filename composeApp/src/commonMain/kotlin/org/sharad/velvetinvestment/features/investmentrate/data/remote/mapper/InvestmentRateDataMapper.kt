package org.sharad.velvetinvestment.features.investmentrate.data.remote.mapper

import org.sharad.velvetinvestment.features.investmentrate.data.remote.model.InvestingTrend
import org.sharad.velvetinvestment.features.investmentrate.data.remote.model.InvestmentRateDto
import org.sharad.velvetinvestment.features.investmentrate.data.remote.model.SpendingCategories
import org.sharad.velvetinvestment.features.investmentrate.domain.model.EssentialsBreakdownDomain
import org.sharad.velvetinvestment.features.investmentrate.domain.model.EssentialsChartData
import org.sharad.velvetinvestment.features.investmentrate.domain.model.InvestmentBreakdownDomain
import org.sharad.velvetinvestment.features.investmentrate.domain.model.InvestmentChartData
import org.sharad.velvetinvestment.features.investmentrate.domain.model.InvestmentRateDomain
import org.sharad.velvetinvestment.features.investmentrate.domain.model.SavingTrendsDomain
import org.sharad.velvetinvestment.features.investmentrate.domain.model.SpendingCategoriesDomain
import org.sharad.velvetinvestment.features.investmentrate.domain.model.SpendingChartData

fun InvestmentRateDto.toDomain(): InvestmentRateDomain {
    val data= this.data
    return InvestmentRateDomain(
        currentSavingPercentage = data.average_savings_pattern.current_savings_percent,
        previousMonthSavingPercentage = data.average_savings_pattern.previous_month_savings_percent,
        percentDelta = data.average_savings_pattern.month_over_month_delta,
        savingDelta = data.average_savings_pattern.total_saved_vs_prev_month,
        trends = data.investing_trend.map {
            it.toDomain()
        }.reversed(),
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

fun SpendingCategories.toDomain(): SpendingCategoriesDomain {
    return SpendingCategoriesDomain(
        savings = SpendingChartData(
            amount = savings.amount,
            percent = savings.percent
        ),
        investments = InvestmentChartData(
            amount = investments.amount,
            percent = investments.percent,
            breakdown = InvestmentBreakdownDomain(
                mutualFunds = SpendingChartData(
                    amount = investments.breakdown.mutualFunds.amount,
                    percent = investments.breakdown.mutualFunds.percent
                ),
                fixedDeposits = SpendingChartData(
                    amount = investments.breakdown.fixedDeposits.amount,
                    percent = investments.breakdown.fixedDeposits.percent
                )
            )
        ),
        essentials = EssentialsChartData(
            amount = essentials.amount,
            percent = essentials.percent,
            breakdown = EssentialsBreakdownDomain(
                house = SpendingChartData(
                    amount = essentials.breakdown.house.amount,
                    percent = essentials.breakdown.house.percent
                ),
                food = SpendingChartData(
                    amount = essentials.breakdown.food.amount,
                    percent = essentials.breakdown.food.percent
                ),
                transportation = SpendingChartData(
                    amount = essentials.breakdown.transportation.amount,
                    percent = essentials.breakdown.transportation.percent
                ),
                others = SpendingChartData(
                    amount = essentials.breakdown.others.amount,
                    percent = essentials.breakdown.others.percent
                )
            )
        )
    )
}
