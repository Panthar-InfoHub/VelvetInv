package org.sharad.velvetinvestment.presentation.mutualfund.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.sharad.emify.core.ui.theme.appGreen
import org.sharad.emify.core.ui.theme.shadowColor
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundDomain
import org.sharad.velvetinvestment.presentation.mutualfund.viewmodel.MutualFundSearchResultViewModel
import org.sharad.velvetinvestment.presentation.mutualfund.viewmodel.SelectedReturnRatePeriod
import org.sharad.velvetinvestment.presentation.mutualfund.viewmodel.defaultFilters
import org.sharad.velvetinvestment.shared.compose.AppSearchBar
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.ErrorScreen
import org.sharad.velvetinvestment.shared.compose.FilterChip
import org.sharad.velvetinvestment.shared.compose.LoaderScreen
import org.sharad.velvetinvestment.shared.compose.PaginationEffect
import org.sharad.velvetinvestment.shared.compose.PaginationFooter
import org.sharad.velvetinvestment.shared.genericDropShadow
import org.sharad.velvetinvestment.shared.theme.LocalVelvetShapes
import org.sharad.velvetinvestment.shared.theme.subHeading
import org.sharad.velvetinvestment.shared.theme.titlesStyle
import org.sharad.velvetinvestment.utils.LabelFilter
import org.sharad.velvetinvestment.utils.LoadingState
import org.sharad.velvetinvestment.utils.MutualFundLabel
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.icon_filter

@Composable
fun MutualFundSearchScreenRoot(
    onBackClick: () -> Unit,
    onFundClick: (String) -> Unit,
    heading: String,
    pv: PaddingValues,
    searchText: String,
    onSearchClick: (String) -> Unit,
    category: String?,
) {

    val viewModel: MutualFundSearchResultViewModel = koinViewModel{
        parametersOf(searchText,category)
    }
    val uiState by viewModel.loadingState.collectAsStateWithLifecycle()
    val selectedYear by viewModel.selectedYear.collectAsStateWithLifecycle()
    val sortedFunds by viewModel.sortedFunds.collectAsStateWithLifecycle()
    val selectedFilter by viewModel.selectedFilter.collectAsStateWithLifecycle()
    val showFilterScreen by viewModel.showFilterScreen.collectAsStateWithLifecycle()
    val filterState by viewModel.filterState.collectAsStateWithLifecycle()
    val searchText by viewModel.searchText.collectAsStateWithLifecycle()
    val isLoadingNext by viewModel.isLoadingNext.collectAsStateWithLifecycle()
    val hasNextPage by viewModel.hasNextPage.collectAsStateWithLifecycle()


    Box(modifier = Modifier.fillMaxSize())
    {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            BackHeader(heading, showBack = true, onBackClick = onBackClick)

            Box(
                modifier = Modifier.weight(1f)
                    .fillMaxSize()
            ) {
                when (uiState) {
                    is LoadingState.Error -> {
                        ErrorScreen((uiState as LoadingState.Error).error, onRetryClick = { viewModel.loadFunds() })
                    }

                    LoadingState.Loading -> {
                        LoaderScreen()
                    }

                    LoadingState.Success -> {
                        MutualFundSearchScreen(
                            result = sortedFunds,
                            onFundClick = onFundClick,
                            pv = pv,
                            isLoadingNext = isLoadingNext,
                            hasNextPage = hasNextPage,
                            loadNext = viewModel::loadNext,
                            toggleRateYear =viewModel::cycleReturnRatePeriod,
                            selectedYear = selectedYear,
                            selectedFilter = selectedFilter,
                            onFilterSelected = viewModel::onFilterSelected,
                            toggleFilterScreen = viewModel::toggleFilterScreen,
                            searchText = searchText,
                            onTextChange = viewModel::onSearchTextChange,
                            onSearchClick =onSearchClick
                        )
                    }
                }
            }

        }
        AnimatedVisibility(
            showFilterScreen,
            enter = slideInHorizontally(
                initialOffsetX = { -it }, // From Left
                animationSpec = tween(
                    durationMillis = 400,
                    easing = FastOutSlowInEasing
                )
            ),
            exit = slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(
                    durationMillis = 400,
                    easing = FastOutSlowInEasing
                )
            )
        ) {
            InvestmentFilterScreen(
                appliedFilter = filterState,
                onClose = {
                    viewModel.toggleFilterScreen()
                },
                onCancelClick = {
                    viewModel.toggleFilterScreen()
                },
                onApplyClick = {
                    viewModel.applyFilter(it)
                    viewModel.toggleFilterScreen()
                },
                pv = pv
            )
        }

    }

}

