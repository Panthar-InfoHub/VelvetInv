package org.sharad.velvetinvestment.presentation.onboarding.models

import org.sharad.velvetinvestment.domain.GoalTypes

data class GoalInfo(
    val name:String="",
    val category: GoalTypes?=null,
    val targetAmount:Long?=null,
    val targetYear:Int?=null
)
