package org.sharad.velvetinvestment.presentation.mutualfund.compose

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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.appGreen
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.domain.models.mutualfunds.BundledMutualFundDomain
import org.sharad.velvetinvestment.presentation.mutualfund.CategoryMutualFundDomain
import org.sharad.velvetinvestment.presentation.mutualfund.viewmodel.CategoryMutualFundViewModel
import org.sharad.velvetinvestment.shared.compose.AppSearchBar
import org.sharad.velvetinvestment.shared.compose.BarHeader
import org.sharad.velvetinvestment.shared.compose.ErrorScreen
import org.sharad.velvetinvestment.shared.compose.LoaderScreen
import org.sharad.velvetinvestment.shared.compose.ShadowCard
import org.sharad.velvetinvestment.shared.genericDropShadow
import org.sharad.velvetinvestment.utils.LoadingState
import org.sharad.velvetinvestment.utils.formatMoneyAfterL
import org.sharad.velvetinvestment.shared.theme.subHeading
import org.sharad.velvetinvestment.shared.theme.titlesStyle
import org.sharad.velvetinvestment.utils.trimTo
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.back_arrow
import velvet.composeapp.generated.resources.mf_haeder_icon

@Composable
fun CategoryMutualFundScreenRoot(
    onBackClick: () -> Unit,
    onIconClick: () -> Unit,
    onFundClick: (String) -> Unit,
    pv: PaddingValues,
    onSearchClick: (String) -> Unit,
    onCategoryClick: (String) -> Unit,
    onBundleClick:() -> Unit,
    onBundledFundClick: (String) -> Unit
){

    val viewModel: CategoryMutualFundViewModel = koinViewModel()
    val combinedState by viewModel.mutualFunds.collectAsStateWithLifecycle()
    val uiState by viewModel.loadingState.collectAsStateWithLifecycle()
    val searchText by viewModel.searchText.collectAsStateWithLifecycle()


    Column(
        modifier = Modifier.fillMaxSize()
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
                    ErrorScreen((uiState as LoadingState.Error).error, onRetryClick = {})
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
                        onTextChange = { viewModel.onSearchTextChange(it) },
                        pv =pv,
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
    bundles: List<BundledMutualFundDomain>,
    onCategoryClick: (String) -> Unit,
    onFundClick: (String) -> Unit,
    searchText: String,
    onTextChange: (String) -> Unit,
    pv: PaddingValues,
    onSearchClick: (String) -> Unit,
    funds: List<CategoryMutualFundDomain>,
    onBundledFundClick: (String) -> Unit,
    onBundleClick: () -> Unit
) {

    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 150.dp),
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        item(span = { GridItemSpan(maxLineSpan) }) {
            AppSearchBar(
                value = searchText,
                onTextChange = { onTextChange(it) },
                onSearchClick = { onSearchClick(searchText) }
            )
        }

        if (bundles.isNotEmpty()){
            item(span = { GridItemSpan(maxLineSpan) }) {
                BarHeader(
                    heading = "Curated Bundles",
                    showArrow = true,
                    onArrowClick = {onBundleClick()}
                )
            }

            items(
                items = bundles,
                key = {it.key}
            ){bundle->
                BundleCard(
                    title = bundle.categoryName,
                    minAmount = "₹" + formatMoneyAfterL(bundle.minAmount.toLong()),
                    onClick = {onBundledFundClick(bundle.key)}
                )
            }
        }

        funds.forEach {category->
            item(span = { GridItemSpan(maxLineSpan) }) {
                BarHeader(
                    heading = category.categoryName,
                    showArrow = true,
                    onArrowClick = {onCategoryClick(category.categorySearchReference)}
                )
            }

            items(
                items = category.mutualFunds,
                key = { category.categorySearchReference+it.id }
            ) { fund ->

                MutualFundGridCard(
                    onClick = { onFundClick(fund.id) },
                    schemeName = fund.name,
                    assetType = fund.type,
                    latestNav = fund.latestNav,
                    oneYearReturn= fund.returnYearsRate.year1
                )
            }
        }
        item(span = { GridItemSpan(maxLineSpan) }){
            Spacer(Modifier.height(pv.calculateBottomPadding()+20.dp))
        }
    }

}

@Composable
fun MutualFundGridCard(
    schemeName: String,
    assetType: String,
    latestNav: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    oneYearReturn: Double?
) {
    ShadowCard(
        modifier = modifier
            .widthIn(min = 160.dp)
            .height(176.dp),
        clickable = true,
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MutualFundIcon(
                    schemeName = schemeName
                )

                Text(
                    text = schemeName,
                    style = subHeading,
                    color = Color.Black,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "1Y Return",
                    style = titlesStyle,
                    color = titleColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Text(
                    text = oneYearReturn?.let {
                        it.trimTo(2) + "%"
                    }?: "N/A",
                    style = subHeading,
                    color = appGreen
                )
            }
        }
    }
}


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

@Composable
private fun ScreenHeader(onIconClick: () -> Unit, onBackClick: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp, horizontal = 12.dp),
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
                painter = painterResource(Res.drawable.mf_haeder_icon),
                contentDescription = null,
                modifier = Modifier.size(24.dp).align(Alignment.Center),
                tint= Secondary
            )
        }
    }

}