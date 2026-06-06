package org.sharad.velvetinvestment.presentation.tradingaccount.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.velvetinvestment.domain.models.tradingaccount.Data
import org.sharad.velvetinvestment.presentation.onboarding.compose.OnBoardingTextField
import org.sharad.velvetinvestment.presentation.onboarding.compose.personaldetails.NextButtonFooter
import org.sharad.velvetinvestment.presentation.tradingaccount.viewmodel.TradingAccountViewModel
import org.sharad.velvetinvestment.shared.DropDownSelector
import org.sharad.velvetinvestment.shared.UiStateContainer
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.theme.Poppins
import org.sharad.velvetinvestment.utils.tradingaccount.AccountType
import org.sharad.velvetinvestment.utils.tradingaccount.DividendPayMode
import org.sharad.velvetinvestment.utils.tradingaccount.YesNo
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.secure


@Composable
fun TradingAccountBankDetailsScreen(
    pv: PaddingValues,
    onClick: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: TradingAccountViewModel
) {
    val state by viewModel.formState.collectAsStateWithLifecycle()
    val visibleAccounts by viewModel.visibleBankAccounts.collectAsStateWithLifecycle()
    val buttonEnabled by viewModel.bankScreenButtonEnabled.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {

        BackHeader(
            "Trading account",
            showBack = true,
            onBackClick = { onBackClick() }
        )

        UiStateContainer(
            uiState = state,
            onRetry = { viewModel.getUserData() },
            modifier = Modifier.fillMaxSize()
        ) { uiData ->
            val data = uiData.data
            Column(modifier = Modifier.fillMaxSize()) {
                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    contentPadding = PaddingValues(bottom = 20.dp)
                ) {
                        item {
                            SecureNote()
                        }

                        item {
                            DropDownSelector(
                                value = DividendPayMode.getDisplayName(data.div_pay_mode),
                                onValueChange = { viewModel.onDivPayModeChange(it.code) },
                                label = "Dividend Payment Mode",
                                placeHolder = "Payment Mode",
                                mandatory = true,
                                modifier = Modifier.fillMaxWidth(),
                                list = DividendPayMode.entries,
                                textConvertor = {
                                    it.displayName
                                }
                            )
                        }
                        items(visibleAccounts) { index ->

                            BankAccountSection(
                                index = index,
                                data = data,
                                viewModel = viewModel,
                                removable = index != 1,
                                onRemove = {
                                    viewModel.removeBankAccount(index)
                                }
                            )
                        }
                        if (visibleAccounts.size < 5) {
                            item {
                                Text(
                                    text = "+ Add Another Bank Account",
                                    modifier = Modifier.clickable {
                                        viewModel.addBankAccount()
                                    }
                                )
                            }
                        }
                    }

                    NextButtonFooter(
                        onClick = onClick,
                        pv = pv,
                        value = "Next",
                        enabled = buttonEnabled
                    )
                }
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

@Composable
fun BankAccountSection(
    index: Int,
    data: Data,
    viewModel: TradingAccountViewModel,
    removable: Boolean,
    onRemove: () -> Unit
){
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        AccountHeader(
            heading = "Bank Account $index",
            showRemove = removable,
            onRemoveClick = onRemove
        )

        DropDownSelector(
            value = AccountType.getDisplayName(
                viewModel.getAccountType(index, data)
            ),
            onValueChange = {
                viewModel.onAccountTypeChange(index, it.code)
            },
            placeHolder = "Account Type",
            mandatory = true,
            label = "Account Type",
            list = AccountType.entries,
            textConvertor = { it.displayName }
        )

        OnBoardingTextField(
            value = viewModel.getAccountNumber(index, data),
            onValueChange = {
                viewModel.onAccountNumberChange(index, it)
            },
            placeHolder = "Enter Account Number",
            label = "Account Number",
            mandatory = true,
            keyboardType = KeyboardType.Number
        )

        OnBoardingTextField(
            value = viewModel.getIfsc(index, data),
            onValueChange = {
                viewModel.onIfscChange(index, it)
            },
            placeHolder = "IFSC",
            label = "IFSC",
            mandatory = true,
            keyboardType = KeyboardType.Text
        )

        OnBoardingTextField(
            value = viewModel.getMicr(index, data),
            onValueChange = {
                viewModel.onMicrChange(index, it)
            },
            placeHolder = "MICR",
            label = "MICR",
            mandatory = false,
            keyboardType = KeyboardType.Number
        )

        DropDownSelector(
            value = YesNo.displayNameFromCode(
                viewModel.getDefaultBank(index, data)
            ),
            onValueChange = {
                viewModel.onDefaultBankChange(index, it.code)
            },
            placeHolder = "Y/N",
            mandatory = true,
            label = "Default Bank",
            list = YesNo.entries,
            textConvertor = { it.displayName }
        )
    }
}

@Composable
private fun AccountHeader(
    modifier: Modifier = Modifier,
    heading: String,
    showRemove: Boolean = false,
    onRemoveClick: () -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .width(5.dp)
                    .clip(CircleShape)
                    .background(Secondary, CircleShape)
            )

            Text(
                text = heading,
                style = MaterialTheme.typography.headlineSmall,
                color = Primary,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )

            if (showRemove) {
                Text(
                    text = "Remove",
                    color = Color.Red,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = onRemoveClick
                        )
                )
            }
        }
    }
}