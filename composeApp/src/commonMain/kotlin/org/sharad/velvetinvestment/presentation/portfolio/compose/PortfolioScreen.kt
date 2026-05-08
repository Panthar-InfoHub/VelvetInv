package org.sharad.velvetinvestment.presentation.portfolio.compose

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.sharad.emify.core.ui.theme.appGreen
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.domain.models.portfolio.FixedDepositPortfolioDomain
import org.sharad.velvetinvestment.domain.models.portfolio.MutualFundPortfolioDomain
import org.sharad.velvetinvestment.domain.models.portfolio.PortfolioDashboardDomain
import org.sharad.velvetinvestment.presentation.portfolio.models.SelectedPortfolio
import org.sharad.velvetinvestment.presentation.portfolio.models.label
import org.sharad.velvetinvestment.presentation.portfolio.viewmodel.PortfolioScreenViewModel
import org.sharad.velvetinvestment.shared.UiStateContainer
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.BarHeader
import org.sharad.velvetinvestment.shared.compose.FixedDepositCard
import org.sharad.velvetinvestment.shared.compose.GenericTabSwitcher
import org.sharad.velvetinvestment.shared.compose.MutualFundsCard
import org.sharad.velvetinvestment.shared.genericDropShadow
import org.sharad.velvetinvestment.utils.formatMoneyAfterL
import org.sharad.velvetinvestment.shared.theme.Poppins
import org.sharad.velvetinvestment.shared.theme.subHeading
import org.sharad.velvetinvestment.shared.theme.subHeadingMedium
import org.sharad.velvetinvestment.utils.trimTo

@Composable
fun PortfolioScreenMain(
    viewModel: PortfolioScreenViewModel,
    onSIPClick: (MutualFundPortfolioDomain) -> Unit,
    onFDClick: (String) -> Unit,
    pv: PaddingValues,
    navigateToCategoryMutualFundScreen: () -> Unit,
    navigateToCategoryFDScreen: () -> Unit
) {

    val screenState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()

    Box(
        modifier=Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column(
            modifier=Modifier.fillMaxSize()
        ){
            BackHeader("Portfolio" )
            Box(modifier = Modifier.weight(1f).fillMaxWidth()) {
                UiStateContainer(
                    uiState = screenState,
                    onRetry = viewModel::loadPortfolio
                ) { data ->
                    PortfolioScreen(
                        selectedTab = selectedTab,
                        mutualFunds = data.mutualFunds,
                        dashBoardData = data.dashboard,
                        fixedDeposits = data.fixedDeposits,
                        changeTab = viewModel::onTabSelected,
                        onSIPClick = onSIPClick,
                        onFDClick = onFDClick,
                        navigateToCategoryFDScreen = navigateToCategoryFDScreen,
                        navigateToCategoryMutualFundScreen = navigateToCategoryMutualFundScreen,
                        reload = viewModel::loadPortfolio
                    )
                }
            }
        }
    }

}

