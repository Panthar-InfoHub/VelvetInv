package org.sharad.velvetinvestment.presentation.mutualfund

import org.sharad.velvetinvestment.domain.models.mutualfunds.CategoryMutualFundDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundDomain
import org.sharad.velvetinvestment.utils.trimDoubleTo
import org.sharad.velvetinvestment.utils.trimTo

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
