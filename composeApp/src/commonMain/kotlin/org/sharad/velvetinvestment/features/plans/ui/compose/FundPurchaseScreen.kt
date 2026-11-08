package org.sharad.velvetinvestment.features.plans.ui.compose

import org.sharad.velvetinvestment.shared.theme.largeTextStyle
import org.sharad.velvetinvestment.shared.theme.subHeadingMedium
import org.sharad.velvetinvestment.shared.theme.titlesStyle
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.emify.core.ui.theme.lightGray
import org.sharad.emify.core.ui.theme.backgroundGray
import org.sharad.velvetinvestment.shared.theme.subHeading
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Black
import org.sharad.emify.core.ui.theme.Gray444
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.SelectedBoxColor
import org.sharad.velvetinvestment.features.core.domain.models.PurchaseMode
import org.sharad.velvetinvestment.features.plans.domain.model.MandateOption
import org.sharad.velvetinvestment.features.plans.domain.model.SchemePlan
import org.sharad.velvetinvestment.features.plans.domain.model.SipThreshold
import org.sharad.velvetinvestment.features.mutualfund.ui.compose.KYCPopup
import org.sharad.velvetinvestment.shared.compose.ErrorScreen
import org.sharad.velvetinvestment.shared.theme.Spacing
import org.sharad.velvetinvestment.features.plans.ui.viewmodels.FundPurchaseEvent
import org.sharad.velvetinvestment.features.plans.ui.viewmodels.FundPurchaseUiState
import org.sharad.velvetinvestment.features.plans.ui.viewmodels.KEYPAD_BACKSPACE
import org.sharad.velvetinvestment.shared.compose.AppButton
import org.sharad.velvetinvestment.shared.compose.AppButtonDefaults
import org.sharad.velvetinvestment.shared.genericDropShadow
import org.sharad.velvetinvestment.shared.theme.LocalVelvetShapes
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.back_arrow
import velvet.composeapp.generated.resources.dob_dropdown_icon
import velvet.composeapp.generated.resources.dropdown_icon
import velvet.composeapp.generated.resources.ic_graph
import velvet.composeapp.generated.resources.icon_arrow_right
import velvet.composeapp.generated.resources.wallet_icon
import kotlin.time.Duration.Companion.milliseconds