@Composable
fun MutualFundSearchScreen(
    result: List<MutualFundDomain>,
    onFundClick: (String) -> Unit,
    pv: PaddingValues,
    selectedYear: SelectedReturnRatePeriod,
    isLoadingNext: Boolean,
    hasNextPage: Boolean,
    loadNext: () -> Unit,
    selectedFilter: LabelFilter?,
    onFilterSelected: (LabelFilter) -> Unit,
    toggleFilterScreen: () -> Unit,
    searchText: String,
    onTextChange: (String) -> Unit,
    onSearchClick: (String) -> Unit,
    toggleRateYear: () -> Unit
) {

    val lazyListState = rememberLazyListState()

    PaginationEffect(lazyListState = lazyListState, onLoadMore = loadNext)

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(top=4.dp),
        state = lazyListState
    ) {
        item {
            AppSearchBar(
                value = searchText,
                onTextChange = { onTextChange(it) },
                onSearchClick = { onSearchClick(searchText) },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            )
        }
        item{Spacer(Modifier.height(20.dp))}
        item {
            FundFilterRowMF(
                filters = defaultFilters,
                selectedFilter = selectedFilter,
                onFilterSelected = onFilterSelected,
                toggleFilterScreen=toggleFilterScreen
            )
        }

        item{Spacer(Modifier.height(20.dp))}

        item{
            YearRow(
                selectedYear = selectedYear,
//                incrementYear = incrementYear,
//                decrementYear = decrementYear,
                toggleRateYear =toggleRateYear,
                totalFunds = result.size
            )
        }

        item{Spacer(Modifier.height(10.dp))}

        itemsIndexed(result, key = { _, item -> item.id }){idx, fund ->
            MutualFundListCard(onClick = { onFundClick(fund.id) }, fund =fund,selectedYear=selectedYear,
                modifier = Modifier.fillMaxWidth().padding(vertical = 14.dp, horizontal = 16.dp))
            if (idx!=result.size-1){
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .height(1.dp)
                        .padding(horizontal = 16.dp)
                        .clip(CircleShape)
                        .background(shadowColor)
                )
            }
        }
        item {
            PaginationFooter(hasNextPage = hasNextPage)
        }

    }

}

