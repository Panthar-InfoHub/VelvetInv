package org.sharad.velvetinvestment.features.goals.data.mapper

import kotlinx.serialization.json.Json
import org.sharad.velvetinvestment.shared.data.local.mapper.toGoalSummary
import org.sharad.velvetinvestment.shared.data.remote.model.userdata.UserDataDto
import org.sharad.velvetinvestment.features.goals.data.remote.model.usergoal.UserGoalsListDto
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * `GET /user/` (Home, under `user_goals`) and `GET /user-goal/` (Your Goals) both send
 * `total_current_value` on every goal row. Both decode into the shared `UserGoal`, so the card's
 * first figure must come out the same from either response.
 */
class GoalCurrentValueResponsesTest {

    // Same settings as the app's HttpClient.
    private val json = Json {
        isLenient = true
        ignoreUnknownKeys = true
    }

    // Trimmed from a real row; current_savings is "0" while the goal is worth 631.98 today.
    private val goalRow = """
        {
          "id": "goal-1",
          "goal_type_id": 3,
          "goal_name": "my dream home",
          "current_cost": "10000",
          "current_savings": "0",
          "future_target_amount": "10500",
          "status": "ACTIVE",
          "calculation_version": "v2.0",
          "holdings": [],
          "total_holdings_value": 631.98,
          "total_invested_amount": 630,
          "total_current_value": 631.98,
          "progress_percent": 6
        }
    """.trimIndent()

    @Test
    fun homeUserDataCarriesTotalCurrentValueToTheGoalCard() {
        val userData = json.decodeFromString<UserDataDto>(
            """{ "code": 200, "message": "ok", "data": { "id": "user-1", "user_goals": [ $goalRow ] } }"""
        )

        val card = userData.data.user_goals.single().toGoalSummary()

        assertEquals(631L, card.amount)
        assertEquals(10_500L, card.targetAmount)
    }

    @Test
    fun yourGoalsListCarriesTotalCurrentValueToTheGoalCard() {
        val list = json.decodeFromString<UserGoalsListDto>(
            """{ "success": true, "message": "ok", "data": [ $goalRow ] }"""
        )

        val card = list.data.single().toDomain().toSummary()

        assertEquals(631L, card.amount)
        assertEquals(10_500L, card.targetAmount)
    }
}
