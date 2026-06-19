package org.sharad.velvetinvestment.presentation.goals.compose



import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.velvetinvestment.domain.GoalType
import org.sharad.velvetinvestment.presentation.goals.viewmodel.GoalFormState
import org.sharad.velvetinvestment.presentation.goals.viewmodel.SingleGoalViewModel
import org.sharad.velvetinvestment.presentation.onboarding.compose.OnBoardingTextField
import org.sharad.velvetinvestment.presentation.onboarding.compose.financialflow.MoneyTextField
import org.sharad.velvetinvestment.presentation.onboarding.compose.goals.GoalEntry
import org.sharad.velvetinvestment.presentation.onboarding.compose.goals.GoalSelectionDropDown
import org.sharad.velvetinvestment.presentation.onboarding.compose.personaldetails.NextButtonFooter
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.goalOptions
import org.sharad.velvetinvestment.presentation.tradingaccount.compose.YearPicker
import org.sharad.velvetinvestment.shared.UiStateContainer
import org.sharad.velvetinvestment.shared.compose.BackHeader


@Composable
fun SingleGoalScreen(
    onBack: () -> Unit
) {

    val vm: SingleGoalViewModel = koinViewModel()

    val state by vm.state.collectAsStateWithLifecycle()
    val loading by vm.loading.collectAsStateWithLifecycle()
    var showYearPicker by remember {
        mutableStateOf(false)
    }


    UiStateContainer(
        uiState = state,
        onRetry = { vm.loadUserData() }
    ) { data ->
        Column(modifier = Modifier.fillMaxSize()) {
            BackHeader(
                heading = "Add Goal",
                showBack = true,
                onBackClick = onBack
            )
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 28.dp)
            ) {

                item {
                    GoalFormSection(
                        form = data.form,
                        onChange = vm::updateForm,
                        showYearPicker = {
                            showYearPicker = true
                        }
                    )
                }

                data.preview?.let {
                    item {
                        GoalEntry(
                            goalInfo = it,
                            dob = data.dob,
                            showDelete = false,
                            onDeleteClick = {}
                        )
                    }
                }
            }

            NextButtonFooter(
                value = "Save Goal",
                onClick = {
                    vm.submit {
                        onBack()
                    }
                },
                enabled = data.isValid,
                loading = loading
            )
        }
        if (showYearPicker) {
            YearPicker(
                selectedYear = data.form.targetYear.toIntOrNull(),
                onYearSelected = { year ->
                        vm.updateForm {
                            copy(
                                targetYear = year.toString()
                            )
                        }
                },
                onDismiss = {
                    showYearPicker = false
                }
            )
        }
    }
}
@Composable
fun GoalFormSection(
    form: GoalFormState,
    onChange: (GoalFormState.() -> GoalFormState) -> Unit,
    showYearPicker: () -> Unit,
    retirementAgeDefault: Int? = null
){

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalFocusManager.current

    LaunchedEffect(Unit) {
        if (form.retirementAge.isEmpty()) {
            retirementAgeDefault?.let {
                onChange { copy(retirementAge = it.toString()) }
            }
        }
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        // ---------- GOAL TYPE ----------
        GoalSelectionDropDown(
            value = form.selectedOption,
            onValueChange = {
                onChange {
                    copy(
                        selectedOption = it,
                        goalName = it.title,
                        goalItemName = it.goalItemName ?: it.title
                    )
                }
            },
            placeHolder = "Select Goal Type",
            label = "Goal Type",
            options = goalOptions
        )

        // ---------- DYNAMIC FORM ----------
        form.selectedOption?.let { option ->

            when (option.type) {

                // ---------------- CHILD ----------------
                GoalType.ChildEducation,
                GoalType.ChildMarriage -> {

                    OnBoardingTextField(
                        value = form.childName,
                        onValueChange = {
                            onChange { copy(childName = it) }
                        },
                        placeHolder = "Child Name",
                        label = "Child Name"
                    )

                    OnBoardingTextField(
                        value = form.childAge,
                        onValueChange = {
                            onChange { copy(childAge = it) }
                        },
                        placeHolder = "Age",
                        label = "Child Age",
                        keyboardType = KeyboardType.Number
                    )

                    MoneyTextField(
                        value = form.goalCost,
                        onValueChange = {
                            onChange { copy(goalCost = it) }
                        },
                        placeHolder = "Enter Goal Cost",
                        label = "Present Goal Value"
                    )

                    OnBoardingTextField(
                        value = form.inflation,
                        onValueChange = { input ->

                            if (input.isEmpty()) {
                                onChange { copy(inflation = "") }
                                return@OnBoardingTextField
                            }

                            val isValid = input.matches(
                                Regex("""^\d+(\.\d{0,2})?$""")
                            )

                            if (isValid) {
                                onChange {
                                    copy(inflation = input)
                                }
                            }
                        },
                        placeHolder = "Inflation %",
                        label = "Inflation Rate(%)",
                        keyboardType = KeyboardType.Decimal
                    )

                    OnBoardingTextField(
                        value = form.targetYear,
                        onValueChange = {},
                        enabled = false,
                        placeHolder = "Select Target Year",
                        label = "Target Year",
                        modifier = Modifier.clickable {
                            keyboardController.clearFocus()
                            focusManager.clearFocus()
                            showYearPicker()
                        }
                    )
                }

                // ---------------- RETIREMENT ----------------
                GoalType.Retirement -> {

                    OnBoardingTextField(
                        value = form.inflation,
                        onValueChange = { input ->

                            if (input.isEmpty()) {
                                onChange { copy(inflation = "") }
                                return@OnBoardingTextField
                            }

                            val isValid = input.matches(
                                Regex("""^\d+(\.\d{0,2})?$""")
                            )

                            if (isValid) {
                                onChange {
                                    copy(inflation = input)
                                }
                            }
                        },
                        placeHolder = "Inflation %",
                        label = "Inflation Rate(%)",
                        keyboardType = KeyboardType.Decimal
                    )

                    OnBoardingTextField(
                        value = form.retirementAge,
                        onValueChange = {
                            onChange { copy(retirementAge = it) }
                        },
                        placeHolder = "Retirement Age",
                        label = "Retirement Age",
                        keyboardType = KeyboardType.Number
                    )

                    OnBoardingTextField(
                        value = form.lifeExpectancy,
                        onValueChange = {
                            onChange { copy(lifeExpectancy = it) }
                        },
                        placeHolder = "Life Expectancy",
                        label = "Life Expectancy",
                        keyboardType = KeyboardType.Number
                    )

                    OnBoardingTextField(
                        value = form.postReturn,
                        onValueChange = { input ->

                            if (input.isEmpty()) {
                                onChange { copy(inflation = "") }
                                return@OnBoardingTextField
                            }

                            val isValid = input.matches(
                                Regex("""^\d+(\.\d{0,2})?$""")
                            )

                            if (isValid) {
                                onChange {
                                    copy(postReturn = input)
                                }
                            }
                        },
                        placeHolder = "Post Retirement Return %",
                        label = "Post Retirement Return(%)",
                        keyboardType = KeyboardType.Decimal
                    )
                }

                // ---------------- WEALTH ----------------
                GoalType.WealthBuilding -> {

                    OnBoardingTextField(
                        value = form.goalName,
                        onValueChange = {
                            onChange { copy(goalName = it) }
                        },
                        placeHolder = "Goal Name",
                        label = "Goal Name"
                    )

                    OnBoardingTextField(
                        value = form.goalItemName,
                        onValueChange = {
                            onChange { copy(goalItemName = it) }
                        },
                        placeHolder = "Goal Category (e.g. House, Travel)",
                        label = "Goal Category"
                    )

                    MoneyTextField(
                        value = form.goalCost,
                        onValueChange = {
                            onChange { copy(goalCost = it) }
                        },
                        placeHolder = "Target Amount",
                        label = "Target Amount"
                    )

                    OnBoardingTextField(
                        value = form.inflation,
                        onValueChange = { input ->

                            if (input.isEmpty()) {
                                onChange { copy(inflation = "") }
                                return@OnBoardingTextField
                            }

                            val isValid = input.matches(
                                Regex("""^\d+(\.\d{0,2})?$""")
                            )

                            if (isValid) {
                                onChange {
                                    copy(inflation = input)
                                }
                            }
                        },
                        placeHolder = "Inflation %",
                        label = "Inflation Rate(%)",
                        keyboardType = KeyboardType.Decimal
                    )

                    OnBoardingTextField(
                        value = form.targetYear,
                        onValueChange = {},
                        enabled = false,
                        placeHolder = "Select Target Year",
                        label = "Target Year",
                        modifier = Modifier.clickable {
                            keyboardController.clearFocus()
                            focusManager.clearFocus()
                            showYearPicker()
                        }
                    )
                }
            }
        }
    }
}