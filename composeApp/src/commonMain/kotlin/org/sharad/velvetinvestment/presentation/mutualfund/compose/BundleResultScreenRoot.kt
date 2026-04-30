package org.sharad.velvetinvestment.presentation.mutualfund.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.sharad.emify.core.ui.theme.shadowColor
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.domain.models.mutualfunds.BundledMutualFundDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.BundledMutualFundItemDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.InvestmentFrequency
import org.sharad.velvetinvestment.presentation.mutualfund.viewmodel.BundleCartUiState
import org.sharad.velvetinvestment.presentation.mutualfund.viewmodel.BundleResultViewModel
import org.sharad.velvetinvestment.presentation.mutualfund.viewmodel.SelectedReturnRatePeriod
import org.sharad.velvetinvestment.presentation.onboarding.compose.personaldetails.NextButtonFooter
import org.sharad.velvetinvestment.shared.AppDialogList
import org.sharad.velvetinvestment.shared.compose.AppButton
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.BarHeader
import org.sharad.velvetinvestment.shared.compose.ErrorScreen
import org.sharad.velvetinvestment.shared.compose.LoaderScreen
import org.sharad.velvetinvestment.utils.FundTypeSelector
import org.sharad.velvetinvestment.utils.LoadingState
import org.sharad.velvetinvestment.utils.SelectedFundType
import org.sharad.velvetinvestment.utils.theme.subHeading
import org.sharad.velvetinvestment.utils.theme.titlesStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BundleResultScreenRoot(
    bundleKey: String,
    heading: String,
    onBackClick: () -> Unit,
    onFundClick: (String) -> Unit
) {

    val viewModel: BundleResultViewModel = koinViewModel {
        parametersOf(bundleKey)
    }

    @OptIn(ExperimentalMaterial3Api::class)
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val uiState by viewModel.loadingState.collectAsStateWithLifecycle()
    val selectedYear by viewModel.selectedYear.collectAsStateWithLifecycle()
    val bundleData by viewModel.bundleData.collectAsStateWithLifecycle()
    val bundleCartState by viewModel.bundleCartState.collectAsStateWithLifecycle()
    val showBottomSheet by viewModel.showCartSheet.collectAsStateWithLifecycle()


    when (uiState) {

        is LoadingState.Error -> {
            ErrorScreen(
                errorMessage = (uiState as LoadingState.Error).error,
                onRetryClick = viewModel::loadBundleFunds
            )
        }

        LoadingState.Loading -> {
            LoaderScreen()
        }

        LoadingState.Success -> {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    BackHeader(
                        heading = heading,
                        showBack = true,
                        onBackClick = onBackClick
                    )
                },
                bottomBar = {
                    NextButtonFooter(
                        onClick = viewModel::showCartSheet,
                        pv = PaddingValues(0.dp),
                        value = "Add To Cart"
                    )
                },
                containerColor = Color.White
            ) {pv->
                BundleResultScreen(
                    data = bundleData,
                    selectedYear = selectedYear,
                    toggleRateYear = viewModel::cycleReturnRatePeriod,
                    onFundClick = onFundClick,
                    modifier= Modifier.padding(pv)
                )
            }
            if (bundleCartState.frequencyDropDownExpanded) {
                AppDialogList(
                    items = InvestmentFrequency.entries,
                    textFormatter = { it.label },
                    onSelect = {
                        viewModel.onBundleFrequencyChange(it)
                    },
                    onDismiss = {
                        viewModel.dismissFrequencyDropDown()
                    }
                )
            }

            if (bundleCartState.sipDayDropDownExpanded) {
                AppDialogList(
                    items = (1..28).toList(),
                    textFormatter = { it.toString() },
                    onSelect = {
                        viewModel.onSipDaySelected(it)
                    },
                    onDismiss = {
                        viewModel.dismissSipDayDropDown()
                    }
                )
            }

            if (showBottomSheet) {
                BundleCartPopup(
                    sheetState = sheetState,
                    onDismiss = {
                        viewModel.hideCartSheet()
                    },
                    title = "Add Bundle To Cart",
                    state = bundleCartState,
                    onAmountChange = viewModel::onBundleAmountChange,
                    onAddClick = viewModel::addBundleToCart,
                    showFrequencyDropDown = viewModel::showFrequencyDropDown,
                    showSipDayDropDown = viewModel::showSipDayDropDown
                )
            }

        }
    }
}

