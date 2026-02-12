package org.sharad.velvetinvestment.presentation.homescreen.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.appGreen
import org.sharad.emify.core.ui.theme.appYellow
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.utils.genericDropShadow
import org.sharad.velvetinvestment.utils.theme.subHeading
import org.sharad.velvetinvestment.utils.theme.titlesStyle
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.circe_chart_icon
import velvet.composeapp.generated.resources.expenses_icon
import velvet.composeapp.generated.resources.human_brain_icon
import velvet.composeapp.generated.resources.thunder_icon

@Composable
fun FeatureNavigationCards(
    onBeatInflationClick: () -> Unit,
    onExpertPickedClick: () -> Unit,
    onStartSmallClick: () -> Unit,
    onDiversifiedClick: () -> Unit
) {

    Column(
        modifier= Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FeatureNavigationCard(
                onClick = onBeatInflationClick,
                title = "Beat Inflation",
                description = "Grow Wealth faster than rising cost",
                icon = {
                    Icon(
                        painter = painterResource(Res.drawable.expenses_icon),
                        contentDescription = null,
                        tint = appGreen,
                        modifier = Modifier.size(24.dp)
                    )
                },
                color = appGreen,
                modifier = Modifier.weight(1f)
            )
            FeatureNavigationCard(
                onClick = onExpertPickedClick,
                title = "Expert Picked",
                description = "Curated fund for better Returns",
                icon = {
                    Icon(
                        painter = painterResource(Res.drawable.human_brain_icon),
                        contentDescription = null,
                        tint = Color(0xffA100FF),
                        modifier = Modifier.size(24.dp)
                    )
                },
                color = Color(0xffA100FF),
                modifier = Modifier.weight(1f)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            FeatureNavigationCard(
                onClick = onBeatInflationClick,
                title = "Start Small",
                description = "Begin your journey with just ₹5000. ",
                icon = {
                    Icon(
                        painter = painterResource(Res.drawable.thunder_icon),
                        contentDescription = null,
                        tint = appYellow,
                        modifier = Modifier.size(24.dp)
                    )
                },
                color = appYellow,
                modifier = Modifier.weight(1f)
            )
            FeatureNavigationCard(
                onClick = onExpertPickedClick,
                title = "Diversified",
                description = "Balance risk with smart allocation",
                icon = {
                    Icon(
                        painter = painterResource(Res.drawable.circe_chart_icon),
                        contentDescription = null,
                        tint = Color(0xff4881FF),
                        modifier = Modifier.size(24.dp)
                    )
                },
                color =  Color(0xff4881FF),
                modifier = Modifier.weight(1f)
            )
        }

    }

}

@Composable
fun FeatureNavigationCard(
    onClick: () -> Unit,
    title: String,
    description: String,
    icon: @Composable () -> Unit,
    color: Color,
    modifier: Modifier=Modifier
){
    Box(
        modifier=modifier.fillMaxWidth()
            .genericDropShadow()
            .clip(RoundedCornerShape(15.dp))
            .background(Color.White, RoundedCornerShape(15.dp))
            .clickable(
                onClick={onClick()}
            ),
        contentAlignment = Alignment.Center
    ){
        Column(
            modifier=Modifier.fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 12.dp)
        ) {
            Box(
                modifier=Modifier.size(44.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(color.copy(alpha= 0.1f), RoundedCornerShape(10.dp)),
                contentAlignment = Alignment.Center
            ){
                icon()
            }

            Spacer(modifier=Modifier.height(24.dp))

            Text(
                text=title,
                color=Color.Black,
                style = subHeading
            )

            Spacer(modifier=Modifier.height(8.dp))

            Text(
                text=description,
                color= titleColor,
                style = titlesStyle
            )
        }
    }
}