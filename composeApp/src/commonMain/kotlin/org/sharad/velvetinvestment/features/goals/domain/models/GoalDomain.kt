package org.sharad.velvetinvestment.features.goals.domain.models

import org.sharad.velvetinvestment.shared.domain.GoalType

/**
 * A goal the user holds, as `GET /user-goal/` and `GET /user-goal/{id}` return it.
 *
 * The projection fields are the server's own `v2.0` arithmetic. Nothing here is recalculated on
 * the client: two devices showing different SIP figures for the same goal would be worse than
 * showing a figure that is a few hours stale.
 */
data class GoalDomain(
    val id: String,
    val userId: String,
    val goalTypeId: Int,
    val goalType: GoalType?,
    /** Empty when the server titles the goal from the child instead; see [displayName]. */
    val goalName: String,
    val childName: String?,
    val childAge: Int?,
    val assetSubtype: String?,
    val yearsRemaining: Int,
    val targetDate: String?,
    /** Set for every type but "Build My Savings". */
    val currentCost: Double?,
    /** Set only for "Build My Savings". */
    val targetAmount: Double?,
    val currentSavings: Double,
    /** Fractions, as stored: 0.06 is 6%. */
    val inflationRate: Double?,
    val expectedReturnRate: Double?,
    val futureTargetAmount: Double,
    val fvCurrentSavings: Double,
    val netRequiredCorpus: Double,
    val requiredMonthlySip: Double,
    val requiredLumpsumToday: Double,
    val status: String,
    val calculationVersion: String?,
    val createdAt: String?,
    val updatedAt: String?,
    /**
     * The server's `progress_percent`, put through [toProgressPercent] once in the mapper, so
     * home, the goals list and the projection all show (and fill the bar with) the same number.
     */
    val progressPercent: Int,
    /** The holdings mapped to this goal; carried by `GET /user-goal/{id}` only. */
    val holdings: List<GoalHoldingDomain> = emptyList(),
    /** What those mapped holdings are worth today, as the server totals them. */
    val totalHoldingsValue: Double = 0.0
) {
    /** The present-day amount the goal was sized from, whichever field carries it. */
    val baseAmount: Double
        get() = currentCost ?: targetAmount ?: 0.0

    val displayName: String
        get() = goalName.ifBlank {
            childName?.let { child -> "$child's ${goalType?.childGoalNoun ?: "Goal"}" }
                ?: goalType?.displayName
                ?: "Goal"
        }

    /** Taken from `target_date` rather than added to today's year, which can disagree by a day. */
    val targetYear: Int?
        get() = targetDate?.take(4)?.toIntOrNull()

    val isActive: Boolean
        get() = status.equals(ACTIVE, ignoreCase = true)

    companion object {
        const val ACTIVE = "ACTIVE"
    }
}

/**
 * The one rounding rule for a goal's progress, matching Janta Nivesh: a raw percent is truncated
 * towards zero (the server's `0.6` is 0%, `1.9` is 1%) and clamped to 0..100. Every goal screen's
 * label and bar go through this, so no two screens can round the same figure differently.
 */
fun Double.toProgressPercent(): Int =
    if (isNaN()) 0 else toInt().coerceIn(0, 100)

/** [toProgressPercent] for an amount saved against a target, e.g. the goals list total. */
fun goalProgressPercent(saved: Double, target: Double): Int =
    if (target <= 0.0) 0 else (saved / target * 100).toProgressPercent()

/**
 * One holding mapped to a goal. [holdingId] is what `/user-goal/map` and `/user-goal/remove`
 * are keyed by — the folio number is for the user to read, not for the API.
 */
data class GoalHoldingDomain(
    val holdingId: String,
    val fundName: String,
    val folioNumber: String,
    val units: Double,
    val nav: Double,
    val currentValue: Double,
    val imageUrl: String?
)
