package org.sharad.velvetinvestment.features.goals.ui.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.features.goals.domain.models.GoalCalculationDomain
import org.sharad.velvetinvestment.features.goals.domain.models.GoalOption
import org.sharad.velvetinvestment.features.goals.ui.viewmodels.AddGoalEvent
import org.sharad.velvetinvestment.features.goals.ui.viewmodels.AddGoalUiState
import org.sharad.velvetinvestment.features.goals.ui.viewmodels.GoalFormState
import org.sharad.velvetinvestment.presentation.tradingaccount.compose.YearPicker
import org.sharad.velvetinvestment.shared.UiStateContainer
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.MoneyTextField
import org.sharad.velvetinvestment.shared.compose.NextButtonFooter
import org.sharad.velvetinvestment.shared.compose.OnBoardingTextField
import org.sharad.velvetinvestment.shared.compose.SelectionDropDown
import org.sharad.velvetinvestment.shared.domain.GoalType
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import org.sharad.velvetinvestment.shared.theme.titlesStyle
import org.sharad.velvetinvestment.utils.DateTimeUtils
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.clearFocusOnTap

/**
 * Creating a goal. Which fields appear, which tenures are allowed and which rates are prefilled
 * all come from `GET /user-goal/config` for the selected type; `POST /user-goal/calculate`
 * previews the projection as the user types. Velvet's Add Goal form and projection card, JN's
 * behaviour.
 */
@Composable
fun FinancialGoalScreen(
    state: UiState<AddGoalUiState>,
    loading: Boolean,
    handleEvent: (AddGoalEvent) -> Unit,
    modifier: Modifier = Modifier,
    title: String = "Add Goal",
    /** FIRE onboarding only collects the goal here ("Add Goal"); it is created later. */
    saveText: String = "Save Goal"
) {
    UiStateContainer(
        uiState = state,
        onRetry = { handleEvent(AddGoalEvent.LoadData) },
        modifier = Modifier.imePadding().clearFocusOnTap()
    ) { data ->
        val form = data.form
        val currentYear = remember { DateTimeUtils.getCurrentYear() }
        var showYearPicker by remember { mutableStateOf(false) }

        Box(modifier = modifier.fillMaxSize()) {
            Column(modifier = Modifier.fillMaxSize()) {
                BackHeader(
                    title = title,
                    showBack = true,
                    onBack = { handleEvent(AddGoalEvent.OnBackClicked) }
                )
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 28.dp)
                ) {
                    item {
                        SelectionDropDown(
                            value = form.selectedOption,
                            onValueChange = { handleEvent(AddGoalEvent.OnOptionSelected(it)) },
                            placeHolder = "Select Goal Type",
                            label = "Goal Type",
                            options = data.options,
                            textConvertor = { it.title }
                        )
                    }

                    form.selectedOption?.purpose?.takeIf { it.isNotBlank() }?.let { purpose ->
                        item {
                            Text(
                                text = purpose,
                                style = titlesStyle.copy(fontSize = 12.sp),
                                color = titleColor
                            )
                        }
                    }

                    item {
                        GoalFormSection(
                            form = form,
                            handleEvent = handleEvent,
                            onTargetYearClicked = { showYearPicker = true }
                        )
                    }

                    if (data.projecting || data.projection != null || data.projectionError != null) {
                        item {
                            ProjectedImpactSection(
                                projection = data.projection,
                                projecting = data.projecting,
                                error = data.projectionError
                            )
                        }
                    }
                }

                NextButtonFooter(
                    value = saveText,
                    onClick = { handleEvent(AddGoalEvent.OnSaveGoalClicked) },
                    enabled = data.isValid,
                    loading = loading
                )
            }

            // The picker scrims the whole screen, so it sits over the form rather than in it.
            if (showYearPicker) {
                val option = form.selectedOption
                YearPicker(
                    selectedYear = form.years.toIntOrNull()?.let { currentYear + it },
                    startOffsetYears = option?.minYears ?: 1,
                    yearsAhead = option?.maxYears ?: 30,
                    onYearSelected = { year ->
                        val years = (year - currentYear).coerceAtLeast(1)
                        handleEvent(AddGoalEvent.UpdateForm { copy(years = years.toString()) })
                    },
                    onDismiss = { showYearPicker = false }
                )
            }
        }
    }
}

