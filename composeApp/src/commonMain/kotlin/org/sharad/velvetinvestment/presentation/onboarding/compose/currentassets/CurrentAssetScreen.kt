package org.sharad.velvetinvestment.presentation.onboarding.compose.currentassets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.bgColor1
import org.sharad.emify.core.ui.theme.bgColor3
import org.sharad.emify.core.ui.theme.bgColor4
import org.sharad.emify.core.ui.theme.bgColor5
import org.sharad.emify.core.ui.theme.bgColor7
import org.sharad.emify.core.ui.theme.bgSecondaryColor
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.presentation.onboarding.compose.financialflow.ExpandableExpenseEntryField
import org.sharad.velvetinvestment.presentation.onboarding.models.AssetFlowDetails
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.CurrentAssetViewModel
import org.sharad.velvetinvestment.shared.compose.ContinueBackButtonFooter
import org.sharad.velvetinvestment.utils.AppBackHandler
import org.sharad.velvetinvestment.utils.formatMoneyWithUnits
import org.sharad.velvetinvestment.utils.theme.Poppins
import org.sharad.velvetinvestment.utils.theme.titlesStyle
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.icon_download
import velvet.composeapp.generated.resources.icon_fd
import velvet.composeapp.generated.resources.icon_gold
import velvet.composeapp.generated.resources.icon_mf
import velvet.composeapp.generated.resources.icon_money
import velvet.composeapp.generated.resources.icon_real_estate
import velvet.composeapp.generated.resources.icon_stocks
import velvet.composeapp.generated.resources.icon_upload

@Composable
fun CurrentAssetScreen(
    modifier: Modifier = Modifier,
    pv: PaddingValues,
    onNext: () -> Unit,
    onPrev: () -> Unit,
    viewModel: CurrentAssetViewModel
) {

    AppBackHandler(true){
        onPrev()
    }


    val assetInfo by viewModel.assetInfo.collectAsStateWithLifecycle()
    val totalAssets by viewModel.totalAssets.collectAsStateWithLifecycle()


    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        )
        {

            item {
                InfoHeader(onClick = viewModel::showCASDialog)
            }

            item {
                AssetHolding(
                    assetInfo = assetInfo,

                    onMutualFundsUpdate = viewModel::onMutualFundsUpdate,
                    onStocksAndSharesUpdate = viewModel::onStocksAndSharesUpdate,
                    onFixedDepositsUpdate = viewModel::onFixedDepositsUpdate,
                    onRealEstateUpdate = viewModel::onRealEstateUpdate,
                    onGoldAndCommoditiesUpdate = viewModel::onGoldAndCommoditiesUpdate,
                    onCashUpdate = viewModel::onCashUpdate
                )

            }

            item{
                TotalAssets(totalAssets=totalAssets)
            }

            item {
                Spacer(
                    modifier = Modifier.height(80.dp+pv.calculateBottomPadding())
                )
            }

        }
        ContinueBackButtonFooter(
            onContinue = onNext,
            onBack = onPrev,
            pv = pv
        )
    }

}

@Composable
fun TotalAssets(totalAssets: Long) {
    Box(
        modifier = Modifier.fillMaxWidth()
            .height(220.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(
                bgColor4.copy(alpha = 0.1f),
                RoundedCornerShape(15.dp)
            ),
        contentAlignment = Alignment.Center
    ){
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Total Current Assets",
                style = MaterialTheme.typography.headlineSmall,
            )
            Text(
                text = "₹${formatMoneyWithUnits(totalAssets)}",
                fontWeight = FontWeight.SemiBold,
                fontFamily = Poppins,
                fontSize = 40.sp
            )
        }
    }
}


@Composable
fun AssetHolding(
    assetInfo: AssetFlowDetails,
    onMutualFundsUpdate: (String) -> Unit,
    onStocksAndSharesUpdate: (String) -> Unit,
    onFixedDepositsUpdate: (String) -> Unit,
    onRealEstateUpdate: (String) -> Unit,
    onGoldAndCommoditiesUpdate: (String) -> Unit,
    onCashUpdate: (String) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        ExpandableExpenseEntryField(
            heading = "Mutual Funds",
            subHeading = "SIP investments, ELSS, equity funds",
            amount = assetInfo.mutualFunds,
            percentage = null,
            accentColor = bgColor1,
            icon = Res.drawable.icon_mf,
            onValueChange = onMutualFundsUpdate
        )

        ExpandableExpenseEntryField(
            heading = "Stocks & Shares",
            subHeading = "Direct equity, trading portfolio",
            amount = assetInfo.stocksAndShares,
            percentage = null,
            accentColor = bgColor4,
            icon = Res.drawable.icon_stocks,
            onValueChange = onStocksAndSharesUpdate
        )

        ExpandableExpenseEntryField(
            heading = "Fixed Deposits",
            subHeading = "FDs, PPF, EPF, NSC",
            amount = assetInfo.fixedDeposits,
            percentage = null,
            accentColor = bgColor5,
            icon = Res.drawable.icon_fd,
            onValueChange = onFixedDepositsUpdate
        )

        ExpandableExpenseEntryField(
            heading = "Real Estate",
            subHeading = "Property, land, REITs",
            amount = assetInfo.realEstate,
            percentage = null,
            accentColor = bgColor7,
            icon = Res.drawable.icon_real_estate,
            onValueChange = onRealEstateUpdate
        )

        ExpandableExpenseEntryField(
            heading = "Gold & Commodities",
            subHeading = "Physical gold, digital gold, ETFs",
            amount = assetInfo.goldAndCommodities,
            percentage = null,
            accentColor = bgColor7,
            icon = Res.drawable.icon_gold,
            onValueChange = onGoldAndCommoditiesUpdate
        )

        ExpandableExpenseEntryField(
            heading = "Cash & Savings",
            subHeading = "Savings account, current account",
            amount = assetInfo.cash,
            percentage = null,
            accentColor = bgSecondaryColor,
            icon = Res.drawable.icon_money,
            onValueChange = onCashUpdate
        )
    }
}


@Composable
fun InfoHeader(
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            Text(
                text = "Your Current Assets",
                style = MaterialTheme.typography.headlineLarge,
                color = Primary,
            )

            Box(
                modifier=Modifier
                    .clip(RoundedCornerShape(25))
                    .clickable(
                        onClick = {onClick()}
                    )
                    .background(bgColor3.copy(0.1f),RoundedCornerShape(25)),
                contentAlignment = Alignment.Center
            ){
                Row(
                    modifier=Modifier.padding(horizontal = 10.dp, vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "CAS Report",
                        style = titlesStyle,
                        color = Secondary,
                    )
                    Icon(
                        painter = painterResource(Res.drawable.icon_download),
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = Secondary
                    )
                }
            }
        }
        Text(
            text="Tell us about your existing investments and assets to create a complete financial picture",
            style = titlesStyle,
            color = titleColor
        )
    }
}