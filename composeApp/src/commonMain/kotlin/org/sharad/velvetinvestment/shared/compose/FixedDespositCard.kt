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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.darkBlue
import org.sharad.velvetinvestment.domain.models.portfolio.FixedDepositPortfolioDomain
import org.sharad.velvetinvestment.presentation.mutualfund.compose.MutualFundIcon
import org.sharad.velvetinvestment.shared.genericDropShadow
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import org.sharad.velvetinvestment.shared.theme.tinyLabel
import org.sharad.velvetinvestment.utils.formatMoneyAfterL
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.ic_callended_filled

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
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SubcomposeAsyncImage(
                    modifier = Modifier.size(44.dp),
                    model = fdData.issuerLogoUrl,
                    contentDescription = null,

                    loading = {
                        MutualFundIcon(
                            schemeName = fdData.issuerDisplayName, size = 44.dp
                        )
                    },

                    error = {
                        MutualFundIcon(
                            schemeName = fdData.issuerDisplayName, size = 44.dp
                        )
                    },

                    success = {
                        SubcomposeAsyncImageContent()
                    }
                )
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = fdData.issuerDisplayName,
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.Black
                    )
                    Text(
                        text = fdData.roiAtBooking+"% p.a.",
                        style = MaterialTheme.typography.titleSmall,
                        color = Color(0xff00658D)
                    )
                }

                Text(
                    text = formatMoneyAfterL(fdData.amount.toDouble().toLong()),
                    style = MaterialTheme.typography.labelSmall,
                    color = darkBlue
                )

            }
            HorizontalDivider(
                thickness = 0.5.dp
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_callended_filled),
                    contentDescription = null,
                    modifier = Modifier.size(14.dp),
                    tint = Color.DarkGray
                )
                Text(
                    text = "Matures "+ (fdData.maturityDate ?: ""),
                    color = Color.DarkGray,
                    style = tinyLabel.copy(fontWeight = FontWeight.Normal)
                )
            }

        }
    }
}

@Preview(showBackground = true)
@Composable
private fun FixedDepositCardPreview() {
    val sampleFd = FixedDepositPortfolioDomain(
        id = "1",
        amount = "100000",
        roiAtBooking = "7.5",
        tenureAtBooking = 12,
        fdIssuedAt = "2023-10-12",
        status = "Active",
        maturityAmount = "107500",
        userId = "user123",
        userFullName = "John Doe",
        userEmail = "john@example.com",
        issuerLogoUrl = "",
        issuerDisplayName = "HDFC Bank",
        maturityDate = "06 July,2025"
    )
    VelvetTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            FixedDepositCard(
                fdData = sampleFd,
                onClick = {}
            )
        }
    }
}