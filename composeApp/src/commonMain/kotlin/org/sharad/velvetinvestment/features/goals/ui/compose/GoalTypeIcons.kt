package org.sharad.velvetinvestment.features.goals.ui.compose

import org.jetbrains.compose.resources.DrawableResource
import org.sharad.velvetinvestment.shared.domain.GoalType
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.education_icon
import velvet.composeapp.generated.resources.ic_home
import velvet.composeapp.generated.resources.ic_piggy_bank
import velvet.composeapp.generated.resources.ins_car_insurance
import velvet.composeapp.generated.resources.ring_icon
import velvet.composeapp.generated.resources.ruppee_circle

/**
 * The icon a goal type is drawn with, shared by the home and goals lists so one goal never shows
 * two different faces. An unknown type — an id added server-side after this build — falls back to
 * the generic rupee mark rather than to nothing.
 */
fun goalIconFor(type: GoalType?): DrawableResource = when (type) {
    GoalType.ChildEducation -> Res.drawable.education_icon
    GoalType.ChildMarriage -> Res.drawable.ring_icon
    GoalType.BuyHome -> Res.drawable.ic_home
    GoalType.BuyVehicle -> Res.drawable.ins_car_insurance
    GoalType.BuildSavings -> Res.drawable.ic_piggy_bank
    GoalType.OtherGoal, null -> Res.drawable.ruppee_circle
}
