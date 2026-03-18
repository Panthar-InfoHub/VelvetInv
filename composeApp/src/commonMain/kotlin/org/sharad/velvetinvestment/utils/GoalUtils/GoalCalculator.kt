package org.sharad.velvetinvestment.utils.GoalUtils

import kotlin.math.*

object GoalCalculator {

    fun calculateFutureValue(
        presentValue: Long,
        inflationRate: Int,
        years: Int
    ): Double {
        val i = inflationRate / 100.0
        return presentValue * ((1 + i).pow(years.toDouble()))
    }

    fun calculateSip(
        futureValue: Double,
        annualReturnRate: Int,
        years: Int
    ): Double {
        val r = annualReturnRate / 100.0 / 12 // monthly rate
        val n = years * 12

        return if (r == 0.0) {
            futureValue / n
        } else {
            (futureValue * r) / ((1 + r).pow( n) - 1)
        }
    }
}