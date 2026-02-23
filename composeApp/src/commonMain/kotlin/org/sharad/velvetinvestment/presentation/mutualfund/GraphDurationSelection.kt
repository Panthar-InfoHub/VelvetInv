package org.sharad.velvetinvestment.presentation.mutualfund

enum class GraphDurationSelection(
    val id: String,
    val label: String
) {

    OneMonth("1m", "1M"),
    SixMonths("6m", "6M"),
    OneYear("1y", "1Y"),
    ThreeYears("3y", "3Y"),
    FiveYears("5y", "5Y"),
    All("all", "All");

}

