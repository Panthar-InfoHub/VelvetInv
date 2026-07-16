package org.sharad.velvetinvestment.presentation.bundle.compose


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.appGreen
import org.sharad.emify.core.ui.theme.appRed
import org.sharad.emify.core.ui.theme.lightGray
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.ReturnYearsRateDomain
import org.sharad.velvetinvestment.presentation.mutualfund.compose.MutualFundIcon
import org.sharad.velvetinvestment.presentation.mutualfund.viewmodel.MutualFundSearchResultViewModel
import org.sharad.velvetinvestment.shared.compose.AppSearchBar
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.ErrorScreen
import org.sharad.velvetinvestment.shared.compose.LoaderScreen
import org.sharad.velvetinvestment.shared.compose.PaginationEffect
import org.sharad.velvetinvestment.shared.compose.PaginationFooter
import org.sharad.velvetinvestment.shared.compose.ShadowCard
import org.sharad.velvetinvestment.shared.theme.LocalVelvetShapes
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import org.sharad.velvetinvestment.shared.theme.buttonTextStyle
import org.sharad.velvetinvestment.shared.theme.titlesStyle
import org.sharad.velvetinvestment.utils.LoadingState
import org.sharad.velvetinvestment.utils.clearFocusOnTap
import org.sharad.velvetinvestment.utils.fundfiltersystem.InvestmentFilter
import org.sharad.velvetinvestment.utils.fundfiltersystem.createInitialInvestmentFilter
import org.sharad.velvetinvestment.utils.toTitleCase
import org.sharad.velvetinvestment.utils.trimTo

@Composable
fun ExploreCategoryFundScreenRoot(
    categorySearchName: String,
    onBackClick: () -> Unit,
    onFundSelected: (String) -> Unit,
    categorTitle: String
) {
    val viewModel: MutualFundSearchResultViewModel = koinViewModel {
        parametersOf("", categorySearchName)
    }
    val uiState by viewModel.loadingState.collectAsStateWithLifecycle()
    val sortedFunds by viewModel.sortedFunds.collectAsStateWithLifecycle()
    val showFilterScreen by viewModel.showFilterScreen.collectAsStateWithLifecycle()
    val filterState by viewModel.filterState.collectAsStateWithLifecycle()
    val searchText by viewModel.searchText.collectAsStateWithLifecycle()
    val isLoadingNext by viewModel.isLoadingNext.collectAsStateWithLifecycle()
    val hasNextPage by viewModel.hasNextPage.collectAsStateWithLifecycle()

    ExploreCategoryFundScreen(
        categoryName = categorySearchName,
        categorTitle= categorTitle,
        uiState = uiState,
        sortedFunds = sortedFunds,
        showFilterScreen = showFilterScreen,
        filterState = filterState,
        searchText = searchText,
        isLoadingNext = isLoadingNext,
        hasNextPage = hasNextPage,
        onBackClick = onBackClick,
        onFundSelected = onFundSelected,
        onSearchTextChange = viewModel::onSearchTextChange,
        onSearchClick = viewModel::loadFunds,
        loadNext = viewModel::loadNext,
        onRetryClick = viewModel::loadFunds,
        toggleFilterScreen = viewModel::toggleFilterScreen,
        applyFilter = viewModel::applyFilter
    )
}

