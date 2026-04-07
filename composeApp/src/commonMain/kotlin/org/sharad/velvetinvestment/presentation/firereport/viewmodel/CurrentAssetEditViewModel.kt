package org.sharad.velvetinvestment.presentation.firereport.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.data.remote.mapper.toAssetsDto
import org.sharad.velvetinvestment.data.remote.model.updateuserdata.AssetUpdateDto
import org.sharad.velvetinvestment.domain.repository.UserAuth
import org.sharad.velvetinvestment.presentation.onboarding.models.AssetFlowDetails
import org.sharad.velvetinvestment.utils.SnackBarController
import org.sharad.velvetinvestment.utils.SnackBarType
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess
import org.sharad.velvetinvestment.utils.parseSafeLong

class CurrentAssetEditViewModel(
    private val userAuth: UserAuth
) : ViewModel() {

    private val _assetInfo =
        MutableStateFlow<UiState<AssetFlowDetails>>(UiState.Loading)
    val assetInfo = _assetInfo.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _showCASDialog = MutableStateFlow(false)
    val showCASDialog = _showCASDialog.asStateFlow()

    val totalAssets = assetInfo
        .map {
            if (it !is UiState.Success) return@map 0L
            val a = it.data
            listOf(
                a.mutualFunds,
                a.stocksAndShares,
                a.fixedDeposits,
                a.realEstate,
                a.goldAndCommodities,
                a.cash
            ).sumOf { it ?: 0L }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0L)

    // ---------------- LOAD ----------------
    fun loadData() {
        viewModelScope.launch {
            _assetInfo.value = UiState.Loading

            userAuth.getUserData()
                .onError {
                    _assetInfo.value = UiState.Error(it.message)
                }
                .onSuccess {
                    val assets = it.data.user_assets

                    _assetInfo.value = UiState.Success(
                        AssetFlowDetails(
                            mutualFunds = assets.mutual_funds.toSafeLong(),
                            stocksAndShares = assets.stocks.toSafeLong(),
                            fixedDeposits = assets.fd.toSafeLong(),
                            realEstate = assets.real_estate.toSafeLong(),
                            goldAndCommodities = assets.gold.toSafeLong(),
                            cash = assets.cash_saving.toSafeLong()
                        )
                    )
                }
        }
    }

    init { loadData() }

    // ---------------- UPDATE ----------------

    private inline fun update(block: (AssetFlowDetails) -> AssetFlowDetails) {
        val current = _assetInfo.value
        if (current is UiState.Success) {
            _assetInfo.value = UiState.Success(block(current.data))
        }
    }
    fun resetData(){
        _assetInfo.value= UiState.Success(AssetFlowDetails())
    }

    fun onMutualFundsUpdate(v: String) = update {
        it.copy(mutualFunds = parseSafeLong(v, it.mutualFunds))
    }

    fun onStocksAndSharesUpdate(v: String) = update {
        it.copy(stocksAndShares = parseSafeLong(v, it.stocksAndShares))
    }

    fun onFixedDepositsUpdate(v: String) = update {
        it.copy(fixedDeposits = parseSafeLong(v, it.fixedDeposits))
    }

    fun onRealEstateUpdate(v: String) = update {
        it.copy(realEstate = parseSafeLong(v, it.realEstate))
    }

    fun onGoldAndCommoditiesUpdate(v: String) = update {
        it.copy(goldAndCommodities = parseSafeLong(v, it.goldAndCommodities))
    }

    fun onCashUpdate(v: String) = update {
        it.copy(cash = parseSafeLong(v, it.cash))
    }

    fun showCASDialog() { _showCASDialog.value = true }
    fun hideCASDialog() { _showCASDialog.value = false }

    // ---------------- SUBMIT ----------------

    fun onSubmit(onSuccess: () -> Unit) {
        val current = _assetInfo.value
        if (current !is UiState.Success) return

        val dto = AssetUpdateDto(
            assets = current.data.toAssetsDto()
        )

        viewModelScope.launch {
            _loading.value = true

            userAuth.updateAssets(dto)
                .onError {
                    _loading.value = false
                    SnackBarController.showSnackBar(
                        SnackBarType.Error(it.message)
                    )
                }
                .onSuccess {
                    _loading.value = false
                    onSuccess()
                }
        }
    }
}

private fun String?.toSafeLong(): Long? {
    return this
        ?.takeIf { it.isNotBlank() && it != "null" }
        ?.toLongOrNull()
}