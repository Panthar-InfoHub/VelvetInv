package org.sharad.velvetinvestment.features.portfolio.ui.models

sealed interface SelectedPortfolio {
    data object Dashboard: SelectedPortfolio
    data object MutualFunds: SelectedPortfolio
    data object ActiveSIP: SelectedPortfolio
    data object FixedDeposits: SelectedPortfolio

    companion object {
        /** The pager reads its page count from this list, so order here is page order there. */
        // FD-HIDDEN: FixedDeposits tab dropped while FD is "coming soon". To restore, swap back to
        // the commented line. See docs/FD_HIDE_RESTORE.md.
        // val tabs = listOf(Dashboard, MutualFunds, ActiveSIP, FixedDeposits)
        val tabs = listOf(Dashboard, MutualFunds, ActiveSIP)
    }
}

fun SelectedPortfolio.label(): String = when (this) {
    SelectedPortfolio.Dashboard -> "Dashboard"
    SelectedPortfolio.MutualFunds -> "Mutual Funds"
    SelectedPortfolio.ActiveSIP -> "Active SIP"
    SelectedPortfolio.FixedDeposits -> "Fixed Deposits"
}
