package org.sharad.velvetinvestment.presentation.tradingaccount.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.jetbrains.compose.resources.DrawableResource
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