@Composable
fun BundleResultScreen(
    data: BundledMutualFundDomain?,
    selectedYear: SelectedReturnRatePeriod,
    toggleRateYear: () -> Unit,
    onFundClick: (String) -> Unit,
    modifier: Modifier
) {

    val funds = data?.mutualFunds.orEmpty()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 6.dp)
    ) {

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                BarHeader(
                    heading = data?.categoryName.orEmpty()
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${funds.size} Funds",
                    style = titlesStyle,
                    color = titleColor
                )
            }
        }

        items(funds, key = { it.id }) { fund ->

            BundleMutualFundListCard(
                fund = fund,
                selectedYear = selectedYear,
                onClick = {
                    onFundClick(fund.id)
                }
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .padding(horizontal = 16.dp)
                    .clip(CircleShape)
                    .background(shadowColor)
            )
        }
    }
}

@Composable
fun BundleMutualFundListCard(
    fund: BundledMutualFundItemDomain,
    selectedYear: SelectedReturnRatePeriod,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        MutualFundIcon(
            schemeName = fund.scheme_name,
            size = 38.dp
        )

        Column(modifier = Modifier.weight(1f)) {

            Text(
                text = fund.scheme_name,
                style = subHeading,
                color = Color.Black,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "${fund.scheme_type} • ${fund.risk_name}",
                style = titlesStyle,
                color = titleColor,
                maxLines = 1
            )
        }

        Column(
            horizontalAlignment = Alignment.End
        ) {

            Text(
                text = "${fund.allocation_percentage}%",
                style = subHeading,
                color = Color.Black
            )

            Text(
                text = "Allocation",
                style = titlesStyle,
                color = titleColor
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BundleCartPopup(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    title: String,
    state: BundleCartUiState,
    onAmountChange: (String) -> Unit,
    onAddClick: () -> Unit,
    showFrequencyDropDown: () -> Unit,
    showSipDayDropDown: () -> Unit,
) {

    val fundType by FundTypeSelector.fundType.collectAsStateWithLifecycle()

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        modifier = Modifier.fillMaxWidth(),
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Text(
                        text = title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.Black,
                        modifier = Modifier.padding(top = 8.dp),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    FundBadge(
                        text = when (fundType) {
                            SelectedFundType.SIP -> "SIP"
                            SelectedFundType.LUMSUM -> "Lump Sum"
                        }
                    )
                }

                Text(
                    text = "Bundle Investment",
                    style = titlesStyle,
                    color = Color.Black
                )
            }

            when (fundType) {

                SelectedFundType.LUMSUM -> {
                    LumpSumCart(
                        amount = state.amount,
                        minAmount = 500,
                        loading = state.loading,
                        onChipClick = { onAmountChange(it.toString()) },
                        onAmountChange = onAmountChange,
                        onAddClick = onAddClick
                    )
                }

                SelectedFundType.SIP -> {
                    SIPBundleCart(
                        amount = state.amount,
                        minAmount = 500,
                        loading = state.loading,
                        frequency = state.selectedFrequency?.label,
                        sipDay = state.sipDay?.toString(),
                        onAmountChange = onAmountChange,
                        onChipClick = { onAmountChange(it.toString()) },
                        onAddClick = onAddClick,
                        showFrequencyDropDown = showFrequencyDropDown,
                        showSipDayDropDown = showSipDayDropDown
                    )
                }
            }
        }
    }
}

@Composable
fun SIPBundleCart(
    amount: Long?,
    minAmount: Long,
    loading: Boolean,
    frequency: String?,
    sipDay: String?,
    onAmountChange: (String) -> Unit,
    onChipClick: (Long) -> Unit,
    onAddClick: () -> Unit,
    showFrequencyDropDown: () -> Unit,
    showSipDayDropDown: () -> Unit,
) {

    val chips = listOf<Long>(500, 1000, 2500, 5000, 10000)

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        ShadowlessTextField(
            value = amount?.toString() ?: "",
            onValueChange = onAmountChange,
            placeHolder = "Enter amount",
            label = "Investment Amount"
        )

        AmountChipsGrid(
            amounts = chips,
            currentAmount = amount,
            onChipClick = onChipClick
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            DropDownField(
                text = frequency ?: "",
                placeholder = "Select Frequency",
                onClick = showFrequencyDropDown,
                label = "Frequency",
                modifier = Modifier.weight(1f)
            )

            DropDownField(
                text = sipDay ?: "",
                placeholder = "Select SIP Day",
                onClick = showSipDayDropDown,
                label = "SIP Day",
                modifier = Modifier.weight(1f)
            )
        }

        AppButton(
            modifier = Modifier.fillMaxWidth(),
            text = "Start SIP",
            loading = loading,
            enabled = amount != null,
            onClick = onAddClick
        )
    }
}