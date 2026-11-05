package org.sharad.velvetinvestment.features.fd.ui.compose

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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.appGreen
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.features.fd.domain.model.FDDetailsDomain
import org.sharad.velvetinvestment.features.fd.domain.model.PayoutType
import org.sharad.velvetinvestment.features.fd.domain.model.FDTenureDomain
import org.sharad.velvetinvestment.features.fd.ui.viewmodels.SetInvestmentDetailsEvent
import org.sharad.velvetinvestment.features.fd.ui.viewmodels.SetInvestmentDetailsUiState
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.features.fd.domain.utils.calculateMaturity
import org.sharad.velvetinvestment.shared.compose.MoneyTextField
import org.sharad.velvetinvestment.shared.compose.NextButtonFooter
import org.sharad.velvetinvestment.shared.DropDownSelector
import org.sharad.velvetinvestment.shared.UiStateContainer
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.ShadowCard
import org.sharad.velvetinvestment.utils.formatMoneyAfterL
import org.sharad.velvetinvestment.shared.theme.Poppins
import org.sharad.velvetinvestment.shared.theme.subHeading
import org.sharad.velvetinvestment.shared.theme.titlesStyle
import org.sharad.velvetinvestment.utils.trimDoubleTo
import org.sharad.velvetinvestment.utils.withInterRupee

/** Velvet's "Set Investment Details" form on JN's [SetInvestmentDetailsUiState]. */
@Composable
fun FDPurchaseScreenRoot(
    state: SetInvestmentDetailsUiState,
    onEvent: (SetInvestmentDetailsEvent) -> Unit,
) {
    Box(modifier = Modifier.fillMaxSize().background(Color.White).imePadding())
    {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            BackHeader("Set Investment Details", showBack = true, onBack = { onEvent(SetInvestmentDetailsEvent.OnBackClicked) })

            UiStateContainer(
                uiState = state.toUiState(),
                onRetry = { onEvent(SetInvestmentDetailsEvent.LoadDetails) },
                modifier = Modifier.weight(1f).fillMaxSize()
            ) { details ->
                FDPurchaseScreen(
                    state = state,
                    details = details,
                    onAmountChange = { onEvent(SetInvestmentDetailsEvent.OnAmountChanged(it)) },
                    onTenureChange = { onEvent(SetInvestmentDetailsEvent.OnTenureChanged(it)) },
                    onFrequencyChange = { onEvent(SetInvestmentDetailsEvent.OnPayoutModeChanged(it)) },
                    onButtonClick = { onEvent(SetInvestmentDetailsEvent.OnContinueClicked) }
                )
            }
        }
    }
}

private fun SetInvestmentDetailsUiState.toUiState(): UiState<FDDetailsDomain> = when {
    isLoading -> UiState.Loading
    details != null -> UiState.Success(details)
    else -> UiState.Error(errorMessage ?: "Something went wrong")
}

@Composable
fun FDPurchaseScreen(
    state: SetInvestmentDetailsUiState,
    details: FDDetailsDomain,
    onAmountChange: (String) -> Unit,
    onTenureChange: (FDTenureDomain) -> Unit,
    onFrequencyChange: (PayoutType) -> Unit,
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
            item{ BankDetailsHeader(details = details) }

            item {
                MoneyTextField(
                    value = state.amount,
                    onValueChange = {value->
                        onAmountChange(value)
                    },
                    placeHolder = "Enter Valid Amount",
                    mandatory = false,
                    label = "Investment Amount",
                    showWarning = state.showError,
                    warningText = state.errorText
                )
            }
            item {
                DropDownSelector(
                    value = state.selectedPayoutMode.displayName,
                    onValueChange = {
                        onFrequencyChange(it)
                    },
                    placeHolder = "Select Payout Mode",
                    mandatory = false,
                    label = "Interest Payout Mode",
                    modifier = Modifier.fillMaxWidth(),
                    list = state.frequencies,
                    textConvertor = {
                        it.displayName
                    }
                )
            }

            item {
                DropDownSelector(
                    value = state.selectedTenure?.tenureLabel?:"",
                    onValueChange = {
                        onTenureChange(it)
                    },
                    placeHolder = if (state.isRefreshingRates) "Loading tenures…" else "Select Tenure",
                    mandatory = false,
                    label = "Select Tenure",
                    modifier = Modifier.fillMaxWidth(),
                    list = if (state.isRefreshingRates) emptyList() else state.availableTenures,
                    textConvertor = {
                        it.tenureLabel+ " (${it.interestRate.trimDoubleTo(1)}%)"
                    }
                )
            }


            item {
                state.selectedTenure?.let{
                    FDProjectedReturnsCard(it, state.amount.toLongOrNull() ?: 0, state.selectedPayoutMode)
                }
            }

            item {
                BlostemFooter()
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
            enabled = state.isButtonEnabled,
            loading = state.isPurchasing
        )
    }

}

@Composable
fun BankDetailsHeader(details: FDDetailsDomain) {
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
                            model = details.bankLogo,
                            contentDescription = null,
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(12.dp))
                        )

                        Spacer(Modifier.width(12.dp))

                        Column(modifier=Modifier.weight(1f)) {

                            Text(
                                text = details.bankName,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.SemiBold
                            )

                            RiskLevelIndicator(risk = details.riskLabel)
                        }

                    }
                }

            }
            Box(
                modifier = Modifier.fillMaxWidth()
                    .background(appGreen.copy(0.1f))
            ){
                Text(
                    text="HIGHEST INTEREST RATE ${details.maxInterestRate}% P.A.",
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
    amount: Long,
    payout: PayoutType,
) {

    // Same maths as the view model's summary (JN `calculateMaturity`), in Velvet's money format.
    val maturityAmount = remember(amount, tenure, payout) {
        calculateMaturity(
            principal = amount,
            rate = tenure.interestRate,
            days = tenure.tenureDays,
            frequency = payout
        )
    }

    val interestEarned = remember(maturityAmount, amount) {
        maturityAmount-amount
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
                    text = ("₹ " + formatMoneyAfterL(maturityAmount.toLong()))
                        .withInterRupee(),
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
                    text = ("+ ₹${formatMoneyAfterL(interestEarned.toLong())}")
                        .withInterRupee(),
                    style = titlesStyle,
                    color = appGreen
                )
            }
        }
    }
}