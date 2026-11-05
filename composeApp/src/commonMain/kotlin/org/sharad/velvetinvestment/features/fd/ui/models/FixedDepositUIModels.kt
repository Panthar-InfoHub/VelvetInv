package org.sharad.velvetinvestment.features.fd.ui.models

import org.sharad.velvetinvestment.features.fd.domain.model.RiskLevel
import org.sharad.velvetinvestment.features.fd.ui.viewmodels.FDTenureUiModel
import org.sharad.velvetinvestment.utils.formatMoneyAfterL
import kotlin.math.round

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
)
/** A row of the interest-rates table: JN's computed slab in Velvet's text format. */
fun FDTenureUiModel.toUIModel(): FixedDepositTenureUIModel {
    val formattedRate = round(interestRate * 100) / 100
    return FixedDepositTenureUIModel(
        tenureText = tenureLabel.replace(
            Regex("\\bMonth\\b", RegexOption.IGNORE_CASE),
            "Months"
        ),
        interestText = "${formattedRate.format2()}%",
        returnText = formatMoneyAfterL(maturityAmount)
    )
}

/** Always two decimals: 7.5 → "7.50". */
fun Double.format2(): String {
    val rounded = round(this * 100) / 100
    val parts = rounded.toString().split('.')
    return if (parts.size == 1) {
        "${parts[0]}.00"
    } else {
        "${parts[0]}.${parts[1].padEnd(2, '0').take(2)}"
    }
}
