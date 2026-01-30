package org.sharad.velvetinvestment.presentation.LoginScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.velvetinvestment.presentation.LoginScreen.viewmodel.LoginScreenViewModel
import org.sharad.velvetinvestment.presentation.SplashScreen.LightGradient
import org.sharad.velvetinvestment.utils.AuthMode
import org.sharad.velvetinvestment.utils.WindowSize
import org.sharad.velvetinvestment.utils.theme.Poppins
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.background_mesh
import velvet.composeapp.generated.resources.bottom_background
import velvet.composeapp.generated.resources.logo_app

@Composable
fun LoginScreen(
    windowSize: WindowSize,
    onLoginSuccessNavigation: () -> Unit,
) {

    val viewModel: LoginScreenViewModel = koinViewModel()
    val authMode by viewModel.authState.collectAsStateWithLifecycle()
    val email by viewModel.email.collectAsStateWithLifecycle()
    val password by viewModel.password.collectAsStateWithLifecycle()
    val buttonEnabled by viewModel.buttonEnabled.collectAsStateWithLifecycle()
    val loading by viewModel.loading.collectAsStateWithLifecycle()


    when (windowSize) {
        WindowSize.PhoneLandscape -> {
            LoginScreenLandscape(
                authMode = authMode,
                email = email,
                password = password,
                buttonEnabled = buttonEnabled,
                loading = loading,
                onLoginClick = { viewModel.onLoginClick() },
                onSignUpClick = { viewModel.onSignUpClick() },
                onEmailChange = { viewModel.onUserNameChange(it) },
                onPasswordChange = { viewModel.onPasswordChange(it) },
                onButtonClick = {})
        }

        else -> {
            LoginScreenPortrait(
                authMode = authMode,
                email = email,
                password = password,
                buttonEnabled = buttonEnabled,
                loading = loading,
                onLoginClick = { viewModel.onLoginClick() },
                onSignUpClick = { viewModel.onSignUpClick() },
                onEmailChange = { viewModel.onUserNameChange(it) },
                onPasswordChange = { viewModel.onPasswordChange(it) },
                onButtonClick = {
                    viewModel.onButtonClick(
                        onSuccess = {
                            onLoginSuccessNavigation()
                        },
                        onFailure = {

                        }
                    )
                }
            )
        }
    }
}

@Composable
fun LoginScreenPortrait(
    buttonEnabled: Boolean,
    password: String,
    email: String,
    authMode: AuthMode,
    loading: Boolean,
    onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onButtonClick: () -> Unit,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        LightGradient()
        Image(
            painter = painterResource(Res.drawable.background_mesh),
            contentDescription = "Background Mesh",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillBounds
        )
        Image(
            painter = painterResource(Res.drawable.bottom_background),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter)
        )
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 32.dp).align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            LogoInfo()
            Spacer(modifier = Modifier.height(40.dp))
            CredentialBox(
                authMode = authMode,
                onLoginClick = onLoginClick,
                onSignUpClick = onSignUpClick,
                email = email,
                password = password,
                onEmailChange = { onEmailChange(it) },
                onPasswordChange = { onPasswordChange(it) },
                buttonEnabled = buttonEnabled,
                onButtonClick = onButtonClick
            )
            Spacer(modifier = Modifier.height(32.dp))
            MIIText()
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
fun LoginScreenLandscape(
    buttonEnabled: Boolean,
    password: String,
    email: String,
    authMode: AuthMode,
    loading: Boolean, onLoginClick: () -> Unit,
    onSignUpClick: () -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onButtonClick: () -> Unit,

    ) {
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        LightGradient()
        Image(
            painter = painterResource(Res.drawable.bottom_background),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter),
            contentScale = ContentScale.FillWidth
        )

        Row(
            modifier = Modifier.fillMaxSize()
        ) {
            Box(
                modifier = Modifier.weight(1f).fillMaxSize(), contentAlignment = Alignment.Center
            ) { LogoInfo() }
            Box(
                modifier = Modifier.weight(1f).fillMaxSize(), contentAlignment = Alignment.Center
            ) {
                CredentialBox(
                    authMode = authMode,
                    onLoginClick = onLoginClick,
                    onSignUpClick = onSignUpClick,
                    email = email,
                    password = password,
                    onEmailChange = { onEmailChange(it) },
                    onPasswordChange = { onPasswordChange(it) },
                    buttonEnabled = buttonEnabled,
                    onButtonClick = onButtonClick,
                )
            }
        }

    }
}

@Composable
fun LogoInfo(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Image(
            painter = painterResource(Res.drawable.logo_app),
            contentDescription = null,
            modifier = Modifier.size(80.dp)
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Velvet Investing",
                style = MaterialTheme.typography.headlineLarge,
                color = Color.Black,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Your Goal based path to financial freedom",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
fun MIIText(
    modifier: Modifier = Modifier,
) {
    Text(
        text = "Made in India \uD83C\uDDEE\uD83C\uDDF3",
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        fontFamily = Poppins,
        color = Color(0xff5A5E60),
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}


