package org.sharad.velvetinvestment.presentation.tradingaccount.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.emify.core.ui.theme.darkBlue
import org.sharad.emify.core.ui.theme.goldenColor
import org.sharad.emify.core.ui.theme.grayColor
import org.sharad.emify.core.ui.theme.orangeColor
import org.sharad.emify.core.ui.theme.redColor
import org.sharad.velvetinvestment.presentation.tradingaccount.viewmodel.TradingAccountViewModel
import org.sharad.velvetinvestment.utils.genericDropShadow
import org.sharad.velvetinvestment.utils.theme.Poppins
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.notice

@Preview(showSystemUi = true)
@Composable
fun TradingScreen2() {
    val viewModel: TradingAccountViewModel = koinViewModel()
    val state by viewModel.panModel.collectAsStateWithLifecycle()
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                "KYC & PAN Details",
                fontSize = 24.sp,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black
            )
            Text(
                "Verify your identity documents",
                fontFamily = Poppins,
                fontSize = 14.sp,
                color = Color(0xff4A5565)
            )
        }

        Column {
            Row {
                Text(
                    "PAN Number ",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Medium,
                    fontSize = 20.sp
                )
                Text("   *", fontWeight = FontWeight.Medium, color = redColor)
            }
            TextField(
                value = state.panNumber,
                onValueChange = viewModel::onPanChange,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
                    .border(2.dp, goldenColor, RoundedCornerShape(10.dp)),
                placeholder = {
                    Text(text = "ABCDE1234F")
                },
                trailingIcon = {
                    TextButton(onClick = { viewModel.verifyPan() }) {
                        Text(
                            text = "Verify",
                            color = darkBlue,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = Poppins,
                            fontSize = 16.sp
                        )
                    }
                }
            )
        }
         Text(
            "Enter your 10-character PAN card number (5 letters, 4 digits, 1 letter)",
            color = grayColor,
            fontFamily = Poppins,
            fontSize = 14.sp
        )

        Box(
            modifier = Modifier.fillMaxWidth().genericDropShadow(RoundedCornerShape(24.dp))
                .clip(RoundedCornerShape(24.dp)).background(
                color =
                    orangeColor.copy(0.1f)
            ).padding(16.dp)
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.notice),
                        contentDescription = "notice icon"
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            "Regulatory Disclosure",
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = grayColor
                        )
                        Text(
                            "UCC will be rejected outright if PAN and KYC identifiers are inconsistent.",
                            fontFamily = Poppins,
                            fontSize = 14.sp,
                            color = grayColor
                        )
                    }
                }
            }
        }


    }
}