/**
 * One screen for all three ways of buying a fund (daily SIP, monthly SIP, one-time). The mode tabs
 * change what the middle of the screen asks for — a monthly SIP needs a debit day, the other two do not — while the amount
 * keypad and the confirm button stay put at the bottom, so the primary action never moves.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FundPurchaseScreen(
    state: FundPurchaseUiState,
    handleEvent: (FundPurchaseEvent) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (state.showDayPicker) {
        PurchaseDayPickerSheet(
            selectedDay = state.installmentDay,
            allowedDays = state.debitDays,
            onDaySelected = { handleEvent(FundPurchaseEvent.OnDaySelected(it)) },
            onDismiss = { handleEvent(FundPurchaseEvent.OnDayPickerDismiss) }
        )
    }

    if (state.showMandateSheet) {
        SelectMandateSheet(
            mandates = state.mandates,
            selectedMandateId = state.selectedMandateId,
            isLoading = state.isLoadingMandates,
            onMandateSelected = { handleEvent(FundPurchaseEvent.OnMandateSelected(it)) },
            onAddMandateClick = { handleEvent(FundPurchaseEvent.OnAddMandateClick) },
            onConfirm = { handleEvent(FundPurchaseEvent.OnMandateConfirm) },
            onDismiss = { handleEvent(FundPurchaseEvent.OnMandateSheetDismiss) }
        )
    }

    if (state.showOtpSheet) {
        PurchaseOtpSheet(
            state = state,
            handleEvent = handleEvent
        )
    }

    if (state.showKycPopup) {
        KYCPopup(
            sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
            isKycInProgress = state.isKycInProgress,
            onDismiss = { handleEvent(FundPurchaseEvent.OnKycPopupDismiss) },
            onClick = { handleEvent(FundPurchaseEvent.OnCompleteKycClick) }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundGray)
    ) {
        PurchaseHeader(
            title = state.headerTitle,
            subtitle = state.fundSubtitle,
            modes = state.availableModes,
            selectedMode = state.mode,
            onModeSelected = { handleEvent(FundPurchaseEvent.OnModeSelected(it)) },
            onBackClick = onBackClick
        )

        when {
            state.isLoadingScheme -> Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Primary)
            }

            state.loadError != null -> ErrorScreen(
                state.loadError,
                onRetryClick = { handleEvent(FundPurchaseEvent.OnRetryLoad) }
            )

            else -> PurchaseForm(
                state = state,
                handleEvent = handleEvent,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun PurchaseForm(
    state: FundPurchaseUiState,
    handleEvent: (FundPurchaseEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            AmountCard(
                amount = state.amount,
                suggestedAmounts = state.suggestedAmounts,
                onSuggestedAmountClick = {
                    handleEvent(FundPurchaseEvent.OnSuggestedAmountClick(it))
                }
            )

            if (state.mode.needsInstallmentDay) {
                DebitDayCard(
                    day = state.installmentDay,
                    expectedNavDate = state.expectedNavDate.toShortDateLabel(),
                    onClick = { handleEvent(FundPurchaseEvent.OnDayFieldClick) }
                )
            }

            // A one-time buy is settled then and there, so there is no mandate to debit.
//            if (state.mode.isSip) {
//                MandateCard(
//                    mandate = state.selectedMandate,
//                    onClick = { handleEvent(FundPurchaseEvent.OnMandateFieldClick) }
//                )
//            }

            state.amountError?.let { error ->
                Text(
                    text = error,
                    style = titlesStyle.copy(fontSize = 12.sp),
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        }

        AmountKeypad(
            onKeyPress = { handleEvent(FundPurchaseEvent.OnKeypadPress(it)) },
            modifier = Modifier.padding(horizontal = 16.dp)
        )

        AppButton(
            text = state.submissionStage?.message ?: when (state.mode) {
                PurchaseMode.DAILY -> "Start Daily SIP" + " · ₹${state.enteredAmount}/day"
                PurchaseMode.MONTHLY -> "Start Monthly SIP" + " · ₹${state.enteredAmount}/month"
                PurchaseMode.ONE_TIME -> "Invest Now " + " · ₹${state.enteredAmount}"
            },
            onClick = { handleEvent(FundPurchaseEvent.OnSubmitClick) },
            loading = state.isSubmitting,
            enabled = state.canSubmit,
            style = AppButtonDefaults.style(height = 58.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 16.dp,
                    end = 16.dp,
                    top = 16.dp,
                    bottom = 16.dp
                )
        )
    }
}

/** Back arrow, fund identity and the mode tabs — the white block pinned above the form. */
@Composable
private fun PurchaseHeader(
    title: String,
    subtitle: String,
    modes: List<PurchaseMode>,
    selectedMode: PurchaseMode,
    onModeSelected: (PurchaseMode) -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(White)
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier.size(36.dp)
                    .clip(CircleShape)
                    .background(backgroundGray)
                    .clickable(
                        onClick = onBackClick,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.back_arrow),
                    contentDescription = "Back",
                    tint = Primary,
                    modifier = Modifier.size(16.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelSmall,
                    color = Primary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                if (subtitle.isNotBlank()) {
                    Text(
                        text = subtitle,
                        style = titlesStyle,
                        color = titleColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        PurchaseModeTabs(
            modes = modes,
            selectedMode = selectedMode,
            onModeSelected = onModeSelected
        )
    }
}

/** Segmented control: one filled pill for the active mode, the rest on a plain grey track. */
@Composable
private fun PurchaseModeTabs(
    modes: List<PurchaseMode>,
    selectedMode: PurchaseMode,
    onModeSelected: (PurchaseMode) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        modes.forEach { mode ->
            val selected = mode == selectedMode

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(LocalVelvetShapes.current.circle)
                    .background(if (selected) Primary else White)
                    .border(color = if (selected) Primary else Secondary, width = 1.dp, shape = LocalVelvetShapes.current.circle)
                    .clickable(
                        onClick = { onModeSelected(mode) },
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = mode.label,
                    style = titlesStyle.copy(
                        fontWeight = if (selected) FontWeight.Medium else FontWeight.Normal
                    ),
                    color = if (selected) White else Gray444,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
                )
            }
        }
    }
}

