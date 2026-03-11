package org.sharad.velvetinvestment.presentation.profile.compose.ProfileNew.viewModel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.sharad.velvetinvestment.presentation.profile.compose.ProfileNew.UImodel.CheckKYCModel

class CheckKYCViewModel: ViewModel() {
    private val _checkKYCModel = MutableStateFlow(CheckKYCModel())
    val checkKYCModel =_checkKYCModel.asStateFlow()
}