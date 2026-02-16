package org.sharad.velvetinvestment.shared.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.presentation.portfolio.models.FundListCardData
import org.sharad.velvetinvestment.utils.genericDropShadow
import org.sharad.velvetinvestment.utils.theme.subHeading
import org.sharad.velvetinvestment.utils.theme.titlesStyle
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.rectangle_19
import velvet.composeapp.generated.resources.splash_cover_1

@Composable
fun MutualFundsCard(fundItem: FundListCardData, onClick: () -> Unit) {

    Box(
        modifier = Modifier.fillMaxWidth()
            .genericDropShadow(RoundedCornerShape(15.dp))
            .clip(RoundedCornerShape(15.dp))
            .background(Color.White)
            .clickable(onClick=onClick),
        contentAlignment = Alignment.Center
    ){

        Row(modifier=Modifier.fillMaxWidth()
            .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            AsyncImage(
                modifier = Modifier.size(44.dp)
                    .clip(RoundedCornerShape(15.dp)),
                model = fundItem.icon,
                contentDescription = null,
                fallback = painterResource(Res.drawable.rectangle_19),
                error = painterResource(Res.drawable.rectangle_19),
                placeholder = painterResource(Res.drawable.rectangle_19)
            )

            Column(
                modifier = Modifier.weight(1f).padding(end = 4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ){
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text=fundItem.fundName,
                        color = Color.Black,
                        style = subHeading
                    )

                    Text(
                        text=(fundItem.amount),
                        color = Color.Black,
                        style = subHeading
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Text(
                        text=fundItem.fundCategory,
                        color = titleColor,
                        style = titlesStyle
                    )
                    fundItem.fundType?.let{
                        Text(
                            text = fundItem.fundType,
                            color = titleColor,
                            style = titlesStyle
                        )
                    }
                }

                fundItem.fundRemark?.let {
                    Text(
                        text = fundItem.fundRemark,
                        color = titleColor,
                        style = titlesStyle
                    )
                }
            }

        }
    }
}