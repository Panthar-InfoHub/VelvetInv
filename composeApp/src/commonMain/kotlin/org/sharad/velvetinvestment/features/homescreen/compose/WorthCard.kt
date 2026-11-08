package org.sharad.velvetinvestment.features.homescreen.compose

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.velvetinvestment.domain.models.home.UserWorthCardDomain
import org.sharad.velvetinvestment.shared.compose.MeshSquareBackground
import org.sharad.velvetinvestment.shared.genericDropShadow
import org.sharad.velvetinvestment.utils.formatMoneyAfterL
import org.sharad.velvetinvestment.utils.formatWithCommas
import org.sharad.velvetinvestment.utils.withInterRupee
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.icon_eye_closed
import velvet.composeapp.generated.resources.icone_eye_open
import velvet.composeapp.generated.resources.worth_img

@Composable
fun UserWorthCard(
    netWorth: UserWorthCardDomain?,
    onInvestingRateClick: () -> Unit,
    hidden: Boolean,
    onHiddenToggle: () -> Unit
) {

    val totalNetWorthFormatted =
        "₹ " + formatWithCommas(netWorth?.totalNetWorth ?: 0)

    // TODO: growth and investing rate are not sent by the server yet; see the commented-out row below.
//    val growthFormatted =
//        "₹" + formatMoneyWithUnits(netWorth?.absGrowth ?: 0) +
//                "/" + (netWorth?.CARGGrowth ?: 0) + "%"
//
//    val investingRateFormatted =
//        "${netWorth?.investingRate ?: 0}%"

    val fixedDepositsFormatted = "₹" + formatMoneyAfterL(netWorth?.fixedDeposits ?: 0)
    val mutualFundsFormatted = "₹" + formatMoneyAfterL(netWorth?.mutualFunds ?: 0)

    val totalNetWorthMasked = "₹ XX,XXX"
    val amountMasked = "₹ XX,XXX"
//    val growthMasked = "Hidden"
//    val investingRateMasked = "Hidden"

    Box(
        modifier = Modifier.fillMaxWidth()
            .genericDropShadow()
            .clip(RoundedCornerShape(20.dp))
            .background(Primary),
        contentAlignment = Alignment.Center
    )
    {

        MeshSquareBackground(modifier = Modifier.matchParentSize())

        Icon(
            painter = painterResource(Res.drawable.worth_img),
            contentDescription = null,
            tint = Color.White.copy(0.7f),
            modifier = Modifier
                .size(136.dp)
                .padding(end = 12.dp)
                .align(Alignment.CenterEnd)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 16.dp, top = 24.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Total Net Worth",
                style = MaterialTheme.typography.labelSmall,
                color = Color.White
            )

            Text(
                text = (if (hidden) totalNetWorthMasked else totalNetWorthFormatted).withInterRupee(),
                style = MaterialTheme.typography.displayMedium,
                color = Color.White
            )

            // TODO: restore the Absolute / CAGR growth chip and the Investing Rate link once
            //  `GET /user/` reports them; neither comes from the server yet. Until then the card
            //  shows the Velvet FD and mutual-fund holdings (`dashboard`), as JN's portfolio card does.
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically
//            ) {

//                Box(
//                    modifier = Modifier
//                        .clip(RoundedCornerShape(10.dp))
//                        .background(Color.White, RoundedCornerShape(10.dp))
//                ) {
//                    Column(
//                        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
//                        horizontalAlignment = Alignment.CenterHorizontally,
//                    ) {

//                        Text(
//                            text = "Absolute / CAGR Growth",
//                            style = tinyLabel,
//                            color = Primary
//                        )

//                        Row(
//                            verticalAlignment = Alignment.CenterVertically,
//                            horizontalArrangement = Arrangement.spacedBy(4.dp)
//                        ) {

//                            Icon(
//                                painter = painterResource(Res.drawable.expenses_icon),
//                                contentDescription = null,
//                                tint = bgColor1,
//                                modifier = Modifier.size(15.dp)
//                            )

//                            Text(
//                                text = (if (hidden) growthMasked else growthFormatted).withInterRupee(),
//                                fontWeight = FontWeight.SemiBold,
//                                fontSize = 14.sp,
//                                fontFamily = Poppins,
//                                color = Primary
//                            )
//                        }
//                    }
//                }

//                Column(
//                    verticalArrangement = Arrangement.spacedBy(4.dp),
//                    horizontalAlignment = Alignment.Start
//                ) {

//                    Text(
//                        text = "Investing Rate",
//                        style = subHeading,
//                        color = Color.White
//                    )

//                    Row(
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.spacedBy(10.dp),
//                        modifier = Modifier.clickable(
//                            onClick = onInvestingRateClick,
//                            indication = null,
//                            interactionSource = remember { MutableInteractionSource() }
//                        )
//                    ) {

//                        Text(
//                            text = if (hidden) investingRateMasked else investingRateFormatted,
//                            style = MaterialTheme.typography.headlineSmall,
//                            color = Secondary
//                        )

//                        Icon(
//                            painter = painterResource(Res.drawable.arrow_right),
//                            contentDescription = null,
//                            tint = Secondary,
//                            modifier = Modifier.size(20.dp)
//                        )
//                    }
//                }
//            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                PortfolioAmountCard(
                    title = "Fixed Deposits",
                    amount = if (hidden) amountMasked else fixedDepositsFormatted,
                    modifier = Modifier.weight(1f)
                )
                PortfolioAmountCard(
                    title = "Mutual Funds",
                    amount = if (hidden) amountMasked else mutualFundsFormatted,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        VisibilityToggleIcon(
            isVisible = hidden,
            onToggle = onHiddenToggle,
            modifier=Modifier.align(Alignment.TopEnd).padding(16.dp)
        )
    }
}


/** One holding on the net-worth card (JN `AmountCard`), on the card's navy background. */
@Composable
private fun PortfolioAmountCard(title: String, amount: String, modifier: Modifier = Modifier) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White.copy(alpha = 0.1f))
            .border(1.dp, Color.White.copy(alpha = 0.05f), RoundedCornerShape(10.dp))
            .padding(12.dp)
    ) {
        Text(text = title, style = MaterialTheme.typography.titleSmall, color = Color.White)
        Text(
            text = amount.withInterRupee(),
            style = MaterialTheme.typography.labelSmall,
            color = Color.White
        )
    }
}

fun maskAmount(amount: String): String {

    return amount.map { char ->
        if (char.isDigit()) '*' else if (char=='+'|| char=='-'|| char=='.' ) "" else char
    }.joinToString("")
}

@Composable
fun VisibilityToggleIcon(
    isVisible: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier
) {
    Crossfade(targetState = isVisible, modifier = modifier) { visible ->
        Icon(
            painter = if (visible) painterResource(Res.drawable.icon_eye_closed)
            else painterResource(Res.drawable.icone_eye_open),
            contentDescription = "Toggle Visibility",
            tint = Color.White,
            modifier = Modifier.size(24.dp)
                .clickable(
                onClick = onToggle,
                indication = null,
                interactionSource = remember { MutableInteractionSource() })
        )
    }
}