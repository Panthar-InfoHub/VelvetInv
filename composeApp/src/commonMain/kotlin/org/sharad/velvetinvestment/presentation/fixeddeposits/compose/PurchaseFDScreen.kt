package org.sharad.velvetinvestment.presentation.fixeddeposits.compose

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.appGreen
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.domain.models.fd.FDTenureDomain
import org.sharad.velvetinvestment.presentation.fixeddeposits.uimodels.FDPurchaseUiModel
import org.sharad.velvetinvestment.presentation.fixeddeposits.viewmodel.FDPurchaseViewModel
import org.sharad.velvetinvestment.presentation.fixeddeposits.viewmodel.calculateInterestEarned
import org.sharad.velvetinvestment.presentation.fixeddeposits.viewmodel.calculateMaturityAmount
import org.sharad.velvetinvestment.presentation.onboarding.compose.financialflow.MoneyTextField
import org.sharad.velvetinvestment.presentation.onboarding.compose.personaldetails.NextButtonFooter
import org.sharad.velvetinvestment.shared.DropDownSelector
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.ErrorScreen
import org.sharad.velvetinvestment.shared.compose.LoaderScreen
import org.sharad.velvetinvestment.shared.compose.ShadowCard
import org.sharad.velvetinvestment.utils.LoadingState
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.formatMoneyAfterL
import org.sharad.velvetinvestment.utils.theme.Poppins
import org.sharad.velvetinvestment.utils.theme.subHeading
import org.sharad.velvetinvestment.utils.theme.titlesStyle

@Composable
fun FDPurchaseScreenRoot(
    onBackClick: () -> Unit,
    pv: PaddingValues,
    id: String,
) {


    val viewModel: FDPurchaseViewModel = koinViewModel{parametersOf(id)}
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val buttonEnabled by viewModel.buttonEnabled.collectAsStateWithLifecycle()


    Box(modifier = Modifier.fillMaxSize().background(Color.White))
    {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            BackHeader("Set Investment Details", showBack = true, onBackClick = onBackClick)

            Box(
                modifier = Modifier.weight(1f)
                    .fillMaxSize()
            ) {
                when (uiState) {
                    is UiState.Error -> {
                        ErrorScreen((uiState as UiState.Error).message, onRetryClick = {})

                    }
                    UiState.Loading ->{
                        LoaderScreen()
                    }
                    is UiState.Success-> {
                        val data= (uiState as UiState.Success<FDPurchaseUiModel>).data
                        FDPurchaseScreen(
                            data=data,
                            onAmountChange=viewModel::updateAmount,
                            onTenureChange=viewModel::updateTenure,
                            onFrequencyChange=viewModel::updateFrequency,
                            pv=pv,
                            buttonEnabled = buttonEnabled,
                            onButtonClick = { viewModel.purchaseFD(){
                                onBackClick()
                            } }
                        )
                    }
                }
            }

        }
    }
}

@Composable
fun FDPurchaseScreen(
    data: FDPurchaseUiModel,
    onAmountChange: (Long) -> Unit,
    onTenureChange: (FDTenureDomain) -> Unit,
    onFrequencyChange: (String) -> Unit,
    pv: PaddingValues,
    buttonEnabled: Boolean,
    onButtonClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ){
        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxSize().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(top = 16.dp)
        ) {
            item{ BankDetailsHeader(data = data) }

            item {
                MoneyTextField(
                    value = data.amount.toString(),
                    onValueChange = {
                        val amount= it.toLongOrNull()
                        amount?.let {
                            onAmountChange(it)
                        }
                    },
                    placeHolder = "Enter Valid Amount",
                    mandatory = false,
                    label = "Investment Amount"
                )
            }
            item {
                DropDownSelector(
                    value = data.selectedFrequency ?: "",
                    onValueChange = {
                        onFrequencyChange(it)
                    },
                    placeHolder = "Select Payout Mode",
                    mandatory = false,
                    label = "Interest Payout Mode",
                    modifier = Modifier.fillMaxWidth(),
                    list = data.frequencies,
                    textConvertor = {
                        it.toLowerCase(Locale.current).capitalize(Locale.current)
                    }
                )
            }

            item {
                DropDownSelector(
                    value = data.selectedTenure?.tenureLabel?:"",
                    onValueChange = {
                        onTenureChange(it)
                    },
                    placeHolder = if (data.selectedFrequency==null) "Select Payout Mode" else "Select Tenure",
                    mandatory = false,
                    label = "Select Tenure",
                    modifier = Modifier.fillMaxWidth(),
                    list = data.tenures,
                    textConvertor = {
                        it.tenureLabel
                    }
                )
            }


            item {
                data.selectedTenure?.let{ FDProjectedReturnsCard(data.selectedTenure, data.amount) }
            }


            item {
                Spacer(Modifier.height(8.dp))
            }

        }


        NextButtonFooter(
            value = "Proceed to Payment",
            onClick = {
                onButtonClick()
            },
            enabled = buttonEnabled,
            pv = pv,
            loading = data.loading
        )
    }

}

@Composable
fun BankDetailsHeader(data: FDPurchaseUiModel) {
    ShadowCard {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier.fillMaxWidth()
            ){

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Row(verticalAlignment = Alignment.CenterVertically) {

                        AsyncImage(
                            model = data.bankLogo,
                            contentDescription = null,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )

                        Spacer(Modifier.width(12.dp))

                        Column(modifier=Modifier.weight(1f)) {

                            Text(
                                text = data.bankName,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.SemiBold
                            )

                            RiskLevelIndicator(risk = data.riskLabel)
                        }

                    }
                }

            }
            Box(
                modifier = Modifier.fillMaxWidth()
                    .background(appGreen.copy(0.1f))
            ){
                Text(
                    text="HIGHEST INTEREST RATE ${data.highestInterestRate}% P.A.",
                    color = appGreen,
                    fontFamily = Poppins,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
fun FDProjectedReturnsCard(
    tenure: FDTenureDomain,
    amount: Long
) {


    val maturityAmount = remember(amount, tenure) {
        calculateMaturityAmount(
            amount = amount,
            rate = tenure.interestRate,
            days = tenure.tenureDays
        )
    }

    val interestEarned = remember(maturityAmount, amount) {
        calculateInterestEarned(maturityAmount, amount)
    }

    ShadowCard(
        modifier = Modifier
            .fillMaxWidth()
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {

            Text(
                text = "PROJECTED RETURNS",
                style = subHeading,
                color = titleColor
            )

            Spacer(Modifier.height(12.dp))

            // 🔹 Maturity Section
            Text(
                text = "Maturity Amount",
                style = titlesStyle,
                color = titleColor
            )

            Spacer(Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = "₹ "+ formatMoneyAfterL(maturityAmount),
                    style = MaterialTheme.typography.headlineMedium,
                    color = Secondary
                )
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(appGreen.copy(alpha = 0.1f))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${tenure.interestRate}% p.a.",
                        style = titlesStyle.copy(fontSize = 12.sp),
                        color = appGreen
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            HorizontalDivider(color = titleColor.copy(alpha = 0.2f))

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = "Total Interest Earned",
                    style = titlesStyle,
                    color = titleColor
                )

                Text(
                    text = "+ ₹${formatMoneyAfterL(interestEarned)}",
                    style = titlesStyle,
                    color = appGreen
                )
            }
        }
    }
}