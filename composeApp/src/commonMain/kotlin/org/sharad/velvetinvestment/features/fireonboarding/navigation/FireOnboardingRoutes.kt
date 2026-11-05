package org.sharad.velvetinvestment.features.fireonboarding.navigation

import kotlinx.serialization.Serializable
import org.sharad.velvetinvestment.features.fireonboarding.domain.models.FireStage

/**
 * Destinations of the FIRE onboarding and FIRE report, kept inside the feature apart from
 * Velvet's app-level `Route`. [FireOnboardingFlow] and the report routes sit in the main app's
 * NavHost; the step routes live in the flow's own nested NavHost.
 */
object FireOnboardingRoute {

    /**
     * The whole FIRE onboarding (finance → assets → loan → insurance → goals) in its own NavHost.
     * It opens on the first step `fire_stages` still has pending and closes itself once none is.
     */
    @Serializable
    data object FireOnboardingFlow

    /** Inner start: reading `GET /user/` to find the step to open. */
    @Serializable
    data object Resolving
    @Serializable
    data object FinancialFlow
    @Serializable
    data object CurrentAssets
    @Serializable
    data object Loan
    @Serializable
    data object AddLoan
    @Serializable
    data object InsuranceCoverage
    @Serializable
    data object Goal
    @Serializable
    data object GoalAdd

    @Serializable
    data object FireReport
    @Serializable
    data object FireReportUpdateOptions
    @Serializable
    data object FinancialFlowEdit
    @Serializable
    data object InsuranceCoverageEdit
    @Serializable
    data object CurrentAssetEdit
}

/** The nested-NavHost route that shows [this] step. */
val FireStage.route: Any
    get() = when (this) {
        FireStage.FINANCE -> FireOnboardingRoute.FinancialFlow
        FireStage.ASSETS -> FireOnboardingRoute.CurrentAssets
        FireStage.LOAN -> FireOnboardingRoute.Loan
        FireStage.INSURANCE -> FireOnboardingRoute.InsuranceCoverage
        FireStage.GOALS -> FireOnboardingRoute.Goal
    }
