package org.sharad.velvetinvestment.presentation.fixeddeposits.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.appGreen
import org.sharad.emify.core.ui.theme.appRed
import org.sharad.emify.core.ui.theme.appYellow
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.domain.models.fixeddeposits.FixedDepositDomain
import org.sharad.velvetinvestment.domain.models.fixeddeposits.RiskLevel
import org.sharad.velvetinvestment.shared.theme.subHeading
import org.sharad.velvetinvestment.shared.theme.titlesStyle
import org.sharad.velvetinvestment.utils.trimTo
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.fd_placeholder

@Composable
fun FDListCard(fd: FixedDepositDomain, onClick: () -> Unit, modifier: Modifier= Modifier,) {
    Column(
        modifier=modifier.fillMaxWidth()
            .clickable(onClick=onClick,
                indication = null,
                interactionSource = remember{MutableInteractionSource()})

    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AsyncImage(
                model = fd.bankLogoUrl,
                contentDescription = null,
                modifier=Modifier.size(40.dp),
                fallback = painterResource(Res.drawable.fd_placeholder),
                error = painterResource(Res.drawable.fd_placeholder),
                placeholder = painterResource(Res.drawable.fd_placeholder)
            )
            Column(
                modifier = Modifier.weight(1f).fillMaxWidth()
            )
            {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = fd.bankName,
                        style = subHeading,
                        color = Color.Black,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = (fd.tenures.firstOrNull()?.interestRate?.trimTo(2) ?: "0") +" %",
                        style = subHeading,
                        color = appGreen
                    )
                }
                Spacer(Modifier.height(2.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = fd.bankTag,
                        style = titlesStyle,
                        color = titleColor,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "3 Y",
                        style = titlesStyle,
                        color = titleColor,
                    )
                }
                RiskLevelIndicator(risk=fd.riskLevel)
            }
        }
    }
}

@Composable
fun RiskLevelIndicator(risk: RiskLevel) {
    val filledBars= when(risk){
        RiskLevel.LOW -> 2
        RiskLevel.MODERATE -> 3
        RiskLevel.HIGH -> 4
    }
    val tintColor=when(risk){
        RiskLevel.LOW ->appGreen
        RiskLevel.MODERATE -> appYellow
        RiskLevel.HIGH -> appRed
    }

    val start=0.5f
    val end=1f
    val count = 5
    val step = (end - start) / (count - 1)

    Row(
        modifier = Modifier.height(20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {

        Text(
            text = risk.label,
            style = titlesStyle.copy(lineHeight = 14.sp),
            color = tintColor,
        )

        Row(
            modifier = Modifier
                .height(12.dp),
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            repeat(5) {
                val multiplier = start + step * it
                Box(
                    modifier = Modifier
                        .fillMaxHeight(multiplier)
                        .width(2.dp)
                        .clip(CircleShape)
                        .background(
                            if (it < filledBars) tintColor else tintColor.copy(0.1f)
                        )
                )
            }
        }
    }
}