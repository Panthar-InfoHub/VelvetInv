package org.sharad.velvetinvestment.utils.fundfiltersystem

fun createInitialInvestmentFilter(): InvestmentFilter {
    return InvestmentFilter(
        groups = listOf(

            // Sort By
            FilterGroup(
                id = "sort",
                title = "Sort By",
                selectionType = SelectionType.SINGLE,
                options = listOf(
                    FilterOption("popularity", "Popularity"),
                    FilterOption("1y", "1Y Returns"),
                    FilterOption("3y", "3Y Returns"),
                    FilterOption("5y", "5Y Returns"),
                    FilterOption("rating", "Rating")
                )
            ),

            // Categories
            FilterGroup(
                id = "category",
                title = "Categories",
                selectionType = SelectionType.SINGLE,
                options = listOf(
                    FilterOption("equity", "Equity"),
                    FilterOption("debt", "Debt"),
                    FilterOption("hybrid", "Hybrid"),
                    FilterOption("commodities", "Commodities")
                )
            ),

            // Risk
            FilterGroup(
                id = "risk",
                title = "Risk",
                selectionType = SelectionType.MULTIPLE,
                options = listOf(
                    FilterOption("low", "Low"),
                    FilterOption("moderately_low", "Moderately Low"),
                    FilterOption("moderate", "Moderate"),
                    FilterOption("moderately_high", "Moderately High"),
                    FilterOption("high", "High"),
                    FilterOption("very_high", "Very High")
                )
            ),
        )
    )
}

fun createInitialFDFilters(): InvestmentFilter {

    return InvestmentFilter(
        groups = listOf(

            FilterGroup(
                id = FDFilterIds.TENURE,
                title = "Tenure",
                selectionType = SelectionType.SINGLE,
                options = listOf(
                    FilterOption(FDFilterIds.TENURE_1Y, "1 Year"),
                    FilterOption(FDFilterIds.TENURE_2Y, "2 Years"),
                    FilterOption(FDFilterIds.TENURE_3Y, "3 Years"),
                    FilterOption(FDFilterIds.TENURE_5Y, "5 Years")
                )
            ),

            FilterGroup(
                id = FDFilterIds.PAYOUT_FREQUENCY,
                title = "Payout Frequency",
                selectionType = SelectionType.SINGLE,
                options = listOf(
                    FilterOption(FDFilterIds.PAYOUT_CUMULATIVE, "Cumulative"),
                    FilterOption(FDFilterIds.PAYOUT_MONTHLY, "Monthly"),
                    FilterOption(FDFilterIds.PAYOUT_QUARTERLY, "Quarterly"),
                    FilterOption(FDFilterIds.PAYOUT_HALF_YEARLY, "Half-Yearly"),
                    FilterOption(FDFilterIds.PAYOUT_YEARLY, "Yearly"),
                    FilterOption(FDFilterIds.PAYOUT_ON_MATURITY, "On Maturity")
                )
            )
        )
    )
}


object FDFilterIds {

    // Groups
    const val TENURE = "tenure"
    const val PAYOUT_FREQUENCY = "payout_frequency"

    // Tenure Options
    const val TENURE_1Y = "1y"
    const val TENURE_2Y = "2y"
    const val TENURE_3Y = "3y"
    const val TENURE_5Y = "5y"

    // Payout Frequency Options
    const val PAYOUT_CUMULATIVE = "CUMULATIVE"
    const val PAYOUT_MONTHLY = "MONTHLY"
    const val PAYOUT_QUARTERLY = "QUARTERLY"
    const val PAYOUT_HALF_YEARLY = "HALF_YEARLY"
    const val PAYOUT_YEARLY = "YEARLY"
    const val PAYOUT_ON_MATURITY = "ON_MATURITY"

    const val CUSTOM = "Custom"
}