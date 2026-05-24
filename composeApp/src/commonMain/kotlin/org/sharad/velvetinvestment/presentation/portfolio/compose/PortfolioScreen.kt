package org.sharad.velvetinvestment.presentation.portfolio.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.PathGray
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.appGreen
import org.sharad.emify.core.ui.theme.appRed
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.domain.models.portfolio.FixedDepositPortfolioDomain
import org.sharad.velvetinvestment.domain.models.portfolio.InvestedAmountBreakdownDomain
import org.sharad.velvetinvestment.domain.models.portfolio.MutualFundPortfolioDomain
import org.sharad.velvetinvestment.domain.models.portfolio.PendingOrderDomain
import org.sharad.velvetinvestment.domain.models.portfolio.PortfolioAllocationDomain
import org.sharad.velvetinvestment.domain.models.portfolio.PortfolioAllocationItemDomain
import org.sharad.velvetinvestment.domain.models.portfolio.PortfolioDashboardDomain
import org.sharad.velvetinvestment.domain.models.portfolio.PortfolioDomain
import org.sharad.velvetinvestment.domain.models.portfolio.TotalInvestmentsDomain
import org.sharad.velvetinvestment.presentation.portfolio.models.SelectedPortfolio
import org.sharad.velvetinvestment.presentation.portfolio.models.label
import org.sharad.velvetinvestment.presentation.portfolio.viewmodel.PortfolioScreenViewModel
import org.sharad.velvetinvestment.shared.UiStateContainer
import org.sharad.velvetinvestment.shared.compose.AppButton
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.BarHeader
import org.sharad.velvetinvestment.shared.compose.FixedDepositCard
import org.sharad.velvetinvestment.shared.compose.GenericTabSwitcher
import org.sharad.velvetinvestment.shared.compose.MutualFundsCard
import org.sharad.velvetinvestment.shared.genericDropShadow
import org.sharad.velvetinvestment.shared.theme.LocalVelvetShapes
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import org.sharad.velvetinvestment.shared.theme.subHeading
import org.sharad.velvetinvestment.shared.theme.subHeadingMedium
import org.sharad.velvetinvestment.shared.theme.tinyLabel
import org.sharad.velvetinvestment.shared.theme.titlesStyle
import org.sharad.velvetinvestment.utils.formatMoneyAfterL
import org.sharad.velvetinvestment.utils.trimTo
import org.sharad.velvetinvestment.utils.withInterRupee
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.download_ic
import kotlin.math.abs

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

    val isExportingCapital by viewModel.isExportingCapital.collectAsStateWithLifecycle()
    val isExportingTax by viewModel.isExportingTax.collectAsStateWithLifecycle()
    val isExportingPortfolio by viewModel.isExportingPortfolio.collectAsStateWithLifecycle()

    val pendingOrders by viewModel.pendingOrders.collectAsStateWithLifecycle()
    val pagerState = rememberPagerState(pageCount = { 3 })

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
                        portfolioData = data,
                        changeTab = viewModel::onTabSelected,
                        onSIPClick = onSIPClick,
                        onFDClick = onFDClick,
                        navigateToCategoryFDScreen = navigateToCategoryFDScreen,
                        navigateToCategoryMutualFundScreen = navigateToCategoryMutualFundScreen,
                        reload = viewModel::loadPortfolio,
                        onDownloadPortfolioReport = viewModel::downloadPortfolioReport,
                        onDownloadCapitalReport = viewModel::downloadCapitalReport,
                        onDownloadTaxReport = { viewModel.downloadTaxReport(2024) },
                        isExportingPortfolio = isExportingPortfolio,
                        isExportingCapital = isExportingCapital,
                        isExportingTax = isExportingTax,
                        pendingOrders = pendingOrders,
                        pagerState=pagerState
                    )
                }
            }
        }
    }

}

