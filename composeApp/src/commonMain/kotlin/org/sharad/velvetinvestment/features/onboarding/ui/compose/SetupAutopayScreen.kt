package org.sharad.velvetinvestment.features.onboarding.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import org.sharad.emify.core.ui.theme.Black
import org.sharad.emify.core.ui.theme.Gray444
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.SetupAutopayEvent
import org.sharad.velvetinvestment.features.onboarding.ui.viewmodels.SetupAutopayUiState
import org.sharad.velvetinvestment.shared.compose.AppBackButton
import org.sharad.velvetinvestment.shared.compose.AppButton
import org.sharad.velvetinvestment.shared.compose.InfoNoteCard
import org.sharad.velvetinvestment.shared.genericDropShadow
import org.sharad.velvetinvestment.shared.theme.Spacing
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import org.sharad.velvetinvestment.utils.clearFocusOnTap
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.info_filled_icon

@Composable
fun SetupAutopayScreen(
    state: SetupAutopayUiState,
    handleEvent: (SetupAutopayEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = White)
            .padding(horizontal=Spacing.dp20)
            .clearFocusOnTap()
            .imePadding()
    ) {
        AppBackButton({})
        LazyColumn(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Spacing.dp20),
            contentPadding = PaddingValues(top = Spacing.dp8)
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(Spacing.dp12)) {
                    Text(
                        text = "Set up UPI Autopay",
                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                    )
                    Text(
                        text = "One approval, and your SIP runs on its own every month. Cancel any time.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Gray444
                    )
                }
            }
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(Spacing.dp20),
                    modifier = Modifier
                        .genericDropShadow()
                        .background(White, RoundedCornerShape(Spacing.dp24))
                        .padding(horizontal = Spacing.dp24, vertical = Spacing.dp32)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Type", style = MaterialTheme.typography.bodyLarge, color = Gray444)
                        Text(
                            text = state.autopayType,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Black
                        )
                    }
                    HorizontalDivider(
                        thickness = Spacing.dp1,
                        color = Secondary,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Limit", style = MaterialTheme.typography.bodyLarge, color = Gray444)
                        Text(
                            text = state.autopayLimit,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Black
                        )
                    }
                }
            }
            item {
                InfoNoteCard(
                    icon = Res.drawable.info_filled_icon,
                    title = "Important Note",
                    subtitle = "Your bank statement and UPI app will show Cybrilla — the official partner processing these mutual-fund transactions for Janta Nivesh."
                )
            }
            item{
                AppButton(
                    text = "Set Autopay",
                    onClick = { handleEvent(SetupAutopayEvent.OnSetAutopayClick) },
                    loading = state.isLoading,
                    modifier = Modifier.fillMaxWidth().padding(top = Spacing.dp24).genericDropShadow()
                )
            }
        }
    }
}

@Preview(locale = "hi")
@Composable
private fun SetupAutopayScreenPreview() {
    VelvetTheme {
        SetupAutopayScreen(
            state = SetupAutopayUiState(),
            handleEvent = {}
        )
    }
}