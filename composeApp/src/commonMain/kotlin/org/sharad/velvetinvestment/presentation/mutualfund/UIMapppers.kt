package org.sharad.velvetinvestment.presentation.mutualfund

import androidx.compose.ui.graphics.Color
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.bgColor4
import org.sharad.velvetinvestment.domain.models.mutualfunds.AssetsAllocationDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.CategoryMutualFundDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundDomain
import org.sharad.velvetinvestment.shared.PieChartEntry
import org.sharad.velvetinvestment.utils.trimDoubleTo

fun CategoryMutualFundDomain.toUI(): CategoryMutualFundUI {
    return CategoryMutualFundUI(
        categoryName = categoryName,
        categorySearchReference = categorySearchReference,
        mutualFunds = mutualFunds.map { it.toUI() }
    )
}

fun MutualFundDomain.toUI(): MutualFundUI {
    return MutualFundUI(
        id = id,
        name = name,
        icon = icon,
        category = category,
        amount = amount,
        remark = remark,
        rating = rating,
        returnYear = returnYear,
        type = type,
        percentage=percentage.trimDoubleTo(2)
    )
}

fun AssetsAllocationDomain.toPieChartEntries(
    primary: Color= Primary,
    secondary: Color=Secondary,
    tertiary: Color= bgColor4
): List<PieChartEntry> {
    return listOf(
        PieChartEntry(
            value = equity.toFloat(),
            color = primary
        ),
        PieChartEntry(
            value = debt.toFloat(),
            color = secondary
        ),
        PieChartEntry(
            value = cash.toFloat(),
            color = tertiary
        )
    )
}
