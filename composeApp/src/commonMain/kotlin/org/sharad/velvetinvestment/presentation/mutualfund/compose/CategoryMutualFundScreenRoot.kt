package org.sharad.velvetinvestment.presentation.mutualfund.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.lightGray
import org.sharad.emify.core.ui.theme.shadowColor
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.domain.models.mutualfunds.BundleMetaDataDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.CombinedFundsDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.CuratedBundleDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.ReturnYearsRateDomain
import org.sharad.velvetinvestment.presentation.mutualfund.CategoryMutualFundDomain
import org.sharad.velvetinvestment.presentation.mutualfund.viewmodel.CategoryMutualFundViewModel
import org.sharad.velvetinvestment.presentation.mutualfund.viewmodel.SelectedReturnRatePeriod
import org.sharad.velvetinvestment.shared.compose.AppSearchBar
import org.sharad.velvetinvestment.shared.compose.BarHeader
import org.sharad.velvetinvestment.shared.compose.ErrorScreen
import org.sharad.velvetinvestment.shared.compose.LoaderScreen
import org.sharad.velvetinvestment.shared.compose.ShadowCard
import org.sharad.velvetinvestment.shared.genericDropShadow
import org.sharad.velvetinvestment.shared.theme.LocalVelvetShapes
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import org.sharad.velvetinvestment.shared.theme.titlesStyle
import org.sharad.velvetinvestment.utils.LoadingState
import org.sharad.velvetinvestment.utils.clearFocusOnTap
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.back_arrow
import velvet.composeapp.generated.resources.cart_icon

@Composable
fun CategoryMutualFundScreenRoot(
    onBackClick: () -> Unit,
    onIconClick: () -> Unit,
    onFundClick: (String) -> Unit,
    onSearchClick: (String) -> Unit,
    onCategoryClick: (String) -> Unit,
    onBundleClick:() -> Unit,
    onBundledFundClick: (String) -> Unit
){

    val viewModel: CategoryMutualFundViewModel = koinViewModel()
    val combinedState by viewModel.mutualFunds.collectAsStateWithLifecycle()
    val uiState by viewModel.loadingState.collectAsStateWithLifecycle()
    val searchText by viewModel.searchText.collectAsStateWithLifecycle()


    CategoryMutualFundScreenRootContent(
        uiState = uiState,
        combinedState = combinedState,
        searchText = searchText,
        onBackClick = onBackClick,
        onIconClick = onIconClick,
        onFundClick = onFundClick,
        onSearchClick = onSearchClick,
        onCategoryClick = onCategoryClick,
        onBundleClick = onBundleClick,
        onBundledFundClick = onBundledFundClick,
        onRetryClick = { viewModel.loadMutualFunds() },
        onSearchTextChange = { viewModel.onSearchTextChange(it) }
    )
}

@Composable
fun CategoryMutualFundScreenRootContent(
    uiState: LoadingState,
    combinedState: CombinedFundsDomain,
    searchText: String,
    onBackClick: () -> Unit,
    onIconClick: () -> Unit,
    onFundClick: (String) -> Unit,
    onSearchClick: (String) -> Unit,
    onCategoryClick: (String) -> Unit,
    onBundleClick: () -> Unit,
    onBundledFundClick: (String) -> Unit,
    onRetryClick: () -> Unit,
    onSearchTextChange: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
            .clearFocusOnTap()
            .background(Color.White)
            .imePadding()
    ) {
        ScreenHeader(
            onBackClick = { onBackClick() },
            onIconClick = { onIconClick() }
        )
        Box(
            modifier = Modifier.weight(1f)
                .fillMaxSize()
        ){
            when(uiState){
                is LoadingState.Error->{
                    ErrorScreen(uiState.error, onRetryClick = onRetryClick)
                }
                LoadingState.Loading -> {
                    LoaderScreen()
                }

                LoadingState.Success -> {
                    CategoryMutualFundScreen(
                        bundles=combinedState.bundleFunds,
                        funds= combinedState.categoryMutualFundDomain,
                        onCategoryClick = onCategoryClick,
                        onFundClick = {onFundClick(it)},
                        searchText =searchText,
                        onTextChange = onSearchTextChange,
                        onSearchClick = {onSearchClick(searchText)},
                        onBundledFundClick = {onBundledFundClick(it)},
                        onBundleClick = {onBundleClick()}
                    )
                }
            }
        }
    }
}

