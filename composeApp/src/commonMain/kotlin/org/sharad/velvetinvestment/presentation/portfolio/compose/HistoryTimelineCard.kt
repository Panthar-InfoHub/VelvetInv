package org.sharad.velvetinvestment.presentation.portfolio.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.appGreen
import org.sharad.emify.core.ui.theme.appRed
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.domain.TransactionStatus
import org.sharad.velvetinvestment.domain.models.portfolio.TransactionHistoryDomain
import org.sharad.velvetinvestment.utils.theme.subHeading
import org.sharad.velvetinvestment.utils.theme.titlesStyle
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.tick_icon

@Composable
fun HistoryTimelineCard(item: TransactionHistoryDomain, showNext: Boolean) {

    Row(modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(modifier = Modifier.size(24.dp)
                .clip(CircleShape)
                .background(when(item.type){
                    TransactionStatus.SUCCESS -> appGreen
                    TransactionStatus.FAILED -> appRed
                    TransactionStatus.PROCESSING -> Secondary
                }),
                contentAlignment = Alignment.Center
            ){
                Icon(
                    painter = painterResource(Res.drawable.tick_icon),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(10.dp)
                )
            }
            if (showNext){ Box(modifier = Modifier.width(1.dp).height(52.dp).background(titleColor)) }
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = item.title,
                style = subHeading,
                color = Color.Black
            )
            Text(
                text = item.date,
                style = titlesStyle,
                color = titleColor
            )
        }

    }
}