package org.sharad.velvetinvestment.features.plans.ui.compose

import org.sharad.velvetinvestment.shared.theme.titlesStyle
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.emify.core.ui.theme.backgroundGray
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Black
import org.sharad.emify.core.ui.theme.Gray444
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.SelectedTenureChipColor
import org.sharad.velvetinvestment.shared.compose.AppButton
import org.sharad.velvetinvestment.shared.compose.InvertedAppButton
import org.sharad.velvetinvestment.features.core.domain.models.PurchaseMode
import org.sharad.velvetinvestment.shared.theme.Spacing
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.tick_icon

/** `start_date` and `scheduled_on` can arrive as full timestamps; only the date part is shown. */
private const val ISO_DATE_LENGTH = 10

/**
 * The end of every purchase, whichever way it was made.
 *
 * What actually happened differs by mode and the copy has to follow: a SIP is a *registration*
 * that will debit later, while a one-time buy is money that has already moved. Telling a user
 * their lumpsum "will be debited automatically through UPI Autopay" would be plainly wrong.
 */
@Composable
fun PurchaseSuccessScreen(
    mode: PurchaseMode,
    schemeName: String,
    amount: String,
    installmentDay: Int?,
    startDate: String?,
    onViewHoldingsClick: () -> Unit,
    onDoneClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(containerColor = White) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(SelectedTenureChipColor),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.tick_icon),
                        contentDescription = null,
                        tint = Primary,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Text(
                    text = mode.successTitle,
                    style = MaterialTheme.typography.headlineLarge,
                    color = Black,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 24.dp)
                )
                Text(
                    text = mode.successSubtitle,
                    style = titlesStyle,
                    color = titleColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 12.dp)
                )

                SummaryCard(
                    mode = mode,
                    schemeName = schemeName,
                    amount = amount,
                    installmentDay = installmentDay,
                    startDate = startDate,
                    modifier = Modifier.padding(top = 32.dp)
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.padding(bottom = 24.dp)
            ) {
                AppButton(
                    text = "View My Holdings",
                    onClick = onViewHoldingsClick,
                    modifier = Modifier.fillMaxWidth()
                )
                InvertedAppButton(
                    text = "Done",
                    onClick = onDoneClick,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun SummaryCard(
    mode: PurchaseMode,
    schemeName: String,
    amount: String,
    installmentDay: Int?,
    startDate: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .border(
                width = 1.dp,
                color = backgroundGray,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(horizontal = 20.dp)
    ) {
        SuccessRow(label = "Scheme", value = schemeName)

        HorizontalDivider(thickness = 1.dp, color = backgroundGray)
        SuccessRow(label = "Transaction", value = mode.transactionLabel)

        amount.takeIf { it.isNotBlank() }?.let {
            HorizontalDivider(thickness = 1.dp, color = backgroundGray)
            SuccessRow(label = "Amount", value = "₹$it${mode.amountSuffix}")
        }

        // Only a monthly SIP has a debit day; the other two would show a meaningless number.
        installmentDay?.takeIf { mode.needsInstallmentDay }?.let {
            HorizontalDivider(thickness = 1.dp, color = backgroundGray)
            SuccessRow(label = "Debit day", value = "$it of every month")
        }

        startDate?.takeIf { it.isNotBlank() }?.let {
            HorizontalDivider(thickness = 1.dp, color = backgroundGray)
            SuccessRow(label = mode.dateLabel, value = it.take(ISO_DATE_LENGTH))
        }
    }
}

@Composable
private fun SuccessRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = titlesStyle,
            color = Gray444
        )
        Text(
            text = value,
            style = titlesStyle.copy(fontWeight = FontWeight.SemiBold),
            color = Black,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
        )
    }
}

private val PurchaseMode.successTitle: String
    get() = when (this) {
        PurchaseMode.DAILY -> "Daily SIP Started!"
        PurchaseMode.MONTHLY -> "SIP Registered Successfully!"
        PurchaseMode.ONE_TIME -> "Investment Successful!"
    }

private val PurchaseMode.successSubtitle: String
    get() = when (this) {
        PurchaseMode.DAILY ->
            "Your daily instalments will be debited automatically through UPI Autopay."

        PurchaseMode.MONTHLY ->
            "Your instalments will be debited automatically through UPI Autopay."

        // The money has already moved by the time a lumpsum reaches this screen, so the only
        // thing still outstanding is the unit allotment.
        PurchaseMode.ONE_TIME ->
            "Your payment has gone through. Units will be allotted at the applicable NAV."
    }

private val PurchaseMode.transactionLabel: String
    get() = when (this) {
        PurchaseMode.DAILY -> "Daily SIP"
        PurchaseMode.MONTHLY -> "Monthly SIP"
        PurchaseMode.ONE_TIME -> "One-time purchase"
    }

private val PurchaseMode.amountSuffix: String
    get() = when (this) {
        PurchaseMode.DAILY -> " / day"
        PurchaseMode.MONTHLY -> " / month"
        PurchaseMode.ONE_TIME -> ""
    }

/** A SIP starts and keeps going; a lumpsum is priced on one date and then done. */
private val PurchaseMode.dateLabel: String
    get() = if (isSip) "Starts on" else "Scheduled for"

@Preview(showBackground = true)
@Composable
private fun MonthlySipSuccessPreview() {
    VelvetTheme {
        PurchaseSuccessScreen(
            mode = PurchaseMode.MONTHLY,
            schemeName = "ADITYA BIRLA SUN LIFE LIQUID FUND - GROWTH",
            amount = "2500",
            installmentDay = 10,
            startDate = "2026-09-10",
            onViewHoldingsClick = {},
            onDoneClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun LumpsumSuccessPreview() {
    VelvetTheme {
        PurchaseSuccessScreen(
            mode = PurchaseMode.ONE_TIME,
            schemeName = "ADITYA BIRLA SUN LIFE LIQUID FUND - GROWTH",
            amount = "10000",
            installmentDay = null,
            startDate = "2026-08-26T00:00:00.000Z",
            onViewHoldingsClick = {},
            onDoneClick = {}
        )
    }
}
