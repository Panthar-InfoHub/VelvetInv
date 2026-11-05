package org.sharad.velvetinvestment.features.orders.ui.compose

import org.sharad.emify.core.ui.theme.Black
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.appRed
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.shared.theme.Poppins
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import velvet.composeapp.generated.resources.Res
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Icon
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import velvet.composeapp.generated.resources.info_icon
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.velvetinvestment.utils.withInterRupee
import org.sharad.velvetinvestment.shared.compose.LoaderScreen
import org.sharad.velvetinvestment.shared.genericDropShadow
import org.sharad.velvetinvestment.features.orders.domain.model.OrderDomain
import org.sharad.velvetinvestment.features.orders.domain.model.OrderFilter
import org.sharad.velvetinvestment.features.orders.domain.model.OrderPlanType
import org.sharad.velvetinvestment.features.orders.domain.model.OrderState
import org.sharad.velvetinvestment.features.orders.domain.model.placedForLabel
import org.sharad.velvetinvestment.features.orders.domain.model.title
import org.sharad.velvetinvestment.features.orders.domain.model.toOrderDate
import org.sharad.velvetinvestment.features.orders.domain.model.typeLabel
import org.sharad.velvetinvestment.features.orders.domain.model.unitsLabel
import org.sharad.velvetinvestment.features.orders.ui.viewmodel.MyOrdersEvent
import org.sharad.velvetinvestment.features.orders.ui.viewmodel.MyOrdersUiState
import org.sharad.velvetinvestment.shared.compose.EmptyFundScreen
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.PaginationEffect
import org.sharad.velvetinvestment.shared.compose.PaginationFooter
import org.sharad.velvetinvestment.shared.compose.FundIcon

@Composable
fun MyOrdersScreen(
    state: MyOrdersUiState,
    onEvent: (MyOrdersEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val lazyListState = rememberLazyListState()

    PaginationEffect(lazyListState = lazyListState) { onEvent(MyOrdersEvent.LoadNext) }

    Column(modifier = modifier.fillMaxSize().background(Color.White)) {

        MyOrdersHeader(onBack = { onEvent(MyOrdersEvent.OnBackClicked) })

        OrderFilterRow(
            selected = state.selectedFilter,
            onFilterSelected = { onEvent(MyOrdersEvent.OnFilterSelected(it)) }
        )

        Box(modifier = Modifier.fillMaxSize()) {
            when {
                state.isLoading && state.orders.isEmpty() ->
                    LoaderScreen()

                state.error != null && state.orders.isEmpty() -> Text(
                    text = state.error,
                    fontFamily = Poppins,
                    fontSize = 14.sp,
                    color = appRed,
                    modifier = Modifier.align(Alignment.Center).padding(24.dp)
                )

                state.orders.isEmpty() -> EmptyFundScreen(
                    title = when (state.selectedFilter) {
                        OrderFilter.ALL -> "No orders yet"
                        else -> "No ${state.selectedFilter.title().lowercase()} orders"
                    },
                    subtitle = when (state.selectedFilter) {
                        OrderFilter.ALL ->
                            "Every purchase and redemption you place will appear here, with its status and units."
                        else -> "Nothing under this filter right now. Try another one to see your other orders."
                    },
                    buttonText = "Go Back",
                    onActionClick = { onEvent(MyOrdersEvent.OnBackClicked) }
                )

                else -> LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    state = lazyListState,
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        end = 16.dp,
                        top = 8.dp,
                        bottom = 24.dp
                    ),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.orders, key = { it.id }) { order ->
                        OrderCard(
                            order = order,
                            onClick = { onEvent(MyOrdersEvent.OnOrderClicked(order)) }
                        )
                    }
                    item { PaginationFooter(hasNextPage = state.hasNextPage) }
                }
            }
        }
    }
}

@Composable
private fun MyOrdersHeader(onBack: () -> Unit) {
    BackHeader(
        title = "My Orders",
        showBack = true,
        onBack = onBack,
        rightContent = { SettlementInfoButton() }
    )
}

/**
 * The info icon at the end of the header. A tap opens a bubble under it explaining when orders
 * settle; it stays until the icon or anywhere else on the screen is tapped.
 */
