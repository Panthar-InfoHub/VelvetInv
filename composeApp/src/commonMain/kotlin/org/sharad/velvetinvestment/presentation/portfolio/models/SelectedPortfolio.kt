package org.sharad.velvetinvestment.presentation.portfolio.models

sealed interface SelectedPortfolio {
    data object MutualFunds: SelectedPortfolio
    data object FixedDeposits: SelectedPortfolio

    companion object {
        val tabs = listOf(MutualFunds, FixedDeposits)
    }
}

fun SelectedPortfolio.label(): String = when (this) {
    SelectedPortfolio.MutualFunds -> "Mutual Funds"
    SelectedPortfolio.FixedDeposits -> "Fixed Deposits"
}
