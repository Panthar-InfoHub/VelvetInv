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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.velvetinvestment.presentation.onboarding.compose.OnBoardingTextField
import org.sharad.velvetinvestment.presentation.onboarding.compose.personaldetails.NextButtonFooter
import org.sharad.velvetinvestment.presentation.tradingaccount.viewmodel.TradingAccountViewModel
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.GenericDropDownField
import org.sharad.velvetinvestment.utils.theme.Poppins
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.secure

@Preview(showSystemUi = true)
@Composable
fun TAScreen5(
    pv: PaddingValues,
    onClick: () -> Unit,
    onBackClick: () -> Unit
) {
    val viewModel: TradingAccountViewModel = koinViewModel()
    val state by viewModel.bankDetailModel.collectAsStateWithLifecycle()

    val accountList = listOf("SB", "CB", "NE", "NO")
    val paymentModeList =
        listOf("01 = Cheque", "02 = Direct Credit", "03 = ECS", "04 = NEFT", "05 = RTGS")

    Column(modifier = Modifier.fillMaxSize()) {

        BackHeader(
            "Trading account",
            showBack = true,
            onBackClick = onBackClick
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        "Bank Details",
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Text(
                        "Link your bank account for trading transactions",
                        fontFamily = Poppins,
                        fontSize = 14.sp,
                        color = Color(0xff4A5565)
                    )
                }
            }

            item {
                SecureNote()
            }

            item {
                GenericDropDownField(
                    value = state.accountType,
                    onValueChange = viewModel::onAccountTypeChange,
                    placeHolder = "Account Type",
                    mandatory = true,
                    label = "Account Type",
                    modifier = Modifier.fillMaxWidth(),
                    list = accountList
                )
            }

            item {
                OnBoardingTextField(
                    value = state.accountNumber,
                    onValueChange = viewModel::onAccountNumberChange,
                    placeHolder = "Enter Account Number",
                    label = "Account Number",
                    mandatory = true,
                    keyboardType = KeyboardType.Number
                )
            }

            item {
                OnBoardingTextField(
                    value = state.ifscCode,
                    onValueChange = viewModel::onIFSCchange,
                    placeHolder = "HDFC0001234",
                    label = "IFSC Code",
                    mandatory = true
                )
            }

            item {
                OnBoardingTextField(
                    value = state.microNumber,
                    onValueChange = viewModel::onMicroNumberChange,
                    label = "Micro Number (Optional)",
                    placeHolder = "Enter Micro Number",
                    mandatory = false
                )
            }

            item {
                GenericDropDownField(
                    value = state.paymentMethod,
                    onValueChange = viewModel::onPaymentModeChange,
                    label = "Payment Mode",
                    placeHolder = "Payment Mode",
                    mandatory = true,
                    modifier = Modifier.fillMaxWidth(),
                    list = paymentModeList
                )
            }

            item {
                AddMoreNominiee("Add More Account")
            }

            item {
                Spacer(modifier = Modifier.height(pv.calculateBottomPadding()))
            }
        }

        NextButtonFooter(
            onClick = onClick,
            pv = pv,
            value = "Next",
            enabled = true
        )
    }
}

@Composable
fun SecureNote() {
    Box(
        modifier = Modifier.fillMaxWidth()
            .border(0.7.dp, color = Color(0xffB9F8CF), shape = RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp)).background(color = Color(0xffF0FDF4)).padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.secure),
                contentDescription = "security",
                tint = Color(0xff00A63E)
            )

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(
                                color = Color(0xff016630),
                                fontWeight = FontWeight.SemiBold,
                                fontFamily = Poppins
                            )
                        ) { append("Secure & Encrypted:") }



                        withStyle(
                            style = SpanStyle(
                                color = Color(0xff016630),
                                fontFamily = Poppins
                            )
                        ) {
                            append(
                                "Your bank details are encrypted and stored securely",
                            )
                        }
                    }
                )
            }
        }
    }
}