@Composable
private fun SettlementInfoButton() {
    var showBubble by remember { mutableStateOf(false) }

    Box {
        Icon(
            painter = painterResource(Res.drawable.info_icon),
            contentDescription = "Settlement info",
            tint = titleColor,
            modifier = Modifier
                .clip(CircleShape)
                .clickable { showBubble = !showBubble }
                .padding(4.dp)
                .size(InfoIconSize)
        )

        if (showBubble) {
            val gapPx = with(LocalDensity.current) { 4.dp.roundToPx() }
            Popup(
                popupPositionProvider = remember(gapPx) { BelowAnchorEndAligned(gapPx) },
                onDismissRequest = { showBubble = false },
                // Focusable, so a tap outside — the icon included — only closes the bubble rather
                // than also reaching what is underneath and opening it again.
                properties = PopupProperties(focusable = true)
            ) {
                InfoBubble(text = "Orders will be settled in T+3 days.")
            }
        }
    }
}

/** A dark bubble with an arrow on its top edge, lined up under the info icon's centre. */
@Composable
private fun InfoBubble(text: String) {
    Column(horizontalAlignment = Alignment.End) {
        Canvas(
            modifier = Modifier
                // The icon's centre sits half its tappable width in from the bubble's right edge.
                .padding(end = (InfoIconSize + 8.dp) / 2 - BubbleArrowWidth / 2)
                .size(width = BubbleArrowWidth, height = BubbleArrowHeight)
        ) {
            drawPath(
                path = Path().apply {
                    moveTo(size.width / 2, 0f)
                    lineTo(size.width, size.height)
                    lineTo(0f, size.height)
                    close()
                },
                color = Black
            )
        }
        Text(
            text = text,
            fontFamily = Poppins,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White,
            modifier = Modifier
                .widthIn(max = 240.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Black)
                .padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}

private val InfoIconSize = 20.dp
private val BubbleArrowWidth = 12.dp
private val BubbleArrowHeight = 6.dp

/** Under the anchor with right edges lined up, kept inside the window. */
private class BelowAnchorEndAligned(private val gapPx: Int) : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        val x = (anchorBounds.right - popupContentSize.width)
            .coerceIn(0, (windowSize.width - popupContentSize.width).coerceAtLeast(0))
        return IntOffset(x, anchorBounds.bottom + gapPx)
    }
}

/** Shared with the Portfolio screen's Orders tab, which renders the same chips. */
@Composable
internal fun OrderFilterRow(
    selected: OrderFilter,
    onFilterSelected: (OrderFilter) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(OrderFilter.entries) { filter ->
            OrderFilterChip(
                title = filter.title(),
                isSelected = filter == selected,
                onClick = { onFilterSelected(filter) }
            )
        }
    }
}

/**
 * The shared [org.sharad.velvetinvestment.shared.compose.FilterChip] puts a
 * dismiss cross on the selected chip, which reads as "remove this filter". These four are one
 * exclusive choice with no empty state to fall back to, so the selection is shown by fill alone.
 */
@Composable
internal fun OrderFilterChip(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Text(
        text = title,
        fontFamily = Poppins,
        fontSize = 13.sp,
        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
        color = if (isSelected) Color.White else titleColor,
        modifier = Modifier
            .clip(CircleShape)
            .background(if (isSelected) Primary else Color.White)
            .border(color= if (isSelected) Color.Transparent else Secondary, width = 1.dp, shape = CircleShape)
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 6.dp)
    )
}

/** Shared with the Portfolio screen's Orders tab, which lists the same rows. */
@Composable
internal fun OrderCard(
    order: OrderDomain,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .genericDropShadow(RoundedCornerShape(15.dp))
            .clip(RoundedCornerShape(15.dp))
            .background(Color.White)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            )
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            OrderFundIcon(order)
            Spacer(Modifier.size(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = order.fundName,
                    fontFamily = Poppins,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "${order.typeLabel()} • ${order.placedForLabel()}".withInterRupee(),
                    fontFamily = Poppins,
                    fontSize = 12.sp,
                    color = titleColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
            Spacer(Modifier.size(8.dp))
            OrderStatusPill(order.state, order.stateLabel)
        }

        Spacer(Modifier.height(14.dp))
        HorizontalDivider(thickness = 1.dp, color = Color.White)
        Spacer(Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Order placed: ${order.createdAt.toOrderDate().ifBlank { "--" }}",
                fontFamily = Poppins,
                fontSize = 12.sp,
                color = titleColor
            )
            Text(
                text = "Units: ${order.unitsLabel()}",
                fontFamily = Poppins,
                fontSize = 12.sp,
                color = titleColor
            )
        }
    }
}