/** The amount the keypad feeds, with the quick-pick chips derived from the fund's minimum. */
@Composable
private fun AmountCard(
    amount: String,
    suggestedAmounts: List<Int>,
    onSuggestedAmountClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .genericDropShadow(RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(White)
            .padding(vertical = 20.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Investment Amount",
            style = titlesStyle,
            color = titleColor
        )

        AmountDisplay(amount = amount)

        AmountChips(
            amounts = suggestedAmounts,
            selectedAmount = amount.toIntOrNull(),
            onClick = onSuggestedAmountClick
        )
    }
}

@Composable
private fun AmountDisplay(
    amount: String
) {
    var cursorVisible by remember { mutableStateOf(true) }

    LaunchedEffect(amount) {
        cursorVisible = true

        while (true) {
            delay(500.milliseconds)
            cursorVisible = !cursorVisible
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "₹ ",
            style = MaterialTheme.typography.headlineMedium,
            color = Primary
        )

        Text(
            text = amount.ifEmpty { "0" },
            style = largeTextStyle,
            color = if (amount.isEmpty()) Secondary else Primary
        )

        // Cursor
        Box(
            modifier = Modifier
                .padding(start = 1.dp)
                .width(1.5.dp)
                .height(40.dp)
                .alpha(if (cursorVisible) 1f else 0f)
                .background(Primary)
        )
    }
}

/**
 * Chips wrap onto a second row rather than scrolling sideways, which is how the four defaults
 * end up as three-then-one on a narrow screen.
 */
@Composable
private fun AmountChips(
    amounts: List<Int>,
    selectedAmount: Int?,
    onClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        amounts.chunked(CHIPS_PER_ROW).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                row.forEach { amount ->
                    AmountChip(
                        amount = amount,
                        selected = amount == selectedAmount,
                        onClick = { onClick(amount) }
                    )
                }
            }
        }
    }
}

private const val CHIPS_PER_ROW = 3

@Composable
private fun AmountChip(
    amount: Int,
    selected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(CircleShape)
            .background(if (selected) Primary else White)
            .border(
                width = 1.dp,
                color = if (selected) Primary else Secondary,
                shape = CircleShape
            )
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
            .padding(horizontal = 20.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "₹${amount.withThousandSeparators()}",
            style = titlesStyle.copy(fontWeight = FontWeight.Medium),
            color = if (selected) White else Black
        )
    }
}

/** Selected debit day, with the NAV date it implies shown underneath. */
@Composable
private fun DebitDayCard(
    day: Int?,
    expectedNavDate: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .genericDropShadow(RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(White)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DebitDayField(day = day, onClick = onClick)

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(start = 4.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_graph),
                contentDescription = null,
                tint = titleColor,
                modifier = Modifier.size(16.dp)
            )
            Text(
                text = "Expected NAV date: $expectedNavDate",
                style = titlesStyle.copy(fontSize = 12.sp),
                color = titleColor
            )
        }
    }
}

/**
 * The tappable monthly debit-day field on its own, for screens that pick a SIP day without the
 * fund-level details [DebitDayCard] adds — the bundle investment summary uses it this way.
 */
@Composable
fun DebitDayField(
    day: Int?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .border(
                width = 1.dp,
                color = Secondary,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(32.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(SelectedBoxColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.dob_dropdown_icon),
                contentDescription = null,
                tint = Secondary,
                modifier = Modifier.size(16.dp)
            )
        }

        Text(
            text = day?.let { "Monthly on ${it.withOrdinalSuffix()}" } ?: "Choose a debit day",
            style = MaterialTheme.typography.displayLarge,
            color = if (day == null) titleColor else Primary,
            modifier = Modifier.weight(1f)
        )

        Icon(
            painter = painterResource(Res.drawable.dropdown_icon),
            contentDescription = null,
            tint = Gray444,
            modifier = Modifier.size(14.dp)
        )
    }
}

