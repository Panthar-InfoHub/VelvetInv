package org.sharad.velvetinvestment.presentation.profile.compose.ProfileNew.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.sharad.velvetinvestment.presentation.tradingaccount.uimodel.NotificationModel

class NotificationCentreViewModel: ViewModel() {
   private val _notificationModel = MutableStateFlow( NotificationModel())
    val notificationModel = _notificationModel.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {

    }
}