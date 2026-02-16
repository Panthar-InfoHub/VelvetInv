package org.sharad.velvetinvestment.presentation.portfolio.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.sharad.velvetinvestment.presentation.onboarding.compose.personaldetails.NextButtonFooter
import org.sharad.velvetinvestment.presentation.portfolio.models.stopSipReasons
import org.sharad.velvetinvestment.shared.compose.ErrorScreen
import org.sharad.velvetinvestment.shared.compose.LoaderScreen
import org.sharad.velvetinvestment.shared.compose.ShadowCard
import org.sharad.velvetinvestment.shared.compose.SidedBackHeader
import org.sharad.velvetinvestment.utils.UIState

@Composable
fun SIPCancellationReasonScreen(
    onBackClick: () -> Unit,
    id: String,
    onConfirmClick: (String) -> Unit,
    pv: PaddingValues
){

    var selectedReasonId by remember { mutableStateOf<Int?>(null) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        SidedBackHeader(
            heading = "SIP Details",
            showBack = true,
            onBackClick = onBackClick,
        )
        LazyColumn(
            modifier = Modifier.weight(1f)
                .fillMaxSize().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        )
        {

            item { Spacer(Modifier) }

            item {
                ShadowCard {
                    Text(
                        text="We're sorry to see you go. Please help us understand why you want to cancel your SIP.",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(0xff364153),
                        modifier=Modifier.padding(16.dp)
                    )
                }
            }

            item {
                Text("Select reason for cancellation",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = Color.Black
                )
            }

            item {

                StopSipReasonSelection(
                    reasons = stopSipReasons,
                    selectedId = selectedReasonId,
                    onReasonSelected = {
                        selectedReasonId = it.id
                    }
                )

            }

            item { Spacer(Modifier.height(16.dp)) }
        }

        NextButtonFooter(
            value = "Continue",
            onClick = {},
            pv = pv
        )
    }

}