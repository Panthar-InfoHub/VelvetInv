package org.sharad.velvetinvestment.utils

sealed interface AuthMode {
    data object Login: AuthMode
    data object SignUp: AuthMode
}