@Composable
fun PortfolioScreen(
    selectedTab: SelectedPortfolio,
    mutualFunds: List<MutualFundPortfolioDomain>,
    dashBoardData: PortfolioDashboardDomain,
    fixedDeposits: List<FixedDepositPortfolioDomain>,
    changeTab: (SelectedPortfolio) -> Unit,
    onSIPClick: (MutualFundPortfolioDomain) -> Unit,
    onFDClick: (String) -> Unit,
    navigateToCategoryFDScreen: () -> Unit,
    navigateToCategoryMutualFundScreen: () -> Unit,
    reload: () -> Unit
) {

    Column(
        modifier=Modifier.fillMaxWidth(),
    ) {

        GenericTabSwitcher(
            tabs = SelectedPortfolio.tabs,
            selectedTab = selectedTab,
            onTabSelected = { changeTab(it) }
        ) { tab, selected ->
            Text(
                text = tab.label(),
                style = subHeadingMedium,
                color = Color.Black,
                softWrap = false,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }

        Box(modifier=Modifier.weight(1f)){
            when (selectedTab) {
                SelectedPortfolio.FixedDeposits -> {
                    FixedDepositPortFolio(fixedDeposits, onFDClick =onFDClick, onEmptyButtonClick=navigateToCategoryFDScreen, reload=reload)
                }

                SelectedPortfolio.MutualFunds -> {
                    MutualFundPortFolio(mutualFunds, dashBoardData, onFundClick =onSIPClick, onEmptyButtonClick=navigateToCategoryMutualFundScreen, reload=reload)
                }
            }
        }
    }

}

@Composable
fun MutualFundPortFolio(
    mutualFund: List<MutualFundPortfolioDomain>,
    dashBoardData: PortfolioDashboardDomain,
    onFundClick: (MutualFundPortfolioDomain) -> Unit,
    onEmptyButtonClick: () -> Unit,
    reload: () -> Unit
) {
    if (mutualFund.isEmpty()){
        EmptyFundScreen(onBrowseClick = onEmptyButtonClick, text = "Grow your wealth with Mutual Funds through SIPs for steady investing or Lumpsum for one-time opportunities.", buttonText = "Browse SIP")
    }else{
        PullToRefreshBox(
            isRefreshing = false,
            onRefresh = reload
        ){
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item { Spacer(modifier = Modifier.height(4.dp)) }
                item { DashBoardCard(dashBoardData) }
                item { BarHeader(heading = "Your Investments") }
                items(mutualFund, key = { it.id }) { item ->
                    MutualFundsCard(fundItem = item, onClick = {
                        onFundClick(item)
                    })
                }
                item { Spacer(modifier = Modifier) }
            }
        }
    }
}

@Composable
fun DashBoardCard(dashBoardData: PortfolioDashboardDomain) {
    Box(
        modifier = Modifier.fillMaxWidth()
            .genericDropShadow(RoundedCornerShape(15.dp))
            .clip(RoundedCornerShape(15.dp))
            .background(Color.White)
    ){

        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Text(
                text = "Total Investment (${dashBoardData.investedAmount})",
                style = subHeadingMedium,
                color = titleColor
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Current Value", fontFamily = Poppins, fontSize = 14.sp, color = titleColor)
                    Text("Total Returns", fontFamily = Poppins, fontSize = 14.sp, color = titleColor)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("₹" + formatMoneyAfterL(dashBoardData.currentValue.toLong()), style = subHeading, color = Color.Black)
                    Text("+₹" + formatMoneyAfterL(dashBoardData.totalReturns.toLong()) + " (" + dashBoardData.returnPercent.trimTo(1) +"%)", style = subHeading, color = appGreen)
                }
            }
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Invested Amount", fontFamily = Poppins, fontSize = 14.sp, color = titleColor)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("₹" + formatMoneyAfterL(dashBoardData.investedAmount.toLong()), style = subHeading, color = Color.Black)
                }
            }

        }

    }
}


@Composable
fun FixedDepositPortFolio(
    fixedDeposits: List<FixedDepositPortfolioDomain>,
    onFDClick: (String) -> Unit,
    onEmptyButtonClick: () -> Unit,
    reload: () -> Unit
) {
    if (fixedDeposits.isEmpty()){
        EmptyFundScreen(onBrowseClick = onEmptyButtonClick, text = "Lock in your savings with Fixed Deposits and earn stable, guaranteed returns over time.", buttonText = "Browse FD")
    }else {
        PullToRefreshBox(
            isRefreshing = false,
            onRefresh = reload
        ){
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item { Spacer(modifier = Modifier.height(4.dp)) }
                items(fixedDeposits, key = { it.id }) { fd ->
                    FixedDepositCard(fdData = fd, onClick = { onFDClick(fd.id) })
                }
            }
        }
    }
}

