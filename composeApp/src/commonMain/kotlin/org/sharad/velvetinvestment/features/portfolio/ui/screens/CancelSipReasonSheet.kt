package org.sharad.velvetinvestment.features.portfolio.ui.screens

import androidx.compose.ui.unit.dp
import org.sharad.emify.core.ui.theme.Black
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.appRed
import org.sharad.emify.core.ui.theme.lightGray
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import org.sharad.velvetinvestment.shared.theme.subHeading
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import velvet.composeapp.generated.resources.Res
import org.sharad.velvetinvestment.shared.compose.AppButton
import org.sharad.velvetinvestment.shared.compose.AppButtonDefaults
import org.sharad.velvetinvestment.features.portfolio.ui.models.SipCancellationReason

/**
 * Collects why the user is stopping a SIP, as a sheet rather than a screen: it is one required
 * choice, and keeping it over the order details means backing out costs nothing and leaves the
 * figures they were reading still on screen behind it.
 *
 * Confirming is blocked until a reason is picked — the gateway will not take a cancellation
 * without one of its codes.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CancelSipReasonSheet(
    sheetState: SheetState,
    selectedReason: SipCancellationReason?,
    isSubmitting: Boolean,
    onReasonSelected: (SipCancellationReason) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "Why are you cancelling?",
                    style = MaterialTheme.typography.labelSmall
                        .copy(fontWeight = FontWeight.Bold),
                    color = Black
                )
                Text(
                    text = "Pick a reason to cancel this SIP. This cannot be undone.",
                    style = subHeading,
                    color = titleColor
                )
            }

            // Capped rather than free-flowing: the list is long enough to push the confirm
            // button off a short screen otherwise.
            LazyColumn(
                modifier = Modifier.heightIn(max = 285.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(SipCancellationReason.entries) { reason ->
                    ReasonRow(
                        reason = reason,
                        isSelected = reason == selectedReason,
                        onClick = { onReasonSelected(reason) }
                    )
                }
            }

            AppButton(
                text = "Cancel SIP",
                onClick = onConfirm,
                enabled = selectedReason != null && !isSubmitting,
                loading = isSubmitting,
                style = AppButtonDefaults.style(containerColor = appRed),
                modifier = Modifier.fillMaxWidth()
            )

            Text(
                text = "Keep it running",
                style = subHeading,
                color = Secondary,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .clickable(
                        onClick = onDismiss,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    )
                    .padding(8.dp)
            )
        }
    }
}

@Composable
private fun ReasonRow(
    reason: SipCancellationReason,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) Secondary else lightGray,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(end = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(selectedColor = Secondary)
        )
        Text(
            text = reason.label,
            style = MaterialTheme.typography.displayLarge,
            color = Black
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true, locale = "hi")
@Composable
private fun CancelSipReasonSheetPreview() {
    VelvetTheme {
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            SipCancellationReason.entries.take(3).forEach { reason ->
                ReasonRow(
                    reason = reason,
                    isSelected = reason == SipCancellationReason.INVEST_LATER,
                    onClick = {}
                )
            }
        }
    }
}