@Composable
fun PortfolioScreen(
    selectedTab: SelectedPortfolio,
    portfolioData: PortfolioDomain,
    changeTab: (SelectedPortfolio) -> Unit,
    onSIPClick: (MutualFundPortfolioDomain) -> Unit,
    onFDClick: (String) -> Unit,
    navigateToCategoryFDScreen: () -> Unit,
    navigateToCategoryMutualFundScreen: () -> Unit,
    reload: () -> Unit,
    onDownloadPortfolioReport: () -> Unit,
    onDownloadCapitalReport: () -> Unit,
    onDownloadTaxReport: () -> Unit,
    isExportingPortfolio: Boolean,
    isExportingCapital: Boolean,
    isExportingTax: Boolean,
    pendingOrders: List<PendingOrderDomain>,
    pagerState: PagerState
) {
    val scope = rememberCoroutineScope()

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .collect { page ->
                changeTab(SelectedPortfolio.tabs[page])
            }
    }


    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {

        GenericTabSwitcher(
            tabs = SelectedPortfolio.tabs,
            selectedTab = selectedTab,
            onTabSelected = {
                changeTab(it)
                scope.launch {
                    pagerState.animateScrollToPage(
                        SelectedPortfolio.tabs.indexOf(it)
                    )
                }
            }
        ) { tab, selected ->
            Text(
                text = tab.label(),
                style = subHeadingMedium,
                color = if (selected) Primary else titleColor,
                softWrap = false,
                modifier = Modifier.padding(horizontal = 4.dp)
            )
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ){
            when(it){
                0 -> {
                    DashboardPortfolio(
                        totalInvestments = portfolioData.totalInvestments,
                        mutualFunds = portfolioData.mutualFunds,
                        fixedDeposits = portfolioData.fixedDeposits,
                        onSeeAllMF = {
                            changeTab(SelectedPortfolio.MutualFunds)
                            scope.launch { pagerState.animateScrollToPage(1) }
                        },
                        onSeeAllFD = {
                            changeTab(SelectedPortfolio.FixedDeposits)
                            scope.launch { pagerState.animateScrollToPage(2) }

                        },
                        onSIPClick = onSIPClick,
                        onFDClick = onFDClick,
                        reload = reload
                    )
                }
                1-> {
                    MutualFundPortfolio(
                        mutualFund = portfolioData.mutualFunds,
                        investedBreakdown = portfolioData.investedAmountBreakdown,
                        onFundClick = onSIPClick,
                        onEmptyButtonClick = navigateToCategoryMutualFundScreen,
                        reload = reload,
                        onDownloadCapitalReport = onDownloadCapitalReport,
                        onDownloadTaxReport = onDownloadTaxReport,
                        isExportingCapital = isExportingCapital,
                        isExportingTax = isExportingTax,
                        onDownloadPortfolioReport = onDownloadPortfolioReport,
                        isExportingPortfolio = isExportingPortfolio,
                        pendingOrders = pendingOrders
                    )
                }
                2-> {
                    FixedDepositPortfolio(
                        fixedDeposits = portfolioData.fixedDeposits,
                        onFDClick = onFDClick,
                        onEmptyButtonClick = navigateToCategoryFDScreen,
                        reload = reload
                    )
                }
            }
        }
    }

}

@Composable
fun DashboardPortfolio(
    totalInvestments: TotalInvestmentsDomain,
    mutualFunds: List<MutualFundPortfolioDomain>,
    fixedDeposits: List<FixedDepositPortfolioDomain>,
    onSeeAllMF: () -> Unit,
    onSeeAllFD: () -> Unit,
    onSIPClick: (MutualFundPortfolioDomain) -> Unit,
    onFDClick: (String) -> Unit,
    reload: () -> Unit
) {
    PullToRefreshBox(
        isRefreshing = false,
        onRefresh = reload
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item { Spacer(modifier = Modifier.height(4.dp)) }
            item { TotalInvestmentCard(totalInvestments) }

            if (mutualFunds.isNotEmpty()) {
                item {
                    BarHeader(
                        heading = "Mutual Funds",
                        showArrow = true,
                        onArrowClick = onSeeAllMF
                    )
                }
                items(mutualFunds.take(2), key = { "mf_${it.id}" }) { item ->
                    MutualFundsCard(fundItem = item, onClick = { onSIPClick(item) })
                }
            }

            if (fixedDeposits.isNotEmpty()) {
                item {
                    BarHeader(
                        heading = "Fixed Deposits",
                        showArrow = true,
                        onArrowClick = onSeeAllFD
                    )
                }
                items(fixedDeposits.take(2), key = { "fd_${it.id}" }) { fd ->
                    FixedDepositCard(fdData = fd, onClick = { onFDClick(fd.id) })
                }
            }
            item { Spacer(modifier = Modifier.height(20.dp)) }
        }
    }
}

