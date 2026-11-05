package org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.features.fireonboarding.data.mapper.toGoalsStepBody
import org.sharad.velvetinvestment.features.fireonboarding.domain.repository.FireOnboardingRepository
import org.sharad.velvetinvestment.features.goals.domain.models.CreateGoalRequest
import org.sharad.velvetinvestment.features.goals.domain.models.GoalCalculationDomain
import org.sharad.velvetinvestment.features.goals.domain.models.GoalOption
import org.sharad.velvetinvestment.features.goals.domain.models.toCalculationRequest
import org.sharad.velvetinvestment.features.goals.domain.models.toOption
import org.sharad.velvetinvestment.features.goals.domain.repository.GoalsRepository
import org.sharad.velvetinvestment.features.goals.ui.viewmodels.AddGoalEvent
import org.sharad.velvetinvestment.features.goals.ui.viewmodels.AddGoalUiState
import org.sharad.velvetinvestment.features.goals.ui.viewmodels.GoalFormState
import org.sharad.velvetinvestment.features.goals.ui.viewmodels.toRequest
import org.sharad.velvetinvestment.utils.SnackBarController
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess
import org.sharad.velvetinvestment.utils.trimTo

/**
 * A goal added during FIRE onboarding but not sent yet: the whole list goes out together in one
 * `POST /onboarding/goals`. [projection] is the server's preview from when it was added, if any.
 */
data class FireGoalDraft(
    val id: Int,
    val request: CreateGoalRequest,
    val option: GoalOption,
    val title: String,
    val projection: GoalCalculationDomain?
)

data class FireGoalsUiState(
    val goals: List<FireGoalDraft> = emptyList(),
    /** The add-goal form: the goals feature's own state, so its screen can be reused as-is. */
    val addGoal: UiState<AddGoalUiState> = UiState.Loading,
    val isSubmitting: Boolean = false
) {
    /** No goals means nothing to save; the user should skip instead. */
    val canSubmit: Boolean get() = goals.isNotEmpty()

    /** What the goals will need, inflation included where a projection is known. */
    val totalTargetAmount: Long
        get() = goals.sumOf {
            (it.projection?.futureTargetAmount
                ?: it.request.targetAmount
                ?: it.request.currentCost
                ?: 0.0).toLong()
        }
}

sealed interface FireGoalsEvent {
    /** Opening the add-goal screen starts a fresh form (and loads the goal config if needed). */
    data object OnAddGoalOpened : FireGoalsEvent
    /** Everything the reused add-goal screen sends. Its Save adds the goal to the list. */
    data class OnAddGoalEvent(val event: AddGoalEvent) : FireGoalsEvent
    data class OnDeleteGoalClicked(val goalId: Int) : FireGoalsEvent
    /** `POST /onboarding/goals` with every goal added. */
    data object OnSubmitClicked : FireGoalsEvent
}

sealed interface FireGoalsEffect {
    /** The add-goal screen closes: a goal was added, or its back was pressed. */
    data object CloseAddGoal : FireGoalsEffect
}

/**
 * The goals step. Goal types, rates and the live projection come from the goals feature's
 * `/user-goal/config` and `/user-goal/calculate`, exactly as its own add-goal screen uses them;
 * the difference is that saving a goal only adds it to [FireGoalsUiState.goals] — nothing is
 * created until the step is submitted.
 */
