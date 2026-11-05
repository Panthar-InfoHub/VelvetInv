package org.sharad.velvetinvestment.features.fireonboarding.presentation.firereport.compose


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.sharad.velvetinvestment.features.fireonboarding.presentation.firereport.viewmodel.AssetField
import org.sharad.velvetinvestment.features.fireonboarding.presentation.firereport.viewmodel.CurrentAssetEditEvent
import org.sharad.velvetinvestment.features.fireonboarding.presentation.firereport.viewmodel.CurrentAssetEditUiState
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.compose.currentassets.AssetHolding
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.compose.currentassets.CASUploadScreenDialog
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.compose.currentassets.InfoHeader
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.compose.currentassets.TotalAssets
import org.sharad.velvetinvestment.shared.compose.NextButtonFooter
import org.sharad.velvetinvestment.shared.UiStateContainer
import org.sharad.velvetinvestment.shared.compose.BackHeader

@Composable
fun CurrentAssetEditScreen(
    state: CurrentAssetEditUiState,
    onEvent: (CurrentAssetEditEvent) -> Unit
) {
    val onAsset: (AssetField) -> (String) -> Unit = { field -> { onEvent(CurrentAssetEditEvent.OnAssetChanged(field, it)) } }

    UiStateContainer(
        uiState = state.assetInfo,
        onRetry = { onEvent(CurrentAssetEditEvent.Retry) },
        modifier = Modifier.fillMaxSize().imePadding()
    ) { data ->
        Column(modifier = Modifier.fillMaxSize()) {

            BackHeader(
                title = "Update Assets",
                onBack = { onEvent(CurrentAssetEditEvent.OnBackClicked) },
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
                    InfoHeader(onClick = { onEvent(CurrentAssetEditEvent.OnImportCasClicked) })
                }

                item {
                    AssetHolding(
                        assetInfo = data,
                        onMutualFundsUpdate = onAsset(AssetField.MUTUAL_FUNDS),
                        onStocksAndSharesUpdate = onAsset(AssetField.STOCKS_AND_SHARES),
                        onFixedDepositsUpdate = onAsset(AssetField.FIXED_DEPOSITS),
                        onRealEstateUpdate = onAsset(AssetField.REAL_ESTATE),
                        onGoldAndCommoditiesUpdate = onAsset(AssetField.GOLD_AND_COMMODITIES),
                        onCashUpdate = onAsset(AssetField.CASH)
                    )
                }

                item {
                    TotalAssets(totalAssets = state.totalAssets)
                }

            }

            NextButtonFooter(
                onClick = { onEvent(CurrentAssetEditEvent.OnSubmitClicked) },
                value = "Submit Changes",
                loading = state.isSubmitting
            )
        }
    }
    if (state.showCasDialog) {
        CASUploadScreenDialog(
            hideDialog = { onEvent(CurrentAssetEditEvent.OnCasDialogDismissed) },
            onSuccess = { report -> onEvent(CurrentAssetEditEvent.OnCasImported(report)) }
        )
    }
}