@Composable
fun ExploreCategoryFundScreen(
    categoryName: String,
    uiState: LoadingState,
    sortedFunds: List<MutualFundDomain>,
    showFilterScreen: Boolean,
    filterState: InvestmentFilter,
    searchText: String,
    isLoadingNext: Boolean,
    hasNextPage: Boolean,
    onBackClick: () -> Unit,
    onFundSelected: (String) -> Unit,
    onSearchTextChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    loadNext: () -> Unit,
    onRetryClick: () -> Unit,
    toggleFilterScreen: () -> Unit,
    applyFilter: (InvestmentFilter) -> Unit,
    categorTitle: String
) {
    Box(
        modifier = Modifier.fillMaxSize()
            .background(Color.White)
            .clearFocusOnTap()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            BackHeader("", showBack = true, onBackClick = onBackClick)

            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "EXPLORE FUNDS",
                    style = titlesStyle.copy(fontSize = 12.sp, fontWeight = FontWeight.Bold),
                    color = appGreen
                )
                Text(
                    text = "Explore $categorTitle Funds",
                    style = MaterialTheme.typography.displayMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Spacer(Modifier.height(20.dp))

            AppSearchBar(
                value = searchText,
                onTextChange = onSearchTextChange,
                onSearchClick = onSearchClick,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(16.dp))

            Text(
                text = "Showing only $categoryName funds • same sub-category",
                style = titlesStyle,
                color = titleColor,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(Modifier.height(16.dp))

            Box(
                modifier = Modifier.weight(1f)
                    .fillMaxSize()
                    .imePadding()
            ) {
                when (uiState) {
                    is LoadingState.Error -> {
                        ErrorScreen(uiState.error, onRetryClick = onRetryClick)
                    }

                    LoadingState.Loading -> {
                        LoaderScreen()
                    }

                    LoadingState.Success -> {
                        ExploreFundList(
                            result = sortedFunds,
                            onFundSelected = onFundSelected,
                            isLoadingNext = isLoadingNext,
                            hasNextPage = hasNextPage,
                            loadNext = loadNext,
                            categoryName = categoryName
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ExploreFundList(
    result: List<MutualFundDomain>,
    onFundSelected: (String) -> Unit,
    isLoadingNext: Boolean,
    hasNextPage: Boolean,
    loadNext: () -> Unit,
    categoryName: String
) {
    val lazyListState = rememberLazyListState()

    PaginationEffect(lazyListState = lazyListState, onLoadMore = loadNext)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = lazyListState,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Text(
                text = "${result.size} ${categoryName.uppercase()} FUNDS",
                style = titlesStyle.copy(fontSize = 12.sp, fontWeight = FontWeight.Bold),
                color = titleColor.copy(alpha = 0.4f)
            )
        }

        itemsIndexed(result, key = { _, item -> item.id }) { _, fund ->
            ExploreMutualFundCard(
                fund = fund,
                onSelect = {
                    onFundSelected(fund.id)
                }
            )
        }
        item {
            PaginationFooter(hasNextPage = hasNextPage)
        }
    }
}

@Composable
fun ExploreMutualFundCard(
    fund: MutualFundDomain,
    onSelect: () -> Unit
) {
    ShadowCard(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                SubcomposeAsyncImage(
                    modifier = Modifier.size(38.dp)
                        .shadow(
                            elevation = 2.dp,
                            shape = LocalVelvetShapes.current.roundedDp12
                        )
                        .clip(LocalVelvetShapes.current.roundedDp12)
                        .background(Color.White),
                    model = fund.icon,
                    contentDescription = null,
                    loading = { MutualFundIcon(fund.name, size = 38.dp) },
                    error = { MutualFundIcon(fund.name, size = 38.dp) },
                    success = { SubcomposeAsyncImageContent() }
                )
                Column {
                    Text(
                        text = fund.name.toTitleCase(),
                        style = buttonTextStyle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${fund.shortName1} • ${fund.category}",
                        style = titlesStyle,
                        color = titleColor
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            val riskColor = when (fund.riskLevel?:0) {
                1 -> Color(0xFF2ECC71)
                2 -> Color(0xFF1B8F2A)
                3 -> Color(0xFF8BC34A)
                4 -> Color(0xFFF9A825)
                5 -> Color(0xFFF57C00)
                6 -> Color(0xFFD32F2F)
                else -> Color.Gray
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FundTagExplore(fund.riskText ?: "N/A", riskColor)
                FundTagExplore(fund.category, Color.Gray)
            }

            Spacer(Modifier.height(20.dp))

            // Metrics Grid
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    MetricItemExplore("1Y*", "${fund.returnYearsRate.year1?.trimTo(1) ?: "N/A"}%", fund.returnYearsRate.year1?.let { if (it>0.0) appGreen else appRed } ?: appRed, modifier=Modifier.weight(1f))
                    MetricItemExplore("3Y*", "${fund.returnYearsRate.year3?.trimTo(1) ?: "N/A"}%", fund.returnYearsRate.year3?.let { if (it>0.0) appGreen else appRed } ?: appRed,modifier=Modifier.weight(1f))
                    MetricItemExplore("5Y*", "${fund.returnYearsRate.year5?.trimTo(1) ?: "N/A"}%", fund.returnYearsRate.year5?.let { if (it>0.0) appGreen else appRed } ?: appRed,modifier=Modifier.weight(1f))
                }
            }

            Spacer(Modifier.height(24.dp))

            OutlinedButton(
                onClick = onSelect,
                modifier = Modifier.fillMaxWidth().height(44.dp),
                shape = RoundedCornerShape(24.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Primary)
            ) {
                Text(text = "Select", color = Primary, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun FundTagExplore(text: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(4.dp),
        color = color.copy(alpha = 0.1f)
    ) {
        Text(
            text = text.uppercase(),
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            style = titlesStyle.copy(fontSize = 10.sp, fontWeight = FontWeight.Bold),
            color = color
        )
    }
}

@Composable
fun MetricItemExplore(label: String, value: String, valueColor: Color, modifier: Modifier= Modifier) {
    Column(
        modifier=modifier.fillMaxWidth()
            .border(width = 0.5.dp,
                shape = LocalVelvetShapes.current.roundedDp12,
                color = lightGray
            )
            .padding(
                vertical = 16.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(text = label, style = titlesStyle.copy(fontSize = 10.sp), color = titleColor.copy(alpha = 0.4f))
        Text(text = value, style = buttonTextStyle, color = valueColor)
    }
}

@Preview(showBackground = true)
@Composable
fun ExploreCategoryFundScreenPreview() {
    VelvetTheme {
        ExploreCategoryFundScreen(
            categoryName = "Index",
            uiState = LoadingState.Success,
            sortedFunds = listOf(
                MutualFundDomain(
                    id = "1",
                    name = "UTI Nifty 50 Index Fund",
                    icon = "",
                    category = "Equity: Index",
                    remark = null,
                    riskText = "Very High",
                    type = "Equity",
                    returnYearsRate = ReturnYearsRateDomain(
                        month3 = 5.0,
                        month6 = 10.0,
                        year1 = 15.0,
                        year3 = 12.0,
                        year5 = 14.0
                    ),
                    latestNav = "100.0",
                    shortName1 = "UTI",
                    shortName2 = "Nifty 50",
                ),
                MutualFundDomain(
                    id = "2",
                    name = "HDFC Index S&P BSE Sensex Fund",
                    icon = "",
                    category = "Equity: Index",
                    remark = null,
                    riskText = "Very High",
                    riskLevel = 6,
                    type = "Equity",
                    returnYearsRate = ReturnYearsRateDomain(
                        month3 = 4.0,
                        month6 = 9.0,
                        year1 = 14.0,
                        year3 = 11.0,
                        year5 = 13.0
                    ),
                    latestNav = "200.0",
                    shortName1 = "HDFC",
                    shortName2 = "Sensex",
                )
            ),
            showFilterScreen = false,
            filterState = createInitialInvestmentFilter(),
            searchText = "",
            isLoadingNext = false,
            hasNextPage = true,
            onBackClick = {},
            onFundSelected = {},
            onSearchTextChange = {},
            onSearchClick = {},
            loadNext = {},
            onRetryClick = {},
            toggleFilterScreen = {},
            applyFilter = {},
            categorTitle = "Index Fund"
        )
    }
}
