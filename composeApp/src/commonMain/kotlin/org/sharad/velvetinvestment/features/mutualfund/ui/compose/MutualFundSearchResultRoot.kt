package org.sharad.velvetinvestment.features.mutualfund.ui.compose

import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.appGreen
import org.sharad.emify.core.ui.theme.lightGray
import org.sharad.emify.core.ui.theme.shadowColor
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.shared.theme.LocalVelvetShapes
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import org.sharad.velvetinvestment.shared.theme.subHeading
import org.sharad.velvetinvestment.shared.theme.tinyLabel
import org.sharad.velvetinvestment.shared.theme.titlesStyle
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.icon_filter
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.sharad.velvetinvestment.features.mutualfund.ui.viewmodel.MutualFundSearchResultViewModel
import org.sharad.velvetinvestment.features.mutualfund.domain.models.SelectedReturnRatePeriod
import org.sharad.velvetinvestment.features.mutualfund.domain.models.forPeriod
import org.sharad.velvetinvestment.features.mutualfund.ui.viewmodel.defaultFilters
import org.sharad.velvetinvestment.shared.compose.PaginationEffect
import org.sharad.velvetinvestment.shared.compose.PaginationFooter
import org.sharad.velvetinvestment.utils.LoadingState
import org.sharad.velvetinvestment.shared.compose.AppSearchBarButton
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.ErrorScreen
import org.sharad.velvetinvestment.shared.compose.FilterChip
import org.sharad.velvetinvestment.shared.compose.LoaderScreen
import org.sharad.velvetinvestment.features.core.utils.LabelFilter
import org.sharad.velvetinvestment.features.mutualfund.domain.models.MutualFundDomain
import androidx.compose.ui.tooling.preview.Preview
import org.sharad.velvetinvestment.utils.clearFocusOnTap
import org.sharad.velvetinvestment.features.core.utils.fundfiltersystem.InvestmentFilter
import org.sharad.velvetinvestment.features.core.utils.MutualFundLabel
import org.sharad.velvetinvestment.features.core.utils.fundfiltersystem.createInitialInvestmentFilter
import org.sharad.velvetinvestment.features.mutualfund.domain.models.ReturnYearsRateDomain
import org.sharad.velvetinvestment.features.mutualfund.utils.toTitleCase
import org.sharad.velvetinvestment.features.search.ui.compose.SearchOverlay
import org.sharad.velvetinvestment.features.search.ui.viewmodels.SearchOverlayEffect
import org.sharad.velvetinvestment.features.search.ui.viewmodels.SearchOverlayViewModel
import org.sharad.velvetinvestment.shared.compose.FundIcon
import org.sharad.velvetinvestment.features.bundles.domain.models.BundleSummaryDomain
import org.sharad.velvetinvestment.features.bundles.presentation.compose.BundleCard
import org.sharad.velvetinvestment.features.bundles.presentation.compose.previewBundleSummary
import org.sharad.velvetinvestment.shared.compose.BarHeader

