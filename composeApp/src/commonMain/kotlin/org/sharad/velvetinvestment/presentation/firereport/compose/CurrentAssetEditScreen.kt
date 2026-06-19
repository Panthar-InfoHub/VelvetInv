package org.sharad.velvetinvestment.presentation.firereport.compose


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.velvetinvestment.presentation.firereport.viewmodel.CurrentAssetEditViewModel
import org.sharad.velvetinvestment.presentation.onboarding.compose.currentassets.AssetHolding
import org.sharad.velvetinvestment.presentation.onboarding.compose.currentassets.CASUploadScreenDialog
import org.sharad.velvetinvestment.presentation.onboarding.compose.currentassets.InfoHeader
import org.sharad.velvetinvestment.presentation.onboarding.compose.currentassets.TotalAssets
import org.sharad.velvetinvestment.presentation.onboarding.compose.personaldetails.NextButtonFooter
import org.sharad.velvetinvestment.shared.UiStateContainer
import org.sharad.velvetinvestment.shared.compose.BackHeader

@Composable
fun CurrentAssetEditScreen(
    onBackClick: () -> Unit,
) {
    val viewModel: CurrentAssetEditViewModel = koinViewModel()

    val state by viewModel.assetInfo.collectAsStateWithLifecycle()
    val totalAssets by viewModel.totalAssets.collectAsStateWithLifecycle()
    val loading by viewModel.loading.collectAsStateWithLifecycle()
    val showDialog by viewModel.showCASDialog.collectAsStateWithLifecycle()

    UiStateContainer(
        uiState = state,
        onRetry = { viewModel.loadData() },
        modifier = Modifier.fillMaxSize()
    ) { data ->
        Column(modifier = Modifier.fillMaxSize()) {

            BackHeader(
                heading = "Update Assets",
                onBackClick = onBackClick,
                showBack = true
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(bottom = 28.dp)
            ) {

                item {
                    InfoHeader(onClick = viewModel::showCASDialog)
                }

                item {
                    AssetHolding(
                        assetInfo = data,
                        onMutualFundsUpdate = viewModel::onMutualFundsUpdate,
                        onStocksAndSharesUpdate = viewModel::onStocksAndSharesUpdate,
                        onFixedDepositsUpdate = viewModel::onFixedDepositsUpdate,
                        onRealEstateUpdate = viewModel::onRealEstateUpdate,
                        onGoldAndCommoditiesUpdate = viewModel::onGoldAndCommoditiesUpdate,
                        onCashUpdate = viewModel::onCashUpdate
                    )
                }

                item {
                    TotalAssets(totalAssets = totalAssets)
                }

            }

            NextButtonFooter(
                onClick = { viewModel.onSubmit { onBackClick() } },
                value = "Submit Changes",
                loading = loading
            )
        }
    }
    if (showDialog) {
        CASUploadScreenDialog(
            hideDialog = viewModel::hideCASDialog,
            onSuccess = {data->
                viewModel.resetData()
                viewModel.onStocksAndSharesUpdate(data.summary.accounts.demat.total_value.toLong().toString())
                viewModel.onMutualFundsUpdate(data.summary.accounts.mutual_funds.total_value.toLong().toString())
            }
        )
    }
}