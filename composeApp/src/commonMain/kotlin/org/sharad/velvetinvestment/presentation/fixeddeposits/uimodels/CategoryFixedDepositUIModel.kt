package org.sharad.velvetinvestment.presentation.fixeddeposits.uimodels

import org.sharad.velvetinvestment.domain.models.fixeddeposits.RiskLevel

data class CategoryFixedDepositUIModel(
    val categoryName: String,
    val categoryId:String,
    val fds: List<FixedDepositUIModel>
)

data class FixedDepositUIModel(
    val id:String,
    val bankName: String,
    val bankLogoUrl: String,
    val riskLevel: RiskLevel,
    val interest: String,
    val tenures: List<FixedDepositTenureUIModel>,
    val bankTag:String="",
    val currentSort: FDTenureSort = FDTenureSort.TENURE_ASC
)
data class FixedDepositTenureUIModel(
    val tenureText: String,
    val interestText: String,
    val returnText: String,
    val minDays: Int,
    val maxDays: Int,
    val interestRate: Double,
    val returnMin: Long,
    val returnMax: Long
)