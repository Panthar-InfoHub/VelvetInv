package org.sharad.velvetinvestment.presentation.LoginScreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.domain.models.auth.LoginDomain
import org.sharad.velvetinvestment.domain.repository.UserAuth
import org.sharad.velvetinvestment.utils.AuthMode
import org.sharad.velvetinvestment.utils.isValidEmail
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess

class LoginScreenViewModel(
    private val repo: UserAuth
): ViewModel() {

    private val _authState= MutableStateFlow<AuthMode>(AuthMode.Login.OTP)
    val authState= _authState.asStateFlow()

    private val _email=MutableStateFlow("")
    val email=_email.asStateFlow()

    private val _password=MutableStateFlow("")
    val password=_password.asStateFlow()

    private val _phoneNumber=MutableStateFlow("")
    val phoneNumber=_phoneNumber.asStateFlow()

    private val _otp=MutableStateFlow("")
    val otp=_otp.asStateFlow()

    private var timerJob: Job?=null

    private val _timer= MutableStateFlow(30)
    val timer=_timer.asStateFlow()


    private val _loadingLogin= MutableStateFlow(false)
    val loading=_loadingLogin.asStateFlow()

    private val _loadingOtp= MutableStateFlow(false)
    val loadingOtp=_loadingOtp.asStateFlow()


    val buttonEnabled =
        combine(_email, _password, _phoneNumber, _authState) { email, password, phone, authState ->

            when(authState){
                AuthMode.Login.OTP -> {
                    phone.length == 10
                }
                AuthMode.Login.Password -> {
                    val isEmailValid= email.isNotBlank() && isValidEmail(email)
                    val isPasswordValid= password.length >=8
                    isEmailValid && isPasswordValid
                }
                AuthMode.SignUp -> {
                    phone.length==10
                }
            }

        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    fun onLoginTabClick(){
        _authState.value=AuthMode.Login.OTP
    }

    fun onLoginPasswordTabClick(){
        _authState.value=AuthMode.Login.Password
    }

    fun onSignUpTabClick(){
        _authState.value=AuthMode.SignUp
    }

    fun onUserNameChange(userName: String){
        _email.value=userName
    }

    fun onPasswordChange(password: String){
        _password.value=password
    }

    fun onPhoneNumberChange(phoneNumber: String) {
        if (phoneNumber.all { it.isDigit() } && phoneNumber.length <= 10) {
            _phoneNumber.value = phoneNumber
        }
    }

    fun onOtpChange(newOtp: String) {
        if (newOtp.length <= 4 && newOtp.all { it.isDigit() }) {
            _otp.value = newOtp
        }
    }

    fun onSendOtpClick(
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ){
        viewModelScope.launch {
            _loadingLogin.value=true
            repo.loginWithNumber(_phoneNumber.value)
                .onSuccess {
                    _loadingLogin.value=false
                    onSuccess()
                }
                .onError {
                   _loadingLogin.value=false
                    onFailure(it.message)
                }
        }
    }

    fun onVerifyOtpClickClick(
        onFailure: (String) -> Unit,
        onSuccess: (LoginDomain) -> Unit,
    ){
        viewModelScope.launch {
            _loadingOtp.value=true
            repo.verifyOTP(_phoneNumber.value, _otp.value)
                .onSuccess {
                    _loadingOtp.value=false
                    onSuccess(it)
                }
                .onError {
                    _loadingOtp.value=false
                    onFailure(it.message)
                }
        }
    }


    fun onButtonClick(
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ){
        onSuccess()
    }

    fun startOtpTimer() {
        timerJob?.cancel()

        _timer.value = 30

        timerJob = viewModelScope.launch {
            while (_timer.value > 0) {
                delay(1000)
                _timer.value= _timer.value-1
            }
        }
    }

}