package org.sharad.velvetinvestment.features.login.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.features.login.domain.model.LoginDomain
import org.sharad.velvetinvestment.features.login.domain.usecases.LoginWithNumberUseCase
import org.sharad.velvetinvestment.features.login.domain.usecases.VerifyOTPUseCase
import org.sharad.velvetinvestment.utils.AuthMode
import org.sharad.velvetinvestment.utils.SnackBarController
import org.sharad.velvetinvestment.utils.isValidEmail
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess
import org.sharad.velvetinvestment.utils.otpautofill.OtpAutofill

class LoginScreenViewModel(
    private val loginWithNumberUseCase: LoginWithNumberUseCase,
    private val verifyOtpUseCase: VerifyOTPUseCase,
    private val otpAutofill: OtpAutofill
): ViewModel() {

    private var otpAutofillJob: Job? = null

    /**
     * Fires once an OTP has been filled in from the SMS. The OTP screen answers it with the same
     * verify call as its button, since it owns where a successful login navigates to.
     */
    private val _otpAutofilled = Channel<Unit>(Channel.CONFLATED)
    val otpAutofilled = _otpAutofilled.receiveAsFlow()

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
        if (newOtp.length <= OTP_LENGTH && newOtp.all { it.isDigit() }) {
            _otp.value = newOtp
        }
    }

    fun onSendOtpClick(
        onSuccess: () -> Unit,
        onFailure: (String) -> Unit
    ){
        // Listening is one-shot, so (re-)arm it before the SMS is sent — first send and resend alike.
        startOtpAutofill()
        viewModelScope.launch {
            _loadingLogin.value=true
            var currentRetry = 0
            val maxRetries = 2

            while (currentRetry <= maxRetries) {
                var success = false
                loginWithNumberUseCase(_phoneNumber.value)
                    .onSuccess {
                        _loadingLogin.value=false
                        _otp.value=""
                        _loadingOtp.value=false
                        startOtpTimer()
                        onSuccess()
                        success = true
                    }
                    .onError {
                        if (currentRetry == maxRetries) {
                            otpAutofillJob?.cancel()
                            _loadingLogin.value=false
                            onFailure(it.message)
                            SnackBarController.showError(it.message)
                        }
                    }

                if (success) break

                currentRetry++
                if (currentRetry <= maxRetries) {
                    delay(1000)
                }
            }
        }
    }

    fun onVerifyOtpClickClick(
        onFailure: (String) -> Unit,
        onSuccess: (LoginDomain) -> Unit,
    ){
        // One attempt, as in JN: a retry would re-submit a wrong or spent OTP.
        if (_otp.value.length != OTP_LENGTH || _loadingOtp.value) return
        viewModelScope.launch {
            _loadingOtp.value=true
            verifyOtpUseCase(_phoneNumber.value, _otp.value)
                .onSuccess {
                    _loadingOtp.value=false
                    onSuccess(it)
                }
                .onError {
                    _loadingOtp.value=false
                    onFailure(it.message)
                    SnackBarController.showError(it.message)
                }
        }
    }



    /**
     * Best effort: if the API is unavailable (iOS), the user denies it or it times out, nothing
     * happens and the code is typed by hand as before.
     */
    private fun startOtpAutofill() {
        otpAutofillJob?.cancel()
        otpAutofillJob = viewModelScope.launch {
            otpAutofill.observeOtp()
                .catch { /* best effort; manual entry still works */ }
                .collect { otp ->
                    onOtpChange(otp)
                    _otpAutofilled.send(Unit)
                }
        }
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

    private companion object {
        const val OTP_LENGTH = 4
    }
}
