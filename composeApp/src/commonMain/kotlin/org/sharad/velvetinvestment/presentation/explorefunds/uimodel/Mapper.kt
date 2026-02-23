package org.sharad.velvetinvestment.presentation.explorefunds.uimodel

import org.sharad.velvetinvestment.domain.models.explore.FixedDepositTopPicksDomain
import org.sharad.velvetinvestment.domain.models.explore.MutualFundTopPicksDomain

fun MutualFundTopPicksDomain.toUi() =
    MutualFundTopPicksUiModel(
        icon = icon,
        name = name,
        metadata = metadata,
        returnYears = returnYears,
        percentage = percentage,
        id=id
    )

fun FixedDepositTopPicksDomain.toUi() =
    FixedTopPicksUiModel(
        icon = icon,
        name = name,
        metadata = metadata,
        returnYears = returnYears,
        percentage = percentage,
        id=id
    )
