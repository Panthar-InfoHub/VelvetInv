package org.sharad.velvetinvestment.utils

import kotlin.math.max
import kotlin.math.roundToLong

fun calculateRecommendedTermLifeInsurance(
    monthlyIncome: Double,
    totalLiabilities: Double,
    totalAnnualExpenses: Double
): Long {

    val annualIncome = monthlyIncome * 12

    val recommended = max(
        15 * annualIncome,
        totalLiabilities + (10 * totalAnnualExpenses)
    )

    return recommended.roundToLong()
}

fun calculateRecommendedHealthInsurance(
    monthlyIncome: Double
): Long {

    val recommended = max(
        4 * monthlyIncome,
        2_000_000.0
    )

    return recommended.roundToLong()
}