package org.sharad.velvetinvestment.features.portfolio.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.sharad.velvetinvestment.features.portfolio.domain.models.FolioFundDomain
import org.sharad.velvetinvestment.features.portfolio.domain.models.HoldingFolioDomain
import org.sharad.velvetinvestment.features.portfolio.domain.models.MutualFundPortfolioDomain
import org.sharad.velvetinvestment.features.portfolio.ui.viewmodel.FundFolioItem
import org.sharad.velvetinvestment.features.portfolio.ui.viewmodel.FundFoliosViewModel
import org.sharad.velvetinvestment.shared.UiStateContainer
import org.sharad.velvetinvestment.shared.compose.BackHeader

/**
 * A portfolio fund split into its folios, one [FolioFundCard] each, opening that folio's order
 * details. The portfolio only says which folios there are; each card's figures are loaded from
 * `GET /user/portfolio/{folio}` by [FundFoliosViewModel].
 */
@Composable
fun FundFoliosScreen(
    title: String,
    folios: List<HoldingFolioDomain>,
    onBack: () -> Unit,
    onFolioClick: (FundFolioItem) -> Unit,
) {
    val viewModel: FundFoliosViewModel = koinViewModel(parameters = { parametersOf(title, folios) })
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize().background(Color.White),
    ) {
        BackHeader(title = "Folios", showBack = true, onBack = onBack)

        Box(modifier = Modifier.weight(1f).fillMaxSize()) {
            UiStateContainer(
                uiState = uiState,
                onRetry = viewModel::load
            ) { items ->
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    itemsIndexed(
                        items,
                        key = { index, item -> "${item.fund.mfHoldingId}_${item.fund.folio}_$index" }
                    ) { _, item ->
                        FolioFundCard(
                            fundItem = item.fund.asCardItem(),
                            onClick = { onFolioClick(item) },
                            isCancelled = item.fund.isSip &&
                                item.fund.state.equals("CANCELLED", ignoreCase = true)
                        )
                    }
                }
            }
        }
    }
}

/** A folio's holding in the shape [FolioFundCard] renders. */
private fun FolioFundDomain.asCardItem() = MutualFundPortfolioDomain(
    id = mfHoldingId,
    title = title,
    category = category,
    amount = amount,
    currentValue = currentValue,
    returnAmount = `return`,
    returnPercentage = returnPercentage,
    folio = actualFolio.ifBlank { folio },
    actualFolio = actualFolio,
    icon = imgUrl,
    minSipAmount = 0L,
    minLumpSumAmount = 0L,
    schemeId = 0,
    balanceUnits = balanceUnits,
    isSip = isSip,
    currentNav = currentNav,
    avgNav = avgNav
)
