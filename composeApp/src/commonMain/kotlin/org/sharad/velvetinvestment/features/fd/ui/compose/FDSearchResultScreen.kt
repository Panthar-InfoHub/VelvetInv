package org.sharad.velvetinvestment.features.fd.ui.compose

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.shadowColor
import org.sharad.velvetinvestment.features.fd.domain.model.FDLabel
import org.sharad.velvetinvestment.features.fd.domain.model.FilterGroup
import org.sharad.velvetinvestment.features.fd.domain.model.FilterOption
import org.sharad.velvetinvestment.features.fd.domain.model.FixedDepositDomain
import org.sharad.velvetinvestment.features.fd.domain.model.InvestmentFilter
import org.sharad.velvetinvestment.features.fd.domain.model.LabelFilter
import org.sharad.velvetinvestment.features.fd.domain.model.SelectionType
import org.sharad.velvetinvestment.features.fd.ui.viewmodels.ExploreFdEvent
import org.sharad.velvetinvestment.features.fd.ui.viewmodels.ExploreFdUiState
import org.sharad.velvetinvestment.features.mutualfund.ui.compose.InvestmentFilterScreen
import org.sharad.velvetinvestment.shared.UiStateContainer
import org.sharad.velvetinvestment.shared.compose.AppSearchBar
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.FilterChip
import org.sharad.velvetinvestment.shared.compose.PaginationEffect
import org.sharad.velvetinvestment.shared.compose.PaginationFooter
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.features.core.utils.fundfiltersystem.FilterGroup as ScreenFilterGroup
import org.sharad.velvetinvestment.features.core.utils.fundfiltersystem.FilterOption as ScreenFilterOption
import org.sharad.velvetinvestment.features.core.utils.fundfiltersystem.InvestmentFilter as ScreenFilter
import org.sharad.velvetinvestment.features.core.utils.fundfiltersystem.SelectionType as ScreenSelectionType
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.icon_filter

/**
 * Velvet's FD list on JN's [ExploreFdUiState]. Search runs in place (JN) instead of pushing a new
 * search screen; the search bar sits outside the loader so it stays put while results reload.
 */
@Composable
fun FDSearchScreenRoot(
    state: ExploreFdUiState,
    onEvent: (ExploreFdEvent) -> Unit,
    heading: String = "Fixed Deposit",
) {
    Box(modifier = Modifier.fillMaxSize())
    {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            BackHeader(heading, showBack = true, onBack = { onEvent(ExploreFdEvent.OnBackClicked) })

            AppSearchBar(
                value = state.searchQuery,
                onTextChange = { onEvent(ExploreFdEvent.OnSearchQueryChanged(it)) },
                onSearchClick = { onEvent(ExploreFdEvent.OnSearchClick) },
                modifier = Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp, top = 4.dp)
            )

            Box(
                modifier = Modifier.weight(1f)
                    .fillMaxSize()
            ) {
                UiStateContainer(
                    uiState = state.toUiState(),
                    onRetry = { onEvent(ExploreFdEvent.OnSearchClick) }
                ) {
                    FDSearchScreen(
                        result = state.fundsList,
                        onFDClick = { onEvent(ExploreFdEvent.OnFundItemClicked(it)) },
                        hasNextPage = state.hasNextPage,
                        loadNext = { onEvent(ExploreFdEvent.OnLoadMoreClicked) },
                    )
                }
            }
            BlostemFooter(modifier = Modifier.padding(vertical = 12.dp))
        }
        AnimatedVisibility(
            state.showFilterScreen,
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
                appliedFilter = state.filterState.toScreenFilter(),
                onClose = { onEvent(ExploreFdEvent.OnFilterMenuClicked) },
                onCancelClick = { onEvent(ExploreFdEvent.OnFilterMenuClicked) },
                onApplyClick = {
                    onEvent(ExploreFdEvent.OnApplyFilter(it.toFdFilter()))
                    onEvent(ExploreFdEvent.OnFilterMenuClicked)
                },
            )
        }

    }
}

/** Full-screen loader/error only while there is nothing to show; a failed next page keeps the list. */
private fun ExploreFdUiState.toUiState(): UiState<Unit> = when {
    isLoading -> UiState.Loading
    errorMessage != null && fundsList.isEmpty() -> UiState.Error(errorMessage)
    else -> UiState.Success(Unit)
}

// Velvet's filter screen works on the shared filter model; JN's FD filter is a copy of the same shape.
private fun InvestmentFilter.toScreenFilter() = ScreenFilter(
    groups = groups.map { group ->
        ScreenFilterGroup(
            id = group.id,
            title = group.title,
            selectionType = ScreenSelectionType.valueOf(group.selectionType.name),
            options = group.options.map { ScreenFilterOption(it.id, it.title, it.isSelected) }
        )
    }
)

private fun ScreenFilter.toFdFilter() = InvestmentFilter(
    groups = groups.map { group ->
        FilterGroup(
            id = group.id,
            title = group.title,
            selectionType = SelectionType.valueOf(group.selectionType.name),
            options = group.options.map { FilterOption(it.id, it.title, it.isSelected) }
        )
    }
)

@Composable
fun FDSearchScreen(
    result: List<FixedDepositDomain>,
    onFDClick: (FixedDepositDomain) -> Unit,
    hasNextPage: Boolean,
    loadNext: () -> Unit,
) {
    val lazyListState= rememberLazyListState()

    PaginationEffect(lazyListState = lazyListState, onLoadMore = loadNext)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = lazyListState,
        contentPadding = PaddingValues(bottom = 28.dp)
    ) {

        item{Spacer(Modifier.height(36.dp))}

        itemsIndexed(result, key = { _, item -> item.id }){idx, fd ->
            FDListCard(fd =fd, onClick = { onFDClick(fd) }, modifier = Modifier.padding(horizontal = 16.dp))
            if (idx!=result.size-1){
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .padding(start = 16.dp,end=16.dp, top=8.dp, bottom = 16.dp)
                        .height(1.dp)
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
fun FundFilterRowMF(
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
            if (selectedFilter is FDLabel.CustomLabel){
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
