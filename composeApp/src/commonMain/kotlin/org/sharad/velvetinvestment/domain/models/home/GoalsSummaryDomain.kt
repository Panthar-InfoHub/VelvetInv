package org.sharad.velvetinvestment.domain.models.home

import org.sharad.velvetinvestment.presentation.goals.uimodels.GoalOption

data class GoalsSummaryDomain(
    val goalTypes: GoalOption,
    val amount:Long,
    val targetAmount: Long,
    val goalId: String
)

fun GoalsSummaryDomain.progressPercent(): Int =
    if (targetAmount > 0)
        ((amount.toDouble() / targetAmount) * 100).toInt()
    else 0
