package org.sharad.velvetinvestment.presentation.firereport.compose

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.presentation.firereport.uimodels.FireProjectionUI
import org.sharad.velvetinvestment.shared.compose.ShadowCard
import org.sharad.velvetinvestment.utils.formatMoneyWithUnits
import org.sharad.velvetinvestment.utils.theme.subHeading
import org.sharad.velvetinvestment.utils.theme.titlesStyle
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.arrow_down

@Composable
fun ProjectionCard(projection: FireProjectionUI) {

    var extended by rememberSaveable { mutableStateOf(false) }

    ShadowCard() {
        Column(
            modifier=Modifier.fillMaxWidth().animateContentSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            UnExtendedProjectionPart(
                year=projection.year,
                age=projection.age,
                fire=projection.firePercent,
                extended=extended,
                onClick={extended=!extended}
            )

            if (extended){
                ExtendedProjectionPart(
                    current=projection.currentPortfolio,
                    net=projection.netOutflow,
                    goals=projection.goals,
                    fire=projection.fireNumber
                )
            }
        }
    }
}

@Composable
fun UnExtendedProjectionPart(
    fire: Double,
    age: Int,
    year: Int,
    extended: Boolean,
    onClick: () -> Unit
) {
    val rotation by animateFloatAsState(
        if (!extended) 0f else 180f
    )
    Row(
        modifier=Modifier.fillMaxWidth()
            .height(76.dp)
            .clickable(onClick=onClick)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = year.toString(),
                style = MaterialTheme.typography.labelLarge,
                color = Primary
            )
            Text(
                text = "Age $age",
                style = titlesStyle,
                color = titleColor
            )
        }
        Text(
            text = "$fire% Fire",
            style = subHeading,
            color = Primary
        )
        Icon(
            painter = painterResource(Res.drawable.arrow_down),
            contentDescription=null,
            tint= Primary,
            modifier=Modifier
                .padding(end = 8.dp)
                .rotate(rotation)
        )
    }
}

@Composable
fun ExtendedProjectionPart(fire: Long, goals: Long, net: Long, current: Long) {
    Column(
        modifier=Modifier.fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        HorizontalDivider(color = titleColor)
        Column(
            modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                DataCard(
                    modifier = Modifier
                        .height(76.dp)
                        .weight(1f)
                    ,
                    title = "Current Portfolio",
                    value = formatMoneyWithUnits(current),
                )

                DataCard(
                    modifier = Modifier
                        .height(76.dp)
                        .weight(1f)
                    ,
                    title = "Net Outflow",
                    value = formatMoneyWithUnits(net),
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                DataCard(
                    modifier = Modifier
                        .height(76.dp)
                        .weight(1f)
                    ,
                    title = "Goals",
                    value = formatMoneyWithUnits(goals),
                )

                DataCard(
                    modifier = Modifier
                        .height(76.dp)
                        .weight(1f)
                    ,
                    title = "F.I.R.E Number",
                    value = formatMoneyWithUnits(fire),
                )
            }
        }
    }
}

@Composable
fun DataCard(value: String, title: String, modifier: Modifier) {
    ShadowCard(
        modifier=modifier
    ) {
        Box(
            modifier=Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomStart,
        ) {
            Column(
                modifier=Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text=title,
                    style = titlesStyle,
                    color = titleColor
                )
                Text(
                    text= "₹ $value",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Primary
                )
            }
        }
    }
}