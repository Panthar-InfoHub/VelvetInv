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
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.appGreen
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.domain.models.portfolio.FixedDepositPortfolioDomain
import org.sharad.velvetinvestment.utils.formatMoneyAfterL
import org.sharad.velvetinvestment.shared.genericDropShadow
import org.sharad.velvetinvestment.shared.theme.subHeading
import org.sharad.velvetinvestment.shared.theme.titlesStyle
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.fd_placeholder

@Composable
fun FixedDepositCard(fdData: FixedDepositPortfolioDomain, onClick: () -> Unit){
    Box(
        modifier = Modifier.fillMaxWidth()
            .genericDropShadow(RoundedCornerShape(15.dp))
            .clip(RoundedCornerShape(15.dp))
            .background(Color.White)
            .clickable(onClick=onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    modifier = Modifier.size(44.dp),
                    model = fdData.issuerLogoUrl,
                    contentDescription = null,
                    placeholder = painterResource(Res.drawable.fd_placeholder),
                    fallback = painterResource(Res.drawable.fd_placeholder),
                    error = painterResource(Res.drawable.fd_placeholder)
                )
                Column {
                    Text(
                        text = fdData.issuerDisplayName,
                        style = subHeading,
                        color = Primary
                    )
                }
            }

            Row(
                modifier=Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Principal",
                        style = titlesStyle,
                        color = titleColor
                    )
                    Text(
                        text = "₹"+ formatMoneyAfterL(fdData.amount.toLong()),
                        style = subHeading,
                        color = Primary
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Rate",
                        style = titlesStyle,
                        color = titleColor
                    )
                    Text(
                        text = fdData.roiAtBooking+"%",
                        style = subHeading,
                        color = Secondary
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Maturity",
                        style = titlesStyle,
                        color = titleColor
                    )
                    Text(
                        text = fdData.maturityAmount?.let { "₹"+ formatMoneyAfterL(it.toLong()) } ?: "Action Pending",
                        style = subHeading,
                        color = appGreen
                    )
                }

            }

        }
    }
}