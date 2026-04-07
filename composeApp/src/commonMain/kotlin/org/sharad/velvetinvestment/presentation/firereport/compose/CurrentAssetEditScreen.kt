package org.sharad.velvetinvestment.presentation.firereport.compose


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

import org.sharad.velvetinvestment.presentation.firereport.viewmodel.CurrentAssetEditViewModel
import org.sharad.velvetinvestment.presentation.onboarding.compose.currentassets.*
import org.sharad.velvetinvestment.presentation.onboarding.compose.personaldetails.NextButtonFooter
import org.sharad.velvetinvestment.presentation.onboarding.models.AssetFlowDetails

import org.sharad.velvetinvestment.shared.compose.*
import org.sharad.velvetinvestment.utils.UiState

@Composable
fun CurrentAssetEditScreen(
    onBackClick: () -> Unit,
    pv: PaddingValues
) {
    val viewModel: CurrentAssetEditViewModel = koinViewModel()

    val state by viewModel.assetInfo.collectAsStateWithLifecycle()
    val totalAssets by viewModel.totalAssets.collectAsStateWithLifecycle()
    val loading by viewModel.loading.collectAsStateWithLifecycle()
    val showDialog by viewModel.showCASDialog.collectAsStateWithLifecycle()

    when (state) {

        is UiState.Error -> {
            ErrorScreen(
                errorMessage = (state as UiState.Error).message,
                onRetryClick = { viewModel.loadData() }
            )
        }

        is UiState.Loading -> {
            LoaderScreen()
        }

        is UiState.Success -> {

            val data = (state as UiState.Success<AssetFlowDetails>).data

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
                    verticalArrangement = Arrangement.spacedBy(20.dp)
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

                    item {
                        Spacer(
                            modifier = Modifier.height(80.dp + pv.calculateBottomPadding())
                        )
                    }
                }

                NextButtonFooter(
                    onClick = { viewModel.onSubmit { onBackClick() } },
                    pv = pv,
                    value = "Submit Changes",
                    loading = loading
                )
            }


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