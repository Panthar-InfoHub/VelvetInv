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

    fun onColorChange(value: Color){
        _notificationModel.update {
            it.copy(color = value)
        }
    }

    fun onIconChange(value: DrawableResource){
        _notificationModel.update {
            it.copy(icon = value)
        }
    }

    fun onHeadingChange(value: String){
        _notificationModel.update {
            it.copy(heading = value)
        }
    }
    fun onBodyChange(value: String) {
        _notificationModel.update {
            it.copy(body = value)
        }
    }

    fun onTimeChange(value: String) {
        _notificationModel.update {
            it.copy(time = value)
        }
    }

    fun onAdditionalInfoChange(value: Boolean) {
        _notificationModel.update {
            it.copy(additionalInfo = value)
        }
    }

    fun onExtraInfoChange(value: String){
        _notificationModel.update {
            it.copy(extraText = value)
        }

    }
}