@Composable
fun GoalFormSection(
    form: GoalFormState,
    handleEvent: (AddGoalEvent) -> Unit,
    onTargetYearClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val option = form.selectedOption ?: return
    val type = option.type ?: return
    val focusManager = LocalFocusManager.current
    val currentYear = remember { DateTimeUtils.getCurrentYear() }

    // The form holds a tenure; the field shows the year it lands on.
    val targetYearText = form.years.toIntOrNull()?.let { (currentYear + it).toString() }.orEmpty()

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        if (type.needsChildDetails) {
            OnBoardingTextField(
                value = form.childName,
                onValueChange = { handleEvent(AddGoalEvent.UpdateForm { copy(childName = it) }) },
                placeHolder = "Child Name",
                label = "Child Name",
                mandatory = true
            )
            OnBoardingTextField(
                value = form.childAge,
                onValueChange = { input ->
                    val digits = input.filter { it.isDigit() }.take(2)
                    handleEvent(AddGoalEvent.UpdateForm { copy(childAge = digits) })
                },
                placeHolder = "Age",
                label = "Child Age",
                keyboardType = KeyboardType.Number,
                mandatory = true
            )
        } else {
            OnBoardingTextField(
                value = form.goalName,
                onValueChange = { handleEvent(AddGoalEvent.UpdateForm { copy(goalName = it) }) },
                placeHolder = goalNamePlaceholder(type),
                label = "Goal Name",
                mandatory = true
            )
        }

        // Config lists the variants only for the vehicle goal; the form starts on the first one.
        if (type.needsAssetSubtype && option.assetSubtypes.size > 1) {
            SelectionDropDown(
                value = form.assetSubtype,
                onValueChange = { handleEvent(AddGoalEvent.UpdateForm { copy(assetSubtype = it) }) },
                placeHolder = "Select Vehicle Type",
                label = "Vehicle Type",
                options = option.assetSubtypes,
                textConvertor = { subtype -> subtype.lowercase().replaceFirstChar { it.uppercase() } }
            )
        }

        MoneyTextField(
            value = form.amount,
            onValueChange = { input ->
                val digits = input.filter { it.isDigit() }
                handleEvent(AddGoalEvent.UpdateForm { copy(amount = digits) })
            },
            placeHolder = if (type.usesTargetAmount) "Enter Target Amount" else "Enter Goal Cost",
            label = if (type.usesTargetAmount) "Target Amount" else "Present Goal Value",
            mandatory = true
        )

        // Inflation is absent for a fixed corpus — there is no present-day cost to inflate.
        if (option.inflationRate != null) {
            OnBoardingTextField(
                value = form.inflation,
                onValueChange = { input ->
                    if (input.isEmpty() || input.matches(Regex("""^\d+(\.\d{0,2})?$"""))) {
                        handleEvent(AddGoalEvent.UpdateForm { copy(inflation = input) })
                    }
                },
                placeHolder = "Inflation %",
                label = "Expected Inflation Rate(%)",
                keyboardType = KeyboardType.Decimal
            )
        }

        // The year is chosen from the wheel picker, never typed.
        OnBoardingTextField(
            value = targetYearText,
            onValueChange = {},
            enabled = false,
            placeHolder = "${currentYear + option.minYears}",
            label = "Target Year",
            mandatory = true,
            modifier = Modifier.clickable {
                focusManager.clearFocus()
                onTargetYearClicked()
            }
        )
    }
}

/** The server's projection for what has been typed so far. */
@Composable
private fun ProjectedImpactSection(
    projection: GoalCalculationDomain?,
    projecting: Boolean,
    error: String?,
    modifier: Modifier = Modifier
) {
    when {
        projection != null -> GoalProjectionCard(
            title = "Projected Impact",
            isFixedCorpus = projection.targetAmount != null,
            todaysCost = projection.targetAmount ?: projection.currentCost ?: 0.0,
            futureValue = projection.futureTargetAmount,
            targetText = (DateTimeUtils.getCurrentYear() + projection.yearsRemaining).toString(),
            monthlySip = projection.requiredMonthlySip,
            projecting = projecting,
            modifier = modifier
        )

        // A failed preview leaves the form usable: saving recalculates server-side anyway.
        error != null && !projecting -> Text(
            text = "Could not project this goal right now. You can still save it.",
            style = titlesStyle.copy(fontSize = 12.sp),
            color = titleColor,
            modifier = modifier
        )

        else -> Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
        ) {
            CircularProgressIndicator(
                strokeWidth = 2.dp,
                color = Primary,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = "Calculating projection…",
                style = titlesStyle,
                color = titleColor
            )
        }
    }
}

private fun goalNamePlaceholder(type: GoalType): String = when (type) {
    GoalType.BuyHome -> "My Dream Home"
    GoalType.BuyVehicle -> "My New Car"
    GoalType.BuildSavings -> "Emergency Cushion"
    else -> "Goal Name"
}

@Preview(showBackground = true, backgroundColor = 0xffffff)
@Composable
private fun FinancialGoalScreenPreview() {
    val option = GoalOption(
        title = "Buy a Vehicle",
        color = Color(0xffFF9D00),
        goalTypeId = GoalType.BuyVehicle.id,
        purpose = "Save for the car you want to drive home",
        inflationRate = 0.06,
        expectedReturnRate = 0.10,
        tenureSuggestions = listOf(3, 5, 10),
        assetSubtypes = listOf("CAR", "BIKE", "SCOOTER")
    )
    VelvetTheme {
        FinancialGoalScreen(
            state = UiState.Success(
                AddGoalUiState(
                    options = listOf(option),
                    form = GoalFormState(
                        selectedOption = option,
                        goalName = "New Car",
                        assetSubtype = "CAR",
                        years = "3",
                        amount = "1500000",
                        inflation = "6",
                        expectedReturn = "10"
                    ),
                    projection = GoalCalculationDomain(
                        goalTypeId = GoalType.BuyVehicle.id,
                        yearsRemaining = 3,
                        currentCost = 1_500_000.0,
                        targetAmount = null,
                        currentSavings = 0.0,
                        inflationRateUsed = 0.06,
                        expectedReturnRateUsed = 0.10,
                        futureTargetAmount = 1_786_524.0,
                        fvCurrentSavings = 0.0,
                        netRequiredCorpus = 1_786_524.0,
                        requiredMonthlySip = 42_500.0,
                        requiredLumpsumToday = 1_342_000.0
                    ),
                    isValid = true
                )
            ),
            loading = false,
            handleEvent = {}
        )
    }
}
