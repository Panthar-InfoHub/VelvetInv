package org.sharad.velvetinvestment.features.plans.ui.compose

import org.sharad.velvetinvestment.shared.theme.titlesStyle
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.shared.theme.subHeading
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.velvetinvestment.shared.compose.AppButton
import org.sharad.velvetinvestment.shared.compose.InvertedAppButton
import org.sharad.velvetinvestment.shared.theme.Spacing
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import org.sharad.velvetinvestment.features.plans.ui.viewmodels.SipSetupStage
import org.sharad.velvetinvestment.features.plans.ui.viewmodels.SipSetupUiState
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.ic_jagged_arrow

/**
 * What the user watches between approving their mandate and typing the OTP. There is nothing to
 * do here, so the screen only reports which step is running. When a wait runs out with the
 * mandate or SIP still pending, it offers to check that status again — never to redo the setup.
 * A real failure has nothing left to check, so the user can only go back.
 */
@Composable
fun SipSetupScreen(
    state: SipSetupUiState,
    schemeName: String,
    onCheckStatusClick: () -> Unit,
    onGoBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(White)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (state.error == null) {
            MandateLoader()

            Text(
                text = state.stage.message,
                style = MaterialTheme.typography.headlineSmall,
                color = Primary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 32.dp)
            )

            Text(
                text = "Please stay on this screen — this can take up to a minute.",
                style = titlesStyle,
                color = titleColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )
        } else {
            Text(
                text = schemeName.ifBlank { "Your SIP" },
                style = MaterialTheme.typography.headlineSmall,
                color = Primary,
                textAlign = TextAlign.Center
            )

            Text(
                text = state.error,
                style = titlesStyle,
                color = titleColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 12.dp, bottom = 32.dp)
            )

            val goBackText = "Go Back"

            if (state.canCheckStatus) {
                AppButton(
                    text = "Check Status",
                    onClick = onCheckStatusClick,
                    modifier = Modifier.fillMaxWidth()
                )

                InvertedAppButton(
                    text = goBackText,
                    onClick = onGoBackClick,
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
                )
            } else {
                // The only way on, so it takes the primary slot.
                AppButton(
                    text = goBackText,
                    onClick = onGoBackClick,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun MandateLoader(){
    Box(
        contentAlignment = Alignment.Center
    )
    {

        CircularProgressIndicator(
            modifier = Modifier.size(92.dp).graphicsLayer { scaleX = -1f },
            color = Secondary,
            strokeWidth = 4.dp
        )
        CircularProgressIndicator(
            modifier = Modifier.size(108.dp),
            color = Primary,
            strokeWidth = 4.dp
        )

        Box(
            modifier = Modifier.size(72.dp)
                .clip(CircleShape)
                .background(Primary),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_jagged_arrow),
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = White
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun SipSetupScreenPreview() {
    VelvetTheme {
        SipSetupScreen(
            state = SipSetupUiState(stage = SipSetupStage.AWAITING_REVIEW),
            schemeName = "SBI Gold Fund",
            onCheckStatusClick = {},
            onGoBackClick = {}
        )
    }
}

@Preview(showBackground = true, locale = "hi")
@Composable
private fun SipSetupScreenErrorPreview() {
    VelvetTheme {
        SipSetupScreen(
            state = SipSetupUiState(
                stage = SipSetupStage.CONFIRMING_MANDATE,
                error = "Your bank has not approved the autopay mandate yet. This can take a " +
                        "few minutes — tap Check Status to see if it has gone through. You will " +
                        "not need to set up the mandate again.",
                canCheckStatus = true
            ),
            schemeName = "SBI Gold Fund",
            onCheckStatusClick = {},
            onGoBackClick = {}
        )
    }
}

@Preview(showBackground = true, locale = "hi")
@Composable
private fun SipSetupScreenFailedPreview() {
    VelvetTheme {
        SipSetupScreen(
            state = SipSetupUiState(
                stage = SipSetupStage.AWAITING_REVIEW,
                error = "This SIP could not be set up. Please go back and start your SIP again."
            ),
            schemeName = "SBI Gold Fund",
            onCheckStatusClick = {},
            onGoBackClick = {}
        )
    }
}
