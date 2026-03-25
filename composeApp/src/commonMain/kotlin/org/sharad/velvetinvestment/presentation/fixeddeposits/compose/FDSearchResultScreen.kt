package org.sharad.velvetinvestment.presentation.fixeddeposits.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.sharad.emify.core.ui.theme.shadowColor
import org.sharad.velvetinvestment.presentation.fixeddeposits.uimodels.FixedDepositUIModel
import org.sharad.velvetinvestment.presentation.fixeddeposits.viewmodel.FDSearchResultViewModel
import org.sharad.velvetinvestment.presentation.mutualfund.compose.FundFilterRow
import org.sharad.velvetinvestment.presentation.mutualfund.compose.InvestmentFilterScreen
import org.sharad.velvetinvestment.presentation.mutualfund.compose.YearRow
import org.sharad.velvetinvestment.presentation.mutualfund.viewmodel.SelectedReturnRatePeriod
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.ErrorScreen
import org.sharad.velvetinvestment.shared.compose.LoaderScreen
import org.sharad.velvetinvestment.utils.FDLabel
import org.sharad.velvetinvestment.utils.LabelFilter
import org.sharad.velvetinvestment.utils.LoadingState

@Composable
fun FDSearchScreenRoot(
    onBackClick: () -> Unit,
    onFDClick: (String) -> Unit,
    heading: String,
    pv: PaddingValues,
    searchId:String
) {


    val viewModel: FDSearchResultViewModel = koinViewModel{parametersOf(searchId)}
    val uiState by viewModel.loadingState.collectAsStateWithLifecycle()
    val selectedYear by viewModel.selectedYear.collectAsStateWithLifecycle()
    val sortedFD by viewModel.sortedFD.collectAsStateWithLifecycle()
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
                    is LoadingState.Error -> {
                        ErrorScreen((uiState as LoadingState.Error).error, onRetryClick = {})
                    }

                    LoadingState.Loading -> {
                        LoaderScreen()
                    }

                    LoadingState.Success -> {
                        FDSearchScreen(
                            result = sortedFD,
                            onFDClick = onFDClick,
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
fun FDSearchScreen(
    result: List<FixedDepositUIModel>,
    onFDClick: (String) -> Unit,
    pv: PaddingValues,
    selectedYear: Int,
    incrementYear: () -> Unit,
    decrementYear: () -> Unit,
    selectedFilter: LabelFilter?,
    onFilterSelected: (LabelFilter) -> Unit,
    toggleFilterScreen: () -> Unit
) {

    val labels: List<LabelFilter> = listOf(
        FDLabel.PublicBank,
        FDLabel.PrivateBank,
        FDLabel.NBFC,
        FDLabel.SmallStart,
        FDLabel.WomenSpecial
    )

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(top=4.dp),
    ) {

        item {
            FundFilterRow(
                filters = labels,
                selectedFilter = selectedFilter,
                onFilterSelected = onFilterSelected,
                toggleFilterScreen=toggleFilterScreen
            )
        }

        item{Spacer(Modifier.height(20.dp))}

        item{
            YearRow(
                selectedYear = SelectedReturnRatePeriod.THREE_YEAR,
//                incrementYear = incrementYear,
//                decrementYear = decrementYear,
                totalFunds = result.size,
                toggleRateYear = {}
            )
        }

        item{Spacer(Modifier.height(10.dp))}

        itemsIndexed(result, key = { _, item -> item.id }){idx, fd ->
            FDListCard(fd=fd, onClick = { onFDClick(fd.id) }, selectedYear=selectedYear)
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