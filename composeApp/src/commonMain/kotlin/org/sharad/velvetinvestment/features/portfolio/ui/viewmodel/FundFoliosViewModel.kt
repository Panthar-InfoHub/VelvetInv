package org.sharad.velvetinvestment.features.portfolio.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.features.portfolio.domain.models.FolioFundDomain
import org.sharad.velvetinvestment.features.portfolio.domain.models.HoldingFolioDomain
import org.sharad.velvetinvestment.features.portfolio.domain.usecases.GetFolioFundsUseCase
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

/** One folio's holding as `GET /user/portfolio/{folio}` reports it. */
data class FundFolioItem(
    val fund: FolioFundDomain,
    /**
     * From the portfolio: the folio call does not report it. Capped at the folio's balance, so it
     * never offers more than the folio holds.
     */
    val redeemableUnits: Double
)

/**
 * Backs the fund's folio listing. The portfolio only names the folios (number, holding id,
 * redeemable units); every figure shown is read from `GET /user/portfolio/{folio}`, one call per
 * folio, in parallel.
 *
 * A folio can hold several of the AMC's schemes, so each call's result is narrowed to this fund's
 * holding: by `mf_holding_id`, then by the fund's name, and only then the whole folio.
 */
class FundFoliosViewModel(
    private val fundTitle: String,
    private val folios: List<HoldingFolioDomain>,
    private val getFolioFundsUseCase: GetFolioFundsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState<List<FundFolioItem>>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading

            val responses = folios.map { folio ->
                async { folio to getFolioFundsUseCase(folio.folioNumber) }
            }.awaitAll()

            // All or nothing: a listing missing a folio would understate what the user holds.
            val error = responses.firstNotNullOfOrNull { (_, response) ->
                (response as? NetworkResponse.Error)?.error
            }
            if (error != null) {
                _uiState.value = UiState.Error(error.message)
                return@launch
            }

            _uiState.value = UiState.Success(
                responses.flatMap { (folio, response) ->
                    val funds = (response as NetworkResponse.Success).data
                    funds.forFolio(folio).map { fund ->
                        FundFolioItem(
                            fund = fund,
                            redeemableUnits = folio.redeemableUnits.coerceIn(
                                0.0,
                                fund.balanceUnits.coerceAtLeast(0.0)
                            )
                        )
                    }
                }
            )
        }
    }

    private fun List<FolioFundDomain>.forFolio(folio: HoldingFolioDomain): List<FolioFundDomain> {
        filter { folio.holdingId.isNotBlank() && it.mfHoldingId == folio.holdingId }
            .ifEmpty { null }?.let { return it }
        filter { it.title.trim().equals(fundTitle.trim(), ignoreCase = true) }
            .ifEmpty { null }?.let { return it }
        return this
    }
}
