package org.sharad.velvetinvestment.presentation.onboarding.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.sharad.velvetinvestment.presentation.onboarding.models.AssetFlowDetails
import org.sharad.velvetinvestment.utils.parseSafeLong

class CurrentAssetViewModel: ViewModel() {

    private val _assetInfo = MutableStateFlow(AssetFlowDetails())
    val assetInfo = _assetInfo.asStateFlow()

    private val _showCASDialog= MutableStateFlow(false)
    val showCASDialog= _showCASDialog.asStateFlow()


    val totalAssets: StateFlow<Long> =
        assetInfo
            .map { assets ->
                listOf(
                    assets.mutualFunds,
                    assets.stocksAndShares,
                    assets.fixedDeposits,
                    assets.realEstate,
                    assets.goldAndCommodities,
                    assets.cash
                ).sumOf { it ?: 0L }
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = 0L
            )


    fun onMutualFundsUpdate(value: String) {
        _assetInfo.value = _assetInfo.value.let { current ->
            current.copy(
                mutualFunds = parseSafeLong(value, current.mutualFunds)
            )
        }
    }

    fun onStocksAndSharesUpdate(value: String) {
        _assetInfo.value = _assetInfo.value.let { current ->
            current.copy(
                stocksAndShares = parseSafeLong(value, current.stocksAndShares)
            )
        }
    }

    fun onFixedDepositsUpdate(value: String) {
        _assetInfo.value = _assetInfo.value.let { current ->
            current.copy(
                fixedDeposits = parseSafeLong(value, current.fixedDeposits)
            )
        }
    }

    fun onRealEstateUpdate(value: String) {
        _assetInfo.value = _assetInfo.value.let { current ->
            current.copy(
                realEstate = parseSafeLong(value, current.realEstate)
            )
        }
    }

    fun onGoldAndCommoditiesUpdate(value: String) {
        _assetInfo.value = _assetInfo.value.let { current ->
            current.copy(
                goldAndCommodities = parseSafeLong(value, current.goldAndCommodities)
            )
        }
    }

    fun onCashUpdate(value: String) {
        _assetInfo.value = _assetInfo.value.let { current ->
            current.copy(
                cash = parseSafeLong(value, current.cash)
            )
        }
    }

    fun showCASDialog(){
        _showCASDialog.value=true
    }

    fun hideCASDialog(){
        _showCASDialog.value=false
    }

}