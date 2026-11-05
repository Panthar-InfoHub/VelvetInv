package org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.models

import androidx.compose.ui.graphics.Color
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.models.GoalType

data class GoalOption(
    val title: String,
    val color: Color,
    val type: GoalType,
    val goalItemId: Int? = null,
    val goalItemName: String? = null
)