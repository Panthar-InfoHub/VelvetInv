package org.sharad.velvetinvestment.features.portfolio.ui.screens

import androidx.compose.ui.unit.dp
import org.sharad.emify.core.ui.theme.Black
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.appRed
import org.sharad.emify.core.ui.theme.backgroundGray
import org.sharad.emify.core.ui.theme.lightGray
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import org.sharad.velvetinvestment.shared.theme.subHeading
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.info_filled_icon
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.GoalIconBg
import org.sharad.emify.core.ui.theme.Gray444
import org.sharad.velvetinvestment.utils.formatWithCommas
import org.sharad.velvetinvestment.shared.compose.AppButton
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.TitledAppTextField
import org.sharad.velvetinvestment.utils.clearFocusOnTap
import org.sharad.velvetinvestment.features.portfolio.ui.viewmodel.RedeemEvent
import org.sharad.velvetinvestment.features.portfolio.ui.viewmodel.RedeemHolding
import org.sharad.velvetinvestment.features.portfolio.ui.viewmodel.RedeemMode
import org.sharad.velvetinvestment.features.portfolio.ui.viewmodel.RedeemUiState

/**
 * How much of one holding to take out.
 *
 * The holding arrives from the order-details screen, so there is nothing to load and nothing to
 * choose between — the screen opens straight on the controls.
 */
@Composable
fun RedeemScreen(
    state: RedeemUiState,
    handleEvent: (RedeemEvent) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .clearFocusOnTap()
            .imePadding()
    ) {
        BackHeader(
            title = "Redeem",
            onBack = onBack,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            item {
                InfoBanner(
                    text = "Money goes back to your registered bank account only."
                )
            }

            item { HoldingCard(holding = state.holding) }

            item {
                RedeemByPicker(
                    selected = state.mode,
                    onSelected = { handleEvent(RedeemEvent.OnModeSelected(it)) }
                )
            }

            item {
                RedeemInput(
                    state = state,
                    handleEvent = handleEvent
                )
            }
        }

        AppButton(
            text = "Redeem Now",
            onClick = { handleEvent(RedeemEvent.OnRedeemClick) },
            loading = state.isRedeeming,
            enabled = state.canRedeem,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp)
        )
    }
}

/** The typed field for Amount/Units, or the confirmation note when redeeming everything. */
@Composable
private fun RedeemInput(
    state: RedeemUiState,
    handleEvent: (RedeemEvent) -> Unit
) {
    val holding = state.holding
    val error = state.inputError

    when (state.mode) {
        RedeemMode.AMOUNT -> TitledAppTextField(
            title = "Amount to redeem",
            value = state.amountInput,
            onValueChange = { handleEvent(RedeemEvent.OnAmountChange(it)) },
            placeholder = "0",
            mandatory = true,
            keyboardType = KeyboardType.Decimal,
            isError = error != null,
            prefix = {
                Text("₹ ", style = subHeading, color = Black)
            },
            supportingText = {
                FieldHint(
                    error = error,
                    hint = "Available: ₹${formatWithCommas(holding.currentValue.toLong())}"
                )
            }
        )

        RedeemMode.UNITS -> TitledAppTextField(
            title = "Units to redeem",
            value = state.unitsInput,
            onValueChange = { handleEvent(RedeemEvent.OnUnitsChange(it)) },
            placeholder = "0.000",
            mandatory = true,
            keyboardType = KeyboardType.Decimal,
            isError = error != null,
            supportingText = {
                FieldHint(
                    error = error,
                    hint = "Available: ${holding.availableUnits} units"
                )
            }
        )

        RedeemMode.ALL_UNITS -> InfoBanner(
            text = "Redeeming all ${holding.availableUnits} units from folio " +
                    "${holding.folioNumber}."
        )
    }
}

/** The limit while the field is fine, the reason it is not once it isn't. */
@Composable
private fun FieldHint(error: String?, hint: String) {
    Text(
        text = error ?: hint,
        style = MaterialTheme.typography.bodySmall,
        color = if (error != null) appRed else titleColor
    )
}

@Composable
private fun InfoBanner(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(GoalIconBg)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            painter = painterResource(Res.drawable.info_filled_icon),
            contentDescription = null,
            tint = Primary,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = text,
            style = subHeading,
            color = Gray444
        )
    }
}

/** What is being redeemed from, restated so the user is not working from memory. */
@Composable
private fun HoldingCard(holding: RedeemHolding) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = lightGray,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = holding.scheme,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = Black
        )

        // The pill now says what kind of holding this is, which is the thing the user cares
        // about here — the gateway's own state has no bearing on redeeming.
        StatePill(state = if (holding.isSip) "SIP" else "LUMPSUM")

        HorizontalDivider(thickness = 1.dp, color = backgroundGray)

        HoldingRow(label = "Folio Number", value = holding.folioNumber)
        HoldingRow(label = "Available Units", value = holding.availableUnits.toString())
        HoldingRow(
            label = "Current Value",
            value = "₹${formatWithCommas(holding.currentValue.toLong())}"
        )
    }
}

@Composable
private fun StatePill(state: String) {
    if (state.isBlank()) return

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Secondary)
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        Text(
            text = state.uppercase(),
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
            color = Color.White
        )
    }
}

@Composable
private fun HoldingRow(label: String, value: String) {
    if (value.isBlank()) return

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.displayLarge,
            color = Gray444
        )
        Text(
            text = value,
            style = MaterialTheme.typography.displayLarge.copy(fontWeight = FontWeight.Bold),
            color = Black,
            textAlign = TextAlign.End,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun RedeemByPicker(
    selected: RedeemMode,
    onSelected: (RedeemMode) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Redeem by",
            style = subHeading.copy(fontWeight = FontWeight.Bold),
            color = Black
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp))
                .border(
                    width = 1.dp,
                    color = lightGray,
                    shape = RoundedCornerShape(8.dp)
                )
        ) {
            RedeemMode.entries.forEach { mode ->
                val isSelected = mode == selected
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .background(if (isSelected) Primary else Color.White)
                        .clickable { onSelected(mode) }
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = mode.label,
                        style = MaterialTheme.typography.displayLarge,
                        color = if (isSelected) Color.White else Black
                    )
                }
            }
        }
    }
}

private val sampleHolding = RedeemHolding(
    holdingId = "cmtl875r60000bbri2ce7qpa9",
    scheme = "Aditya Birla Sun Life Gold Fund-Growth",
    folioNumber = "1051586674",
    availableUnits = 2.311,
    currentValue = 100.0,
    isSip = true
)

@Preview(showBackground = true)
@Composable
private fun RedeemScreenPreview() {
    VelvetTheme {
        RedeemScreen(
            state = RedeemUiState(holding = sampleHolding),
            handleEvent = {},
            onBack = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun RedeemScreenByAmountPreview() {
    VelvetTheme {
        RedeemScreen(
            state = RedeemUiState(
                holding = sampleHolding,
                mode = RedeemMode.AMOUNT,
                amountInput = "1000"
            ),
            handleEvent = {},
            onBack = {}
        )
    }
}
