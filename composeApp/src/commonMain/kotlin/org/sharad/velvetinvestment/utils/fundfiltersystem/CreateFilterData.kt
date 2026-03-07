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

fun createInitialFDFilters(): InvestmentFilter {

    return InvestmentFilter(
        groups = listOf(

            FilterGroup(
                id = "filters",
                title = "Filters",
                selectionType = SelectionType.MULTIPLE,
                options = listOf(
                    FilterOption(
                        id = "senior_rates",
                        title = "Senior citizen rates\nRate for user aged 60 and above"
                    ),
                    FilterOption(
                        id = "insured_rbi",
                        title = "Insured by RBI\nUp to ₹5L"
                    ),
                    FilterOption(
                        id = "tax_saver",
                        title = "Tax saver FDs\nFD eligible under 80C for tax saving"
                    ),
                    FilterOption(
                        id = "low_min_investment",
                        title = "Low min. investment\nCreate FDs with less than ₹2k"
                    )
                )
            ),

            // Tenure
            FilterGroup(
                id = "tenure",
                title = "Tenure",
                selectionType = SelectionType.SINGLE,
                options = listOf(
                    FilterOption("lt_1y", "< 1 year"),
                    FilterOption("1_3y", "1-3 years"),
                    FilterOption("3_5y", "3-5 years"),
                    FilterOption("gt_5y", "> 5 years")
                )
            ),

            // Risk Level
            FilterGroup(
                id = "risk_level",
                title = "Risk Level",
                selectionType = SelectionType.SINGLE,
                options = listOf(
                    FilterOption(
                        "low",
                        "Low\nCRISIL rating AAA"
                    ),
                    FilterOption(
                        "moderate",
                        "Moderate\nCRISIL rating AA and AA"
                    ),
                    FilterOption(
                        "high",
                        "High\nCRISIL category B"
                    ),
                    FilterOption(
                        "very_high",
                        "Very high\nCRISIL category C and D"
                    )
                )
            ),

            FilterGroup(
                id = "issuer_type",
                title = "Issuer type",
                selectionType = SelectionType.SINGLE,
                options = listOf(
                    FilterOption("public_bank", "Public banks"),
                    FilterOption("nbfc", "NBFC"),
                    FilterOption("private_bank", "Private banks"),
                    FilterOption("small_finance", "Small finance")
                )
            )
        )
    )
}
