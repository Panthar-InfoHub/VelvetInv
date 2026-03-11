package org.sharad.velvetinvestment.utils

sealed interface AuthMode {

    sealed interface Login : AuthMode {
        data object Password : Login
        data object OTP : Login
    }

    data object SignUp : AuthMode
}