/** The mandate the SIP will be debited against — a mandate, not a bank account. */
@Composable
private fun MandateCard(
    mandate: MandateOption?,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .genericDropShadow(RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(White)
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(SelectedBoxColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.wallet_icon),
                contentDescription = null,
                tint = Secondary,
                modifier = Modifier.size(20.dp)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Pay via Mandate",
                style = MaterialTheme.typography.labelSmall,
                color = Black
            )
            Text(
                // Naming the mandate and its ceiling is what makes a change visible: there is no
                // select-mandate call yet, so this row is the whole feedback for the choice.
                text = mandate?.let { "${it.displayName} · ${it.subtitle}" }
                    ?: "Set up autopay to start a SIP",
                style = titlesStyle,
                color = titleColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        Box(
            modifier = Modifier.size(24.dp)
                .clip(CircleShape)
                .background(backgroundGray),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.icon_arrow_right),
                contentDescription = null,
                tint = Gray444,
                modifier = Modifier.size(12.dp)
            )
        }
    }
}

/**
 * The on-screen keypad. The amount field is never focusable, so this is the only way to type an
 * amount — which keeps the system IME from covering the summary the user is checking.
 */
@Composable
private fun AmountKeypad(
    onKeyPress: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth().padding(top= 4.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        KEYPAD_ROWS.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                row.forEach { key ->
                    KeypadKey(
                        key = key,
                        onClick = { onKeyPress(key) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun KeypadKey(
    key: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isBackspace = key == KEYPAD_BACKSPACE

    Box(
        modifier = modifier
            .height(44.dp)
            .clip(RoundedCornerShape(10.dp))
            .background(if (isBackspace) lightGray else White)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (isBackspace) "⌫" else key,
            style = subHeadingMedium,
            color = Black
        )
    }
}

private val KEYPAD_ROWS = listOf(
    listOf("1", "2", "3"),
    listOf("4", "5", "6"),
    listOf("7", "8", "9"),
    listOf(".", "0", KEYPAD_BACKSPACE)
)

@Preview(showBackground = true, heightDp = 900)
@Composable
private fun FundPurchaseScreenPreview() {
    VelvetTheme {
        FundPurchaseScreen(
            state = FundPurchaseUiState(
                fundName = "SBI Gold Fund",
                fundSubtitle = "High Risk · Commodities · Gold",
                scheme = previewScheme,
                isLoadingScheme = false,
                installmentDay = 13,
                amount = "500"
            ),
            handleEvent = {},
            onBackClick = {}
        )
    }
}

private val previewMonthlySip = SipThreshold(
    amountMin = 500,
    amountMax = 999_999_999,
    amountMultiples = 1,
    installmentsMin = 6,
    dates = SipThreshold.ALL_DEBIT_DAYS
)

private val previewDailySip = SipThreshold(
    amountMin = 30,
    amountMax = 999_999_999,
    amountMultiples = 1,
    installmentsMin = 10,
    dates = SipThreshold.ALL_DEBIT_DAYS
)

private val previewLumpsum = SipThreshold(
    amountMin = 100,
    amountMax = 999_999_999,
    amountMultiples = 1,
    installmentsMin = 0,
    dates = emptyList(),
    additionalAmountMin = 100
)

private val previewScheme = SchemePlan(
    id = "cmt83a057005583ricu8zd443",
    isin = "INF209K01RU9",
    schemeName = "SBI Gold Fund",
    fundName = "SBI Mutual Fund",
    option = "growth",
    planType = "regular",
    monthlySip = previewMonthlySip,
    dailySip = previewDailySip,
    lumpsum = previewLumpsum
)
