package org.sharad.velvetinvestment.presentation.mutualfund

import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundDetailsDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundGraphDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundGraphPointsDomain

data class CategoryMutualFundUI(
    val categoryName: String,
    val categorySearchReference: String,
    val mutualFunds: List<MutualFundUI>
)

data class MutualFundUI(
    val id: String,
    val name: String,
    val icon: String,
    val category: String,
    val remark: String?,
    val riskText: String?,
    val type: String,
    val returnYearsRate: ReturnYearsRateUi
)

data class ReturnYearsRateUi(
    val month3:Double?,
    val month6:Double?,
    val year1:Double?,
    val year3:Double?,
)



sealed interface DetailsState {
    data object Loading : DetailsState
    data class Success(val data: MutualFundDetailsDomain) : DetailsState
    data class Error(val error: String) : DetailsState
}

sealed interface GraphState {
    data object Loading : GraphState
    data class Success(val data: MutualFundGraphDomain) : GraphState
    data class Error(val error: String) : GraphState
}

data class MutualFundScreenState(
    val detailsState: DetailsState=DetailsState.Loading,
    val graphState: GraphState=GraphState.Loading,
    val chartPoints: List<MutualFundGraphPointsDomain> = emptyList()
)

data class StableMetricUi(
    val label: String,
    val value: Double
)