package org.sharad.velvetinvestment.features.portfolio.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.sharad.velvetinvestment.features.portfolio.domain.models.FolioFundDomain
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.TextGray
import org.sharad.emify.core.ui.theme.appGreen
import org.sharad.emify.core.ui.theme.appRed
import org.sharad.velvetinvestment.shared.compose.MutualFundIcon
import org.sharad.velvetinvestment.features.portfolio.ui.viewmodel.MFPortfolioDetailsViewModel
import org.sharad.velvetinvestment.features.portfolio.ui.viewmodel.MFPortfolioSideEffects
import org.sharad.velvetinvestment.shared.Navigation.Route
import org.sharad.velvetinvestment.shared.compose.ErrorScreen
import org.sharad.velvetinvestment.shared.compose.LoaderScreen
import org.sharad.velvetinvestment.shared.compose.ShadowCard
import org.sharad.velvetinvestment.shared.compose.SidedBackHeader
import org.sharad.velvetinvestment.shared.theme.LocalVelvetShapes
import org.sharad.velvetinvestment.shared.theme.Poppins
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import org.sharad.velvetinvestment.shared.theme.subHeadingMedium
import org.sharad.velvetinvestment.shared.theme.titlesStyle
import org.sharad.velvetinvestment.utils.AppEventsController
import org.sharad.velvetinvestment.utils.LoadingState
import org.sharad.velvetinvestment.utils.formatWithCommas
import org.sharad.velvetinvestment.utils.formatMoneyAfterLWithPaise
import org.sharad.velvetinvestment.utils.withInterRupee
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.document_kyc_icon
import velvet.composeapp.generated.resources.download_ic
import velvet.composeapp.generated.resources.ic_graph
import velvet.composeapp.generated.resources.ic_jagged_arrow
import velvet.composeapp.generated.resources.ic_menu
import velvet.composeapp.generated.resources.ic_more_vert
import velvet.composeapp.generated.resources.ic_percent
import velvet.composeapp.generated.resources.icon_callender
import velvet.composeapp.generated.resources.icon_clock
import velvet.composeapp.generated.resources.icon_mf
import velvet.composeapp.generated.resources.info_icon
import velvet.composeapp.generated.resources.rupeesign

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MFPortfolioDetailsScreen(
    onBackClick: () -> Unit,
    data: Route.SIPPortfolioDetails,
    /**
     * JN: redeeming is its own screen ([Route.Redeem]), reached with the figures this one has —
     * the folio call's when it has loaded, else the route's.
     */
    onRedeemClick: (Route.SIPPortfolioDetails) -> Unit,
    /** After a cancellation: back to the portfolio, which the refresh event reloads. */
    onOrderCancelled: () -> Unit,
) {

    val viewModel: MFPortfolioDetailsViewModel = koinViewModel(
        parameters = { parametersOf(data.actualFolio.ifBlank { data.folio }, data.holdingId) }
    )
    val screenState by viewModel.loadingState.collectAsStateWithLifecycle()
    val folioFund by viewModel.folioFund.collectAsStateWithLifecycle()
    // The route's figures until the folio call answers, then the holding as it reports it.
    val details = remember(data, folioFund) { folioFund?.let(data::withFolioFund) ?: data }
    val soaDownloading by viewModel.soaDownloading.collectAsStateWithLifecycle()
    val showCancelSheet by viewModel.showCancelSheet.collectAsStateWithLifecycle()
    val selectedCancelReason by viewModel.selectedCancelReason.collectAsStateWithLifecycle()
    val isSubmitting by viewModel.isSubmitting.collectAsStateWithLifecycle()
    val cancelSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.sideEffects.collect {
            when (it) {
                MFPortfolioSideEffects.OrderCancelled -> {
                    scope.launch {
                        AppEventsController.sendPortfolioRefreshEvent()
                        onOrderCancelled()
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .imePadding()
    ) {
        SidedBackHeader(
            heading = "Withdraw Fund",
            showBack = true,
            onBackClick = onBackClick,
            trailingContent = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        modifier = Modifier.clip(RoundedCornerShape(8.dp))
                        .background(Secondary.copy(alpha = 0.1f))
                        .clickable { viewModel.downloadSOA(details.folio) }
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = "SOA", style = TextStyle(
                                fontFamily = Poppins,
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp,
                                color = Secondary
                            )
                        )
                        if (soaDownloading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(12.dp), strokeWidth = 1.dp, color = Secondary
                            )
                        } else {
                            Icon(
                                painter = painterResource(Res.drawable.download_ic),
                                contentDescription = null,
                                modifier = Modifier.size(14.dp),
                                tint = Secondary
                            )
                        }
                    }

                    // JN: the order's actions live behind the header's overflow menu, shown once
                    // the screen has loaded.
                    if (screenState == LoadingState.Success) {
                        OrderActionsMenu(
                            isSip = details.isSip,
                            // Cancelling is keyed on the plan; without one, or once it is
                            // cancelled, there is nothing to cancel.
                            canCancel = details.planId.isNotBlank() &&
                                !details.status.equals("CANCELLED", ignoreCase = true),
                            onRedeem = { onRedeemClick(details) },
                            onCancelClick = viewModel::onShowCancelSheet
                        )
                    }
                }
            })

        Box(modifier = Modifier.weight(1f)) {
            when (screenState) {
                is LoadingState.Error -> {
                    ErrorScreen(
                        (screenState as LoadingState.Error).error,
                        onRetryClick = viewModel::loadDetails
                    )
                }

                LoadingState.Loading -> {
                    LoaderScreen()
                }

                LoadingState.Success -> {
                    SIPDetailsLoadedScreen(data = details)
                }
            }
        }
    }

    // JN: cancelling a SIP asks for one of the gateway's fixed reasons, then cancels the plan.
    if (showCancelSheet) {
        CancelSipReasonSheet(
            sheetState = cancelSheetState,
            selectedReason = selectedCancelReason,
            isSubmitting = isSubmitting,
            onReasonSelected = viewModel::onCancelReasonSelected,
            onConfirm = { viewModel.cancelSipPlan(details.planId) },
            onDismiss = viewModel::onDismissCancelSheet
        )
    }
}

