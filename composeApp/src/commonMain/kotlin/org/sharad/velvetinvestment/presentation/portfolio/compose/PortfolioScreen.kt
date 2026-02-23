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
import org.sharad.velvetinvestment.presentation.portfolio.models.FDCardData
import org.sharad.velvetinvestment.presentation.portfolio.models.FundListCardData
import org.sharad.velvetinvestment.presentation.portfolio.models.MutualFundDashBoardData
import org.sharad.velvetinvestment.presentation.portfolio.models.SelectedPortfolio
import org.sharad.velvetinvestment.presentation.portfolio.models.label
import org.sharad.velvetinvestment.presentation.portfolio.viewmodel.PortfolioScreenViewModel
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.BarHeader
import org.sharad.velvetinvestment.shared.compose.ErrorScreen
import org.sharad.velvetinvestment.shared.compose.FixedDepositCard
import org.sharad.velvetinvestment.shared.compose.GenericTabSwitcher
import org.sharad.velvetinvestment.shared.compose.LoaderScreen
import org.sharad.velvetinvestment.shared.compose.MutualFundsCard
import org.sharad.velvetinvestment.utils.UIState
import org.sharad.velvetinvestment.utils.formatMoneyAfterL
import org.sharad.velvetinvestment.utils.genericDropShadow
import org.sharad.velvetinvestment.utils.theme.Poppins
import org.sharad.velvetinvestment.utils.theme.subHeading
import org.sharad.velvetinvestment.utils.theme.subHeadingMedium
import org.sharad.velvetinvestment.utils.trimTo

@Composable
fun PortfolioScreenMain(
    viewModel: PortfolioScreenViewModel,
    onSIPClick: (id: String) -> Unit,
    onFDClick: (String) -> Unit,
    pv: PaddingValues
) {

    val screenState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val mutualFunds by viewModel.mutualFunds.collectAsStateWithLifecycle()
    val dashBoardData by viewModel.dashboard.collectAsStateWithLifecycle()
    val fixedDeposits by viewModel.fds.collectAsStateWithLifecycle()

    Box(
        modifier=Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ){
        Column(
            modifier=Modifier.fillMaxSize()
        ){
            BackHeader("Portfolio" )
            Box(modifier=Modifier.weight(1f).fillMaxWidth()){
                when (screenState) {
                    is UIState.Error -> {
                        ErrorScreen(
                            "Error"
                        )
                    }

                    UIState.Loading -> {
                        LoaderScreen()
                    }

                    UIState.Success -> {
                        PortfolioScreen(
                            selectedTab = selectedTab,
                            mutualFunds = mutualFunds,
                            dashBoardData = dashBoardData,
                            fixedDeposits = fixedDeposits,
                            changeTab=viewModel::onTabSelected,
                            onSIPClick=onSIPClick,
                            onFDClick=onFDClick
                        )
                    }
                }
            }
        }
    }

}

@Composable
fun PortfolioScreen(
    selectedTab: SelectedPortfolio,
    mutualFunds: List<FundListCardData>,
    dashBoardData: MutualFundDashBoardData?,
    fixedDeposits: List<FDCardData>,
    changeTab: (SelectedPortfolio) -> Unit,
    onSIPClick: (String) -> Unit,
    onFDClick: (String) -> Unit
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
                    FixedDepositPortFolio(fixedDeposits, onFDClick=onFDClick)
                }

                SelectedPortfolio.MutualFunds -> {
                    MutualFundPortFolio(mutualFunds, dashBoardData, onSIPClick=onSIPClick)
                }
            }
        }
    }

}

@Composable
fun MutualFundPortFolio(
    mutualFund: List<FundListCardData>,
    dashBoardData: MutualFundDashBoardData?,
    onSIPClick: (String) -> Unit
) {
    if (mutualFund.isEmpty()){
        EmptyFundScreen(onBrowseClick = {}, text = "Invest a little each month and watch your wealth grow with SIP!", buttonText = "Browse SIP")
    }else{
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item { Spacer(modifier = Modifier.height(4.dp)) }
            item{ DashBoardCard(dashBoardData) }
            item { BarHeader(heading="Your Investments") }
            items(mutualFund, key = {it.id}){item->
                MutualFundsCard(fundItem=item, onClick={
                    onSIPClick(item.id)
                })
            }
            item { Spacer(modifier = Modifier) }
        }
    }
}

@Composable
fun DashBoardCard(dashBoardData: MutualFundDashBoardData?) {
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
                text = if (dashBoardData!=null) "Total Investment (${dashBoardData.total})" else "Total Investment (-)",
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
                    Text("₹" + formatMoneyAfterL(dashBoardData?.currentValue), style = subHeading, color = Color.Black)
                    Text("+₹" + formatMoneyAfterL(dashBoardData?.totalReturns) + " (" + (dashBoardData?.totalReturnsPercentage?.trimTo(1)?.toString()?:"0")+"%)", style = subHeading, color = appGreen)
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
                    Text("1D returns", fontFamily = Poppins, fontSize = 14.sp, color = titleColor)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("₹" + formatMoneyAfterL(dashBoardData?.investedAmount), style = subHeading, color = Color.Black)
                    Text("+₹" + formatMoneyAfterL(dashBoardData?.oneDayReturns) + " (" + (dashBoardData?.oneDayReturnsPercentage?.trimTo(1)?.toString()?:"0")+"%)", style = subHeading, color = appGreen)
                }
            }

        }

    }
}


@Composable
fun FixedDepositPortFolio(fixedDeposits: List<FDCardData>, onFDClick: (String) -> Unit) {
    if (fixedDeposits.isEmpty()){
        EmptyFundScreen(onBrowseClick = {}, text = "Invest a little each month and watch your wealth grow with SIP!", buttonText = "Browse FD")
    }else {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item { Spacer(modifier = Modifier.height(4.dp)) }
            items(fixedDeposits, key = {it.id}) {fd->
                FixedDepositCard(fdData = fd, onClick = {onFDClick(fd.id)})
            }
        }
    }
}

