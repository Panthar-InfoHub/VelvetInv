package org.sharad.velvetinvestment.presentation.settingscreens.compose

import androidx.compose.foundation.background
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.goldenColor
import org.sharad.velvetinvestment.presentation.settingscreens.viewmodels.SettingViewModel
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.utils.genericDropShadow
import org.sharad.velvetinvestment.utils.theme.Poppins
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.hidden_icon
import velvet.composeapp.generated.resources.icon__2_

@Preview(showSystemUi = true)
@Composable
fun ChangePasswordScreen() {
    val viewModel : SettingViewModel = koinViewModel()
    val status by viewModel.changePasswordModel.collectAsStateWithLifecycle()
    Column(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        BackHeader("Change Password", true)
        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                Text("New Password", fontSize = 14.sp, fontFamily = Poppins)
                TextField(
                    value = status.newPassword,
                    onValueChange = viewModel::updateNewPassword,
                    placeholder = {
                        Text(
                            "8-32 character",
                            fontSize = 16.sp,
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Medium
                        )
                    },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(Res.drawable.hidden_icon),
                            contentDescription = "hidden icon",
                            tint = goldenColor
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = goldenColor,
                        unfocusedIndicatorColor = goldenColor,
                        unfocusedContainerColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
            item {
                Text("Confirm New Password", fontSize = 14.sp, fontFamily = Poppins)
                TextField(
                    value = status.confirmPassword,
                    onValueChange = viewModel::updateConfirmPassword,
                    placeholder = {
                        Text(
                            "8-32 character",
                            fontSize = 16.sp,
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Medium
                        )
                    },
                    trailingIcon = {
                        Icon(
                            painter = painterResource(Res.drawable.hidden_icon),
                            contentDescription = "hidden icon",
                            tint = goldenColor
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = goldenColor,
                        unfocusedIndicatorColor = goldenColor,
                        unfocusedContainerColor = Color.Transparent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }


        }
        Box(
            modifier = Modifier.fillMaxWidth().genericDropShadow(RoundedCornerShape(24.dp))
                .clip(RoundedCornerShape(24.dp)).background(color = Color.White)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth().background(Color(0xffFF9D00).copy(0.1f))
                    .padding(vertical = 24.dp, horizontal = 16.dp)
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Icon(
                        painter = painterResource(Res.drawable.icon__2_),
                        contentDescription = "Icon",
                        tint = goldenColor, modifier = Modifier.size(25.dp)
                    )
                    Text(
                        "Your password needs to include at least one uppercase letter and one lowercase letter.\n It should also have at least one number and one special character. Avoid using your first or last name, as well as your email address.\n Additionally, it must be distinct from your previous passwords.",
                        fontSize = 12.sp,
                        fontFamily = Poppins,
                        color = Primary, lineHeight = 20.sp
                    )
                }
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


