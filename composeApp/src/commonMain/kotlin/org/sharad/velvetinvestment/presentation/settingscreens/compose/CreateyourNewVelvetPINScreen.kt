package org.sharad.velvetinvestment.presentation.settingscreens.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.velvetinvestment.shared.compose.OtpGrid
import org.sharad.velvetinvestment.utils.theme.Poppins
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.arrowback_elements

@Composable
fun CreateNewVelvetPINScreen() {

    val otpLength = 4
    var otp by remember { mutableStateOf("") }
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
                    "Create your new Velvet PIN",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 20.sp,
                    fontFamily = Poppins
                )
            }

            item {
                Text(
                    "For enhanced security of your finances, please set a new PIN. You’ll need to enter this PIN each time you access the app.",
                    fontFamily = Poppins,
                    fontSize = 14.sp,
                    color = Color(0xff12110D)
                )
            }

            item {

                OtpGrid(
                    modifier = Modifier,
                    otpLength = otpLength,
                    otp = otp,
                    onOtpChange = {
                        otp=it
                    },
                )


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
                "Get OTP",
                fontWeight = FontWeight.Medium,
                fontFamily = Poppins,
                fontSize = 18.sp
            )
        }
    }
}


