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
import androidx.compose.foundation.layout.imePadding
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
import org.sharad.velvetinvestment.shared.theme.subHeadingMedium
import org.sharad.velvetinvestment.shared.theme.titlesStyle
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.notice

@Composable
fun TradingAccountPANDetailsScreen(
    onClick: () -> Unit,
    onBackClick: () -> Boolean,
    viewModel: TradingAccountViewModel
) {
    val state by viewModel.formState.collectAsStateWithLifecycle()
    val panVerified by viewModel.panVerified.collectAsStateWithLifecycle()
    val verifiedPanNumber by viewModel.verifiedPanNumber.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize().imePadding()) {

        BackHeader(
            "Trading account",
            showBack = true,
            onBackClick = { onBackClick() },
            rightContent = {
                Text(
                    text = "Step 2/5",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.Gray
                )
            }
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
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 20.dp)
                ) {

                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(
                                "KYC & PAN Details",
                                style = MaterialTheme.typography.headlineLarge
                            )
                            Text(
                                "Verify your identity documents",
                                style = titlesStyle,
                                color = titleColor
                            )
                        }
                    }

                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            Row(verticalAlignment = Alignment.Top) {
                                Text(
                                    text = "PAN Number",
                                    style = subHeadingMedium,
                                    color = Color.Black
                                )
                                Text(
                                    text = "*",
                                    color = Color.Red,
                                    style = subHeadingMedium
                                )
                            }

                            BasicTextField(
                                value = data.primary_holder_pan,
                                onValueChange = {
                                    viewModel.onPanChange(
                                        it.toUpperCase(
                                            Locale.current
                                        )
                                    )
                                },
                                singleLine = true,
                                textStyle = MaterialTheme.typography.bodySmall,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(54.dp)
                                    .clip(RoundedCornerShape(15.dp))
                                    .background(Color.White, RoundedCornerShape(15.dp))
                                    .border(
                                        width = 0.7.dp,
                                        shape = RoundedCornerShape(15.dp),
                                        color = Color(0xFFC5A572)
                                    ),
                                decorationBox = { innerTextField ->

                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(horizontal = 16.dp),
                                        contentAlignment = Alignment.CenterStart
                                    ) {

                                        if (data.primary_holder_pan.isEmpty()) {
                                            Text(
                                                text = "ABCDE1234F",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = Color(0xffC5C5C5),
                                                maxLines = 1
                                            )
                                        }

                                        innerTextField()

                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.End
                                        ) {
                                            if (!panVerified || verifiedPanNumber != data.primary_holder_pan) {
                                                TextButton(onClick = {
                                                    viewModel.verifyPan(
                                                        data.primary_holder_pan
                                                    )
                                                }) {
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
                        }
                    }

                    item {
                        Text(
                            "Enter your 10-character PAN card number (5 letters, 4 digits, 1 letter)",
                            style = titlesStyle,
                            color = titleColor
                        )
                    }

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
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(Res.drawable.notice),
                                        contentDescription = "notice icon",
                                        tint = Secondary
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
                                            lineHeight = 15.sp,
                                            color = titleColor
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                NextButtonFooter(
                    onClick = onClick,
                    value = "Next",
                    enabled = panVerified && verifiedPanNumber == data.primary_holder_pan
                )
            }
        }
    }
}