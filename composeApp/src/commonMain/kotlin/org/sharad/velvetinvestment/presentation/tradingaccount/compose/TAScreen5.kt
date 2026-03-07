package org.sharad.velvetinvestment.presentation.tradingaccount.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.emify.core.ui.theme.greenColor
import org.sharad.velvetinvestment.presentation.onboarding.compose.OnBoardingTextField
import org.sharad.velvetinvestment.presentation.tradingaccount.viewmodel.TradingAccountViewModel
import org.sharad.velvetinvestment.shared.compose.GenericDropDownField
import org.sharad.velvetinvestment.utils.theme.Poppins
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.secure

@Preview(showSystemUi = true)
@Composable
fun TAScreen5() {
    val viewmodel: TradingAccountViewModel = koinViewModel()
    val state by viewmodel.bankDetailModel.collectAsStateWithLifecycle()

    val AccountList = listOf("SB", "CB", "NE", "NO")
    val PaymentMode =
        listOf("01 = Cheque", "02 = Direct Credit", "03 = ECS", "04 = NEFT", "05 = RTGS")

    LazyColumn(
        modifier = Modifier.fillMaxWidth().padding(16.dp).safeDrawingPadding()
            .navigationBarsPadding(), verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    "Bank Details",
                    fontSize = 24.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
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
                onValueChange = viewmodel::onAccountTypeChange,
                placeHolder = "Account Type",
                mandatory = true,
                label = "Account Type",
                list = AccountList
            )
        }
        item {
            OnBoardingTextField(
                value = state.accountNumber,
                onValueChange = viewmodel::onAccountNumberChange,
                placeHolder = "Enter Account Number",
                label = "Account Number",
                mandatory = true,
                keyboardType = KeyboardType.Number
            )
        }
        item {
            OnBoardingTextField(
                value = state.ifscCode,
                onValueChange = viewmodel::onIFSCchange,
                placeHolder = "HDFC0001234",
                label = "IFSC Code",
                mandatory = true,
            )


        }
        item {
            OnBoardingTextField(
                value = state.microNumber,
                onValueChange = viewmodel::onMicroNumberChange,
                label = "Micro Number (Optional)",
                placeHolder = "Enter Micro Number",
                mandatory = false,
            )
        }
        item {
            GenericDropDownField(
                value = state.paymentMethod,
                onValueChange = viewmodel::onPaymentModeChange,
                label = "Payment Mode",
                placeHolder = " Payment Mode",
                mandatory = true,
                list = PaymentMode
            )
        }

        item {
            AddMoreNominiee("Add More Account")
        }
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
