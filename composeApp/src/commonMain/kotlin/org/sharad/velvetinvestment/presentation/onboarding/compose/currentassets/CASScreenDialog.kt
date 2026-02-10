package org.sharad.velvetinvestment.presentation.onboarding.compose.currentassets

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.bgColor3
import org.sharad.emify.core.ui.theme.shadowColor
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.shared.compose.AppButton
import org.sharad.velvetinvestment.utils.AppBackHandler
import org.sharad.velvetinvestment.utils.theme.Poppins
import org.sharad.velvetinvestment.utils.theme.titlesStyle
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.arrow_down
import velvet.composeapp.generated.resources.icon_cas
import velvet.composeapp.generated.resources.icon_cross

@Composable
fun CASUploadScreenDialog(
    hideDialog: () -> Unit,
) {

    AppBackHandler(true) {
        hideDialog()
    }

    Box(
        modifier = Modifier.fillMaxSize()
            .background(Color.Black.copy(alpha = 0.25f))
            .clickable(
                onClick = {},
                indication = null,
                interactionSource = null
            ),
        contentAlignment = Alignment.Center
    ) {

        Box(
            modifier=Modifier.fillMaxSize()
                .blur(20.dp)
        )

        Box(
            modifier = Modifier.fillMaxWidth()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 48.dp, horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
            ) {

                Column(
                    modifier=Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ){
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Import CAS Report",
                            style = MaterialTheme.typography.headlineLarge,
                            color = Primary
                        )

                        Icon(
                            painter = painterResource(Res.drawable.icon_cross),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                                .clickable(
                                    onClick = { hideDialog() }
                                )
                        )
                    }

                    Text(
                        text = "Please upload your Consolidated Account Statement (CAS) PDF to sync your portfolio holdings.",
                        style = titlesStyle,
                        color = titleColor
                    )
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .dropShadow(
                            shadow = Shadow(
                                radius = 24.dp,
                                color = shadowColor,
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White, RoundedCornerShape(10.dp))
                        .animateContentSize()
                )
                {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                            .padding(vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Box(
                                modifier = Modifier.size(44.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        bgColor3.copy(alpha = 0.1f),
                                        RoundedCornerShape(10.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.icon_cas),
                                    contentDescription = null,
                                    modifier = Modifier.size(24.dp),
                                    tint = Secondary
                                )
                            }

                            Column(
                                modifier = Modifier.weight(1f)
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    text = "CAS Report...",
                                    style = MaterialTheme.typography.labelLarge,
                                    maxLines = 1,
                                    color = Primary
                                )
                                Text(
                                    text = "Upload your existing CAS file",
                                    style = titlesStyle,
                                    color = titleColor,
                                    maxLines = 1
                                )
                            }

                            Icon(
                                painter = painterResource(Res.drawable.arrow_down),
                                contentDescription = null,
                                tint = Primary,
                                modifier = Modifier
                                    .padding(end = 16.dp)
                                    .rotate(-90f)
                            )
                        }
                    }
                }

                Box(
                    modifier=Modifier.fillMaxWidth()
                        .clip(RoundedCornerShape(15.dp))
                        .background(
                            bgColor3.copy(0.15f), RoundedCornerShape(15.dp)
                        )
                ){
                    Row(
                        modifier=Modifier.fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {

                        Icon(
                            painter = painterResource(Res.drawable.icon_cas),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = Secondary
                        )

                        Text(
                            modifier=Modifier.weight(1f)
                                .fillMaxWidth(),
                            text="A CAS (Consolidate Account Statement) Provides an automated way to track all your Mutual Fund holdings across different brokers in one place.",
                            color = Primary,
                            fontSize = 12.sp,
                            fontFamily = Poppins
                        )

                    }
                }

                AppButton(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {},
                    text = "Proceed"
                )

            }
        }
    }
}