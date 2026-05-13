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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.domain.models.portfolio.MutualFundPortfolioDomain
import org.sharad.velvetinvestment.presentation.mutualfund.compose.MutualFundIcon
import org.sharad.velvetinvestment.shared.genericDropShadow
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import org.sharad.velvetinvestment.utils.formatMoneyAfterL
import org.sharad.velvetinvestment.shared.theme.subHeading
import org.sharad.velvetinvestment.shared.theme.titlesStyle

@Composable
fun MutualFundsCard(fundItem: MutualFundPortfolioDomain, onClick: () -> Unit) {

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
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            SubcomposeAsyncImage(
                modifier = Modifier.size(44.dp), model = fundItem.icon, contentDescription = null,

                loading = {
                    MutualFundIcon(
                        schemeName = fundItem.title, size = 44.dp
                    )
                },

                error = {
                    MutualFundIcon(
                        schemeName = fundItem.title, size = 44.dp
                    )
                },

                success = {
                    SubcomposeAsyncImageContent()
                }
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
                        text=fundItem.title,
                        color = Color.Black,
                        style = subHeading,
                        modifier = Modifier.weight(1f)
                    )

                    Text(
                        text= "₹"+formatMoneyAfterL(fundItem.amount.toLong()),
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
                        text=fundItem.category,
                        color = titleColor,
                        style = titlesStyle
                    )
                }
                Text(
                    text="Start Date : "+fundItem.startDate,
                    color = titleColor,
                    style = titlesStyle
                )
            }

        }
    }
}

@Preview
@Composable
private fun MutualFundsCardPreview() {
    val sampleFund = MutualFundPortfolioDomain(
        id = 1,
        title = "SBI Bluechip Fund Direct Growth Fund Direct Growth",
        category = "Equity: Large Cap",
        amount = 125000.0,
        isSip = true,
        startDate = "12 Oct 2023",
        returnPercentage = "14.5%",
        returnAmount = 18000,
        xirr = "15.2%",
        currentNav = 78.5,
        avgNav = 65.0,
        folio = "12345678/90",
        balanceUnits = 1592.35,
        icon = ""
    )
    VelvetTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            MutualFundsCard(
                fundItem = sampleFund,
                onClick = {}
            )
        }
    }
}