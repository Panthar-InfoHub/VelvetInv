package org.sharad.velvetinvestment.features.bundles.presentation.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.sharad.emify.core.ui.theme.appGreen
import org.sharad.emify.core.ui.theme.appRed
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.features.bundles.domain.models.unavailableNote
import org.sharad.velvetinvestment.features.core.domain.models.PurchaseMode
import org.sharad.velvetinvestment.features.core.utils.fundfiltersystem.InvestmentModeFilter
import org.sharad.velvetinvestment.features.core.utils.fundfiltersystem.MfFilterIds
import org.sharad.velvetinvestment.features.mutualfund.domain.models.MutualFundDomain
import org.sharad.velvetinvestment.features.mutualfund.domain.models.ReturnYearsRateDomain
import org.sharad.velvetinvestment.features.mutualfund.ui.viewmodel.MutualFundSearchResultViewModel
import org.sharad.velvetinvestment.features.mutualfund.utils.toTitleCase
import org.sharad.velvetinvestment.shared.compose.NextButtonFooter
import org.sharad.velvetinvestment.shared.compose.AppSearchBar
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.ErrorScreen
import org.sharad.velvetinvestment.shared.compose.LoaderScreen
import org.sharad.velvetinvestment.shared.compose.PaginationEffect
import org.sharad.velvetinvestment.shared.compose.PaginationFooter
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import org.sharad.velvetinvestment.shared.theme.titlesStyle
import org.sharad.velvetinvestment.utils.LoadingState
import org.sharad.velvetinvestment.utils.clearFocusOnTap

/** The `GET /mf/funds` tags a bundle category can be listed by; others fall back to a name search. */
private val categoryTags = setOf(
    MfFilterIds.TAG_LARGE_CAP,
    MfFilterIds.TAG_MID_CAP,
    MfFilterIds.TAG_SMALL_CAP,
    MfFilterIds.TAG_FLEXI_CAP,
    MfFilterIds.TAG_MULTI_CAP,
    MfFilterIds.TAG_DEBT,
    MfFilterIds.TAG_OTHERS
)

/**
 * The full fund list for one bundle category, to pick a fund the bundle doesn't offer.
 *
 * The list is [MutualFundSearchResultViewModel], whose search text is fixed when it is created, so
 * each submitted query gets its own instance, keyed by the query. A category the fund list has a
 * tag for is filtered by that tag; one it hasn't — gold, large & mid cap — is searched for by name
 * until the user types a query of their own.
 *
 * The list is narrowed to funds that can be bought as the selected SIP — daily or monthly — with
 * no minimum-installment filter; a one-time purchase loads it unfiltered.
 */
@Composable
fun ExploreCategoryFundScreenRoot(
    categoryName: String,
    categoryTitle: String,
    purchaseMode: PurchaseMode,
    /** Funds held by the category's other slots, which this slot can't take. */
    unavailableFundIds: Set<String>,
    /** Funds whose details show they can't be bought as [purchaseMode]; listed but not pickable. */
    unselectableFundIds: Set<String>,
    selectedFundId: String?,
    /** A tapped fund's details are loading. */
    isLoadingFund: Boolean,
    onBackClick: () -> Unit,
    onFundSelected: (MutualFundDomain) -> Unit,
    onSaveClick: () -> Unit
) {
    var searchText by rememberSaveable { mutableStateOf("") }
    var submittedQuery by rememberSaveable { mutableStateOf("") }

    val tag = categoryName.takeIf { it in categoryTags }
    val search = submittedQuery.ifBlank { if (tag == null) categoryTitle else "" }
    val investmentMode = InvestmentModeFilter.from(purchaseMode).id

    val viewModel: MutualFundSearchResultViewModel = koinViewModel(
        key = "explore:$categoryName:${purchaseMode.name}:$submittedQuery",
        parameters = { parametersOf(search, tag, null, null, investmentMode) }
    )
    val loadingState by viewModel.loadingState.collectAsStateWithLifecycle()
    val funds by viewModel.sortedFunds.collectAsStateWithLifecycle()
    val hasNextPage by viewModel.hasNextPage.collectAsStateWithLifecycle()

    ExploreCategoryFundScreen(
        categoryTitle = categoryTitle,
        purchaseMode = purchaseMode,
        loadingState = loadingState,
        funds = funds,
        hasNextPage = hasNextPage,
        searchText = searchText,
        unavailableFundIds = unavailableFundIds,
        unselectableFundIds = unselectableFundIds,
        selectedFundId = selectedFundId,
        isLoadingFund = isLoadingFund,
        onBackClick = onBackClick,
        onFundSelected = onFundSelected,
        onSaveClick = onSaveClick,
        onSearchTextChange = { searchText = it },
        onSearchClick = { submittedQuery = searchText.trim() },
        loadNext = viewModel::loadNext,
        onRetryClick = viewModel::loadFunds
    )
}

