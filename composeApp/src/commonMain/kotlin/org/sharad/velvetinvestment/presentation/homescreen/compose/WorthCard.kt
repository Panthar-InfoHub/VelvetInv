package org.sharad.velvetinvestment.presentation.homescreen.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.bgColor1
import org.sharad.velvetinvestment.domain.models.home.UserWorthCardDomain
import org.sharad.velvetinvestment.shared.compose.MeshSquareBackground
import org.sharad.velvetinvestment.utils.formatMoneyWithUnits
import org.sharad.velvetinvestment.utils.genericDropShadow
import org.sharad.velvetinvestment.utils.theme.Poppins
import org.sharad.velvetinvestment.utils.theme.subHeading
import org.sharad.velvetinvestment.utils.theme.titlesStyle
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.arrow_right
import velvet.composeapp.generated.resources.expenses_icon
import velvet.composeapp.generated.resources.worth_img

@Composable
fun UserWorthCard(netWorth: UserWorthCardDomain?, onInvestingRateClick: () -> Unit) {
    Box(
        modifier=Modifier.fillMaxWidth()
            .genericDropShadow()
            .clip(RoundedCornerShape(20.dp))
            .background(Primary),
        contentAlignment = Alignment.Center
    ){
        MeshSquareBackground(modifier = Modifier.matchParentSize())

        Icon(
            painter = painterResource(Res.drawable.worth_img),
            contentDescription = null,
            tint = Color.White.copy(0.7f),
            modifier = Modifier.size(136.dp)
                .padding(end = 12.dp)
                .align(Alignment.CenterEnd)
        )

        Column(
            modifier = Modifier.fillMaxWidth().padding( start = 20.dp, end = 16.dp, top = 24.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Total Net Worth",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White
            )

            Text(
                text = "₹ "+( netWorth?.totalNetWorth?:0),
                style = MaterialTheme.typography.displayMedium,
                color = Color.White
            )

            Row (
                modifier=Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){

                Box(
                    modifier=Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color.White, RoundedCornerShape(10.dp))
                )
                {
                    Column(
                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = "Absolute / CAGR Growth",
                            style = titlesStyle,
                            color = Primary
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.expenses_icon),
                                contentDescription = null,
                                tint = bgColor1,
                                modifier = Modifier.size(15.dp)
                            )
                            Text(
                                text = "₹"+ formatMoneyWithUnits(netWorth?.absGrowth?:0) +"/"+(netWorth?.CARGGrowth?:0)+"%",
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 14.sp,
                                fontFamily = Poppins,
                                color = Primary
                            )
                        }
                    }
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.Start
                ) {

                    Text(
                        text = "Investing Rate",
                        style = subHeading,
                        color = Color.White
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.clickable(
                            onClick=onInvestingRateClick,
                            indication = null,
                            interactionSource = remember{ MutableInteractionSource() }
                        )
                    ) {

                        Text(
                            text = "${netWorth?.investingRate?:0}%",
                            style = MaterialTheme.typography.headlineSmall,
                            color = Secondary
                        )

                        Icon(
                            painter = painterResource(Res.drawable.arrow_right),
                            contentDescription = null,
                            tint = Secondary,
                            modifier = Modifier.size(20.dp)
                        )

                    }

                }

            }

        }

    }
}
