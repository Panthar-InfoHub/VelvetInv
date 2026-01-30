package org.sharad.velvetinvestment.presentation.LoginScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import org.sharad.velvetinvestment.shared.AppButton
import org.sharad.velvetinvestment.shared.LoginTextField
import org.sharad.velvetinvestment.utils.AuthMode
import org.sharad.velvetinvestment.utils.Log


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
    onLoginClick: () -> Unit
){
    Box(
        modifier = Modifier.width(320.dp)
            .dropShadow(
                shape = RoundedCornerShape(20.dp),
                shadow = Shadow(
                    radius = 8.dp,
                    spread = 4.dp,
                    color = Color.Black.copy(0.25f),
                    offset = DpOffset(x = 0.dp, 4.dp)
                )
            )
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
    ){

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp, horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(28.dp),
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
                LoginTextField(
                    value = email,
                    onValueChange = { onEmailChange(it) },
                    placeHolder = "Email"
                )
                LoginTextField(
                    value = password,
                    onValueChange = { onPasswordChange(it) },
                    placeHolder = "Password"
                )
            }
            AppButton(
                text = "Continue",
                enabled = buttonEnabled,
                onClick = {onButtonClick}
            )
        }
    }
}