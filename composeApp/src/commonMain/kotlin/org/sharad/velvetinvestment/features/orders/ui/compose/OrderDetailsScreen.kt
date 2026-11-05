package org.sharad.velvetinvestment.features.orders.ui.compose

import org.sharad.emify.core.ui.theme.Black
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.lightGray
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.shared.theme.Poppins
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import androidx.compose.ui.graphics.Color
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.building_icon
import velvet.composeapp.generated.resources.check_circle_outline_icon
import velvet.composeapp.generated.resources.headphone
import velvet.composeapp.generated.resources.ic_graph
import velvet.composeapp.generated.resources.ic_ruppee_filled
import velvet.composeapp.generated.resources.icon_clock
import velvet.composeapp.generated.resources.icon_warning
import velvet.composeapp.generated.resources.receipt_icon
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.sharad.velvetinvestment.utils.trimDoubleTo
import org.sharad.velvetinvestment.utils.withInterRupee
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.AppButton
import org.sharad.velvetinvestment.shared.genericDropShadow
import org.sharad.velvetinvestment.features.orders.domain.model.OrderDomain
import org.sharad.velvetinvestment.features.orders.domain.model.OrderPlanType
import org.sharad.velvetinvestment.features.orders.domain.model.OrderState
import org.sharad.velvetinvestment.features.orders.domain.model.asMoney
import org.sharad.velvetinvestment.features.orders.domain.model.asUnits
import org.sharad.velvetinvestment.features.orders.domain.model.toOrderDate
import org.sharad.velvetinvestment.features.orders.domain.model.toOrderDateTime
import org.sharad.velvetinvestment.features.orders.domain.model.typeLabel

/** Shown wherever the payload reports nothing, rather than a zero that would read as a fact. */
private const val ABSENT = "--"

/**
 * One order in full.
 *
 * There is no per-order endpoint, so everything here comes off the route — see
 * [org.sharad.velvetinvestment.features.orders.domain.model.toDetailsRoute]. Which of the
 * four layouts the designs call for is decided here rather than by the caller: a redemption is
 * about where the money goes, while a purchase is about what was bought, and within a purchase
 * the completed, pending and failed states differ in what they can honestly show.
 */
@Composable
fun OrderDetailsScreen(
    order: OrderDomain,
    onBack: () -> Unit,
    onRetryOrder: () -> Unit,
    onDownloadReceipt: () -> Unit,
    onNeedHelp: () -> Unit,
    /** The account a redemption pays out to. Blank hides the card — see `Route.OrderDetails`. */
    payoutAccount: String = "",
    modifier: Modifier = Modifier
) {
    val isRedemption = order.planType == OrderPlanType.REDEMPTION

    Column(modifier = modifier.fillMaxSize().background(Color.White)) {

        OrderDetailsHeader(onBack = onBack)

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatusBanner(order)

            if (isRedemption) {
                RedemptionSummaryCard(order)
            } else {
                InvestmentDetailsCard(order)
                TransactionDetailsCard(order)
            }

            OrderTimelineCard(order)

            if (isRedemption && payoutAccount.isNotBlank()) {
                PayoutAccountCard(payoutAccount)
            }

            Spacer(Modifier.height(8.dp))
        }

//        OrderDetailsFooter(
//            order = order,
//            isRedemption = isRedemption,
//            onRetryOrder = onRetryOrder,
//            onDownloadReceipt = onDownloadReceipt,
//            onNeedHelp = onNeedHelp
//        )
    }
}

@Composable
private fun OrderDetailsHeader(onBack: () -> Unit) {
    BackHeader(title = "Order details", showBack = true, onBack = onBack)
}

/**
 * The one-line verdict at the top. A failed order explains itself with the gateway's own reason
 * when it gave one, because a generic apology tells the user nothing about what to do next.
 */
