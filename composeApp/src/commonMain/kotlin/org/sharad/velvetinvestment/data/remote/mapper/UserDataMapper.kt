package org.sharad.velvetinvestment.data.remote.mapper

import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.bgColor1
import org.sharad.emify.core.ui.theme.bgColor4
import org.sharad.emify.core.ui.theme.homeGoalColor
import org.sharad.velvetinvestment.data.remote.model.useedata.Data
import org.sharad.velvetinvestment.data.remote.model.useedata.UserDataDto
import org.sharad.velvetinvestment.data.remote.model.useedata.UserGoal
import org.sharad.velvetinvestment.domain.GoalType
import org.sharad.velvetinvestment.domain.models.home.GoalsSummaryDomain
import org.sharad.velvetinvestment.domain.models.home.UserWorthCardDomain
import org.sharad.velvetinvestment.presentation.goals.uimodels.GoalOption
import org.sharad.velvetinvestment.presentation.homescreen.uimodels.HomeScreenUiData
import org.sharad.velvetinvestment.utils.trimDoubleTo
import org.sharad.velvetinvestment.utils.trimTo

fun UserDataDto.toHomeScreenUiData(): HomeScreenUiData {
    return HomeScreenUiData(
        name = data.full_name.trim(),
        userWorth = data.toUserWorth(),
        fireReport = data.user_home_data.fire_percentage.emi_include.trimDoubleTo(2),
        goals = data.user_goals.take(5).map { it.toGoalSummary() },
        kycCompletion = data.toKycCompletion(),
        tradingAccountCompletion = data.toTradingCompletion(),
    )
}

fun Data.toUserWorth(): UserWorthCardDomain {


    return UserWorthCardDomain(
        totalNetWorth = this.user_home_data.net_worth.toLong(),
        absGrowth = 0L,
        CARGGrowth = 0.0,
        investingRate = 0.0
    )
}
fun UserGoal.toGoalSummary(): GoalsSummaryDomain {
    return GoalsSummaryDomain(
        goalTypes = mapGoalOption(),
        amount = current_saved_amount.toLong(),
        targetAmount = current_goal_cost?.toLong()?:0L,
        goalId = id
    )
}

fun UserGoal.mapGoalOption(): GoalOption {
    return when (goal_type_id) {

        1 -> GoalOption(
            title = "Child Education",
            color = bgColor1,
            type = GoalType.ChildEducation
        )

        2 -> GoalOption(
            title = "Child Marriage",
            color = Secondary,
            type = GoalType.ChildMarriage
        )

        3 -> GoalOption(
            title = "Retirement",
            color = bgColor4,
            type = GoalType.Retirement
        )

        4 -> GoalOption(
            title = goal_name?:"Wealth Building",
            color = homeGoalColor,
            type = GoalType.WealthBuilding,
            goalItemId = goal_item_id,
            goalItemName = goal_item_name
        )

        else -> GoalOption(
            title = "Unknown",
            color = Primary,
            type = GoalType.WealthBuilding
        )
    }
}

fun Data.toKycCompletion(): Boolean {


    val isCompleted = this.kyc_types.mf?.status
        ?.lowercase()
        ?.contains("verified") == true

    return isCompleted
}

fun Data.toTradingCompletion(): Boolean {
    val isCompleted = this.kyc_types.trading?.status
        ?.lowercase()
        ?.contains("verified") == true
    return isCompleted
}