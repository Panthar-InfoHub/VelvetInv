package org.sharad.velvetinvestment.features.bundles.presentation.compose

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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.appGreen
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.features.bundles.domain.models.BundleSummaryDomain
import org.sharad.velvetinvestment.features.bundles.presentation.viewmodel.AllBundlesEvent
import org.sharad.velvetinvestment.features.bundles.presentation.viewmodel.AllBundlesUiState
import org.sharad.velvetinvestment.features.core.domain.models.PurchaseMode
import org.sharad.velvetinvestment.features.cart.presentation.compose.CartFab
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.ErrorScreen
import org.sharad.velvetinvestment.shared.compose.LoaderScreen
import org.sharad.velvetinvestment.shared.compose.TwoWaySwitch
import org.sharad.velvetinvestment.shared.theme.LocalVelvetShapes
import org.sharad.velvetinvestment.shared.theme.Poppins
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import org.sharad.velvetinvestment.shared.theme.buttonTextStyle
import org.sharad.velvetinvestment.shared.theme.tinyLabel
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.ic_pointer_right

/**
 * The bundles tab. [showBack] is false when it is opened as a bottom-nav tab, which has nothing
 * behind it; the cart is reached from the floating cart button, as elsewhere in Velvet.
 */
@Composable
fun AllBundlesScreen(
    state: AllBundlesUiState,
    onEvent: (AllBundlesEvent) -> Unit,
    showBack: Boolean = true
) {
    Scaffold(
        topBar = {
            BackHeader(
                title = "Curated Bundles",
                showBack = showBack,
                onBack = { onEvent(AllBundlesEvent.OnBackClicked) }
            )
        },
        floatingActionButton = {
            CartFab(
                onClick = { onEvent(AllBundlesEvent.OnCartClicked) },
                cartAmount = state.cartAmount
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            Box(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                PurchaseModeSwitch(
                    selected = state.purchaseMode,
                    onSelected = { onEvent(AllBundlesEvent.OnPurchaseModeSelected(it)) }
                )
            }

            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                when {
                    state.isLoading && state.bundles.isEmpty() -> LoaderScreen()

                    state.error != null && state.bundles.isEmpty() -> ErrorScreen(
                        errorMessage = state.error,
                        onRetryClick = { onEvent(AllBundlesEvent.Retry) }
                    )

                    else -> LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        itemsIndexed(state.bundles, key = { _, bundle -> bundle.id }) { _, bundle ->
                            BundleCardAll(
                                bundle = bundle,
                                purchaseMode = state.purchaseMode,
                                onClick = { onEvent(AllBundlesEvent.OnBundleClicked(bundle.id)) },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        item {
                            // Clears the floating cart button.
                            Spacer(modifier = Modifier.height(72.dp + innerPadding.calculateBottomPadding()))
                        }
                    }
                }
            }
        }
    }
}

/** How each offered mode is named on Velvet's purchase-mode switch. */
internal fun PurchaseMode.switchLabel(): String = when (this) {
    PurchaseMode.ONE_TIME -> "One-time"
    // Velvet offers no daily SIP; a daily mode is never shown, but reads as a SIP if it were.
    PurchaseMode.MONTHLY-> "SIP"
}

/**
 * Velvet's two-way switch over [PurchaseMode.offered]: a normal SIP or a one-time lumpsum. JN's
 * three-way control also offered a ₹10/day SIP, which Velvet doesn't.
 */
@Composable
internal fun PurchaseModeSwitch(
    selected: PurchaseMode,
    onSelected: (PurchaseMode) -> Unit
) {
    val (first, second) = PurchaseMode.offered
    TwoWaySwitch(
        isFirstSelected = selected != second,
        firstText = first.switchLabel(),
        secondText = second.switchLabel(),
        onFirstClick = { onSelected(first) },
        onSecondClick = { onSelected(second) }
    )
}

@Composable
private fun BundleCardAll(
    bundle: BundleSummaryDomain,
    purchaseMode: PurchaseMode,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    BundleCardContainer(
        bundle = bundle,
        onClick = onClick,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f).fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Text(
                    text = bundle.name,
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black
                )
                if (bundle.description.isNotBlank()) {
                    Text(
                        text = bundle.description,
                        style = tinyLabel.copy(fontWeight = FontWeight.Normal),
                        color = titleColor
                    )
                }
            }
            Row(
                modifier = Modifier
                    .clip(LocalVelvetShapes.current.circle)
                    .background(appGreen.copy(alpha = 0.1f))
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = bundle.metaData.investmentGrowth,
                    style = tinyLabel,
                    color = appGreen
                )
            }
        }

        BundleInfoRowAll(
            onClick = onClick,
            modifier = Modifier.padding(top = 20.dp)
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                // Label, amount and suffix all follow the selected mode.
                val startAmount = bundle.startAmountFor(purchaseMode)
                Text(
                    text = purchaseMode.minAmountLabel(),
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    fontSize = 8.sp,
                    color = titleColor
                )
                Text(
                    text = buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.Bold, color = Color.Black)) {
                            // A dash when the API didn't send this mode's amount.
                            append(startAmount?.let { "₹$it" } ?: "—")
                        }
                        if (startAmount != null) append(purchaseMode.amountSuffix())
                    },
                    style = tinyLabel,
                    color = titleColor,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "${bundle.fundCountFor(purchaseMode)} Mutual Funds",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    fontSize = 8.sp,
                    color = titleColor
                )
                if (bundle.assetClassLabel.isNotBlank()) {
                    Text(
                        text = bundle.assetClassLabel,
                        style = tinyLabel,
                        color = titleColor,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

/** The card's minimum label for each mode. */
private fun PurchaseMode.minAmountLabel(): String = when (this) {
    PurchaseMode.MONTHLY -> "MIN. MONTHLY SIP"
    PurchaseMode.ONE_TIME -> "MIN. ONE-TIME"
}

/** What follows the amount; a one-time amount has no period. */
private fun PurchaseMode.amountSuffix(): String = when (this) {
    PurchaseMode.MONTHLY -> "/mo"
    PurchaseMode.ONE_TIME -> ""
}

@Composable
private fun BundleInfoRowAll(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            modifier = Modifier.weight(1f)
        ) { content() }
        Spacer(modifier = Modifier.width(12.dp))
        Button(
            onClick = onClick,
            shape = LocalVelvetShapes.current.roundedDp12,
            colors = ButtonDefaults.buttonColors(containerColor = Primary, contentColor = Color.White),
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 2.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Invest",
                    style = buttonTextStyle.copy(fontSize = 12.sp)
                )
                Icon(
                    painter = painterResource(Res.drawable.ic_pointer_right),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.padding(start = 4.dp).size(12.dp)
                )
            }
        }
    }
}

@Preview
@Composable
private fun AllBundlesScreenPreview() {
    VelvetTheme {
        AllBundlesScreen(
            state = AllBundlesUiState(
                bundles = listOf(
                    previewBundleSummary,
                    previewBundleSummary.copy(
                        id = "2",
                        name = "Aggressive",
                        description = "Velvet Long Term Vision"
                    )
                ),
                cartAmount = 5000
            ),
            onEvent = {},
            showBack = false
        )
    }
}
