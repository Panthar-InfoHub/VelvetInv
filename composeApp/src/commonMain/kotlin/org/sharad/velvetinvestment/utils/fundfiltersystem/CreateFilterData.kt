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

            // Rating
            FilterGroup(
                id = "rating",
                title = "Rating",
                selectionType = SelectionType.SINGLE,
                options = listOf(
                    FilterOption("5", "5★"),
                    FilterOption("4", "4★"),
                    FilterOption("3", "3★"),
                    FilterOption("2", "2★"),
                    FilterOption("1", "1★")
                )
            )
        )
    )
}