@Composable
fun MutualFundPortfolio(
    mutualFund: List<MutualFundPortfolioDomain>,
    investedBreakdown: InvestedAmountBreakdownDomain,
    onFundClick: (MutualFundPortfolioDomain) -> Unit,
    onEmptyButtonClick: () -> Unit,
    reload: () -> Unit,
    onDownloadCapitalReport: () -> Unit,
    onDownloadTaxReport: () -> Unit,
    isExportingCapital: Boolean,
    isExportingTax: Boolean,
    onDownloadPortfolioReport: () -> Unit,
    isExportingPortfolio: Boolean,
    pendingOrders: List<PendingOrderDomain>
) {
    if (mutualFund.isEmpty()) {
        EmptyFundScreen(
            onBrowseClick = onEmptyButtonClick,
            text = "Grow your wealth with Mutual Funds through SIPs for steady investing or Lumpsum for one-time opportunities.",
            buttonText = "Browse SIP"
        )
    } else {
        PullToRefreshBox(
            isRefreshing = false,
            onRefresh = reload
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item { Spacer(modifier = Modifier.height(4.dp)) }
                item {
                    MFInvestmentsCard(
                        investedBreakdown,
                        onInvestMore = onEmptyButtonClick,
                        onDownloadCapitalReport = onDownloadCapitalReport,
                        onDownloadTaxReport = onDownloadTaxReport,
                        onDownloadPortfolioReport=onDownloadPortfolioReport,
                        isExportingPortfolio= isExportingPortfolio,
                        isExportingCapital = isExportingCapital,
                        isExportingTax = isExportingTax
                    )
                }
                if (pendingOrders.isNotEmpty()) {
                    item { BarHeader(heading = "Pending Payments") }
                    items(pendingOrders, key = { it.id }) { item ->
                        PendingPaymentsCard(item)
                    }
                }
                item { BarHeader(heading = "Mutual Funds") }
                items(mutualFund, key = { it.id }) { item ->
                    MutualFundsCard(fundItem = item, onClick = {
                        onFundClick(item)
                    })
                }
                item { Spacer(modifier = Modifier.height(20.dp)) }
            }
        }
    }
}

@Composable
fun FixedDepositPortfolio(
    fixedDeposits: List<FixedDepositPortfolioDomain>,
    onFDClick: (String) -> Unit,
    onEmptyButtonClick: () -> Unit,
    reload: () -> Unit
) {
    if (fixedDeposits.isEmpty()) {
        EmptyFundScreen(
            onBrowseClick = onEmptyButtonClick,
            text = "Lock in your savings with Fixed Deposits and earn stable, guaranteed returns over time.",
            buttonText = "Browse FD"
        )
    } else {
        PullToRefreshBox(
            isRefreshing = false,
            onRefresh = reload
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                item { Spacer(modifier = Modifier.height(4.dp)) }
                item { BarHeader(heading = "Fixed Deposits") }
                items(fixedDeposits, key = { it.id }) { fd ->
                    FixedDepositCard(fdData = fd, onClick = { onFDClick(fd.id) })
                }
                item {
                    Box(modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp), contentAlignment = Alignment.Center) {
                        AppButton(
                            text = "Invest More",
                            onClick = onEmptyButtonClick,
                            modifier = Modifier.width(180.dp),
                            shape = RoundedCornerShape(50)
                        )
                    }
                }
                item { Spacer(modifier = Modifier.height(20.dp)) }
            }
        }
    }
}

