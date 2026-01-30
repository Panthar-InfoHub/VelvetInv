package org.sharad.velvetinvestment.presentation.LoginScreen.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import org.sharad.velvetinvestment.utils.AuthMode
import org.sharad.velvetinvestment.utils.isValidEmail

class LoginScreenViewModel: ViewModel() {

    private val _authState= MutableStateFlow<AuthMode>(AuthMode.Login)
    val authState= _authState.asStateFlow()

    private val _email=MutableStateFlow("")
    val email=_email.asStateFlow()

    private val _password=MutableStateFlow("")
    val password=_password.asStateFlow()

    private val _loading= MutableStateFlow(false)
    val loading=_loading.asStateFlow()

    val buttonEnabled= combine(_email,_password){ email, password->
        val isEmailValid= email.isNotBlank() && isValidEmail(email)
        val isPasswordValid= password.length >=8
        isEmailValid && isPasswordValid
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    fun onLoginClick(){
        _authState.value=AuthMode.Login
    }

    fun onSignUpClick(){
        _authState.value=AuthMode.SignUp
    }

    fun onUserNameChange(userName: String){
        _email.value=userName
    }

    fun onPasswordChange(password: String){
        _password.value=password
    }

    fun onButtonClick(
        onSuccess: () -> Unit,
        onFailure: () -> Unit
    ){
        onSuccess()
    }

}