package org.sharad.velvetinvestment.presentation.explorefunds.uimodel

import org.sharad.velvetinvestment.domain.models.fixeddeposits.FixedDepositDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundDomain

fun MutualFundDomain.toTopPickUi() =
    MutualFundTopPicksUiModel(
        icon = icon,
        name = name,
        metadata = (riskText?.let { "$it . " } ?:"") + category+" . " + type ,
        returnYears = 1,
        percentage = returnYearsRate.year1,
        id =id
    )

fun FixedDepositDomain.toTopPickUi() =
    FixedTopPicksUiModel(
        icon = bankLogoUrl,
        name = bankName,
        metadata = bankTag,
        percentage = baseInterest,
        id=id
    )
