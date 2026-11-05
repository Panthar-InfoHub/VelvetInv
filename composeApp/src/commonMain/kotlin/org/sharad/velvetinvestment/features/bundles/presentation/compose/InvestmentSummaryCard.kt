package org.sharad.velvetinvestment.features.bundles.presentation.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.features.bundles.domain.models.AssetAllocationDomain
import org.sharad.velvetinvestment.features.bundles.domain.models.investmentName
import org.sharad.velvetinvestment.features.core.domain.models.PurchaseMode
import org.sharad.velvetinvestment.shared.compose.ShadowCard
import org.sharad.velvetinvestment.shared.theme.LocalVelvetShapes
import org.sharad.velvetinvestment.shared.theme.Poppins
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import org.sharad.velvetinvestment.shared.theme.titlesStyle
import org.sharad.velvetinvestment.utils.formatWithCommas
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.arrow_down
import velvet.composeapp.generated.resources.plus_icon

private const val AMOUNT_STEP = 500L

/** The grey of Velvet's amount stepper buttons and SIP-day field. */
private val StepperGrey = Color(0xffF3F4F5)

@Composable
fun InvestmentSummaryCard(
    bundleName: String,
    investmentAmount: Long,
    purchaseMode: PurchaseMode,
    /** Selected funds that can't be bought as [purchaseMode]; empty when all of them can. */
    unsupportedFundNames: List<String>,
    selectedSipDay: Int?,
    onPurchaseModeChange: (PurchaseMode) -> Unit,
    onAmountChange: (Long) -> Unit,
    onSipDayClick: () -> Unit,
    minAmount: Long,
    assetAllocation: AssetAllocationDomain,
    modifier: Modifier = Modifier
) {
    ShadowCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            PurchaseModeSwitch(
                selected = purchaseMode,
                onSelected = onPurchaseModeChange
            )

            Spacer(Modifier.height(20.dp))

            // Local text mirror of the amount so the field can be edited freely.
            // An empty field reads as 0, so clearing it doesn't get overwritten with "0".
            var amountText by remember {
                mutableStateOf(if (investmentAmount == 0L) "" else investmentAmount.toString())
            }
            LaunchedEffect(investmentAmount) {
                if ((amountText.toLongOrNull() ?: 0L) != investmentAmount) {
                    amountText = investmentAmount.toString()
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Surface(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(LocalVelvetShapes.current.roundedDp12)
                        .clickable { onAmountChange((investmentAmount - AMOUNT_STEP).coerceAtLeast(0)) },
                    shape = LocalVelvetShapes.current.roundedDp12,
                    color = StepperGrey
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Box(Modifier.width(16.dp).height(2.dp).background(Color.Black))
                    }
                }

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    BasicTextField(
                        value = amountText,
                        onValueChange = { input ->
                            // Leading zeros are dropped so the digits always read as the number.
                            val digits = input.filter { it.isDigit() }.trimStart('0').take(9)
                            amountText = digits
                            onAmountChange(digits.toLongOrNull() ?: 0L)
                        },
                        textStyle = MaterialTheme.typography.displayMedium.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        cursorBrush = SolidColor(Color.Black),
                        // The ₹ prefix is dropped while empty so it can't sit next to the placeholder.
                        visualTransformation = if (amountText.isEmpty()) {
                            VisualTransformation.None
                        } else {
                            RupeeGroupingTransformation
                        },
                        decorationBox = { innerTextField ->
                            Box(contentAlignment = Alignment.Center) {
                                if (amountText.isEmpty()) {
                                    Text(
                                        text = "Enter amount",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xffC5C5C5),
                                        textAlign = TextAlign.Center
                                    )
                                }
                                innerTextField()
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    )
                    Text(
                        text = when (purchaseMode) {
                            PurchaseMode.MONTHLY -> "MONTHLY"
                            PurchaseMode.ONE_TIME -> "ONE-TIME"
                        },
                        style = titlesStyle.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
                        color = titleColor.copy(alpha = 0.8f)
                    )
                }

                Surface(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(LocalVelvetShapes.current.roundedDp12)
                        .clickable { onAmountChange(investmentAmount + AMOUNT_STEP) },
                    shape = LocalVelvetShapes.current.roundedDp12,
                    color = StepperGrey
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.plus_icon),
                        contentDescription = "Increase amount",
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }

            // While a fund can't be bought this way there is no real minimum to compare against.
            if (unsupportedFundNames.isEmpty() && investmentAmount < minAmount) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "Amount is less than the minimum of ₹${formatWithCommas(minAmount)}",
                    style = titlesStyle.copy(fontSize = 12.sp),
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
            }

            if (unsupportedFundNames.isNotEmpty()) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "${purchaseMode.investmentName} isn't available for " +
                            "${unsupportedFundNames.joinToString(", ")}. " +
                            "Change ${if (unsupportedFundNames.size == 1) "this fund" else "these funds"} " +
                            "below or pick another way to invest.",
                    style = titlesStyle.copy(fontSize = 12.sp),
                    color = Color.Red,
                    textAlign = TextAlign.Center
                )
            }

            if (purchaseMode.needsInstallmentDay) {
                Spacer(Modifier.height(24.dp))
                DebitDayField(day = selectedSipDay, onClick = onSipDayClick)
            }
        }
    }
}

