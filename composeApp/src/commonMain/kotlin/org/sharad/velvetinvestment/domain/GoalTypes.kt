package org.sharad.velvetinvestment.domain

import androidx.compose.ui.graphics.Color
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.bgColor1
import org.sharad.emify.core.ui.theme.bgColor4
import org.sharad.emify.core.ui.theme.homeGoalColor

enum class GoalTypes(val displayName: String,val color: Color) {
    WEALTH_BUILDING("Wealth Building", homeGoalColor),
    RETIREMENT("Retirement", bgColor4),
    EDUCATION("Education", bgColor1),
    MARRIAGE("Marriage", Secondary),
    CUSTOM_GOAL("Custom Goal", Primary)
}