@Composable
fun ExploreCategoryFundScreen(
    categoryTitle: String,
    purchaseMode: PurchaseMode,
    loadingState: LoadingState,
    funds: List<MutualFundDomain>,
    hasNextPage: Boolean,
    searchText: String,
    unavailableFundIds: Set<String>,
    unselectableFundIds: Set<String>,
    selectedFundId: String?,
    isLoadingFund: Boolean,
    onBackClick: () -> Unit,
    onFundSelected: (MutualFundDomain) -> Unit,
    onSaveClick: () -> Unit,
    onSearchTextChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    loadNext: () -> Unit,
    onRetryClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .clearFocusOnTap()
    ) {
        BackHeader("", showBack = true, onBack = onBackClick)

        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = "EXPLORE FUNDS",
                style = titlesStyle.copy(fontSize = 12.sp, fontWeight = FontWeight.Bold),
                color = appGreen
            )
            Text(
                text = "Explore $categoryTitle Funds",
                style = MaterialTheme.typography.displayMedium,
                color = Color.Black
            )
            Text(
                text = "Pick any fund from the full $categoryTitle list",
                style = titlesStyle,
                color = titleColor
            )
        }

        Spacer(Modifier.height(20.dp))

        AppSearchBar(
            value = searchText,
            onTextChange = onSearchTextChange,
            onSearchClick = onSearchClick,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        )

        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .imePadding()
        ) {
            when (loadingState) {
                is LoadingState.Error -> ErrorScreen(loadingState.error, onRetryClick = onRetryClick)

                LoadingState.Loading -> LoaderScreen()

                LoadingState.Success -> ExploreFundList(
                    funds = funds,
                    categoryTitle = categoryTitle,
                    hasNextPage = hasNextPage,
                    unavailableFundIds = unavailableFundIds,
                    unselectableFundIds = unselectableFundIds,
                    unavailableNote = purchaseMode.unavailableNote(),
                    selectedFundId = selectedFundId,
                    onFundSelected = onFundSelected,
                    loadNext = loadNext
                )
            }
        }

        NextButtonFooter(
            onClick = onSaveClick,
            value = "Save Fund",
            enabled = selectedFundId != null && !isLoadingFund,
            loading = isLoadingFund
        )
    }
}

@Composable
private fun ExploreFundList(
    funds: List<MutualFundDomain>,
    categoryTitle: String,
    hasNextPage: Boolean,
    unavailableFundIds: Set<String>,
    unselectableFundIds: Set<String>,
    unavailableNote: String,
    selectedFundId: String?,
    onFundSelected: (MutualFundDomain) -> Unit,
    loadNext: () -> Unit
) {
    val lazyListState = rememberLazyListState()
    // A fund another slot of the category holds can't be picked for this one, so it isn't listed.
    val availableFunds = funds.filter { it.id !in unavailableFundIds }

    PaginationEffect(lazyListState = lazyListState, onLoadMore = loadNext)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        state = lazyListState,
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            FundSectionHeader(
                title = "$categoryTitle funds",
                subtitle = "Showing ${availableFunds.size} funds. You can choose any one of them.",
                modifier = Modifier.padding(bottom = 4.dp)
            )
        }

        items(availableFunds, key = { it.id }) { fund ->
            val name = fund.name.toTitleCase()
            // Known only once the fund has been tapped and its details fetched.
            val isSelectable = fund.id !in unselectableFundIds
            FundOptionCard(
                name = fundDisplayName(name),
                subtitle = if (isSelectable) fund.subtitle(name) else unavailableNote,
                subtitleColor = if (isSelectable) titleColor else appRed,
                enabled = isSelectable,
                iconUrl = fund.icon,
                return1Y = fund.returnYearsRate.year1,
                return3Y = fund.returnYearsRate.year3,
                return5Y = fund.returnYearsRate.year5,
                isSelected = fund.id == selectedFundId,
                onSelect = { onFundSelected(fund) }
            )
        }

        item {
            PaginationFooter(hasNextPage = hasNextPage)
        }
    }
}

/** "Direct • Growth" when the name says so, otherwise the fund's category. */
private fun MutualFundDomain.subtitle(titleCasedName: String): String =
    fundPlanLabel(titleCasedName) ?: category

@Preview
@Composable
private fun ExploreCategoryFundScreenPreview() {
    val fund = MutualFundDomain(
        id = "1",
        name = "PARAG PARIKH FLEXI CAP FUND - DIRECT PLAN - GROWTH",
        icon = "",
        category = "Flexi Cap",
        returnYearsRate = ReturnYearsRateDomain(month3 = 2.0, month6 = 7.0, year1 = 15.2, year3 = 21.7, year5 = 19.2)
    )
    VelvetTheme {
        ExploreCategoryFundScreen(
            categoryTitle = "Flexi Cap",
            purchaseMode = PurchaseMode.MONTHLY,
            loadingState = LoadingState.Success,
            funds = listOf(fund, fund.copy(id = "2", name = "HDFC FLEXI CAP FUND - REGULAR PLAN - IDCW")),
            hasNextPage = false,
            searchText = "",
            unavailableFundIds = emptySet(),
            unselectableFundIds = setOf("2"),
            selectedFundId = "1",
            isLoadingFund = false,
            onBackClick = {},
            onFundSelected = {},
            onSaveClick = {},
            onSearchTextChange = {},
            onSearchClick = {},
            loadNext = {},
            onRetryClick = {}
        )
    }
}
