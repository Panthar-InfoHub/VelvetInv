package org.sharad.velvetinvestment.presentation.profile.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.domain.models.notifications.NotificationDomain
import org.sharad.velvetinvestment.domain.usecases.user.GetNotificationsUseCase
import org.sharad.velvetinvestment.domain.usecases.user.MarkNotificationsAsReadUseCase
import org.sharad.velvetinvestment.utils.AppEventsController
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

data class NotificationUiState(
    val isLoading: Boolean = false,
    val notifications: List<NotificationDomain> = emptyList(),
    val error: String? = null
)

class NotificationViewModel(
    private val getNotificationsUseCase: GetNotificationsUseCase,
    private val markNotificationsAsReadUseCase: MarkNotificationsAsReadUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(NotificationUiState())
    val uiState = _uiState.asStateFlow()

    init {
        fetchNotifications()
    }

    fun fetchNotifications() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val response = getNotificationsUseCase()) {
                is NetworkResponse.Success -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            notifications = response.data,
                            error = null
                        )
                    }
                    markAsRead()
                }
                is NetworkResponse.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = response.error.message
                        )
                    }
                }
            }
        }
    }

    private fun markAsRead() {
        viewModelScope.launch {
            AppEventsController.sendNotificationClearEvent()
            markNotificationsAsReadUseCase()
        }
    }
}