@Composable
internal fun OrderFundIcon(order: OrderDomain, size: androidx.compose.ui.unit.Dp = 40.dp) {
    FundIcon(
        iconUrl = order.fundIconUrl,
        name = order.fundName,
        size = size,
        cornerRadius = 12.dp
    )
}

@Composable
internal fun OrderStatusPill(state: OrderState, stateLabel: String) {
    val color = state.color()
    Text(
        text = state.label(stateLabel),
        fontFamily = Poppins,
        fontSize = 10.sp,
        fontWeight = FontWeight.Bold,
        color = color,
        modifier = Modifier
            .clip(CircleShape)
            .background(color.copy(alpha = 0.12f))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    )
}

/** Pending sits between the two outcomes, so it takes the warm middle rather than a second red. */
internal val PendingAmber = Color(0xffF97316)
internal val CompletedGreen = Color(0xff16A34A)

internal fun OrderState.color(): Color = when (this) {
    OrderState.SUCCESSFUL -> CompletedGreen
    OrderState.PENDING -> PendingAmber
    OrderState.FAILED -> appRed
    OrderState.CANCELLED -> titleColor
    OrderState.UNKNOWN -> titleColor
}

/** An unrecognised state keeps the server's own wording rather than being called "unknown". */
internal fun OrderState.label(stateLabel: String): String = when (this) {
    OrderState.SUCCESSFUL -> "COMPLETE"
    OrderState.PENDING -> "PENDING"
    OrderState.FAILED -> "FAILED"
    OrderState.CANCELLED -> "CANCELLED"
    OrderState.UNKNOWN -> stateLabel.uppercase().ifBlank { "UNKNOWN" }
}

@Preview
@Composable
fun MyOrdersScreenPreview() {
    VelvetTheme {
        MyOrdersScreen(
            state = MyOrdersUiState(
                orders = listOf(
                    previewOrder(
                        id = "1",
                        state = OrderState.PENDING,
                        planType = OrderPlanType.PURCHASE,
                        amount = 5000.0
                    ),
                    previewOrder(
                        id = "2",
                        state = OrderState.SUCCESSFUL,
                        planType = OrderPlanType.PURCHASE,
                        amount = 2000.0,
                        systematic = true,
                        allottedUnits = 45.23
                    ),
                    previewOrder(
                        id = "3",
                        state = OrderState.FAILED,
                        planType = OrderPlanType.REDEMPTION,
                        units = 2.311
                    )
                )
            ),
            onEvent = {}
        )
    }
}

internal fun previewOrder(
    id: String = "1",
    state: OrderState = OrderState.PENDING,
    planType: OrderPlanType = OrderPlanType.PURCHASE,
    amount: Double? = null,
    units: Double? = null,
    systematic: Boolean = false,
    allottedUnits: Double? = null
) = OrderDomain(
    id = id,
    planType = planType,
    state = state,
    stateLabel = state.name,
    fundName = "Aditya Birla Sun Life Gold Fund-Growth",
    fundIconUrl = "",
    fundCategory = "Debt • Growth",
    mfProductId = "cmt839yv8004e83ri5ttw9efb",
    isin = "INF209K01PF4",
    folioNumber = "1051586674",
    orderId = "mfp_4be241a208234c329c32a2887c7961c8",
    transactionId = "8",
    amount = amount,
    units = units,
    systematic = systematic,
    frequency = if (systematic) "MONTHLY" else "",
    installmentDay = null,
    numberOfInstallments = null,
    remainingInstallments = null,
    allottedUnits = allottedUnits,
    allottedNavDate = "",
    purchasedAmount = null,
    purchasedPrice = null,
    latestNav = 44.4605,
    paymentMethod = "NET_BANKING",
    paymentSource = "",
    switchToScheme = "",
    reason = if (state == OrderState.FAILED) "Payment was declined by your bank" else "",
    scheduledOn = "2026-09-07T00:00:00.000Z",
    tradedOn = "",
    createdAt = "2026-09-01T16:02:00.885Z",
    submittedAt = "2026-09-01T16:02:27.000Z",
    succeededAt = if (state == OrderState.SUCCESSFUL) "2026-09-03T09:00:00.000Z" else "",
    failedAt = if (state == OrderState.FAILED) "2026-09-02T09:00:00.000Z" else ""
)
