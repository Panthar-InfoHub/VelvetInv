package org.sharad.velvetinvestment.presentation.tradingaccount.compose
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.appGreen
import org.sharad.emify.core.ui.theme.bgColor3
import org.sharad.emify.core.ui.theme.darkBlue
import org.sharad.emify.core.ui.theme.grayColor
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.presentation.onboarding.compose.personaldetails.NextButtonFooter
import org.sharad.velvetinvestment.presentation.tradingaccount.viewmodel.TradingAccountViewModel
import org.sharad.velvetinvestment.shared.UiStateContainer
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.genericDropShadow
import org.sharad.velvetinvestment.shared.theme.Poppins
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.notice

@Composable
fun TAScreen8(
    pv: PaddingValues,
    onClick: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: TradingAccountViewModel
) {
    val state by viewModel.formState.collectAsStateWithLifecycle()
    val panVerified by viewModel.panVerified.collectAsStateWithLifecycle()
    val verifiedPanNumber by viewModel.verifiedPanNumber.collectAsStateWithLifecycle()
    Column(modifier = Modifier.fillMaxSize()) {
        BackHeader(
            "Trading account",
            showBack = true,
            onBackClick = onBackClick
        )
        UiStateContainer(
            uiState = state,
            onRetry = { viewModel.getUserData() }
        ) { baseResponse ->
            val data = baseResponse.data
            Column(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                "Guardian PAN Verification",
                                style = MaterialTheme.typography.headlineLarge
                            )
                            Text(
                                "Verify guardian PAN for minor account compliance",
                                fontFamily = Poppins,
                                fontSize = 14.sp,
                                color = Color(0xff4A5565)
                            )
                        }
                    }
                    ///////////////////////////////////////////////////////
                    // GUARDIAN PAN CARD
                    ///////////////////////////////////////////////////////
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(
                                    0.7.dp,
                                    shape = RoundedCornerShape(10.dp),
                                    color = Color(0xffFEE685)
                                )
                                .clip(RoundedCornerShape(24.dp))
                                .background(Color(0xffFFFBEB))
                                .padding(16.dp)
                        ) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(Res.drawable.notice),
                                        contentDescription = "notice icon",
                                        tint = Color(0xffE17100)
                                    )
                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            "Guardian PAN Required",
                                            fontFamily = Poppins,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            color = Color(0xff7B3306)
                                        )
                                        Text(
                                            "Guardian PAN is mandatory for minor trading accounts",
                                            fontFamily = Poppins,
                                            fontSize = 14.sp,
                                            color = Color(0xffBB4D00)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.Top) {
                                    Text(
                                        text = "Guardian PAN Number",
                                        fontFamily = Poppins,
                                        fontWeight = FontWeight.Medium,
                                        fontSize = 15.sp,
                                        color = Color.Black
                                    )
                                    Text(
                                        text = "*",
                                        color = Color.Red,
                                        fontSize = 15.sp
                                    )
                                }
                                BasicTextField(
                                    value = data.guardian_pan,
                                    onValueChange = {
                                        viewModel.onGuardianPanChange(
                                            it.toUpperCase(Locale.current)
                                        )
                                    },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Text
                                    ),
                                    textStyle = MaterialTheme.typography.bodySmall,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(54.dp)
                                        .clip(RoundedCornerShape(15.dp))
                                        .background(Color.White)
                                        .border(
                                            width = 0.7.dp,
                                            color = Color(0xFFC5A572),
                                            shape = RoundedCornerShape(15.dp)
                                        ),
                                    decorationBox = { innerTextField ->
                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .padding(horizontal = 16.dp),
                                            contentAlignment = Alignment.CenterStart
                                        ) {
                                            if (data.guardian_pan.isEmpty()) {
                                                Text(
                                                    text = "ABCDE1234F",
                                                    color = Color(0xffC5C5C5),
                                                    style = MaterialTheme.typography.bodySmall
                                                )
                                            }
                                            innerTextField()
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.End,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                if (
                                                    !panVerified ||
                                                    verifiedPanNumber != data.guardian_pan
                                                ) {
                                                    TextButton(
                                                        onClick = {
                                                            viewModel.verifyPan(data.guardian_pan)
                                                        }
                                                    ) {
                                                        Text(
                                                            text = "Verify",
                                                            color = darkBlue,
                                                            fontWeight = FontWeight.SemiBold,
                                                            fontFamily = Poppins,
                                                            fontSize = 16.sp
                                                        )
                                                    }
                                                } else {
                                                    Text(
                                                        text = "Verified",
                                                        color = appGreen,
                                                        fontWeight = FontWeight.SemiBold,
                                                        fontFamily = Poppins,
                                                        fontSize = 16.sp
                                                    )
                                                }
                                            }
                                        }
                                    }
                                )
                                Text(
                                    "Enter guardian PAN in 10-character format",
                                    fontFamily = Poppins,
                                    fontSize = 13.sp,
                                    color = Color(0xff7B3306)
                                )
                            }
                        }
                    }
                    ///////////////////////////////////////////////////////
                    // REGULATORY DISCLOSURE
                    ///////////////////////////////////////////////////////
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .genericDropShadow(RoundedCornerShape(24.dp))
                                .clip(RoundedCornerShape(24.dp))
                                .background(bgColor3.copy(0.1f))
                                .padding(16.dp)
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(Res.drawable.notice),
                                        contentDescription = "notice icon",
                                        tint = Secondary
                                    )
                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Text(
                                            "Regulatory Disclosure",
                                            fontFamily = Poppins,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 14.sp,
                                            color = grayColor
                                        )
                                        Text(
                                            "Guardian PAN mismatch or invalid PAN may cause account rejection.",
                                            fontFamily = Poppins,
                                            fontSize = 14.sp,
                                            lineHeight = 15.sp,
                                            color = titleColor
                                        )
                                    }
                                }
                            }
                        }
                    }
                    item {
                        Spacer(
                            modifier = Modifier.height(
                                pv.calculateBottomPadding()
                            )
                        )
                    }
                }
                NextButtonFooter(
                    onClick = onClick,
                    pv = pv,
                    value = "Next",
                    enabled = panVerified &&
                            verifiedPanNumber == data.guardian_pan
                )
            }
        }
    }
}