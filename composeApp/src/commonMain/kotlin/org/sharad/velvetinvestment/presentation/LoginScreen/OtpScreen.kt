package org.sharad.velvetinvestment.presentation.LoginScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.presentation.LoginScreen.viewmodel.LoginScreenViewModel
import org.sharad.velvetinvestment.shared.compose.AppButton
import org.sharad.velvetinvestment.shared.compose.OtpGrid
import org.sharad.velvetinvestment.utils.theme.titlesStyle

@Composable
fun OtpScreen(
    viewModel: LoginScreenViewModel,
    navigateToMainApp: () -> Unit,
    navigateToOnboarding: (Int) -> Unit,
) {

    val otpSize=4
    val otp by viewModel.otp.collectAsStateWithLifecycle()
    val number by viewModel.phoneNumber.collectAsStateWithLifecycle()
    val resendTime by viewModel.timer.collectAsStateWithLifecycle()
    val loading by viewModel.loadingOtp.collectAsStateWithLifecycle()

    LaunchedEffect(resendTime){
        if (resendTime==30){
            viewModel.startOtpTimer()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .navigationBarsPadding()
    ) {

        LazyColumn(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(top = 46.dp)
        ) {
            item {
                Text(
                    "Enter the Code",
                    style = MaterialTheme.typography.headlineSmall
                )
            }

            item {
                Text(
                    buildAnnotatedString {
                        append("A verification code has been sent to ")

                        withStyle(
                            SpanStyle(
                                color = Color.Black,
                            )
                        ) {
                            append(number)
                        }
                    },
                    style = titlesStyle,
                    fontWeight = FontWeight.Bold,
                    color = titleColor
                )
            }

            item {

                OtpGrid(
                    modifier = Modifier,
                    otpLength = 4,
                    otp = otp,
                    onOtpChange = viewModel::onOtpChange
                )
            }

            item {
                if (resendTime>0){
                    Text(
                        text="You can resend the code in $resendTime seconds",
                        style = MaterialTheme.typography.displayLarge,
                        color = titleColor
                    )
                }else
                {
                    Text(
                        text = "Resend Otp",
                        style = MaterialTheme.typography.displayLarge,
                        color = Primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            item {
                AppButton(
                    modifier= Modifier.fillMaxWidth(),
                    text = "Verify Otp",
                    onClick = {
                        viewModel.onVerifyOtpClickClick(
                            onFailure = {}
                        ){
                            if (it.onboarded){
                                navigateToMainApp()
                            }else{
                                navigateToOnboarding(it.onboardingStep)
                            }
                        }
                    },
                    enabled = otp.length==otpSize,
                    loading = loading
                )
            }
        }
    }
}


