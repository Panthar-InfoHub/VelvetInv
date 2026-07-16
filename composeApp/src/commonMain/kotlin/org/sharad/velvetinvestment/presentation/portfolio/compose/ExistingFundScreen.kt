package org.sharad.velvetinvestment.presentation.portfolio.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.velvetinvestment.presentation.portfolio.viewmodel.PortfolioScreenViewModel
import org.sharad.velvetinvestment.shared.UiStateContainer
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.FolioFundCard

@Composable
fun ExistingFundScreenRoot(
    onBack: () -> Unit,
    onFundClick: (id: String, folio: String) -> Unit,
) {
    val viewModel: PortfolioScreenViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        BackHeader(
            heading = "Existing Fund",
            showBack = true,
            onBackClick = onBack
        )

        UiStateContainer(
            uiState = uiState,
            onRetry = viewModel::loadPortfolio
        ) { data ->
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(bottom = 28.dp)
            ) {
                item { Spacer(modifier = Modifier.height(8.dp)) }
                items(data.mutualFunds, key = {it.folio }) { fund ->
                    FolioFundCard(
                        fundItem = fund,
                        onClick = { onFundClick(fund.id, fund.actualFolio) }
                    )
                }
            }
        }
    }
}
