package org.sharad.velvetinvestment.presentation.homescreen.compose

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.appRed
import org.sharad.emify.core.ui.theme.appYellow
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.shared.domain.models.OnboardingPrompt
import org.sharad.velvetinvestment.shared.genericDropShadow
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import org.sharad.velvetinvestment.shared.theme.titlesStyle
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.arrow_right
import velvet.composeapp.generated.resources.kyc_icon
import velvet.composeapp.generated.resources.refresh_icon

/**
 * Home's "finish setting up" card, in Velvet's look. JN's KYC card behaviour on top:
 * an optional [subtitle], an [accentColor] per prompt, and either a tappable card ([onClick],
 * ending in an arrow) or a notice with a refresh icon ([onRefreshClick]) that spins while
 * [refreshing]. With neither it is a plain notice.
 */
@Composable
fun KYCCard(
    text: String,
    onClick: (() -> Unit)?,
    subtitle: String? = null,
    accentColor: Color = appRed,
    onRefreshClick: (() -> Unit)? = null,
    refreshing: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .then(
                if (subtitle == null) Modifier.height(76.dp)
                else Modifier.heightIn(min = 76.dp).height(IntrinsicSize.Min)
            )
            .genericDropShadow()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White, RoundedCornerShape(12.dp))
            .then(if (onClick != null) Modifier.clickable(onClick = onClick) else Modifier),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier.fillMaxHeight(0.8f)
                .width(2.dp)
                .clip(
                    RoundedCornerShape(
                        topStartPercent = 50,
                        bottomStartPercent = 50,
                        topEndPercent = 20,
                        bottomEndPercent = 20
                    )
                )
                .background(accentColor)
        )

        Box(
            modifier = Modifier.size(40.dp).clip(RoundedCornerShape(10.dp))
                .background(accentColor.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.kyc_icon),
                contentDescription = null,
                tint = accentColor,
                modifier = Modifier.width(22.dp)
            )
        }

        Column(
            modifier = Modifier.weight(1f)
                .padding(vertical = if (subtitle == null) 0.dp else 12.dp)
                .padding(end = if (onClick == null && onRefreshClick == null) 16.dp else 0.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall.copy(fontSize = 14.sp),
                color = Color.Black
            )
            if (subtitle != null) {
                Text(
                    text = subtitle,
                    style = titlesStyle.copy(fontSize = 12.sp),
                    color = titleColor
                )
            }
        }

        when {
            onRefreshClick != null -> KycRefreshButton(
                onClick = onRefreshClick,
                refreshing = refreshing,
                tint = accentColor,
                modifier = Modifier.padding(end = 8.dp)
            )
            onClick != null -> Icon(
                painter = painterResource(Res.drawable.arrow_right),
                contentDescription = null,
                tint = accentColor,
                modifier = Modifier.padding(end = 16.dp).height(24.dp)
            )
        }
    }
}

/** Home's card for each [OnboardingPrompt] (JN `HomeScreen` copy); nothing for NONE. */
@Composable
fun OnboardingPromptCard(
    prompt: OnboardingPrompt,
    kycFailureReason: String,
    refreshing: Boolean,
    onContinueOnboarding: () -> Unit,
    onRefresh: () -> Unit
) {
    when (prompt) {
        // KYC form not filled in yet: onboarding resumes at the server's current stage.
        OnboardingPrompt.COMPLETE_KYC -> KYCCard(
            text = "Complete your KYC to start investing",
            onClick = onContinueOnboarding
        )
        // Submitted and being verified: nothing to open, but the status can be re-checked in place.
        OnboardingPrompt.KYC_IN_PROGRESS -> KYCCard(
            text = "KYC verification in progress",
            subtitle = "Your submitted KYC is being verified. We'll notify you once it's " +
                    "verified so you can complete the remaining steps.",
            onClick = null,
            accentColor = appYellow,
            onRefreshClick = onRefresh,
            refreshing = refreshing
        )
        // KYC rejected: the server has moved the stage back, so onboarding resumes from there.
        OnboardingPrompt.KYC_FAILED -> KYCCard(
            text = "KYC verification failed",
            subtitle = kycFailureReason,
            onClick = onContinueOnboarding
        )
        // KYC verified: the finalization steps resume at the server's current stage.
        OnboardingPrompt.COMPLETE_REMAINING -> KYCCard(
            text = "Complete your KYC finalization to start investing",
            onClick = onContinueOnboarding,
            accentColor = Secondary
        )
        OnboardingPrompt.NONE -> Unit
    }
}

/** Spins while [refreshing], and ignores taps then so one refresh runs at a time. */
@Composable
private fun KycRefreshButton(
    onClick: () -> Unit,
    refreshing: Boolean,
    tint: Color,
    modifier: Modifier = Modifier
) {
    val rotation by if (refreshing) {
        rememberInfiniteTransition(label = "kycRefresh").animateFloat(
            initialValue = 0f,
            targetValue = 360f,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = 900, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "kycRefreshRotation"
        )
    } else {
        remember { mutableFloatStateOf(0f) }
    }

    IconButton(
        onClick = onClick,
        enabled = !refreshing,
        colors = IconButtonDefaults.iconButtonColors(
            contentColor = tint,
            disabledContentColor = tint
        ),
        modifier = modifier
    ) {
        Icon(
            painter = painterResource(Res.drawable.refresh_icon),
            contentDescription = "Refresh KYC status",
            modifier = Modifier
                .size(20.dp)
                .rotate(rotation)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xffffff)
@Composable
private fun OnboardingPromptCardPreview() {
    VelvetTheme {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OnboardingPrompt.entries.forEach {
                OnboardingPromptCard(
                    prompt = it,
                    kycFailureReason = "PAN name does not match the bank account",
                    refreshing = false,
                    onContinueOnboarding = {},
                    onRefresh = {}
                )
            }
        }
    }
}
