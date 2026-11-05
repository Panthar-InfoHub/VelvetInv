package org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.compose.goals

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.models.GoalOption
import org.sharad.velvetinvestment.shared.compose.SelectionDropDown

@Composable
fun GoalSelectionDropDown(
    value: GoalOption?,
    onValueChange: (GoalOption) -> Unit,
    placeHolder: String,
    label: String,
    modifier: Modifier = Modifier,
    options: List<GoalOption>
) {
    SelectionDropDown(
        value = value,
        onValueChange = onValueChange,
        placeHolder = placeHolder,
        label = label,
        options = options,
        textConvertor = { it.title },
        modifier = modifier
    )
}
