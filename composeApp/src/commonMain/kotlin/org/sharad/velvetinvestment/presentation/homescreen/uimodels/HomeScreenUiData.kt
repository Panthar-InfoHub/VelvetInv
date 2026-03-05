package org.sharad.velvetinvestment.presentation.homescreen.uimodels

import org.sharad.velvetinvestment.domain.models.home.FireReportSummaryDomain
import org.sharad.velvetinvestment.domain.models.home.GoalsSummaryDomain
import org.sharad.velvetinvestment.domain.models.home.KYCCompletion
import org.sharad.velvetinvestment.domain.models.home.UserWorthCardDomain

data class HomeScreenUiData(
    val name:String="Pooja",
    val userWorth: UserWorthCardDomain,
    val fireReport: FireReportSummaryDomain?,
    val goals: List<GoalsSummaryDomain>,
    val kyc: KYCCompletion?
)