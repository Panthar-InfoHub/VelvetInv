package org.sharad.velvetinvestment.utils

sealed interface FundFilter {

    val title: String

    data object IndexOnly : FundFilter {
        override val title: String = "Index Only"
    }

    data object FlexiCap : FundFilter {
        override val title: String = "Flexi Cap"
    }

    data object Sectoral : FundFilter {
        override val title: String = "Sectoral"
    }

    data object LargeCap : FundFilter {
        override val title: String = "Large Cap"
    }

    data class Custom(
        override val title: String
    ) : FundFilter
}
