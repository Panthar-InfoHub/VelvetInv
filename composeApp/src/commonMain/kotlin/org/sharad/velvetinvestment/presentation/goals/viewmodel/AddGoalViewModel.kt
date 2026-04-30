package org.sharad.velvetinvestment.presentation.goals.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.sharad.velvetinvestment.domain.GoalType
import org.sharad.velvetinvestment.domain.models.goals.GoalRequest
import org.sharad.velvetinvestment.domain.usecases.user.GetUserDataUseCase
import org.sharad.velvetinvestment.domain.repository.UserFinance
import org.sharad.velvetinvestment.presentation.goals.uimodels.GoalOption
import org.sharad.velvetinvestment.utils.AppEvents
import org.sharad.velvetinvestment.utils.DateTimeUtils
import org.sharad.velvetinvestment.utils.SnackBarController
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess
import kotlin.time.Clock
import kotlin.time.Instant


data class GoalUiState(
    val form: GoalFormState = GoalFormState(),
    val preview: GoalRequest? = null,
    val isValid: Boolean = false,
    val currentAge: Int = 0,
    val dob:Long
)

data class GoalFormState(
    val selectedOption: GoalOption? = null,

    val childName: String = "",
    val childAge: String = "",
    val goalCost: String = "",
    val targetYear: String = "",

    val retirementAge: String = "60",
    val lifeExpectancy: String = "90",
    val monthlyExpense: String = "",
    val postReturn: String = "",

    val goalName: String = "",
    val goalItemName: String = "",

    val inflation: String = "8",
    val returns: String = "10"
)