@Composable
private fun StatusBanner(order: OrderDomain) {
    val color = order.state.color()

    val (title, message) = when (order.state) {
        OrderState.SUCCESSFUL -> "Completed" to
            "Your order has been successfully processed."

        OrderState.PENDING -> "Order Pending" to
            "Your order is being processed. Expected allotment within 2-3 business days."

        OrderState.FAILED -> "Order Failed" to
            order.reason.ifBlank { "Payment was declined. Please try again or use a different payment method." }

        OrderState.CANCELLED -> "Order Cancelled" to
            order.reason.ifBlank { "This order was cancelled and no money was debited." }

        OrderState.UNKNOWN -> order.stateLabel.ifBlank { "Order placed" } to
            "We'll update this as soon as the exchange confirms your order."
    }

    val icon = when (order.state) {
        OrderState.SUCCESSFUL -> Res.drawable.check_circle_outline_icon
        OrderState.PENDING -> Res.drawable.icon_clock
        else -> Res.drawable.icon_warning
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(color.copy(alpha = 0.05f))
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(20.dp)
        )
        Column {
            Text(
                text = title,
                fontFamily = Poppins,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = message,
                fontFamily = Poppins,
                fontSize = 12.sp,
                lineHeight = 17.sp,
                color = color.copy(alpha = 0.85f)
            )
        }
    }
}

@Composable
private fun InvestmentDetailsCard(order: OrderDomain) {
    SectionCard(title = "Investment Details", icon = Res.drawable.ic_ruppee_filled) {
        FactRow(
            "Fund Name" to order.fundName,
            "Investment Type" to order.investmentTypeLabel()
        )
        Spacer(Modifier.height(20.dp))
        FactRow(
            "Amount" to order.amountLabel(),
            "NAV" to order.navLabel()
        )
    }
}

@Composable
private fun TransactionDetailsCard(order: OrderDomain) {
    // Only the rows the payload can actually fill: a pending order has no allotment date and no
    // payment id, and an empty row labelled "--" adds nothing the reader did not already know.
    val rows = listOfNotNull(
        ("Order Date" to order.createdAt.toOrderDate()).withValue(),
        ("Payment Method" to order.paymentMethodLabel()).withValue(),
        ("Allotment Date" to order.allotmentDateLabel()).withValue()
    )
    val ids = listOfNotNull(
        ("Order ID" to order.orderId).withValue(),
        ("Transaction ID" to order.transactionId).withValue()
    )

    if (rows.isEmpty() && ids.isEmpty()) return

    SectionCard(title = "Transaction Details", icon = Res.drawable.receipt_icon) {
        rows.forEachIndexed { index, (label, value) ->
            if (index > 0) Spacer(Modifier.height(12.dp))
            LabelledRow(label, value)
        }
        if (rows.isNotEmpty() && ids.isNotEmpty()) {
            Spacer(Modifier.height(16.dp))
            HorizontalDivider(thickness = 1.dp, color = lightGray)
            Spacer(Modifier.height(16.dp))
        }
        ids.forEachIndexed { index, (label, value) ->
            if (index > 0) Spacer(Modifier.height(12.dp))
            LabelledRow(label, value, monospaceish = true)
        }
    }
}

/**
 * A redemption is money leaving, so the card leads with what is being sold rather than with the
 * NAV and allotment figures a purchase is judged on — those do not exist yet at this point.
 */
@Composable
private fun RedemptionSummaryCard(order: OrderDomain) {
    SectionCard(title = "Summary", icon = null) {
        FactRow(
            "Fund Name" to order.fundName,
            "Amount" to order.amountLabel()
        )
        Spacer(Modifier.height(20.dp))
        FactRow(
            "Units" to (order.units ?: order.allottedUnits)?.asUnits().orEmpty().ifBlank { ABSENT },
            "Folio Number" to order.folioNumber.ifBlank { ABSENT }
        )
    }
}

@Composable
private fun OrderTimelineCard(order: OrderDomain) {
    val steps = order.timelineSteps()
    if (steps.isEmpty()) return

    SectionCard(title = "Order Timeline", icon = Res.drawable.ic_graph) {
        steps.forEachIndexed { index, step ->
            TimelineRow(step = step, isLast = index == steps.lastIndex)
        }
    }
}

@Composable
private fun PayoutAccountCard(account: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .genericDropShadow(
                shape = RoundedCornerShape(12.dp),
            )
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(1.dp, lightGray, RoundedCornerShape(12.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            painter = painterResource(Res.drawable.building_icon),
            contentDescription = null,
            tint = Primary,
            modifier = Modifier.size(24.dp)
        )
        Column {
            Text(
                text = "Crediting to",
                fontFamily = Poppins,
                fontSize = 12.sp,
                color = titleColor
            )
            Text(
                text = account,
                fontFamily = Poppins,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Black
            )
        }
    }
}