@Composable
fun CategoryMutualFundScreen(
    bundles: List<CuratedBundleDomain>,
    onCategoryClick: (String) -> Unit,
    onFundClick: (String) -> Unit,
    searchText: String,
    onTextChange: (String) -> Unit,
    onSearchClick: (String) -> Unit,
    funds: List<CategoryMutualFundDomain>,
    onBundledFundClick: (String) -> Unit,
    onBundleClick: () -> Unit
) {

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 28.dp )
    ) {
        item {
            AppSearchBar(
                value = searchText,
                onTextChange = { onTextChange(it) },
                onSearchClick = { onSearchClick(searchText) },
                modifier = Modifier.fillMaxWidth()
            )
        }


        item {
            BarHeader(
                heading = "Curated Bundles",
                showArrow = true,
                onArrowClick = { onBundleClick() },
                modifier = Modifier.padding(vertical = 4.dp)

            )
        }
        if (bundles.isEmpty()) {
            item {
                ShadowCard {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                            .height(120.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Coming Soon !",
                            style = MaterialTheme.typography.headlineSmall
                        )
                    }
                }
            }

        }
        else{
            item{
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(
                        items = bundles,
                        key = { bundle -> bundle.id }
                    ){bundle->
                        CuratedBundleCard(
                            type = bundle.name,
                            title = bundle.description,
                            onClick = {onBundledFundClick(bundle.id)}
                        )
                    }
                }
            }
        }

        funds.forEach {category->
            item {
                BarHeader(
                    heading = category.categoryName,
                    showArrow = true,
                    onArrowClick = {onCategoryClick(category.categorySearchReference)},
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

            items(
                items = category.mutualFunds,
                key = {fund -> category.categorySearchReference + fund.id }
            ) {  fund ->

                Column(
                ) {
                    MutualFundListCard(
                        onClick = { onFundClick(fund.id) },
                        fund = fund,
                        selectedYear = SelectedReturnRatePeriod.THREE_YEAR,
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 20.dp )
                            .height(1.dp)
                            .clip(CircleShape)
                            .background(shadowColor)
                    )
                }
            }
        }
    }

}

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

@Composable
fun CuratedBundleCard(
    type: String,
    title: String,
    onClick: () -> Unit,
    modifier: Modifier= Modifier
) {

    Column(
        modifier = modifier
            .clip(LocalVelvetShapes.current.roundedDp12)
            .border(
                width = 1.dp,
                color = lightGray,
                shape = LocalVelvetShapes.current.roundedDp12,
            )
            .clickable(
                onClick = onClick
            )
            .padding(vertical = 20.dp, horizontal = 20.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = type.toUpperCase(Locale.current),
            style = titlesStyle,
            color = titleColor
        )
        Text(
            text = title,
            style = MaterialTheme.typography.labelLarge
        )
    }
}

@Composable
private fun ScreenHeader(onIconClick: () -> Unit, onBackClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth()
            .padding(bottom = 16.dp, start = 12.dp, end = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Mutual Funds",
            style = MaterialTheme.typography.headlineLarge,
            color = Primary
        )
        Icon(
            painter = painterResource(Res.drawable.back_arrow),
            contentDescription = null,
            modifier = Modifier.size(22.dp).clickable(
                onClick = onBackClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ).align(Alignment.CenterStart)
        )

        Box(
            modifier=Modifier
                .size(52.dp)
                .genericDropShadow(CircleShape)
                .clip(CircleShape)
                .background(Color.White)
                .clickable(
                    onClick = onIconClick
                ).align(Alignment.CenterEnd)
        ){
            Icon(
                painter = painterResource(Res.drawable.cart_icon),
                contentDescription = null,
                modifier = Modifier.size(24.dp).align(Alignment.Center),
                tint= Secondary
            )
        }
    }
}


