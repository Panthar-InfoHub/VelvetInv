package org.sharad.velvetinvestment.presentation.onboarding.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.data.remote.model.onboarding.OnBoardingBodyDto
import org.sharad.velvetinvestment.domain.repository.UserAuth
import org.sharad.velvetinvestment.utils.SnackBarController
import org.sharad.velvetinvestment.utils.SnackBarType
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess

class OnBoardingConfirmationViewModel(
    private val repo: UserAuth
): ViewModel() {

    private val _loading= MutableStateFlow(false)
    val loading=_loading.asStateFlow()

    fun onboardUser(
        data: OnBoardingBodyDto,
        onSuccess:()->Unit
    ) {
        viewModelScope.launch {
            _loading.value=true
            repo.onBoardUser(data)
                .onSuccess {
                    _loading.value=false
                    onSuccess()
                }
                .onError {
                    SnackBarController.showSnackBar(SnackBarType.Error(it.message))
                    _loading.value=false
                }
        }
    }

}