@Composable
private fun OrderDetailsFooter(
    order: OrderDomain,
    isRedemption: Boolean,
    onRetryOrder: () -> Unit,
    onDownloadReceipt: () -> Unit,
    onNeedHelp: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        when {
            // Retrying needs the scheme to place the order against; without it the button would
            // lead nowhere, so it is left off rather than shown broken.
            order.state == OrderState.FAILED && order.mfProductId.isNotBlank() -> AppButton(
                text = "Try Again",
                onClick = onRetryOrder,
                modifier = Modifier.fillMaxWidth()
            )

            isRedemption -> Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(1.dp, lightGray, CircleShape)
                    .clickable(onClick = onNeedHelp)
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.headphone),
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "Need Help?",
                    fontFamily = Poppins,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Primary
                )
            }

            order.state == OrderState.SUCCESSFUL -> AppButton(
                text = "Download Receipt",
                onClick = onDownloadReceipt,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

// region building blocks

@Composable
private fun SectionCard(
    title: String,
    icon: DrawableResource?,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .genericDropShadow(
                shape = RoundedCornerShape(15.dp),
            )
            .clip(RoundedCornerShape(15.dp))
            .background(Color.White)
            .padding(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (icon != null) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = Secondary,
                    modifier = Modifier.size(18.dp)
                )
            }
            Text(
                text = title,
                fontFamily = Poppins,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Black
            )
        }
        Spacer(Modifier.height(20.dp))
        content()
    }
}

/** Two facts side by side, the way the designs pair them. */
@Composable
private fun FactRow(left: Pair<String, String>, right: Pair<String, String>) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Fact(left.first, left.second, Modifier.weight(1f))
        Spacer(Modifier.width(12.dp))
        Fact(right.first, right.second, Modifier.weight(1f))
    }
}

@Composable
private fun Fact(label: String, value: String, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        Text(
            text = label.uppercase(),
            fontFamily = Poppins,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            letterSpacing = 0.6.sp,
            color = titleColor
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = value.ifBlank { ABSENT }.withInterRupee(),
            fontFamily = Poppins,
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Black,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/** Label on the left, value on the right — the transaction-details shape. */
@Composable
private fun LabelledRow(label: String, value: String, monospaceish: Boolean = false) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            fontFamily = Poppins,
            fontSize = 13.sp,
            color = titleColor
        )
        Spacer(Modifier.width(16.dp))
        Text(
            text = value,
            fontFamily = Poppins,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Black,
            textAlign = TextAlign.End,
            maxLines = if (monospaceish) 2 else 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f, fill = false)
        )
    }
}

private enum class StepState { DONE, CURRENT, UPCOMING }

private data class TimelineStep(
    val title: String,
    val subtitle: String,
    val state: StepState
)

@Composable
private fun TimelineRow(step: TimelineStep, isLast: Boolean) {
    val color = when (step.state) {
        StepState.DONE -> CompletedGreen
        StepState.CURRENT -> Secondary
        StepState.UPCOMING -> lightGray
    }

    Row(modifier = Modifier.fillMaxWidth()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(20.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = if (step.state == StepState.UPCOMING) 1f else 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                if (step.state != StepState.UPCOMING) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(color)
                    )
                }
            }
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(32.dp)
                        .background(lightGray)
                )
            }
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.padding(bottom = if (isLast) 0.dp else 8.dp)) {
            Text(
                text = step.title,
                fontFamily = Poppins,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = if (step.state == StepState.UPCOMING) titleColor else Black
            )
            if (step.subtitle.isNotBlank()) {
                Spacer(Modifier.height(2.dp))
                Text(
                    text = step.subtitle,
                    fontFamily = Poppins,
                    fontSize = 12.sp,
                    color = titleColor
                )
            }
        }
    }
}

// endregion

// region what each state can honestly say

private fun OrderDomain.investmentTypeLabel(): String {
    val type = typeLabel()
    return if (systematic && frequency.isNotBlank()) {
        "$type • ${frequency.lowercase().replaceFirstChar { it.uppercase() }}"
    } else {
        type
    }
}

/** The amount actually put in once the AMC confirms it, and the amount asked for until then. */
private fun OrderDomain.amountLabel(): String =
    (purchasedAmount ?: amount)?.let { "₹${it.asMoney()}" } ?: ABSENT

/** The scheme's `latest_nav` from the orders payload, trimmed to two decimals. */
private fun OrderDomain.navLabel(): String =
    latestNav?.trimDoubleTo(2)?.toString() ?: ABSENT

