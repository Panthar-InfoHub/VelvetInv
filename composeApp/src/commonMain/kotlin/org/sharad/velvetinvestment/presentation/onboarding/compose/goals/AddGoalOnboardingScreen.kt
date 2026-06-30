package org.sharad.velvetinvestment.presentation.onboarding.compose.goals

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import org.sharad.velvetinvestment.domain.GoalType
import org.sharad.velvetinvestment.presentation.onboarding.compose.OnBoardingTextField
import org.sharad.velvetinvestment.presentation.onboarding.compose.financialflow.MoneyTextField
import org.sharad.velvetinvestment.presentation.onboarding.compose.personaldetails.NextButtonFooter
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.GoalScreenOnboardingViewModel
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.goalOptions
import org.sharad.velvetinvestment.utils.clearFocusOnTap

@Composable
fun AddGoalOnboardingScreen(
    modifier: Modifier = Modifier,
    viewModel: GoalScreenOnboardingViewModel,
    onBack: () -> Unit,
    pv: PaddingValues,
    retirementAgeMain: Int?,
    showYearPicker:()-> Unit,
    monthlyCalculatedExpense: Long
) {

    val selectedOption by viewModel.selectedGoalOption.collectAsStateWithLifecycle()

    val goalName by viewModel.goalName.collectAsStateWithLifecycle()
    val inflation by viewModel.inflationRate.collectAsStateWithLifecycle()
    val returns by viewModel.returnRate.collectAsStateWithLifecycle()
    val saved by viewModel.currentSavedAmount.collectAsStateWithLifecycle()

    val childName by viewModel.childName.collectAsStateWithLifecycle()
    val childAge by viewModel.childAge.collectAsStateWithLifecycle()

    val targetYear by viewModel.targetYear.collectAsStateWithLifecycle()
    val goalCost by viewModel.currentGoalCost.collectAsStateWithLifecycle()

//    val currentAge by viewModel.currentAge.collectAsStateWithLifecycle()
    val retirementAge by viewModel.retirementAge.collectAsStateWithLifecycle()
    val lifeExpectancy by viewModel.lifeExpectancy.collectAsStateWithLifecycle()
    val monthlyExpense by viewModel.currentMonthlyExpense.collectAsStateWithLifecycle()
    val postReturn by viewModel.postRetirementReturn.collectAsStateWithLifecycle()

    val goalItemName by viewModel.goalItemNameInput.collectAsStateWithLifecycle()
    val buttonEnabled by viewModel.isFormValid.collectAsStateWithLifecycle()

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalFocusManager.current

    LaunchedEffect(Unit){
        if (retirementAge.isEmpty()){
            retirementAgeMain?.let{ viewModel.onRetirementAgeChange(it.toString()) }
            viewModel.onMonthlyExpenseChange(monthlyCalculatedExpense.toString())
        }
    }

    Column(modifier = modifier.fillMaxSize()
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
                    onValueChange = viewModel::onGoalOptionSelected,
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
                                onValueChange = viewModel::onChildNameChange,
                                placeHolder = "Child Name",
                                label = "Child Name"
                            )
                        }

                        item {
                            OnBoardingTextField(
                                value = childAge,
                                onValueChange = viewModel::onChildAgeChange,
                                placeHolder = "Age",
                                label = "Child Age",
                                keyboardType = KeyboardType.Number
                            )
                        }

                        item {
                            MoneyTextField(
                                value = goalCost,
                                onValueChange = viewModel::onGoalCostChange,
                                placeHolder = "Enter Goal Cost",
                                label = "Present Goal Value"
                            )
                        }

                        item {
                            OnBoardingTextField(
                                value = inflation,
                                onValueChange = { input ->

                                    if (input.isEmpty()) {
                                        viewModel.onInflationChange("")
                                        return@OnBoardingTextField
                                    }

                                    val isValid = input.matches(
                                        Regex("""^\d+(\.\d{0,2})?$""")
                                    )

                                    if (isValid) {
                                        viewModel.onInflationChange(input)
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
                                        viewModel.onInflationChange("")
                                        return@OnBoardingTextField
                                    }

                                    val isValid = input.matches(
                                        Regex("""^\d+(\.\d{0,2})?$""")
                                    )

                                    if (isValid) {
                                        viewModel.onInflationChange(input)
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
                                onValueChange = viewModel::onRetirementAgeChange,
                                placeHolder = "Retirement Age",
                                label = "Retirement Age",
                                keyboardType = KeyboardType.Number
                            )
                        }

                        item {
                            OnBoardingTextField(
                                value = lifeExpectancy,
                                onValueChange = viewModel::onLifeExpectancyChange,
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
                                        viewModel.onPostReturnChange("")
                                        return@OnBoardingTextField
                                    }

                                    val isValid = input.matches(
                                        Regex("""^\d+(\.\d{0,2})?$""")
                                    )

                                    if (isValid) {
                                        viewModel.onPostReturnChange(input)
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
                                onValueChange = viewModel::onGoalNameChange,
                                placeHolder = "Goal Name",
                                label = "Goal Name"
                            )
                        }

                        item {
                            OnBoardingTextField(
                                value = goalItemName,
                                onValueChange = viewModel::onGoalItemNameChange,
                                placeHolder = "Goal Category (e.g. House, Travel)",
                                label = "Goal Category"
                            )
                        }

                        item {
                            MoneyTextField(
                                value = goalCost,
                                onValueChange = viewModel::onGoalCostChange,
                                placeHolder = "Target Amount",
                                label = "Target Amount"
                            )
                        }

                        item {
                            OnBoardingTextField(
                                value = inflation,
                                onValueChange = { input ->

                                    if (input.isEmpty()) {
                                        viewModel.onInflationChange("")
                                        return@OnBoardingTextField
                                    }

                                    val isValid = input.matches(
                                        Regex("""^\d+(\.\d{0,2})?$""")
                                    )

                                    if (isValid) {
                                        viewModel.onInflationChange(input)
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
                viewModel.addGoal(
                    onSuccess={onBack()}
                )
            },
            pv = pv,
            enabled = buttonEnabled
        )
    }
}