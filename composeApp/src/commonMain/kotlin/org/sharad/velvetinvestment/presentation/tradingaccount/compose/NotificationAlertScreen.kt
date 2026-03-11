package org.sharad.velvetinvestment.presentation.tradingaccount.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.emify.core.ui.theme.darkBlue
import org.sharad.emify.core.ui.theme.grayColor
import org.sharad.velvetinvestment.presentation.tradingaccount.viewmodel.NotificationCentreViewModel
import org.sharad.velvetinvestment.utils.theme.Poppins


@Composable
fun NotificationAlertScreen() {
    val viewModel: NotificationCentreViewModel = koinViewModel()
    val state by viewModel.notificationModel.collectAsStateWithLifecycle()
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.SpaceAround) {
        AlertComposable(
            color = state.color,
            icon = state.icon,
            heading = state.heading,
            body = state.body,
            time = state.time,
            extraText = state.extraText
        )


    }
}

@Composable
fun AlertComposable(
    color: Color,
    icon: DrawableResource,
    heading: String,
    body: String,
    time: String,
    extraText: String?
) {
    Box(
        Modifier.fillMaxWidth().drawBehind {
            val shadowHeight = 6.dp.toPx()
            val radius = 20.dp.toPx()
            drawRoundRect(
                color = Color(0xffF0F0F0),
                topLeft = Offset(0f, 1.5.dp.toPx()),
                cornerRadius = CornerRadius(radius, y = radius),
                size = size
            )
        }.border(1.dp, color = Color(0xffF0F0F0), shape = RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White, shape = RoundedCornerShape(20.dp)).padding(16.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth().weight(1f),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = "Alert Box Icon",
                    tint = color,
                    modifier = Modifier.background(color = color.copy(0.05f), shape = CircleShape)
                        .padding(10.dp)
                )
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        heading,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = Poppins,
                        color = darkBlue
                    )
                    Text(
                        text = body,
                        fontFamily = Poppins,
                        fontSize = 14.sp,
                        color = grayColor
                    )
                    extraText?.let {it->
                        Text(
                            it,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = Poppins,
                            color = darkBlue
                        )
                    }
                }
            }
            Text("$time ago", fontSize = 12.sp, fontFamily = Poppins, color = Color(0xff8D94A5))

        }
    }
}