/**
 * The route with the folio call's figures in place of the ones it was opened with; anything the
 * call leaves blank keeps the route's value.
 */
private fun Route.SIPPortfolioDetails.withFolioFund(fund: FolioFundDomain) = copy(
    holdingId = fund.mfHoldingId.ifBlank { holdingId },
    title = fund.title.ifBlank { title },
    category = fund.category.ifBlank { category },
    amount = fund.amount,
    isSip = fund.isSip,
    startDate = fund.startDate.ifBlank { startDate },
    returnPercentage = fund.returnPercentage.ifBlank { returnPercentage },
    returnAmount = fund.`return`,
    xirr = fund.xirr.ifBlank { xirr },
    currentNav = fund.currentNav,
    avgNav = fund.avgNav,
    folio = fund.folio.ifBlank { folio },
    actualFolio = fund.actualFolio.ifBlank { actualFolio },
    balanceUnits = fund.balanceUnits,
    img_url = fund.imgUrl.ifBlank { img_url.orEmpty() },
    orderId = fund.orderId.ifBlank { orderId },
    currentValue = fund.currentValue,
    status = fund.state.ifBlank { status },
    planId = fund.fpId.ifBlank { planId },
    // The folio call reports no redeemable figure; it can only ever cap the one the route had.
    redeemableUnits = redeemableUnits.coerceAtMost(fund.balanceUnits)
)

/** JN's overflow menu: Redeem / Cancel for a SIP, Proceed to Withdraw for a lumpsum. */
@Composable
private fun OrderActionsMenu(
    isSip: Boolean,
    canCancel: Boolean,
    onRedeem: () -> Unit,
    onCancelClick: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(8.dp))
                .clickable { expanded = true }
                .padding(6.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_more_vert),
                contentDescription = "More options",
                modifier = Modifier.size(20.dp),
                tint = Secondary
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.background(Color.White)
        ) {
            if (isSip) {
                OrderActionItem(
                    text = "Redeem",
                    onClick = {
                        expanded = false
                        onRedeem()
                    }
                )
                if (canCancel) {
                    OrderActionItem(
                        text = "Cancel",
                        color = appRed,
                        onClick = {
                            expanded = false
                            onCancelClick()
                        }
                    )
                }
            } else {
                OrderActionItem(
                    text = "Proceed to Withdraw",
                    onClick = {
                        expanded = false
                        onRedeem()
                    }
                )
            }
        }
    }
}

