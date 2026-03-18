package org.sharad.velvetinvestment.presentation.goals.uimodels

import androidx.compose.ui.graphics.Color
import org.sharad.velvetinvestment.domain.GoalType

data class GoalOption(
    val title: String,
    val color: Color,
    val type: GoalType,
    val goalItemId: Int? = null,   // only for wealth goals
    val goalItemName: String? = null
)