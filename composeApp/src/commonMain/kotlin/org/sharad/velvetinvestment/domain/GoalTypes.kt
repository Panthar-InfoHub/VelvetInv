package org.sharad.velvetinvestment.domain

import androidx.compose.ui.graphics.Color
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.bgColor1
import org.sharad.emify.core.ui.theme.bgColor4
import org.sharad.emify.core.ui.theme.homeGoalColor

sealed interface GoalType {
    val id: Int

    data object ChildEducation : GoalType { override val id = 1 }
    data object ChildMarriage : GoalType { override val id = 2 }
    data object Retirement : GoalType { override val id = 3 }
    data object WealthBuilding : GoalType { override val id = 4 }
}
