package org.sharad.velvetinvestment.features.auth.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.lightGray
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.features.auth.ui.viewmodels.BiometricSettingsEvent
import org.sharad.velvetinvestment.features.auth.ui.viewmodels.BiometricSettingsUiState
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.ShadowCard
import org.sharad.velvetinvestment.shared.compose.ToggleSwitch
import org.sharad.velvetinvestment.shared.dottedBorder
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import org.sharad.velvetinvestment.shared.theme.titlesStyle
import org.sharad.velvetinvestment.utils.AppBackHandler
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.biometric
import velvet.composeapp.generated.resources.ic_face_id
import velvet.composeapp.generated.resources.info_icon
import velvet.composeapp.generated.resources.tick_icon

/** The switch controlling whether the app lock offers the biometric shortcut (JN). */
@Composable
fun BiometricSettingsScreen(
    state: BiometricSettingsUiState,
    onEvent: (BiometricSettingsEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    AppBackHandler(enabled = true) { onEvent(BiometricSettingsEvent.OnBackClicked) }

    Scaffold(containerColor = Color.White) { padding ->
        Column(modifier = modifier.fillMaxSize().padding(padding)) {
            BackHeader(
                title = "Biometric Login",
                showBack = true,
                onBack = { onEvent(BiometricSettingsEvent.OnBackClicked) }
            )

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp),
                contentPadding = PaddingValues(top = 8.dp, bottom = 24.dp)
            ) {
                item {
                    // Same tint as Velvet's profile row icons.
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFDEE2F6)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.biometric),
                            contentDescription = null,
                            tint = Primary,
                            modifier = Modifier.size(48.dp)
                        )
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(8.dp)
                                .dottedBorder(
                                    color = Primary.copy(0.2f),
                                    strokeWidth = 1.dp,
                                    cornerRadius = 55.dp
                                )
                        )
                    }
                }

                item {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "Biometric Login",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Primary,
                            textAlign = TextAlign.Center
                        )
                        Text(
                            text = "Faster, more secure access to your account using your device's " +
                                "biometric sensors.",
                            style = titlesStyle,
                            color = titleColor,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 28.dp)
                        )
                    }
                }

                item {
                    ShadowCard(modifier = Modifier.fillMaxWidth()) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(Color(0xFFDEE2F6)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        painter = painterResource(Res.drawable.ic_face_id),
                                        contentDescription = null,
                                        tint = Primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                                Column(
                                    modifier = Modifier.weight(1f),
                                    verticalArrangement = Arrangement.spacedBy(2.dp)
                                ) {
                                    Text(
                                        text = "Enable Biometric Login",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = Color.Black
                                    )
                                    Text(
                                        text = "Use Face ID or Touch ID",
                                        style = titlesStyle.copy(fontSize = 12.sp),
                                        color = titleColor
                                    )
                                }
                                ToggleSwitch(
                                    checked = state.enabled,
                                    onCheckedChange = {
                                        onEvent(BiometricSettingsEvent.OnEnabledChanged(it))
                                    }
                                )
                            }

                            HorizontalDivider(thickness = 1.dp, color = lightGray)

                            BenefitRow(
                                label = "Enhanced Security",
                                detail = "Your biometric data is encrypted and never leaves your device."
                            )
                            BenefitRow(
                                label = "Instant Access",
                                detail = "Skip the PIN typing and log in instantly."
                            )
                        }
                    }
                }

                item {
                    // InfoNoteCard's look (no title): Velvet's info-note border and fill.
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp))
                            .border(1.dp, Color(0xFFE4E1E7), RoundedCornerShape(24.dp))
                            .background(Color(0xFFF0F4FF))
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.info_icon),
                            contentDescription = null,
                            tint = Primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Text(
                            text = "If you disable biometric login, you will need to enter your PIN " +
                                "manually each time you open the app.",
                            style = titlesStyle,
                            color = titleColor
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BenefitRow(
    label: String,
    detail: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .padding(top = 4.dp)
                .size(12.dp)
                .clip(CircleShape)
                .background(Secondary),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(Res.drawable.tick_icon),
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier.size(6.dp)
            )
        }
        Text(
            text = buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                    append("$label: ")
                }
                append(detail)
            },
            style = titlesStyle,
            color = Color.Black
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BiometricSettingsScreenPreview() {
    VelvetTheme {
        BiometricSettingsScreen(
            state = BiometricSettingsUiState(enabled = true),
            onEvent = {}
        )
    }
}
