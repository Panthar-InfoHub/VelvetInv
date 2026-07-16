package org.sharad.velvetinvestment.presentation.homescreen.uimodels

import org.sharad.velvetinvestment.domain.models.home.FireReportSummaryDomain
import org.sharad.velvetinvestment.domain.models.home.GoalsSummaryDomain
import org.sharad.velvetinvestment.domain.models.home.KYCCompletion
import org.sharad.velvetinvestment.domain.models.home.UserWorthCardDomain

data class HomeScreenUiData(
    val name:String="",
    val userWorth: UserWorthCardDomain,
    val fireReport: Double,
    val goals: List<GoalsSummaryDomain>,
    val kycCompletion: Boolean,
    val tradingAccountCompletion: Boolean,
    val hidden: Boolean=true,
    val hasUnreadNotifications: Boolean = false
)
