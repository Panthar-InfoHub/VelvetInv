package org.sharad.velvetinvestment.presentation.mutualfund

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
    val amount: String,
    val remark: String?,
    val rating: Int?,
    val returnYear: Int,
    val type: String,
    val percentage:Double
)
