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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.domain.models.portfolio.MutualFundPortfolioDomain
import org.sharad.velvetinvestment.presentation.mutualfund.compose.MutualFundIcon
import org.sharad.velvetinvestment.shared.genericDropShadow
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import org.sharad.velvetinvestment.shared.theme.tinyLabel
import org.sharad.velvetinvestment.shared.theme.titlesStyle
import org.sharad.velvetinvestment.utils.formatMoneyAfterL
import org.sharad.velvetinvestment.utils.withInterRupee

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

        Column(
            modifier = Modifier.padding(
                vertical = 24.dp
            )
        ){
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                SubcomposeAsyncImage(
                    modifier = Modifier.size(44.dp),
                    model = fundItem.icon,
                    contentDescription = null,

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
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = fundItem.title,
                            color = Color.Black,
                            style = MaterialTheme.typography.labelSmall.copy(lineHeight = 20.sp),
                            modifier = Modifier.weight(1f),
                            maxLines = 2
                        )

                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = fundItem.category,
                            color = titleColor,
                            style = titlesStyle
                        )
                    }
                }
                Column(
                    modifier = Modifier.align(Alignment.Top),
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = ("₹" + formatMoneyAfterL(fundItem.amount.toLong())).withInterRupee(),
                        color = Color.Black,
                        style = MaterialTheme.typography.labelSmall,
                    )
                    Text(
                        text = fundItem.returnPercentage,
                        style = tinyLabel,
                        color = titleColor
                    )
                }
            }
            HorizontalDivider(
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp, horizontal = 20.dp),
                thickness = 0.5.dp
            )
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column {
                    Text(
                        text = "Invested",
                        style = MaterialTheme.typography.titleSmall,
                        color = titleColor
                    )
                    Text(
                        text = ("₹" + formatMoneyAfterL(fundItem.amount.toLong())).withInterRupee(),
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Black
                    )
                }
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = "Start Date",
                        style = MaterialTheme.typography.titleSmall,
                        color = titleColor
                    )
                    Text(
                        text = fundItem.startDate,
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Black
                    )
                }
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