private fun OrderDomain.allottedUnitsLabel(): String = when {
    allottedUnits != null -> allottedUnits.asUnits()
    state == OrderState.FAILED -> "Failed"
    else -> ABSENT
}

private fun OrderDomain.paymentMethodLabel(): String =
    paymentMethod.replace('_', ' ').lowercase()
        .split(' ')
        .filter { it.isNotBlank() }
        .joinToString(" ") { it.replaceFirstChar { c -> c.uppercase() } }

private fun OrderDomain.allotmentDateLabel(): String =
    allottedNavDate.toOrderDate().ifBlank { succeededAt.toOrderDate() }

/**
 * The steps the order has actually been through, plus the ones still ahead of it. A failed order
 * stops where it failed — there is no allotment to be waiting on.
 */
private fun OrderDomain.timelineSteps(): List<TimelineStep> {
    val placed = TimelineStep(
        title = if (planType == OrderPlanType.REDEMPTION) "Request Placed" else "Order Placed",
        subtitle = createdAt.toOrderDateTime(),
        state = StepState.DONE
    )

    if (state == OrderState.FAILED || state == OrderState.CANCELLED) {
        val closedAt = failedAt.toOrderDateTime()
        return listOf(
            placed,
            TimelineStep(
                title = if (state == OrderState.FAILED) "Order Failed" else "Order Cancelled",
                subtitle = closedAt,
                state = StepState.DONE
            )
        )
    }

    val middleTitle =
        if (planType == OrderPlanType.REDEMPTION) "Processed by AMC" else "Payment Confirmed"
    val finalTitle =
        if (planType == OrderPlanType.REDEMPTION) "Payout to Bank" else "Units Allotted"

    val middleDone = submittedAt.isNotBlank()
    val finalDone = succeededAt.isNotBlank() || allottedNavDate.isNotBlank()

    return listOf(
        placed,
        TimelineStep(
            title = middleTitle,
            subtitle = submittedAt.toOrderDateTime().ifBlank { "In progress" },
            state = if (middleDone) StepState.DONE else StepState.CURRENT
        ),
        TimelineStep(
            title = finalTitle,
            subtitle = when {
                succeededAt.isNotBlank() -> succeededAt.toOrderDateTime()
                allottedNavDate.isNotBlank() -> allottedNavDate.toOrderDate()
                else -> "Awaited"
            },
            state = when {
                finalDone -> StepState.DONE
                middleDone -> StepState.CURRENT
                else -> StepState.UPCOMING
            }
        )
    )
}

/** Keeps a label/value pair only when there is a value to show; drops the row otherwise. */
private fun Pair<String, String>.withValue(): Pair<String, String>? =
    takeIf { it.second.isNotBlank() }

// endregion

@Preview
@Composable
fun OrderDetailsCompletedPreview() {
    VelvetTheme {
        OrderDetailsScreen(
            order = previewOrder(state = OrderState.SUCCESSFUL, amount = 5000.0).copy(
                purchasedAmount = 5000.0,
                purchasedPrice = 29.189,
                allottedUnits = 171.29,
                allottedNavDate = "2026-09-03T00:00:00.000Z",
                paymentMethod = "NET_BANKING"
            ),
            onBack = {},
            onRetryOrder = {},
            onDownloadReceipt = {},
            onNeedHelp = {}
        )
    }
}

@Preview
@Composable
fun OrderDetailsPendingPreview() {
    VelvetTheme {
        OrderDetailsScreen(
            order = previewOrder(state = OrderState.PENDING, amount = 5000.0),
            onBack = {},
            onRetryOrder = {},
            onDownloadReceipt = {},
            onNeedHelp = {}
        )
    }
}

@Preview
@Composable
fun OrderDetailsFailedPreview() {
    VelvetTheme {
        OrderDetailsScreen(
            order = previewOrder(state = OrderState.FAILED, amount = 5000.0),
            onBack = {},
            onRetryOrder = {},
            onDownloadReceipt = {},
            onNeedHelp = {}
        )
    }
}

@Preview
@Composable
fun OrderDetailsRedemptionPreview() {
    VelvetTheme {
        OrderDetailsScreen(
            order = previewOrder(
                state = OrderState.PENDING,
                planType = OrderPlanType.REDEMPTION,
                units = 172.41
            ),
            onBack = {},
            onRetryOrder = {},
            onDownloadReceipt = {},
            onNeedHelp = {},
            payoutAccount = "HDFC Bank - XXXX1234"
        )
    }
}