@Preview
@Composable
private fun CategoryMutualFundScreenRootPreview() {

    val sampleFunds = listOf(
        MutualFundDomain(
            id = "1",
            name = "SBI Bluechip Fund",
            icon = "",
            category = "Equity: Large Cap",
            remark = null,
            riskText = "Very High",
            type = "Equity",
            returnYearsRate = ReturnYearsRateDomain(
                month3 = 5.0,
                month6 = 10.0,
                year1 = 15.0,
                year3 = 45.0,
                year5 = 55.0
            ),
            latestNav = "78.5",
            "", "",
        ),
        MutualFundDomain(
            id = "2",
            name = "Axis Midcap Fund",
            icon = "",
            category = "Equity: Mid Cap",
            remark = null,
            riskText = "High",
            type = "Equity",
            returnYearsRate = ReturnYearsRateDomain(
                month3 = 4.2,
                month6 = 9.1,
                year1 = 13.4,
                year3 = 38.0,
                year5 = 39.5
            ),
            latestNav = "52.7", "", "",
        ),
        MutualFundDomain(
            id = "3",
            name = "ICICI Flexi Cap Fund",
            icon = "",
            category = "Flexi Cap",
            remark = null,
            riskText = "Moderately High",
            type = "Equity",
            returnYearsRate = ReturnYearsRateDomain(
                month3 = 3.5,
                month6 = 7.8,
                year1 = 11.2,
                year3 = 31.5,
                year5 = 42.0
            ),
            latestNav = "102.3", "", "",
        ),
        MutualFundDomain(
            id = "4",
            name = "HDFC Small Cap Fund",
            icon = "",
            category = "Small Cap",
            remark = null,
            riskText = "Very High",
            type = "Equity",
            returnYearsRate = ReturnYearsRateDomain(
                month3 = 6.5,
                month6 = 12.4,
                year1 = 18.7,
                year3 = 49.2,
                year5 = 59.8
            ),
            latestNav = "36.2", "", "",
        )
    )

    val sampleCategories = listOf(
        CategoryMutualFundDomain(
            categoryName = "Large Cap",
            categorySearchReference = "large_cap",
            mutualFunds = sampleFunds
        ),
        CategoryMutualFundDomain(
            categoryName = "Mid Cap",
            categorySearchReference = "mid_cap",
            mutualFunds = sampleFunds
        ),
        CategoryMutualFundDomain(
            categoryName = "Flexi Cap",
            categorySearchReference = "flexi_cap",
            mutualFunds = sampleFunds
        ),
        CategoryMutualFundDomain(
            categoryName = "Small Cap",
            categorySearchReference = "small_cap",
            mutualFunds = sampleFunds
        )
    )

    val sampleBundle = CuratedBundleDomain(
        id = "1",
        name = "Aggressive",
        description = "Velvet Long Term Vision",
        equityPercentage = 95,
        commodityPercentage = 5,
        debtPercentage = 0,
        hybridPercentage = 0,
        metaData = BundleMetaDataDomain(
            riskLevel = "AGGRESSIVE",
            investmentTime = "7+ YEARS",
            investmentGrowth = "LONG-TERM WEALTH"
        )
    )

    val sampleCombinedState = CombinedFundsDomain(
        bundleFunds = listOf(sampleBundle),
        categoryMutualFundDomain = sampleCategories
    )

    VelvetTheme {
        CategoryMutualFundScreenRootContent(
            uiState = LoadingState.Success,
            combinedState = sampleCombinedState,
            searchText = "",
            onBackClick = {},
            onIconClick = {},
            onFundClick = {},
            onSearchClick = {},
            onCategoryClick = {},
            onBundleClick = {},
            onBundledFundClick = {},
            onRetryClick = {},
            onSearchTextChange = {}
        )
    }
}