class SingleGoalViewModel(
    private val getUserDataUseCase: GetUserDataUseCase,
    private val userFinance: UserFinance
) : ViewModel() {

    private val _state = MutableStateFlow<UiState<GoalUiState>>(UiState.Loading)
    val state = _state.asStateFlow()

    private val _loading= MutableStateFlow(false)
    val loading = _loading.asStateFlow()


    init {
        loadUserData()
    }

    // ---------- LOAD USER ----------
     fun loadUserData() {
        viewModelScope.launch {
            getUserDataUseCase()
                .onSuccess { res ->

                    val data = res.data
                    val finance = data.user_finance

                    val monthlyExpense =
                        finance.expense_others.toLong() +
                                finance.expense_food.toLong() +
                                finance.expense_house.toLong() +
                                finance.expense_transportation.toLong()

                    val age = getAgeFromDob(data.dob)

                    _state.value = UiState.Success(
                        GoalUiState(
                            form = GoalFormState(
                                monthlyExpense = monthlyExpense.toString(),
                                retirementAge = "60",
                                lifeExpectancy = "90"
                            ),
                            currentAge = age,
                            dob = DateTimeUtils.dobToEpochMillis(data.dob)
                        )
                    )
                }
                .onError {
                    _state.value = UiState.Error("Failed to load user data")
                }
        }
    }

    // ---------- UPDATE FORM ----------
    fun updateForm(update: GoalFormState.() -> GoalFormState) {
        val current = _state.value as? UiState.Success ?: return

        val newForm = current.data.form.update()
        val preview = createPreview(newForm, current.data.currentAge)

        _state.value = UiState.Success(
            current.data.copy(
                form = newForm,
                preview = preview,
                isValid = preview != null
            )
        )
    }

    // ---------- GOAL TYPE ----------
//    fun onGoalTypeSelected(option: GoalOption) {
//        val current = _state.value as? UiState.Success ?: return
//
//        val newForm = GoalFormState(
//            selectedOption = option,
//            goalName = option.title,
//            goalItemName = option.goalItemName ?: option.title,
//            retirementAge = "60",
//            lifeExpectancy = "90",
//            inflation = "8",
//            returns = "10"
//        )
//
//        _state.value = UiState.Success(
//            current.data.copy(
//                form = newForm,
//                preview = null,
//                isValid = false
//            )
//        )
//    }

    // ---------- PREVIEW ----------
    private fun createPreview(
        form: GoalFormState,
        currentAge: Int
    ): GoalRequest? {

        val option = form.selectedOption ?: return null

        val inflation = form.inflation.toIntOrNull() ?: return null
        val returns = form.returns.toIntOrNull() ?: return null

        return when (option.type) {

            GoalType.ChildEducation -> {
                val age = form.childAge.toIntOrNull()
                val year = form.targetYear.toIntOrNull()
                val cost = form.goalCost.toLongOrNull()

                if (form.childName.isBlank() || age == null || year == null || cost == null) return null

                GoalRequest.ChildEducation(
                    childName = form.childName,
                    childAge = age,
                    yearsToGoal = year - Clock.System.now()
                        .toLocalDateTime(TimeZone.UTC).year,
                    currentGoalCost = cost,
                    inflationRate = inflation,
                    returnRate = returns,
                    currentSavedAmount = 0,
                    title = option.title
                )
            }

            GoalType.ChildMarriage -> {
                val age = form.childAge.toIntOrNull()
                val year = form.targetYear.toIntOrNull()
                val cost = form.goalCost.toLongOrNull()

                if (form.childName.isBlank() || age == null || year == null || cost == null) return null

                GoalRequest.ChildMarriage(
                    childName = form.childName,
                    childAge = age,
                    yearsToGoal = year - Clock.System.now()
                        .toLocalDateTime(TimeZone.UTC).year,
                    currentGoalCost = cost,
                    inflationRate = inflation,
                    returnRate = returns,
                    currentSavedAmount = 0,
                    title = option.title
                )
            }

            GoalType.Retirement -> {
                val retirementAge = form.retirementAge.toIntOrNull()
                val life = form.lifeExpectancy.toIntOrNull()
                val expense = form.monthlyExpense.toLongOrNull()
                val post = form.postReturn.toIntOrNull()

                if (retirementAge == null || life == null || expense == null || post == null) return null
                if (retirementAge <= currentAge || life <= retirementAge) return null

                GoalRequest.Retirement(
                    currentAge = currentAge,
                    retirementAge = retirementAge,
                    lifeExpectancy = life,
                    currentMonthlyExpense = expense,
                    postRetirementReturn = post,
                    inflationRate = inflation,
                    returnRate = returns,
                    currentSavedAmount = 0,
                    yearsToGoal = retirementAge - currentAge,
                    title = option.title
                )
            }

            GoalType.WealthBuilding -> {
                val cost = form.goalCost.toLongOrNull()
                val year = form.targetYear.toIntOrNull()

                if (form.goalName.isBlank() || cost == null || year == null) return null

                GoalRequest.WealthBuildingGoal(
                    goalName = form.goalName,
                    goalItemId = option.goalItemId ?: 1,
                    goalItemName = form.goalItemName,
                    yearsToGoal = year - Clock.System.now()
                        .toLocalDateTime(TimeZone.UTC).year,
                    currentGoalCost = cost,
                    inflationRate = inflation,
                    returnRate = returns,
                    currentSavedAmount = 0,
                    title = option.title
                )
            }
        }
    }

    fun submit(onSuccess: () -> Unit) {
        val current = _state.value as? UiState.Success ?: return
        val goal = current.data.preview ?: return

        viewModelScope.launch {

            _loading.value=true
            val result = when (goal) {

                is GoalRequest.ChildEducation -> {
                    userFinance.addChildEducationGoal(goal)
                }

                is GoalRequest.ChildMarriage -> {
                    userFinance.addChildMarriageGoal(goal)
                }

                is GoalRequest.Retirement -> {
                    userFinance.addRetirementGoal(goal)
                }

                is GoalRequest.WealthBuildingGoal -> {
                    userFinance.addWealthBuildingGoal(goal)
                }
            }

            result
                .onSuccess {
                    onSuccess()
                    SnackBarController.showSuccess(message = "Goal added successfully")
                    AppEvents.sendGoalRefreshEvent()
                    AppEvents.sendHomeRefreshEvent()
                    _loading.value=false
                }
                .onError { error ->
                    SnackBarController.showError(message = error.message)
                    _loading.value=false
                }
        }
    }
}

fun getAgeFromDob(dob: String): Int {
    val instant = Instant.parse(dob)
    val birthDate = instant.toLocalDateTime(TimeZone.UTC).date
    val today = Clock.System.now().toLocalDateTime(TimeZone.UTC).date
    return today.year - birthDate.year
}