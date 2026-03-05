package org.sharad.velvetinvestment.presentation.fixeddeposits.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.domain.usecases.fixeddepositusecases.GetTopPickFDUseCase
import org.sharad.velvetinvestment.presentation.fixeddeposits.uimodels.CategoryFixedDepositUIModel
import org.sharad.velvetinvestment.presentation.fixeddeposits.uimodels.FDTenureSort
import org.sharad.velvetinvestment.presentation.fixeddeposits.uimodels.toUI
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess

class FDCategoryViewModel(
    private val getTopPickFDUseCase: GetTopPickFDUseCase
) : ViewModel() {

    private val _uiState =
        MutableStateFlow<UiState<List<CategoryFixedDepositUIModel>>>(UiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _searchText=MutableStateFlow("")
    val searchText= _searchText.asStateFlow()


    init {
        loadTopPicks()
    }

    private fun loadTopPicks() {

        viewModelScope.launch {
            _uiState.value = UiState.Loading
            getTopPickFDUseCase().onSuccess { categories ->
                val ui = categories.map { category ->
                    CategoryFixedDepositUIModel(
                        categoryName = category.categoryName,
                        categoryId = category.categorySearchReference,
                        fds = category.fixedDeposits.map { it.toUI() }
                    )
                }
                _uiState.value = UiState.Success(ui)
            }.onError {
                _uiState.value = UiState.Error("Failed to load FD data")
            }
        }
    }

    fun reorderTenures(sort: FDTenureSort) {
        val current = _uiState.value
        if (current !is UiState.Success) return
        val updated = current.data.map { category ->
            category.copy(
                fds = category.fds.map { fd ->
                    val sortedTenures = when (sort) {
                        FDTenureSort.TENURE_ASC ->
                            fd.tenures.sortedBy { it.minDays }

                        FDTenureSort.TENURE_DESC ->
                            fd.tenures.sortedByDescending { it.maxDays }

                        FDTenureSort.INTEREST_ASC ->
                            fd.tenures.sortedBy { it.interestRate }

                        FDTenureSort.INTEREST_DESC ->
                            fd.tenures.sortedByDescending { it.interestRate }

                        FDTenureSort.RETURNS_ASC ->
                            fd.tenures.sortedBy { it.returnMin }

                        FDTenureSort.RETURNS_DESC ->
                            fd.tenures.sortedByDescending { it.returnMax }
                    }
                    fd.copy(tenures = sortedTenures)
                }
            )
        }
        _uiState.value = UiState.Success(updated)
    }

    fun onSearchTextChange(text:String){
        _searchText.value=text
    }
}