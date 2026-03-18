package org.sharad.velvetinvestment.presentation.LoginScreen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.velvetinvestment.shared.compose.AppButton
import org.sharad.velvetinvestment.shared.compose.LoginTextField
import org.sharad.velvetinvestment.shared.compose.PhoneNumberTextField
import org.sharad.velvetinvestment.utils.AuthMode
import org.sharad.velvetinvestment.utils.genericDropShadow
import org.sharad.velvetinvestment.utils.theme.buttonTextStyle


@Composable
fun CredentialBox(
    email: String,
    password: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    buttonEnabled: Boolean,
    onButtonClick: () -> Unit,
    authMode: AuthMode,
    onSignUpClick: () -> Unit,
    onLoginClick: () -> Unit,
    onLoginPasswordTabClick: () -> Unit,
    onPhoneNumberChange: (String) -> Unit,
    number: String,
    loading: Boolean
){
    Box(
        modifier = Modifier.width(320.dp)
            .genericDropShadow(RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
    ){

        Column(
            modifier = Modifier
                .animateContentSize()
                .fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AuthSwitch(
                authMode = authMode,
                onLoginClick = onLoginClick,
                onSignUpClick = onSignUpClick
            )
            Column(
                modifier=Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                when(authMode){
                    AuthMode.Login.OTP -> {
                        PhoneNumberTextField(
                            value = number,
                            onValueChange = { onPhoneNumberChange(it) },
                        )
                    }
                    AuthMode.Login.Password -> {
                        LoginTextField(
                            value = email,
                            onValueChange = { onEmailChange(it) },
                            placeHolder = "Username"
                        )
                        LoginTextField(
                            value = password,
                            onValueChange = { onPasswordChange(it) },
                            placeHolder = "Password"
                        )
                    }
                    AuthMode.SignUp -> {
                        PhoneNumberTextField(
                            value = number,
                            onValueChange = { onPhoneNumberChange(it) },
                        )
                    }
                }
            }
            AppButton(
                text = "Continue",
                enabled = buttonEnabled,
                onClick = {onButtonClick()},
                loading = loading
            )

            if (authMode is AuthMode.Login){
                Text(
                    text= when(authMode){
                        AuthMode.Login.OTP -> "Continue with password"
                        AuthMode.Login.Password -> "Continue with number"
                    },
                    style = buttonTextStyle,
                    color = Primary,
                    modifier = Modifier.clickable(
                        onClick = {
                            when(authMode) {
                                AuthMode.Login.OTP -> {
                                    onLoginPasswordTabClick()
                                }
                                AuthMode.Login.Password -> {
                                    onLoginClick()
                                }
                            }
                        },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    )
                )
            }

        }
    }
}