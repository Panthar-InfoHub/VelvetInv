package org.sharad.velvetinvestment.features.fireonboarding.domain.models

import org.sharad.velvetinvestment.shared.domain.models.OnboardingStatusDomain

/**
 * The FIRE onboarding steps, in the order they are asked. [key] is the step's key in
 * `onboarding.fire_stages`; [path] is its submit/skip endpoint.
 */
enum class FireStage(val key: String, val path: String, val title: String) {
    FINANCE("finance", "/onboarding/finance", "Financial Flow"),
    ASSETS("assets", "/onboarding/assets", "Current Assets"),
    LOAN("loan", "/onboarding/loans", "Loans & Debts"),
    INSURANCE("insurance", "/onboarding/insurance", "Insurance Coverage"),
    GOALS("goals", "/onboarding/goals", "Financial Goals");

    /** 1-based, for the header's progress bar. */
    val stepNumber: Int get() = ordinal + 1

    companion object {
        val total: Int get() = entries.size
    }
}

/** This step's status in `fire_stages` (`PENDING` / `VERIFIED` / `SKIPPED`). */
fun OnboardingStatusDomain.statusOf(stage: FireStage): String = when (stage) {
    FireStage.FINANCE -> financeStatus
    FireStage.ASSETS -> assetsStatus
    FireStage.LOAN -> loanStatus
    FireStage.INSURANCE -> insuranceStatus
    FireStage.GOALS -> goalsStatus
}

/**
 * Whether the step still has to be shown. Only a filled-in (`VERIFIED`) or `SKIPPED` step is
 * settled; `PENDING` — and a status the server left out — means it is still owed.
 */
fun OnboardingStatusDomain.isPending(stage: FireStage): Boolean {
    val status = statusOf(stage)
    return !status.equals(OnboardingStatusDomain.VERIFIED, ignoreCase = true) &&
            !status.equals(OnboardingStatusDomain.SKIPPED, ignoreCase = true)
}

/**
 * The step to show next: walking finance → assets → loan → insurance → goals, the first one
 * still pending. Null once every step is verified or skipped.
 */
fun OnboardingStatusDomain.nextFireStage(): FireStage? =
    FireStage.entries.firstOrNull { isPending(it) }