@Composable
fun TotalInvestmentCard(totalInvestments: TotalInvestmentsDomain) {
    val shapes = LocalVelvetShapes.current
    val labelColorBlue= Color(0xff00658D)
    Box(
        modifier = Modifier.fillMaxWidth()
            .genericDropShadow(shapes.roundedDp15)
            .clip(shapes.roundedDp15)
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column{
                Text(text = "Current Value", style = titlesStyle, color = Color.Black)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "₹${formatMoneyAfterL(totalInvestments.currentValue.toLong())}".withInterRupee(),
                        style = subHeading.copy(fontSize = 36.sp, fontWeight = FontWeight.ExtraBold),
                        color = Primary
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier.clip(CircleShape)
                            .background(
                                if (totalInvestments.returnPercent >= 0) Color(0xffC6E7FF).copy(
                                    0.3f
                                ) else appRed.copy(0.1f)
                            )
                            .padding(horizontal = 12.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = "${if (totalInvestments.returnPercent >= 0) "+" else ""}${
                                totalInvestments.returnPercent.trimTo(
                                    2
                                )
                            }%",
                            style = tinyLabel,
                            color = if (totalInvestments.returnPercent >= 0) labelColorBlue else Color.Red,
                        )
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Allocation", style = tinyLabel)
                    Text(
                        text = "Total: ₹${formatMoneyAfterL(totalInvestments.currentValue.toLong())}".withInterRupee(),
                        style = tinyLabel,
                        color = Primary
                    )
                }

                val mfPercent = totalInvestments
                    .allocation
                    .mutualFunds
                    .percent
                    .toFloat()

                val fdPercent = totalInvestments
                    .allocation
                    .fixedDeposits
                    .percent
                    .toFloat()

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(CircleShape)
                        .background(PathGray)
                ) {

                    if (mfPercent > 0f) {
                        Box(
                            modifier = Modifier
                                .weight(mfPercent)
                                .fillMaxHeight()
                                .background(Primary)
                        )
                    }

                    if (fdPercent > 0f) {
                        Box(
                            modifier = Modifier
                                .weight(fdPercent)
                                .fillMaxHeight()
                                .background(Secondary)
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Primary))
                        Text(
                            text = "Mutual Funds (${totalInvestments.allocation.mutualFunds.percent.trimTo(0)}%)",
                            style = tinyLabel,
                            color = Color.Black.copy(alpha = 0.8f)
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Secondary))
                        Text(
                            text = "Fixed Deposits (${totalInvestments.allocation.fixedDeposits.percent.trimTo(0)}%)",
                            color = Color.Black.copy(alpha = 0.8f),
                            style = tinyLabel
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun MFInvestmentsCard(
    investedBreakdown: InvestedAmountBreakdownDomain,
    onInvestMore: () -> Unit,
    onDownloadCapitalReport: () -> Unit,
    onDownloadTaxReport: () -> Unit,
    isExportingCapital: Boolean,
    isExportingTax: Boolean,
    onDownloadPortfolioReport: () -> Unit,
    isExportingPortfolio: Boolean
) {
    val shapes = LocalVelvetShapes.current
    Box(
        modifier = Modifier.fillMaxWidth()
            .genericDropShadow(shapes.roundedDp15)
            .clip(shapes.roundedDp15)
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column {
                    Text(text = "Total Investments", style = titlesStyle, color = titleColor)
                    Text(
                        text = "₹${formatMoneyAfterL(investedBreakdown.investedAmount.toLong() + investedBreakdown.returnsAmount.toLong())}".withInterRupee(),
                        style = subHeading.copy(fontSize = 24.sp),
                        color = Primary
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "${if (investedBreakdown.returnsPercent >= 0) "+" else ""}${
                                investedBreakdown.returnsPercent.trimTo(
                                    2
                                )
                            }%",
                            style = titlesStyle,
                            color = if (investedBreakdown.returnsPercent >= 0) appGreen else Color.Red
                        )
                        Text(
                            text = "Total Returns (${if (investedBreakdown.returnsAmount < 0) "-₹" else "₹"}${
                                formatMoneyAfterL(
                                    abs(
                                        investedBreakdown.returnsAmount.toLong()
                                    )
                                )
                            })".withInterRupee(),
                            style = titlesStyle,
                            color = titleColor
                        )
                    }
                }
            }
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f), modifier = Modifier.padding(vertical = 12.dp))

            Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Invested Amount", style = titlesStyle, color = titleColor)
                    Text(
                        text = "₹${formatMoneyAfterL(investedBreakdown.investedAmount.toLong())}".withInterRupee(),
                        style = subHeading,
                        color = Color.Black
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = "Return Percent", style = titlesStyle, color = titleColor)
                    Text(
                        text = investedBreakdown.returnsPercent.trimTo(2) + "%",
                        style = subHeading,
                        color = if (investedBreakdown.returnsPercent >= 0) appGreen else Color.Red
                    )
                }
            }
            HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f), modifier = Modifier.padding(vertical = 12.dp))
            Row{
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    ReportDownloadItem(
                        text = "Holdings PDF",
                        loading = isExportingCapital,
                        onClick = onDownloadCapitalReport
                    )

                    ReportDownloadItem(
                        text = "Tax Saving PDF",
                        loading = isExportingTax,
                        onClick = onDownloadTaxReport
                    )

                    ReportDownloadItem(
                        text = "Portfolio PDF",
                        loading = isExportingPortfolio,
                        onClick = onDownloadPortfolioReport
                    )
                }
                AppButton(
                    text = "INVEST MORE",
                    onClick = onInvestMore,
                    modifier = Modifier.width(160.dp),
                    shape = RoundedCornerShape(12.dp),
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xffffff)
@Composable
fun MFInvestmentsCardPreview() {
    VelvetTheme {
        Box(modifier = Modifier.padding(16.dp)) {
            MFInvestmentsCard(
                investedBreakdown = previewPortfolioData.investedAmountBreakdown,
                onInvestMore = {},
                onDownloadCapitalReport = {},
                onDownloadTaxReport = {},
                isExportingCapital = false,
                isExportingTax = false,
                onDownloadPortfolioReport = {},
                isExportingPortfolio = false,
            )
        }
    }
}
@Composable
private fun ReportDownloadItem(
    text: String,
    loading: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick, indication = null, interactionSource = remember { MutableInteractionSource() })
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        if (loading) {
            CircularProgressIndicator(
                modifier = Modifier.size(14.dp),
                strokeWidth = 2.dp
            )
        } else {
            Icon(
                painter = painterResource(Res.drawable.download_ic),
                contentDescription = null,
                modifier = Modifier.size(14.dp)
            )
        }

        Text(
            text = text,
            style = titlesStyle.copy(
                fontWeight = FontWeight.Medium
            ),
            color = Primary
        )
    }
}
@Preview(showBackground = true, backgroundColor = 0xffffff)
@Composable
fun DashboardPortfolioPreview() {
    val pagerState= rememberPagerState(initialPage = 0) { 3 }
    VelvetTheme {
        PortfolioScreen(
            selectedTab = SelectedPortfolio.Dashboard,
            portfolioData = previewPortfolioData,
            changeTab = {},
            onSIPClick = {},
            onFDClick = {},
            navigateToCategoryFDScreen = {},
            navigateToCategoryMutualFundScreen = {},
            reload = {},
            onDownloadPortfolioReport = {},
            onDownloadCapitalReport = {},
            onDownloadTaxReport = {},
            isExportingPortfolio = false,
            isExportingCapital = false,
            isExportingTax = false,
            pendingOrders = samplePendingOrders,
            pagerState=pagerState
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xffffff)
@Composable
fun MutualFundPortfolioPreview() {
    val pagerState= rememberPagerState(initialPage = 1) { 3 }
    VelvetTheme {
        PortfolioScreen(
            selectedTab = SelectedPortfolio.MutualFunds,
            portfolioData = previewPortfolioData,
            changeTab = {},
            onSIPClick = {},
            onFDClick = {},
            navigateToCategoryFDScreen = {},
            navigateToCategoryMutualFundScreen = {},
            reload = {},
            onDownloadPortfolioReport = {},
            onDownloadCapitalReport = {},
            onDownloadTaxReport = {},
            isExportingPortfolio = false,
            isExportingCapital = false,
            isExportingTax = false,
            pendingOrders = samplePendingOrders,
            pagerState=pagerState
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xffffff)
@Composable
fun FixedDepositPortfolioPreview() {
    VelvetTheme {
        FixedDepositPortfolio(
            fixedDeposits = previewPortfolioData.fixedDeposits,
            onFDClick = {},
            onEmptyButtonClick = {},
            reload = {}
        )
    }
}

@Composable
fun PendingPaymentsCard(item: PendingOrderDomain) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .genericDropShadow(RoundedCornerShape(15.dp))
            .clip(RoundedCornerShape(15.dp))
            .background(Color.White)
            .padding(16.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = item.schemeName,
                        color = Color.Black,
                        style = MaterialTheme.typography.labelSmall.copy(lineHeight = 20.sp),
                        maxLines = 2
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = item.amc,
                        color = titleColor,
                        style = titlesStyle
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "₹${formatMoneyAfterL(item.amount.toLong())}".withInterRupee(),
                        color = Color.Black,
                        style = MaterialTheme.typography.labelSmall
                    )
                    Text(
                        text = item.type,
                        style = tinyLabel,
                        color = Secondary
                    )
                }
            }

            HorizontalDivider(color = PathGray.copy(alpha = 0.5f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Status",
                        style = tinyLabel,
                        color = Color.Gray
                    )
                    Text(
                        text = item.status,
                        style = subHeadingMedium.copy(fontSize = 12.sp),
                        color = if (item.status.contains("Pending", true)) appRed else appGreen
                    )
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "Date",
                        style = tinyLabel,
                        color = Color.Gray
                    )
                    Text(
                        text = item.date,
                        style = subHeadingMedium.copy(fontSize = 12.sp),
                        color = titleColor
                    )
                }
            }

            if (item.statusRemark.isNotEmpty()) {
                Text(
                    text = item.statusRemark,
                    style = tinyLabel.copy(fontSize = 10.sp),
                    color = appRed.copy(alpha = 0.8f)
                )
            }
        }
    }
}

