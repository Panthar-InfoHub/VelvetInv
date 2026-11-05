package org.sharad.velvetinvestment.features.plans.ui.compose

import org.sharad.velvetinvestment.shared.theme.titlesStyle
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.emify.core.ui.theme.lightGray
import org.sharad.emify.core.ui.theme.backgroundGray
import androidx.compose.ui.unit.dp
import org.sharad.velvetinvestment.shared.theme.subHeading
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Black
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.SelectedBoxColor
import org.sharad.velvetinvestment.features.plans.domain.model.MandateOption
import org.sharad.velvetinvestment.shared.compose.AppButton
import org.sharad.velvetinvestment.shared.compose.AppButtonDefaults
import org.sharad.velvetinvestment.shared.theme.Spacing
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.check_circle_outline_icon
import velvet.composeapp.generated.resources.wallet_icon

/**
 * Picks the autopay mandate the SIP debits against.
 *
 * The rows name mandates, not bank accounts: a user can hold several mandates with different
 * limits, and it is the mandate the gateway attaches the SIP to. Only approved mandates are
 * listed — anything still with the bank cannot carry a debit, so offering it would only produce
 * a rejection later.
 *
 * There is no select-mandate call yet, so choosing one is a local change: the row and the
 * payment card update, and nothing is sent until that API exists.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SelectMandateSheet(
    mandates: List<MandateOption>,
    selectedMandateId: String?,
    isLoading: Boolean,
    onMandateSelected: (String) -> Unit,
    onAddMandateClick: () -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 20.dp,
                    end = 20.dp,
                    bottom = 32.dp
                ),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            SheetHeader()

            when {
                isLoading && mandates.isEmpty() -> Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(72.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Primary)
                }

                mandates.isEmpty() -> NoMandatesState()

                else -> Column {
                    mandates.forEachIndexed { index, mandate ->
                        MandateRow(
                            mandate = mandate,
                            selected = mandate.id == selectedMandateId,
                            onClick = { onMandateSelected(mandate.id) }
                        )
                        if (index != mandates.lastIndex) {
                            HorizontalDivider(
                                thickness = 1.dp,
                                color = backgroundGray
                            )
                        }
                    }
                }
            }

            if (mandates.isEmpty() && !isLoading) {
                AppButton(
                    text = "Set Up Autopay",
                    onClick = onAddMandateClick,
                    style = AppButtonDefaults.style(height = 58.dp),
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                AppButton(
                    text = "Confirm",
                    onClick = onConfirm,
                    enabled = selectedMandateId != null,
                    style = AppButtonDefaults.style(height = 58.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Add another mandate",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.SemiBold
                    ),
                    color = Secondary,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            onClick = onAddMandateClick,
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        )
                )
            }
        }
    }
}

@Composable
private fun SheetHeader() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(SelectedBoxColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.check_circle_outline_icon),
                contentDescription = null,
                tint = Secondary,
                modifier = Modifier.size(20.dp)
            )
        }
        Text(
            text = "Select mandate",
            style = MaterialTheme.typography.headlineSmall,
            color = Black
        )
    }
}

/** Shown when the user holds no approved mandate — the only way forward is to set one up. */
@Composable
private fun NoMandatesState() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "No approved mandate yet",
            style = MaterialTheme.typography.labelSmall,
            color = Black
        )
        Text(
            text = "A SIP debits through a UPI autopay mandate. Set one up once and your " +
                    "instalments run on their own.",
            style = titlesStyle,
            color = titleColor,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun MandateRow(
    mandate: MandateOption,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
            .padding(vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(if (selected) SelectedBoxColor else backgroundGray),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.wallet_icon),
                contentDescription = null,
                tint = if (selected) Secondary else Primary,
                modifier = Modifier.size(20.dp)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = mandate.displayName,
                style = MaterialTheme.typography.labelSmall,
                color = Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = mandate.subtitle,
                style = titlesStyle,
                color = titleColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        RadioDot(selected = selected)
    }
}

/** Single-choice marker, drawn rather than themed so it matches the rest of the sheet. */
@Composable
private fun RadioDot(selected: Boolean) {
    Box(
        modifier = Modifier
            .size(24.dp)
            .clip(CircleShape)
            .border(
                width = 2.dp,
                color = if (selected) Secondary else lightGray,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        if (selected) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .clip(CircleShape)
                    .background(Secondary)
            )
        }
    }
}

/** "Up to ₹1,00,000 per debit · Cybrillapoa" — the limit is what tells two mandates apart. */
internal val MandateOption.subtitle: String
    get() = listOfNotNull(
        limit?.let { "Up to ₹${it.toInt().withThousandSeparators()} per debit" },
        providerName.takeIf { it.isNotBlank() }?.lowercase()?.replaceFirstChar { it.uppercase() }
    ).joinToString(" · ").ifBlank { "Autopay mandate" }

@Preview(showBackground = true)
@Composable
private fun MandateRowPreview() {
    VelvetTheme {
        Column(modifier = Modifier.background(White).padding(20.dp)) {
            MandateRow(
                mandate = MandateOption(
                    id = "32",
                    mandateType = "UPI",
                    providerName = "CYBRILLAPOA",
                    status = MandateOption.APPROVED,
                    limit = 100_000,
                    umrn = "HDFC0000123456",
                    startDate = "2026-08-07T00:00:00.000Z"
                ),
                selected = true,
                onClick = {}
            )
            MandateRow(
                mandate = MandateOption(
                    id = "33",
                    mandateType = "UPI",
                    providerName = "CYBRILLAPOA",
                    status = MandateOption.APPROVED,
                    limit = 25_000,
                    umrn = null,
                    startDate = null
                ),
                selected = false,
                onClick = {}
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun NoMandatesStatePreview() {
    VelvetTheme {
        Column(modifier = Modifier.background(White).padding(20.dp)) {
            SheetHeader()
            NoMandatesState()
        }
    }
}
