package org.sharad.velvetinvestment.domain.models.home

import org.sharad.velvetinvestment.domain.GoalTypes

data class GoalsSummaryDomain(
    val goalTypes: GoalTypes,
    val amount:Long,
    val targetAmount: Long
)
