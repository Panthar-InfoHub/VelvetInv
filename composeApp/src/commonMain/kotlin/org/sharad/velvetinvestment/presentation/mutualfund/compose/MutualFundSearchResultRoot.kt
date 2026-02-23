package org.sharad.velvetinvestment.presentation.mutualfund.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.sharad.emify.core.ui.theme.appGreen
import org.sharad.emify.core.ui.theme.shadowColor
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.presentation.mutualfund.MutualFundUI
import org.sharad.velvetinvestment.presentation.mutualfund.viewmodel.MutualFundSearchResultViewModel
import org.sharad.velvetinvestment.presentation.mutualfund.viewmodel.defaultFilters
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.ErrorScreen
import org.sharad.velvetinvestment.shared.compose.FilterChip
import org.sharad.velvetinvestment.shared.compose.LoaderScreen
import org.sharad.velvetinvestment.utils.FundFilter
import org.sharad.velvetinvestment.utils.UIState
import org.sharad.velvetinvestment.utils.theme.subHeading
import org.sharad.velvetinvestment.utils.theme.titlesStyle
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.icon_filter
import velvet.composeapp.generated.resources.mf_placeholder

@Composable
fun MutualFundSearchScreenRoot(
    onBackClick: () -> Unit,
    onFundClick: (String) -> Unit,
    heading: String,
    pv: PaddingValues,
    searchId:String
) {

    val viewModel: MutualFundSearchResultViewModel = koinViewModel{parametersOf(searchId)}
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val selectedYear by viewModel.selectedYear.collectAsStateWithLifecycle()
    val sortedFunds by viewModel.sortedFunds.collectAsStateWithLifecycle()
    val selectedFilter by viewModel.selectedFilter.collectAsStateWithLifecycle()
    val showFilterScreen by viewModel.showFilterScreen.collectAsStateWithLifecycle()
    val filterState by viewModel.filterState.collectAsStateWithLifecycle()




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
                    is UIState.Error -> {
                        ErrorScreen((uiState as UIState.Error).error, onRetryClick = {})
                    }

                    UIState.Loading -> {
                        LoaderScreen()
                    }

                    UIState.Success -> {
                        MutualFundSearchScreen(
                            result = sortedFunds,
                            onFundClick = onFundClick,
                            pv = pv,
                            incrementYear = viewModel::incrementYear,
                            decrementYear = viewModel::decrementYear,
                            selectedYear = selectedYear,
                            selectedFilter = selectedFilter,
                            onFilterSelected = viewModel::onFilterSelected,
                            toggleFilterScreen = viewModel::toggleFilterScreen
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
                    viewModel.clearFilter()
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
    result: List<MutualFundUI>,
    onFundClick: (String) -> Unit,
    pv: PaddingValues,
    selectedYear: Int,
    incrementYear: () -> Unit,
    decrementYear: () -> Unit,
    selectedFilter: FundFilter?,
    onFilterSelected: (FundFilter) -> Unit,
    toggleFilterScreen: () -> Unit
) {

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(top=4.dp),
    ) {

        item {
            FundFilterRow(
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
                incrementYear = incrementYear,
                decrementYear = decrementYear,
                totalFunds = result.size
            )
        }

        item{Spacer(Modifier.height(10.dp))}

        itemsIndexed(result, key = { _, item -> item.id }){idx, fund ->
            MutualFundListCard(fund=fund, onClick = { onFundClick(fund.id) })
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


        item { Spacer(Modifier.height(pv.calculateBottomPadding()+20.dp)) }

    }

}

@Composable
fun MutualFundListCard(onClick: () -> Unit, fund: MutualFundUI, modifier: Modifier=Modifier) {
    Row(
        modifier=modifier.fillMaxWidth().clickable { onClick() }
            .padding(vertical = 14.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model=fund.icon,
            contentDescription = null,
            placeholder = painterResource(Res.drawable.mf_placeholder),
            error = painterResource(Res.drawable.mf_placeholder),
            fallback = painterResource(Res.drawable.mf_placeholder),
            modifier = Modifier.size(38.dp)
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

                Text(
                    text= fund.percentage.toString()+"%",
                    style = subHeading,
                    color = if (fund.percentage>0) appGreen else Color.Red
                )
            }
            Row{
                Text(
                    text = fund.category +
                            (fund.remark?.let { " • $it" }.orEmpty()) +
                            (fund.rating?.let { " • $it★" }.orEmpty()),

                    style = titlesStyle,
                    color = titleColor,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = fund.returnYear.toString()+"Y",
                    style = titlesStyle,
                    color = titleColor
                )
            }
        }
    }
}

@Composable
fun YearRow(
    selectedYear: Int,
    incrementYear: () -> Unit,
    decrementYear: () -> Unit,
    totalFunds: Int
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
                    modifier = Modifier.clickable { decrementYear() }
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
                    modifier = Modifier.clickable { incrementYear() }
                )
            }
            Text(
                text = "${selectedYear}Y Returns",
                fontWeight = FontWeight.SemiBold,
                style = titlesStyle,
                color = titleColor
            )
        }
    }

}

@Composable
fun FundFilterRow(
    filters: List<FundFilter>,
    selectedFilter: FundFilter?,
    onFilterSelected: (FundFilter) -> Unit,
    toggleFilterScreen: () -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        item {
            if (selectedFilter is FundFilter.Custom){
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