@Composable
private fun OrderActionItem(
    text: String,
    color: Color = Color.Black,
    onClick: () -> Unit
) {
    DropdownMenuItem(
        text = {
            Text(
                text = text,
                style = titlesStyle,
                fontWeight = FontWeight.Medium,
                color = color
            )
        },
        onClick = onClick
    )
}

@Composable
fun SIPDetailsLoadedScreen(
    data: Route.SIPPortfolioDetails,
    folioIcon: DrawableResource = Res.drawable.document_kyc_icon,
    calendarIcon: DrawableResource = Res.drawable.icon_callender,
    returnPercentIcon: DrawableResource = Res.drawable.icon_mf,
    returnAmountIcon: DrawableResource = Res.drawable.rupeesign,
    xirrIcon: DrawableResource = Res.drawable.ic_percent,
    currentNavIcon: DrawableResource = Res.drawable.ic_jagged_arrow,
    avgNavIcon: DrawableResource = Res.drawable.ic_graph,
    balanceUnitsIcon: DrawableResource = Res.drawable.icon_clock,
    investmentTypeIcon: DrawableResource = Res.drawable.ic_menu,
    infoIcon: DrawableResource = Res.drawable.info_icon,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxSize().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }
            item { FundSummaryCard(data = data) }

            item {
                Text(
                    text = "Fund details",
                    style = MaterialTheme.typography.headlineSmall,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }

            item {
                ShadowCard {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        FundDetailItem(folioIcon, "Folio", data.actualFolio)
                        HorizontalDivider(color = Color.LightGray.copy(0.2f))
                        FundDetailItem(
                            returnPercentIcon,
                            "Return %",
                            data.returnPercentage,
                            valueColor = if (data.returnPercentage.contains("-")) appRed else appGreen
                        )
                        HorizontalDivider(color = Color.LightGray.copy(0.2f))
                        FundDetailItem(
                            returnAmountIcon,
                            "Return amount",
                            "₹${formatMoneyAfterLWithPaise(data.returnAmount)}",
                            valueColor = if (data.returnAmount > 0) appGreen else appRed
                        )
                        HorizontalDivider(color = Color.LightGray.copy(0.2f))
                        FundDetailItem(xirrIcon, "XIRR", data.xirr)
                        HorizontalDivider(color = Color.LightGray.copy(0.2f))
                        FundDetailItem(currentNavIcon, "Current NAV", "₹${data.currentNav}")
                        HorizontalDivider(color = Color.LightGray.copy(0.2f))
                        FundDetailItem(avgNavIcon, "Average NAV", "₹${data.avgNav}")
                        HorizontalDivider(color = Color.LightGray.copy(0.2f))
                        FundDetailItem(
                            balanceUnitsIcon,
                            "Balance units",
                            data.balanceUnits.toString()
                        )
                        HorizontalDivider(color = Color.LightGray.copy(0.2f))
                        // Below the balance while units are locked in; what Redeem offers.
                        FundDetailItem(
                            balanceUnitsIcon,
                            "Redeemable units",
                            data.redeemableUnits.coerceIn(0.0, data.balanceUnits.coerceAtLeast(0.0)).toString()
                        )
                        HorizontalDivider(color = Color.LightGray.copy(0.2f))
                        FundDetailItem(
                            investmentTypeIcon,
                            "Investment type",
                            if (data.isSip) "SIP" else "Lumpsum"
                        )
                    }
                }
            }

            item {
                BottomNotice(infoIcon)
            }

            item { Spacer(Modifier.height(16.dp)) }
        }
    }
}

