package org.sharad.velvetinvestment.presentation.settingscreens.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.goldenColor
import org.sharad.velvetinvestment.presentation.onboarding.compose.OnBoardingTextField
import org.sharad.velvetinvestment.presentation.settingscreens.viewmodels.SettingViewModel
import org.sharad.velvetinvestment.utils.genericDropShadow
import org.sharad.velvetinvestment.utils.theme.Poppins
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.arrowback_elements

@Preview(showSystemUi = true)
@Composable
fun VerifyYourVelvetPIN() {
    val viewModel: SettingViewModel = koinViewModel()
    val status by viewModel.verifyOtpModel.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp).statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Row(modifier = Modifier.padding(vertical = 32.dp).fillMaxWidth()) {
            Icon(
                painter = painterResource(Res.drawable.arrowback_elements),
                contentDescription = "arrowback"
            )
        }

        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Text(
                    "Verify your velvet PIN with OTP",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    fontFamily = Poppins
                )
            }

            item {
                Text(
                    "An OTP has been sent to +91 -1234567891",
                    fontFamily = Poppins,
                    fontSize = 14.sp,
                    color = Color(0xff12110D)
                )
            }
            item {
                TextField(
                    value = status.verifyOtp.toString(),
                    onValueChange = {}, placeholder = {Text("6-digit OTP", fontSize = 16.sp, fontFamily = Poppins, fontWeight = FontWeight.Medium)},
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth().height(60.dp).genericDropShadow(
                        RoundedCornerShape(16.dp)
                    ).border(1.dp, shape = RoundedCornerShape(16.dp), color = goldenColor)
                        .clip(RoundedCornerShape(16.dp)).background(color = Color.White)
                )

            }
            item {
                val time:String= "00:24"
                Text("Resend OTP in $time")
            }
        }
        TextButton(
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 10.dp).fillMaxWidth()
                .height(50.dp),
            onClick = {},
            colors = ButtonDefaults.buttonColors(
                containerColor = Primary,
                contentColor = Color.White
            )
        ) {
            Text(
                "Confirm",
                fontWeight = FontWeight.Medium,
                fontFamily = Poppins,
                fontSize = 18.sp
            )
        }
    }
}