@Composable
fun MutualFundSearchScreenRoot(
    onBackClick: () -> Unit,
    /** The whole fund: its details screen is opened with the id and the ISIN purchases need. */
    onFundClick: (MutualFundDomain) -> Unit,
    heading: String,
    searchText: String,
    onSearchClick: (String) -> Unit,
    /** `tag`, `category` and `amount_type` of `GET /mf/funds`, when the screen was opened pre-filtered. */
    tag: String?,
    category: String? = null,
    amountType: String? = null,
    onBookFdClick: () -> Unit,
    onCreateGoalClick: () -> Unit,
    /** "View all" on the recommended bundles header. */
    onBundleClick: () -> Unit,
    onBundledFundClick: (bundleId: String) -> Unit,
) {

    val viewModel: MutualFundSearchResultViewModel = koinViewModel {
        parametersOf(searchText, tag, category, amountType, null)
    }
    val uiState by viewModel.loadingState.collectAsStateWithLifecycle()
    val selectedYear by viewModel.selectedYear.collectAsStateWithLifecycle()
    val sortedFunds by viewModel.sortedFunds.collectAsStateWithLifecycle()
    val selectedChipIds by viewModel.selectedChipIds.collectAsStateWithLifecycle()
    val customFilter by viewModel.customFilter.collectAsStateWithLifecycle()
    val showFilterScreen by viewModel.showFilterScreen.collectAsStateWithLifecycle()
    val filterState by viewModel.filterState.collectAsStateWithLifecycle()
    val isLoadingNext by viewModel.isLoadingNext.collectAsStateWithLifecycle()
    val hasNextPage by viewModel.hasNextPage.collectAsStateWithLifecycle()
    val totalFunds by viewModel.totalFunds.collectAsStateWithLifecycle()
    val bundles by viewModel.bundles.collectAsStateWithLifecycle()
    val isBundlesLoading by viewModel.isBundlesLoading.collectAsStateWithLifecycle()

    // Presentation-only, like the one on the category screen: the results underneath stay loaded
    // while the overlay is open, and surviving configuration changes is all it needs to persist.
    var showSearchOverlay by rememberSaveable { mutableStateOf(false) }

    val searchViewModel: SearchOverlayViewModel = koinViewModel()
    val searchState by searchViewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(searchViewModel.effect) {
        searchViewModel.effect.collect { effect ->
            when (effect) {
                is SearchOverlayEffect.Search -> {
                    showSearchOverlay = false
                    searchViewModel.resetQuery()
                    // Re-enters this same screen with the new term, so the results are a fresh
                    // destination the user can back out of to the previous ones.
                    onSearchClick(effect.query)
                }
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        MutualFundSearchScreenContent(
            uiState = uiState,
            selectedYear = selectedYear,
            sortedFunds = sortedFunds,
            selectedChipIds = selectedChipIds,
            customFilter = customFilter,
            showFilterScreen = showFilterScreen,
            filterState = filterState,
            searchText = searchText,
            isLoadingNext = isLoadingNext,
            hasNextPage = hasNextPage,
            totalFunds = totalFunds,
            bundles = bundles,
            isBundlesLoading = isBundlesLoading,
            heading = heading,
            onBackClick = onBackClick,
            onFundClick = onFundClick,
            onBundleClick = onBundleClick,
            onBundledFundClick = onBundledFundClick,
            onSearchBarClick = { showSearchOverlay = true },
            onRetryClick = { viewModel.loadFunds() },
            loadNext = { viewModel.loadNext() },
            toggleRateYear = { viewModel.cycleReturnRatePeriod() },
            onFilterSelected = { viewModel.onFilterSelected(it) },
            toggleFilterScreen = { viewModel.toggleFilterScreen() },
            applyFilter = {
                viewModel.applyFilter(it)
                viewModel.toggleFilterScreen()
            }
        )

        if (showSearchOverlay) {
            SearchOverlay(
                state = searchState,
                handleEvent = searchViewModel::handleEvent,
                onDismiss = {
                    showSearchOverlay = false
                    searchViewModel.resetQuery()
                },
                // JN closes the overlay here (its FD list is this screen); Velvet's FDs have their
                // own list, so Book FD navigates. No Daily ₹10 / Monthly ₹100 shortcuts in Velvet.
                onBookFdClick = {
                    showSearchOverlay = false
                    onBookFdClick()
                },
                onCreateGoalClick = {
                    showSearchOverlay = false
                    onCreateGoalClick()
                },
                onFundClick = { fund ->
                    showSearchOverlay = false
                    searchViewModel.onSuggestionOpened()
                    onFundClick(fund)
                }
            )
        }
    }
}

@Composable
private fun MutualFundSearchScreenContent(
    uiState: LoadingState,
    selectedYear: SelectedReturnRatePeriod,
    sortedFunds: List<MutualFundDomain>,
    selectedChipIds: Set<String>,
    customFilter: LabelFilter?,
    showFilterScreen: Boolean,
    filterState: InvestmentFilter,
    searchText: String,
    isLoadingNext: Boolean,
    hasNextPage: Boolean,
    totalFunds: Int,
    bundles: List<BundleSummaryDomain>,
    isBundlesLoading: Boolean,
    heading: String,
    onBackClick: () -> Unit,
    onFundClick: (MutualFundDomain) -> Unit,
    onBundleClick: () -> Unit,
    onBundledFundClick: (String) -> Unit,
    onSearchBarClick: () -> Unit,
    onRetryClick: () -> Unit,
    loadNext: () -> Unit,
    toggleRateYear: () -> Unit,
    onFilterSelected: (LabelFilter) -> Unit,
    toggleFilterScreen: () -> Unit,
    applyFilter: (InvestmentFilter) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.White
    ) {
        Box(modifier = Modifier.fillMaxSize()
            .clearFocusOnTap())
        {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {

                BackHeader(heading, showBack = true, onBack = onBackClick, modifier = Modifier.padding(horizontal = 16.dp))

                Box(
                    modifier = Modifier.weight(1f)
                        .fillMaxSize()
                ) {
                    when (uiState) {
                        is LoadingState.Error -> {
                            ErrorScreen(
                                uiState.error,
                                onRetryClick = onRetryClick
                            )
                        }

                        LoadingState.Loading -> {
                            LoaderScreen()
                        }

                        LoadingState.Success -> {
                            MutualFundSearchScreen(
                                result = sortedFunds,
                                onFundClick = onFundClick,
                                isLoadingNext = isLoadingNext,
                                hasNextPage = hasNextPage,
                                totalFunds = totalFunds,
                                bundles = bundles,
                                isBundlesLoading = isBundlesLoading,
                                onBundleClick = onBundleClick,
                                onBundledFundClick = onBundledFundClick,
                                loadNext = loadNext,
                                toggleRateYear = toggleRateYear,
                                selectedYear = selectedYear,
                                selectedChipIds = selectedChipIds,
                                customFilter = customFilter,
                                onFilterSelected = onFilterSelected,
                                toggleFilterScreen = toggleFilterScreen,
                                searchText = searchText,
                                onSearchBarClick = onSearchBarClick
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
                        toggleFilterScreen()
                    },
                    onCancelClick = {
                        toggleFilterScreen()
                    },
                    onApplyClick = {
                        applyFilter(it)
                    },
                )
            }

        }
    }
}

@Composable
fun MutualFundSearchScreen(
    result: List<MutualFundDomain>,
    onFundClick: (MutualFundDomain) -> Unit,
    selectedYear: SelectedReturnRatePeriod,
    isLoadingNext: Boolean,
    hasNextPage: Boolean,
    totalFunds: Int,
    bundles: List<BundleSummaryDomain>,
    isBundlesLoading: Boolean,
    onBundleClick: () -> Unit,
    onBundledFundClick: (String) -> Unit,
    loadNext: () -> Unit,
    selectedChipIds: Set<String>,
    customFilter: LabelFilter?,
    onFilterSelected: (LabelFilter) -> Unit,
    toggleFilterScreen: () -> Unit,
    searchText: String,
    onSearchBarClick: () -> Unit,
    toggleRateYear: () -> Unit
) {

    val lazyListState = rememberLazyListState()

    PaginationEffect(lazyListState = lazyListState, onLoadMore = loadNext)

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(top=4.dp),
        state = lazyListState
    ) {
        item {
            AppSearchBarButton(
                onClick = onSearchBarClick,
                // The term that produced these results, so the bar says what is being shown.
                placeholder = searchText.ifBlank { "Search For Funds...." },
                trailingIcon = Res.drawable.icon_filter,
                onTrailingIconClick = toggleFilterScreen,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            )
        }
        item{Spacer(Modifier.height(20.dp))}

        // Hidden once loaded empty or failed; the funds below never wait on it.
        if (isBundlesLoading || bundles.isNotEmpty()) {
            item {
                BarHeader(
                    heading = "Curated Bundles",
                    showArrow = !isBundlesLoading,
                    onArrowClick = onBundleClick,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
            item{Spacer(Modifier.height(16.dp))}
            item {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    userScrollEnabled = !isBundlesLoading
                ) {
                    if (isBundlesLoading) {
                        items(BUNDLE_SKELETON_COUNT) {
                            BundleCardSkeleton(modifier = Modifier.fillParentMaxWidth(0.85f))
                        }
                    } else {
                        items(bundles, key = { it.id }) { bundle ->
                            BundleCard(
                                bundle = bundle,
                                onClick = { onBundledFundClick(bundle.id) },
                                modifier = Modifier.fillParentMaxWidth(0.85f)
                            )
                        }
                    }
                }
            }
            item{Spacer(Modifier.height(20.dp))}
        }

        item {
            FundFilterRowMF(
                filters = defaultFilters,
                selectedChipIds = selectedChipIds,
                customFilter = customFilter,
                onFilterSelected = onFilterSelected
            )
        }

        item{Spacer(Modifier.height(20.dp))}

        item{
            YearRow(
                selectedYear = selectedYear,
                toggleRateYear =toggleRateYear,
                totalFunds = totalFunds
            )
        }

        item{Spacer(Modifier.height(10.dp))}

        itemsIndexed(result, key = { _, item -> item.id }){idx, fund ->
            MutualFundListCard(onClick = { onFundClick(fund) }, fund =fund,selectedYear=selectedYear,
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
        FundIcon(
            iconUrl = fund.icon,
            name = fund.name,
            size = 38.dp
        )
        Column(
            modifier=Modifier.weight(1f)
        ) {
            Row{
                Text(
                    text = fund.name.toTitleCase(),
                    style = subHeading,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
            }
            Row{
                Text(
                    text = "NAV ${fund.latestNav}",

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

            val rate = fund.returnYearsRate.forPeriod(selectedYear)

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

private const val BUNDLE_SKELETON_COUNT = 2

/** A pulsing stand-in shaped like [BundleCard], shown while the bundles load. */
@Composable
private fun BundleCardSkeleton(modifier: Modifier = Modifier) {
    val shapes = LocalVelvetShapes.current
    val pulse by rememberInfiniteTransition(label = "bundleSkeleton").animateFloat(
        initialValue = 1f,
        targetValue = 0.4f,
        animationSpec = infiniteRepeatable(tween(800), RepeatMode.Reverse),
        label = "bundleSkeletonAlpha"
    )

    @Composable
    fun Bone(modifier: Modifier) {
        Box(modifier = modifier.clip(shapes.roundedDp8).background(shadowColor))
    }

    Column(
        modifier = modifier
            .clip(shapes.roundedDp20)
            .border(1.dp, lightGray, shapes.roundedDp20)
            .padding(20.dp)
            .alpha(pulse)
    ) {
        Bone(Modifier.fillMaxWidth(0.6f).height(22.dp))
        Spacer(Modifier.height(8.dp))
        Bone(Modifier.fillMaxWidth(0.8f).height(12.dp))
        Spacer(Modifier.height(16.dp))

        repeat(3) {
            Row(
                modifier = Modifier.padding(vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(Modifier.size(28.dp).clip(CircleShape).background(shadowColor))
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Bone(Modifier.size(width = 100.dp, height = 12.dp))
                    Bone(Modifier.size(width = 70.dp, height = 10.dp))
                }
            }
        }

        Spacer(Modifier.height(16.dp))
        Bone(Modifier.fillMaxWidth().height(1.dp))
        Spacer(Modifier.height(12.dp))
        Bone(Modifier.size(width = 140.dp, height = 36.dp))
    }
}

@Composable
fun YearRow(
    selectedYear: SelectedReturnRatePeriod,
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

/**
 * The filter chips. The tray no longer opens from here — that moved to the search bar — so this
 * row is the shortcuts alone, plus [customFilter] standing in for a tray selection no chip
 * covers.
 *
 * More than one can be lit: the minimum-installment chips and the fund-type chips drive
 * different query parameters, so picking one does not clear the other.
 */
@Composable
private fun FundFilterRowMF(
    filters: List<LabelFilter>,
    selectedChipIds: Set<String>,
    customFilter: LabelFilter?,
    onFilterSelected: (LabelFilter) -> Unit
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        if (customFilter != null) {
            item {
                FilterChip(
                    title = customFilter.title,
                    isSelected = true,
                    onClick = { onFilterSelected(customFilter) }
                )
            }
        }
        items(filters) { filter ->
            FilterChip(
                title=filter.title,
                isSelected = filter.id in selectedChipIds,
                onClick = { onFilterSelected(filter) }
            )
        }
    }
}

@Preview
@Composable
fun MutualFundSearchScreenPreview() {
    VelvetTheme {
        MutualFundSearchScreenContent(
            uiState = LoadingState.Success,
            selectedYear = SelectedReturnRatePeriod.ONE_YEAR,
            sortedFunds = listOf(
                MutualFundDomain(
                    id = "1",
                    name = "Axis Bluechip Fund",
                    icon = "",
                    category = "Equity",
                    remark = "Growth",
                    riskText = "Very High Risk",
                    type = "Open Ended",
                    returnYearsRate = ReturnYearsRateDomain(
                        month3 = 5.0,
                        month6 = 10.0,
                        year1 = 15.0,
                        year3 = 45.0
                    ),
                    latestNav = "50.5"
                ),
                MutualFundDomain(
                    id = "2",
                    name = "SBI Small Cap Fund",
                    icon = "",
                    category = "Equity",
                    remark = "Growth",
                    riskText = "Very High Risk",
                    type = "Open Ended",
                    returnYearsRate = ReturnYearsRateDomain(
                        month3 = 2.0,
                        month6 = 8.0,
                        year1 = 20.0,
                        year3 = 60.0
                    ),
                    latestNav = "120.3"
                )
            ),
            selectedChipIds = setOf(MutualFundLabel.Popular.id),
            customFilter = null,
            showFilterScreen = false,
            filterState = createInitialInvestmentFilter(),
            searchText = "Axis",
            isLoadingNext = false,
            hasNextPage = true,
            totalFunds = 2,
            bundles = listOf(previewBundleSummary, previewBundleSummary.copy(id = "2", name = "Aggressive")),
            isBundlesLoading = false,
            heading = "Search Results",
            onBackClick = {},
            onFundClick = {},
            onBundleClick = {},
            onBundledFundClick = {},
            onSearchBarClick = {},
            onRetryClick = {},
            loadNext = {},
            toggleRateYear = {},
            onFilterSelected = {},
            toggleFilterScreen = {},
            applyFilter = {}
        )
    }
}
