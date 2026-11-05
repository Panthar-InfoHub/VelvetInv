package org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.compose.goals

import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel.GoalOnboardingUiState

import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel.GoalOnboardingEvent

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.models.GoalType
import org.sharad.velvetinvestment.shared.compose.OnBoardingTextField
import org.sharad.velvetinvestment.shared.compose.MoneyTextField
import org.sharad.velvetinvestment.shared.compose.NextButtonFooter
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel.GoalScreenOnboardingViewModel
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel.goalOptions
import org.sharad.velvetinvestment.utils.clearFocusOnTap

@Composable
fun AddGoalOnboardingScreen(
    modifier: Modifier = Modifier,
    state: GoalOnboardingUiState,
    onEvent: (GoalOnboardingEvent) -> Unit,
    onBack: () -> Unit,
    pv: PaddingValues,
    retirementAgeMain: Int?,
    showYearPicker:()-> Unit,
    monthlyCalculatedExpense: Long
) {

    val form = state.form
    val selectedOption = form.selectedOption
    val goalName = form.goalName
    val inflation = form.inflationRate
    val childName = form.childName
    val childAge = form.childAge
    val targetYear = form.targetYear
    val goalCost = form.currentGoalCost
    val retirementAge = form.retirementAge
    val lifeExpectancy = form.lifeExpectancy
    val postReturn = form.postRetirementReturn
    val goalItemName = form.goalItemName
    val buttonEnabled = form.isValid

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalFocusManager.current

    LaunchedEffect(Unit){
        if (retirementAge.isEmpty()){
            retirementAgeMain?.let { onEvent(GoalOnboardingEvent.OnRetirementAgeChanged(it.toString())) }
            onEvent(GoalOnboardingEvent.OnMonthlyExpenseChanged(monthlyCalculatedExpense.toString()))
        }
    }

    Column(modifier = modifier.fillMaxSize()
        .imePadding()
        .clearFocusOnTap()) {

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            //  ONLY DROPDOWN INITIALLY

            item {
                GoalSelectionDropDown(
                    value = selectedOption,
                    onValueChange = { onEvent(GoalOnboardingEvent.OnGoalOptionSelected(it)) },
                    placeHolder = "Select Goal Type",
                    label = "Goal Type",
                    options = goalOptions
                )
            }


            if (selectedOption != null) {

//                item {
//                    OnBoardingTextField(
//                        value = returns,
//                        onValueChange = viewModel::onReturnChange,
//                        placeHolder = "Return %",
//                        label = "Return Rate",
//                        keyboardType = KeyboardType.Decimal
//                    )
//                }

                when (selectedOption?.type) {

                    GoalType.ChildEducation,
                    GoalType.ChildMarriage -> {

                        item {
                            OnBoardingTextField(
                                value = childName,
                                onValueChange = { onEvent(GoalOnboardingEvent.OnChildNameChanged(it)) },
                                placeHolder = "Child Name",
                                label = "Child Name"
                            )
                        }

                        item {
                            OnBoardingTextField(
                                value = childAge,
                                onValueChange = { onEvent(GoalOnboardingEvent.OnChildAgeChanged(it)) },
                                placeHolder = "Age",
                                label = "Child Age",
                                keyboardType = KeyboardType.Number
                            )
                        }

                        item {
                            MoneyTextField(
                                value = goalCost,
                                onValueChange = { onEvent(GoalOnboardingEvent.OnGoalCostChanged(it)) },
                                placeHolder = "Enter Goal Cost",
                                label = "Present Goal Value"
                            )
                        }

                        item {
                            OnBoardingTextField(
                                value = inflation,
                                onValueChange = { input ->

                                    if (input.isEmpty()) {
                                        onEvent(GoalOnboardingEvent.OnInflationChanged(""))
                                        return@OnBoardingTextField
                                    }

                                    val isValid = input.matches(
                                        Regex("""^\d+(\.\d{0,2})?$""")
                                    )

                                    if (isValid) {
                                        onEvent(GoalOnboardingEvent.OnInflationChanged(input))
                                    }
                                },
                                placeHolder = "Inflation %",
                                label = "Inflation Rate(%)",
                                keyboardType = KeyboardType.Decimal
                            )
                        }
//                        item {
//                            MoneyTextField(
//                                value = saved,
//                                onValueChange = viewModel::onSavedAmountChange,
//                                placeHolder = "Current Savings",
//                                label = "Current Savings"
//                            )
//                        }

                        item {
                            OnBoardingTextField(
                                value = targetYear,
                                onValueChange = {},
                                placeHolder = "Select Target Year",
                                label = "Target Year",
                                enabled = false,
                                modifier = Modifier.clickable {
                                    keyboardController.clearFocus()
                                    focusManager.clearFocus()
                                    showYearPicker()
                                }
                            )
                        }

                    }

                    GoalType.Retirement -> {

//                        item {
//                            OnBoardingTextField(
//                                value = currentAge,
//                                onValueChange = viewModel::onCurrentAgeChange,
//                                placeHolder = "Current Age",
//                                label = "Current Age",
//                                keyboardType = KeyboardType.Number
//                            )
//                        }

//                        item {
//                            MoneyTextField(
//                                value = monthlyExpense,
//                                onValueChange = viewModel::onMonthlyExpenseChange,
//                                placeHolder = "Monthly Expense",
//                                label = "Monthly Expense"
//                            )
//                        }

                        item {
                            OnBoardingTextField(
                                value = inflation,
                                onValueChange = { input ->

                                    if (input.isEmpty()) {
                                        onEvent(GoalOnboardingEvent.OnInflationChanged(""))
                                        return@OnBoardingTextField
                                    }

                                    val isValid = input.matches(
                                        Regex("""^\d+(\.\d{0,2})?$""")
                                    )

                                    if (isValid) {
                                        onEvent(GoalOnboardingEvent.OnInflationChanged(input))
                                    }
                                },
                                placeHolder = "Inflation %",
                                label = "Inflation Rate(%)",
                                keyboardType = KeyboardType.Decimal
                            )
                        }

//                        item {
//                            MoneyTextField(
//                                value = saved,
//                                onValueChange = viewModel::onSavedAmountChange,
//                                placeHolder = "Current Savings",
//                                label = "Current Savings"
//                            )
//                        }

                        item {
                            OnBoardingTextField(
                                value = retirementAge,
                                onValueChange = { onEvent(GoalOnboardingEvent.OnRetirementAgeChanged(it)) },
                                placeHolder = "Retirement Age",
                                label = "Retirement Age",
                                keyboardType = KeyboardType.Number
                            )
                        }

                        item {
                            OnBoardingTextField(
                                value = lifeExpectancy,
                                onValueChange = { onEvent(GoalOnboardingEvent.OnLifeExpectancyChanged(it)) },
                                placeHolder = "Life Expectancy",
                                label = "Life Expectancy",
                                keyboardType = KeyboardType.Number
                            )
                        }

                        item {
                            OnBoardingTextField(
                                value = postReturn,
                                onValueChange = { input ->

                                    if (input.isEmpty()) {
                                        onEvent(GoalOnboardingEvent.OnPostReturnChanged(""))
                                        return@OnBoardingTextField
                                    }

                                    val isValid = input.matches(
                                        Regex("""^\d+(\.\d{0,2})?$""")
                                    )

                                    if (isValid) {
                                        onEvent(GoalOnboardingEvent.OnPostReturnChanged(input))
                                    }
                                },
                                placeHolder = "Post Retirement Return %",
                                label = "Post Retirement Return(%)",
                                keyboardType = KeyboardType.Decimal
                            )
                        }
                    }

                    GoalType.WealthBuilding -> {

                        item {
                            OnBoardingTextField(
                                value = goalName,
                                onValueChange = { onEvent(GoalOnboardingEvent.OnGoalNameChanged(it)) },
                                placeHolder = "Goal Name",
                                label = "Goal Name"
                            )
                        }

                        item {
                            OnBoardingTextField(
                                value = goalItemName,
                                onValueChange = { onEvent(GoalOnboardingEvent.OnGoalItemNameChanged(it)) },
                                placeHolder = "Goal Category (e.g. House, Travel)",
                                label = "Goal Category"
                            )
                        }

                        item {
                            MoneyTextField(
                                value = goalCost,
                                onValueChange = { onEvent(GoalOnboardingEvent.OnGoalCostChanged(it)) },
                                placeHolder = "Target Amount",
                                label = "Target Amount"
                            )
                        }

                        item {
                            OnBoardingTextField(
                                value = inflation,
                                onValueChange = { input ->

                                    if (input.isEmpty()) {
                                        onEvent(GoalOnboardingEvent.OnInflationChanged(""))
                                        return@OnBoardingTextField
                                    }

                                    val isValid = input.matches(
                                        Regex("""^\d+(\.\d{0,2})?$""")
                                    )

                                    if (isValid) {
                                        onEvent(GoalOnboardingEvent.OnInflationChanged(input))
                                    }
                                },
                                placeHolder = "Inflation %",
                                label = "Inflation Rate(%)",
                                keyboardType = KeyboardType.Decimal
                            )
                        }

//                        item {
//                            MoneyTextField(
//                                value = saved,
//                                onValueChange = viewModel::onSavedAmountChange,
//                                placeHolder = "Current Savings",
//                                label = "Current Savings"
//                            )
//                        }


                        item {
                            OnBoardingTextField(
                                value = targetYear,
                                onValueChange = {},
                                placeHolder = "Select Target Year",
                                label = "Target Year",
                                enabled = false,
                                modifier = Modifier.clickable {
                                    keyboardController.clearFocus()
                                    focusManager.clearFocus()
                                    showYearPicker()
                                }
                            )
                        }
                    }

                    null -> Unit
                }
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        NextButtonFooter(
            value = "Add Goal",
            onClick = {
                onEvent(GoalOnboardingEvent.OnAddGoalClicked)
            },
            pv = pv,
            enabled = buttonEnabled
        )
    }
}