/**
 * The tappable monthly debit-day field (JN's `DebitDayField`), drawn like Velvet's SIP-day
 * selector: a "SIP DAY" label over a grey rounded field.
 */
@Composable
fun DebitDayField(
    day: Int?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "SIP DAY",
            style = titlesStyle.copy(fontSize = 12.sp, fontWeight = FontWeight.Bold),
            color = titleColor
        )
        Spacer(Modifier.height(8.dp))
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .clickable(onClick = onClick),
            shape = RoundedCornerShape(12.dp),
            color = StepperGrey,
            border = BorderStroke(1.dp, titleColor.copy(alpha = 0.1f))
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = day?.let { "Monthly on $it${daySuffix(it)}" } ?: "Choose a debit day",
                    style = titlesStyle,
                    color = if (day == null) titleColor else Primary
                )
                Icon(
                    painter = painterResource(Res.drawable.arrow_down),
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }
    }
}

private fun daySuffix(day: Int): String = when {
    day in 11..13 -> "th"
    day % 10 == 1 -> "st"
    day % 10 == 2 -> "nd"
    day % 10 == 3 -> "rd"
    else -> "th"
}

/**
 * Displays a digits-only amount as "₹33,33,333". Display only — the field's value
 * stays digits-only, so nothing downstream ever sees the ₹ or the commas.
 */
private val RupeeGroupingTransformation = VisualTransformation { text ->
    val digits = text.text
    val grouped = digits.toLongOrNull()?.let { formatWithCommas(it) } ?: digits

    // Transformed index each original digit lands on, offset by 1 for the ₹ prefix.
    val digitOffsets = IntArray(digits.length + 1)
    var digitIndex = 0
    grouped.forEachIndexed { index, char ->
        if (char != ',' && digitIndex < digits.length) {
            digitOffsets[digitIndex] = index + 1
            digitIndex++
        }
    }
    digitOffsets[digits.length] = grouped.length + 1

    TransformedText(
        AnnotatedString("₹$grouped"),
        object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int =
                digitOffsets[offset.coerceIn(0, digits.length)]

            override fun transformedToOriginal(offset: Int): Int {
                val clamped = offset.coerceIn(0, grouped.length + 1)
                if (clamped <= 1) return 0
                return grouped.take(clamped - 1).count { it != ',' }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
private fun InvestmentSummaryCardPreview() {
    VelvetTheme {
        InvestmentSummaryCard(
            bundleName = "Aggressive",
            investmentAmount = 10000L,
            purchaseMode = PurchaseMode.MONTHLY,
            unsupportedFundNames = emptyList(),
            selectedSipDay = 5,
            onPurchaseModeChange = {},
            onAmountChange = {},
            onSipDayClick = {},
            minAmount = 5000L,
            assetAllocation = AssetAllocationDomain(equity = 95.0, debt = 0.0, hybrid = 0.0, commodity = 5.0),
            modifier = Modifier.padding(16.dp)
        )
    }
}
