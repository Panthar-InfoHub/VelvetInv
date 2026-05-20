package org.sharad.velvetinvestment.presentation.portfolio.compose

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.presentation.onboarding.compose.personaldetails.NextButtonFooter
import org.sharad.velvetinvestment.presentation.portfolio.viewmodel.MFPortfolioDetailsViewModel
import org.sharad.velvetinvestment.presentation.portfolio.viewmodel.MFPortfolioSideEffects
import org.sharad.velvetinvestment.shared.Navigation.Route
import org.sharad.velvetinvestment.shared.compose.ErrorScreen
import org.sharad.velvetinvestment.shared.compose.LoaderScreen
import org.sharad.velvetinvestment.shared.compose.ShadowCard
import org.sharad.velvetinvestment.shared.compose.SidedBackHeader
import org.sharad.velvetinvestment.shared.rememberBrowserReturnLauncher
import org.sharad.velvetinvestment.utils.LoadingState
import org.sharad.velvetinvestment.shared.theme.subHeading
import org.sharad.velvetinvestment.shared.theme.titlesStyle
import org.sharad.velvetinvestment.utils.AppEventsController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MFPortfolioDetailsScreen(
    onBackClick: () -> Unit,
    onCancelClick: (String) -> Unit,
    pv: PaddingValues,
    data: Route.SIPPortfolioDetails,
) {

    val viewModel: MFPortfolioDetailsViewModel = koinViewModel()
    val screenState by viewModel.loadingState.collectAsStateWithLifecycle()
    val showRedemptionSheet by viewModel.showRedemptionSheet.collectAsStateWithLifecycle()
    val selectedRedemptionType by viewModel.selectedRedemptionType.collectAsStateWithLifecycle()
    val selectedInputType by viewModel.selectedInputType.collectAsStateWithLifecycle()
    val redemptionUnits by viewModel.redemptionUnits.collectAsStateWithLifecycle()
    val redemptionAmount by viewModel.redemptionAmount.collectAsStateWithLifecycle()
    val isSubmitting by viewModel.isSubmitting.collectAsStateWithLifecycle()
    val browserLauncher = rememberBrowserReturnLauncher()
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.sideEffects.collect {
            when (it) {
                is MFPortfolioSideEffects.openRedeemptionUrl -> {
                    browserLauncher.launch(it.url) {
                        scope.launch {
                            AppEventsController.sendPortfolioRefreshEvent()
                            onBackClick()
                        }
                    }
                }
            }
        }
    }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        SidedBackHeader(
            heading = "Withdraw Funds",
            showBack = true,
            onBackClick = onBackClick,
            textIcon = null
        )

        Box(modifier = Modifier.weight(1f)) {
            when (screenState) {
                is LoadingState.Error -> {
                    ErrorScreen((screenState as LoadingState.Error).error, onRetryClick = {
                        viewModel.submitRedemption(
                            schemeId = data.id,
                            folioNo = data.folio
                        )
                    })
                }

                LoadingState.Loading -> {
                    LoaderScreen()
                }

                LoadingState.Success -> {
                    SIPDetailsLoadedScreen(
                        data = data,
                        pv = pv,
                        onWithdrawClick = { viewModel.onShowRedemptionSheet() }
                    )
                }
            }
        }
    }

    if (showRedemptionSheet) {
        RedemptionBottomSheet(
            sheetState = sheetState,
            onDismiss = { viewModel.onDismissRedemptionSheet() },
            schemeId = data.id,
            folioNo = data.folio,
            selectedRedemptionType = selectedRedemptionType,
            selectedInputType = selectedInputType,
            redemptionUnits = redemptionUnits,
            redemptionAmount = redemptionAmount,
            maxUnits = data.balanceUnits,
            maxAmount = data.amount,
            onRedemptionTypeChange = viewModel::onRedemptionTypeChange,
            onInputTypeChange = viewModel::onInputTypeChange,
            onUnitsChange = viewModel::onUnitsChange,
            onAmountChange = viewModel::onAmountChange,
            onSubmit = {
                viewModel.submitRedemption(
                    schemeId = data.id,
                    folioNo = data.folio
                )
            },
            loading = isSubmitting
        )
    }
}

@Composable
fun SIPDetailsLoadedScreen(
    data: Route.SIPPortfolioDetails,
    pv: PaddingValues,
    onWithdrawClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier) }
            item { SIPDetailsCard(data) }
            item { Spacer(Modifier.height(16.dp)) }
        }

        NextButtonFooter(
            value = "Withdraw",
            onClick = onWithdrawClick,
            pv = pv,
        )
    }
}

@Composable
fun SIPDetailsCard(data: Route.SIPPortfolioDetails) {
    ShadowCard {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = data.title,
                        color = Color.Black,
                        style = subHeading,
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text = "₹${data.amount}",
                        color = Color.Black,
                        style = subHeading,
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }

                Text(
                    text = data.category,
                    color = titleColor,
                    style = titlesStyle
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                SIPInfoRow("Folio", data.folio)
                SIPInfoRow("Start Date", data.startDate)
                SIPInfoRow("Return %", data.returnPercentage)
                SIPInfoRow("Return Amount", "₹${data.returnAmount}")
                SIPInfoRow("XIRR", data.xirr)
                SIPInfoRow("Current NAV", data.currentNav.toString())
                SIPInfoRow("Average NAV", data.avgNav.toString())
                SIPInfoRow("Balance Units", data.balanceUnits.toString())
                SIPInfoRow("Investment Type", if (data.isSip) "SIP" else "Lumpsum")
            }
        }
    }
}

@Composable
fun SIPInfoRow(
    title: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            color = titleColor,
            style = titlesStyle
        )

        Text(
            text = value,
            color = Color.Black,
            style = subHeading
        )
    }
}