@Composable
fun MutualFundListCard(
    onClick: () -> Unit,
    fund: MutualFundDomain,
    modifier: Modifier = Modifier,
    selectedYear: SelectedReturnRatePeriod
) {
    Row(
        modifier=modifier.fillMaxWidth().clickable(
            indication = null,
            interactionSource = remember{ MutableInteractionSource() }
        ) { onClick() },
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SubcomposeAsyncImage(
            modifier = Modifier.size(38.dp)
                .genericDropShadow()
                .clip(LocalVelvetShapes.current.roundedDp12)
                .background(Color.White),
            model = fund.icon,
            contentDescription = null,
            loading = {
                MutualFundIcon(
                    schemeName = fund.name,
                    size = 38.dp
                )
            },
            error = {
                MutualFundIcon(
                    schemeName = fund.name,
                    size = 38.dp
                )
            },
            success = {
                SubcomposeAsyncImageContent()
            }
        )
        Column(
            modifier=Modifier.weight(1f)
        ) {
            Row{
                Text(
                    text = fund.name,
                    style = subHeading,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }
            Row{
                Text(
                    text = fund.category +
                            (fund.remark?.let { " • $it" }.orEmpty()) +
                            (fund.riskText?.let { " • $it" }.orEmpty()),

                    style = titlesStyle,
                    color = titleColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

            }
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(start = 4.dp)
        ) {

            val rate= when(selectedYear){
                SelectedReturnRatePeriod.SIX_MONTH -> fund.returnYearsRate.month6
                SelectedReturnRatePeriod.THREE_MONTH -> fund.returnYearsRate.month3
                SelectedReturnRatePeriod.ONE_YEAR -> fund.returnYearsRate.year1
                SelectedReturnRatePeriod.THREE_YEAR -> fund.returnYearsRate.year3
            }

            val text= when(selectedYear){
                SelectedReturnRatePeriod.SIX_MONTH -> "6M"
                SelectedReturnRatePeriod.THREE_MONTH -> "3M"
                SelectedReturnRatePeriod.ONE_YEAR -> "1Y"
                SelectedReturnRatePeriod.THREE_YEAR -> "3Y"
            }

            if (rate !=null){
                Text(
                    text= "$rate %",
                    style = subHeading,
                    color = if (rate>0) appGreen else Color.Red
                )
                Text(
                    text= text,
                    style = titlesStyle,
                    color = titleColor,
                )
            }
            else{
                Text(
                    text= "N/A",
                    style = subHeading,
                    color = Color.Black
                )
            }
        }
    }
}

@Composable
fun YearRow(
    selectedYear: SelectedReturnRatePeriod,
//    incrementYear: () -> Unit,
//    decrementYear: () -> Unit,
    totalFunds: Int,
    toggleRateYear: () -> Unit
) {

    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
        ) {
        Text(
            text = "$totalFunds Funds",
            fontWeight = FontWeight.SemiBold,
            style = titlesStyle,
            color = titleColor
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row {
                Text(
                    text = "<",
                    style = titlesStyle.copy(lineHeight = 0.sp),
                    fontSize = 16.sp,
                    color = titleColor,
//                    modifier = Modifier.clickable { decrementYear() }
                )
                Text(
                    text = " ",
                    style = titlesStyle,
                    color = titleColor
                )
                Text(
                    text = ">",
                    style = titlesStyle.copy(lineHeight = 0.sp),
                    color = titleColor,
                    fontSize = 16.sp,
//                    modifier = Modifier.clickable { incrementYear() }
                )
            }
            Text(
                text = "${selectedYear.displayText} Returns",
                fontWeight = FontWeight.SemiBold,
                style = titlesStyle,
                color = titleColor,
                modifier = Modifier.clickable(onClick = {toggleRateYear()})
            )
        }
    }

}

@Composable
private fun FundFilterRowMF(
    filters: List<LabelFilter>,
    selectedFilter: LabelFilter?,
    onFilterSelected: (LabelFilter) -> Unit,
    toggleFilterScreen: () -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        item {
            if (selectedFilter is MutualFundLabel.CustomLabel){
                FilterChip(
                    title=selectedFilter.title,
                    isSelected = true,
                    onClick = {
                        onFilterSelected(selectedFilter)
                    }
                )
            }else{
                Box(
                    modifier=Modifier.size(32.dp)
                        .clip(RoundedCornerShape(50))
                        .background(Color(0xFFDEE2F6).copy(0.7f))
                        .clickable { toggleFilterScreen()  },
                    contentAlignment = Alignment.Center
                ){
                    Icon(
                        painter = painterResource(Res.drawable.icon_filter),
                        contentDescription = null,
                        tint= Color.Black,
                        modifier=Modifier.size(20.dp)
                    )
                }
            }
        }
        items(filters) { filter ->
            FilterChip(
                title=filter.title,
                isSelected = selectedFilter == filter,
                onClick = { onFilterSelected(filter) }
            )
        }
    }
}