@Composable
fun FundSummaryCard(
    data: Route.SIPPortfolioDetails,
) {
    ShadowCard(
        modifier = Modifier.border(
            width = 1.dp,
            color = Color.LightGray.copy(alpha = 0.5f),
            shape = LocalVelvetShapes.current.roundedDp15
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.Top, modifier = Modifier.fillMaxWidth()
            ) {
                SubcomposeAsyncImage(
                    modifier = Modifier.size(48.dp).clip(LocalVelvetShapes.current.roundedDp12)
                        .background(Color.White), model = data.img_url, contentDescription = null,

                    loading = {
                        MutualFundIcon(
                            schemeName = data.title, size = 48.dp
                        )
                    },

                    error = {
                        MutualFundIcon(
                            schemeName = data.title, size = 48.dp
                        )
                    },

                    success = {
                        SubcomposeAsyncImageContent()
                    })
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = data.title,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Black
                    )
                    Text(
                        text = data.category, style = titlesStyle, color = TextGray
                    )
                    // Only a cancelled SIP is marked; an active one or a lumpsum shows nothing.
                    if (data.isSip && data.status.equals("CANCELLED", ignoreCase = true)) {
                        Text(
                            text = "SIP Cancelled",
                            style = titlesStyle,
                            color = appRed,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 20.dp),
                thickness = 1.dp,
                color = Color.LightGray.copy(alpha = 0.5f)
            )

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier) {
                    Text(
                        text = "Invested amount", style = TextStyle(
                            fontFamily = Poppins, fontSize = 12.sp, color = Color.DarkGray
                        )
                    )
                    Text(
                        text = "₹${formatMoneyAfterLWithPaise(data.amount)}".withInterRupee(),
                        style = subHeadingMedium,
                        color = Primary
                    )
                }
                Column(modifier = Modifier) {
                    Text(
                        text = "Current value", style = TextStyle(
                            fontFamily = Poppins, fontSize = 12.sp, color = Color.DarkGray
                        )
                    )
                    Text(
                        // JN: the portfolio's own current value; invested + return only when absent.
                        text = "₹${formatMoneyAfterLWithPaise(data.currentValue.takeIf { it != 0.0 } ?: (data.amount + data.returnAmount))}".withInterRupee(),
                        style = subHeadingMedium,
                        color = Secondary
                    )
                }
            }
        }
    }
}

@Composable
fun FundDetailItem(
    icon: DrawableResource, label: String, value: String, valueColor: Color = Color.Black
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp, horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(36.dp).clip(RoundedCornerShape(8.dp))
                .background(Primary.copy(alpha = 0.05f)), contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(18.dp),
                tint = Primary
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = label, style = TextStyle(
                fontFamily = Poppins,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Gray
            ), modifier = Modifier.weight(1f)
        )
        Text(
            text = value.withInterRupee(), style = TextStyle(
                fontFamily = Poppins,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = valueColor
            )
        )

    }
}

@Composable
fun BottomNotice(icon: DrawableResource) {
    Row(
        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(8.dp))
            .background(Primary.copy(alpha = 0.05f)).padding(12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.size(16.dp),
            tint = Primary
        )
        Text(
            text = "All redemptions are processed as per AMC timelines.", style = TextStyle(
                fontFamily = Poppins,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                color = Color.Black.copy(alpha = 0.7f)
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SIPDetailsLoadedScreenPreview() {
    VelvetTheme {
        SIPDetailsLoadedScreen(
            data = Route.SIPPortfolioDetails(
                id = 1,
                title = "Parag Parikh Flexi Cap Fund - Reg - Gr",
                category = "MF-Flexi-cap Fund",
                amount = 694965.34,
                isSip = false,
                startDate = "28-Sep-2020",
                returnPercentage = "56.18%",
                returnAmount = -390424.0,
                xirr = "15.29%",
                currentNav = 82.693,
                avgNav = 52.947,
                folio = "CM_10534533",
                balanceUnits = 13125.567,
                orderId = "",
                actualFolio = ""
            )
        )
    }
}