class FireGoalsViewModel(
    private val goalsRepository: GoalsRepository,
    private val repository: FireOnboardingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(FireGoalsUiState())
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<FireGoalsEffect>()
    val effect = _effect.receiveAsFlow()

    private val _stepEffect = Channel<FireStepEffect>()
    val stepEffect = _stepEffect.receiveAsFlow()

    private var projectionJob: Job? = null
    private var nextDraftId = 1

    init {
        loadConfig()
    }

    fun handleEvent(event: FireGoalsEvent) {
        when (event) {
            FireGoalsEvent.OnAddGoalOpened -> resetForm()
            is FireGoalsEvent.OnAddGoalEvent -> handleFormEvent(event.event)
            is FireGoalsEvent.OnDeleteGoalClicked ->
                _uiState.update { s -> s.copy(goals = s.goals.filterNot { it.id == event.goalId }) }
            FireGoalsEvent.OnSubmitClicked -> submit()
        }
    }

    private fun handleFormEvent(event: AddGoalEvent) {
        when (event) {
            AddGoalEvent.OnBackClicked -> sendEffect(FireGoalsEffect.CloseAddGoal)
            AddGoalEvent.LoadData -> loadConfig()
            is AddGoalEvent.OnOptionSelected -> selectOption(event.option)
            is AddGoalEvent.UpdateForm -> updateForm(event.update)
            AddGoalEvent.OnSaveGoalClicked -> addGoal()
        }
    }

    private fun loadConfig() {
        viewModelScope.launch {
            setForm { UiState.Loading }
            when (val response = goalsRepository.getGoalConfig()) {
                is NetworkResponse.Error -> setForm { UiState.Error(response.error.message) }
                is NetworkResponse.Success -> setForm {
                    UiState.Success(AddGoalUiState(options = response.data.map { it.toOption() }))
                }
            }
        }
    }

    /** A new form keeps the loaded goal types; a failed config load is retried. */
    private fun resetForm() {
        projectionJob?.cancel()
        val current = formData()
        if (current == null) {
            loadConfig()
            return
        }
        setForm { UiState.Success(AddGoalUiState(options = current.options)) }
    }

    /** As the goals feature: a new type restarts the form with that type's defaults. */
    private fun selectOption(option: GoalOption) {
        val current = formData() ?: return
        projectionJob?.cancel()
        setForm {
            UiState.Success(
                current.copy(
                    form = GoalFormState(
                        selectedOption = option,
                        assetSubtype = option.assetSubtypes.firstOrNull(),
                        inflation = option.inflationRate.toPercentField(),
                        expectedReturn = option.expectedReturnRate.toPercentField()
                    ),
                    projection = null,
                    projecting = false,
                    projectionError = null,
                    isValid = false
                )
            )
        }
    }

    /** As the goals feature: the server previews the goal once typing pauses. */
    private fun updateForm(update: GoalFormState.() -> GoalFormState) {
        val current = formData() ?: return
        val form = current.form.update()
        val request = form.toRequest()
        setForm {
            UiState.Success(
                current.copy(
                    form = form,
                    isValid = request != null,
                    projection = current.projection.takeIf { request != null },
                    projectionError = null
                )
            )
        }

        projectionJob?.cancel()
        if (request == null) return
        projectionJob = viewModelScope.launch {
            delay(PROJECTION_DEBOUNCE_MS)
            setForm { UiState.Success((formData() ?: return@launch).copy(projecting = true)) }
            when (val response = goalsRepository.calculateGoal(request.toCalculationRequest())) {
                is NetworkResponse.Success -> setForm {
                    UiState.Success(
                        (formData() ?: return@launch).copy(
                            projection = response.data,
                            projecting = false,
                            projectionError = null
                        )
                    )
                }
                is NetworkResponse.Error -> setForm {
                    UiState.Success(
                        (formData() ?: return@launch).copy(
                            projecting = false,
                            projectionError = response.error.message
                        )
                    )
                }
            }
        }
    }

    private fun addGoal() {
        val data = formData() ?: return
        val request = data.form.toRequest() ?: return
        val option = data.form.selectedOption ?: return
        projectionJob?.cancel()
        val draft = FireGoalDraft(
            id = nextDraftId++,
            request = request,
            option = option,
            title = request.goalName?.takeIf { it.isNotBlank() }
                ?: request.childName?.takeIf { it.isNotBlank() }?.let { "$it's ${option.title}" }
                ?: option.title,
            projection = data.projection
        )
        _uiState.update {
            it.copy(
                goals = it.goals + draft,
                addGoal = UiState.Success(AddGoalUiState(options = data.options))
            )
        }
        sendEffect(FireGoalsEffect.CloseAddGoal)
    }

    private fun submit() {
        val state = _uiState.value
        if (state.isSubmitting || !state.canSubmit) return
        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true) }
            repository.submitGoals(state.goals.map { it.request }.toGoalsStepBody())
                .onSuccess {
                    _uiState.update { s -> s.copy(isSubmitting = false) }
                    _stepEffect.send(FireStepEffect.Submitted)
                }
                .onError {
                    _uiState.update { s -> s.copy(isSubmitting = false) }
                    SnackBarController.showError(it.message)
                }
        }
    }

    private fun formData(): AddGoalUiState? = (_uiState.value.addGoal as? UiState.Success)?.data

    private inline fun setForm(block: () -> UiState<AddGoalUiState>) {
        val form = block()
        _uiState.update { it.copy(addGoal = form) }
    }

    private fun sendEffect(effect: FireGoalsEffect) {
        viewModelScope.launch { _effect.send(effect) }
    }

    private companion object {
        const val PROJECTION_DEBOUNCE_MS = 450L
    }
}

/** A config rate (0.06) as the percent the form shows (6); blank when the type has none. */
private fun Double?.toPercentField(): String = this?.let { (it * 100).trimTo(2) } ?: ""
