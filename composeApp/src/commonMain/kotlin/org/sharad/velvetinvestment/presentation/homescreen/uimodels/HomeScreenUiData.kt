package org.sharad.velvetinvestment.presentation.homescreen.uimodels

import org.sharad.velvetinvestment.features.bottomNavigation.domain.models.GoalsSummaryDomain
import org.sharad.velvetinvestment.domain.models.home.UserWorthCardDomain
import org.sharad.velvetinvestment.shared.domain.models.OnboardingPrompt

data class HomeScreenUiData(
    val name:String="",
    val userWorth: UserWorthCardDomain,
    val fireReport: Double,
    val goals: List<GoalsSummaryDomain>,
    val kycCompletion: Boolean,
    val tradingAccountCompletion: Boolean,
    val hidden: Boolean=true,
    val hasUnreadNotifications: Boolean = false,
    /** Which KYC/onboarding card to show, if any (JN home). */
    val onboardingPrompt: OnboardingPrompt = OnboardingPrompt.NONE,
    /** Shown on the KYC-failed card. */
    val kycFailureReason: String = "",
    /** A reload that keeps the screen up; only the KYC-in-progress card's refresh icon shows it. */
    val isKycRefreshing: Boolean = false,
    /** FIRE steps verified or skipped so far, for the journey card. */
    val fireStepsDone: Int = 0,
    val fireStepsTotal: Int = 5
) {
    /**
     * Every step in `fire_stages` is verified or skipped (5 of 5): the FIRE report card shows;
     * otherwise the "Complete your FIRE journey" card (opens the FIRE onboarding). Worked out from
     * the stages rather than `is_fire_completed`, which the server already sets with goals pending.
     */
    val isFireCompleted: Boolean
        get() = fireStepsDone >= fireStepsTotal
}