private val samplePendingOrders = listOf(
    PendingOrderDomain(
        id = "1",
        type = "SIP",
        schemeName = "SBI Bluechip Fund - Direct Plan - Growth",
        amount = 5000.0,
        date = "2023-10-25T10:00:00Z",
        status = "Payment Pending",
        statusRemark = "Action required: Complete payment to start SIP",
        amc = "SBI Mutual Fund",
        frequency = "Monthly",
        startDate = "2023-11-01"
    ),
//    PendingOrderDomain(
//        id = "2",
//        type = "Lumpsum",
//        schemeName = "HDFC Index Fund - Nifty 50 Plan",
//        amount = 10000.0,
//        date = "2023-10-24T15:30:00Z",
//        status = "Processing",
//        statusRemark = "Waiting for AMC confirmation",
//        amc = "HDFC Mutual Fund",
//        frequency = "One-time",
//        startDate = "2023-10-24"
//    )
)

private val previewPortfolioData = PortfolioDomain(
    dashboard = PortfolioDashboardDomain(
        currentValue = 1250000.0,
        investedAmount = 1000000.0,
        totalReturns = 250000,
        returnPercent = 25.0
    ),
    totalInvestments = TotalInvestmentsDomain(
        currentValue = 1250000.0,
        totalReturns = 250000.0,
        returnPercent = -25.06,
        allocation = PortfolioAllocationDomain(
            mutualFunds = PortfolioAllocationItemDomain(value = 750000.0, percent = 60.0),
            fixedDeposits = PortfolioAllocationItemDomain(value = 500000.0, percent = 40.0)
        )
    ),
    investedAmountBreakdown = InvestedAmountBreakdownDomain(
        investedAmount = 1000000.0,
        investedItemsCount = 5,
        returnsAmount = 250000.0,
        returnsPercent = 25.0
    ),
    mutualFunds = listOf(
        MutualFundPortfolioDomain(
            id = 1,
            title = "Axis Bluechip Fund",
            category = "Equity",
            amount = 50000.0,
            isSip = true,
            startDate = "2021-01-01",
            returnPercentage = "15.5",
            returnAmount = 7500,
            xirr = "18.2",
            currentNav = 45.2,
            avgNav = 38.5,
            folio = "12345678",
            balanceUnits = 1298.7,
            icon = ""
        ),
        MutualFundPortfolioDomain(
            id = 2,
            title = "SBI Small Cap Fund",
            category = "Equity",
            amount = 30000.0,
            isSip = false,
            startDate = "2021-06-15",
            returnPercentage = "22.1",
            returnAmount = 6630,
            xirr = "25.4",
            currentNav = 112.5,
            avgNav = 92.1,
            folio = "87654321",
            balanceUnits = 266.6,
            icon = ""
        )
    ),
    fixedDeposits = listOf(
        FixedDepositPortfolioDomain(
            id = "fd1",
            amount = "100000",
            roiAtBooking = "7.5",
            tenureAtBooking = 12,
            fdIssuedAt = "2023-10-01",
            status = "ACTIVE",
            maturityAmount = "107500",
            userId = "user1",
            userFullName = "John Doe",
            userEmail = "john@example.com",
            issuerLogoUrl = "",
            issuerDisplayName = "HDFC Bank",
            maturityDate = "06 July,2024"
        )
    )
)

