package org.sharad.velvetinvestment.features.portfolio.ui.models


/**
 * The reasons `POST /mf/purchase-plan/{id}/cancel` accepts.
 *
 * [code] is sent verbatim — the gateway rejects anything outside this fixed set — while
 * [label] is what the user reads (English only in Velvet), so the wire value and its wording stay paired here rather
 * than drifting apart in a lookup somewhere else.
 */
enum class SipCancellationReason(
    val code: String,
    val label: String
) {
    AMOUNT_NOT_AVAILABLE(
        "amount_not_available",
        "Funds not available"
    ),
    RETURNS_NOT_AS_EXPECTED(
        "investment_returns_not_as_expected",
        "Returns not as expected"
    ),
    AMC_SUPPORT_NOT_SATISFACTORY(
        "amc_support_not_satisfactory",
        "AMC support not satisfactory"
    ),
    EXIT_LOAD_NOT_AS_EXPECTED(
        "exit_load_not_as_expected",
        "Exit load not as expected"
    ),
    SWITCH_TO_OTHER_SCHEME(
        "switch_to_other_scheme",
        "Switching to another scheme"
    ),
    FUND_MANAGER_CHANGED(
        "fund_manager_changed",
        "Fund manager changed"
    ),
    INVESTMENT_GOAL_COMPLETE(
        "investment_goal_complete",
        "Investment goal achieved"
    ),
    MANDATE_NOT_READY(
        "mandate_not_ready",
        "Mandate not ready"
    ),
    INVEST_LATER(
        "invest_later",
        "Want to invest later"
    ),
    CUSTOMER_SUPPORT_NOT_SATISFACTORY(
        "customer_support_not_satisfactory",
        "Customer support not satisfactory"
    )
}
