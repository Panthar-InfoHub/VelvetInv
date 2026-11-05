package org.sharad.velvetinvestment.features.goals.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.features.bottomNavigation.domain.models.GoalsSummaryDomain
import org.sharad.velvetinvestment.features.goals.domain.models.goalOptionFor
import org.sharad.velvetinvestment.features.goals.ui.viewmodels.YourGoalsEvent
import org.sharad.velvetinvestment.features.goals.ui.viewmodels.YourGoalsUiData
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.EmptyFundScreen
import org.sharad.velvetinvestment.shared.compose.NextButtonFooter
import org.sharad.velvetinvestment.shared.genericDropShadow
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import org.sharad.velvetinvestment.shared.theme.titlesStyle
import org.sharad.velvetinvestment.utils.withInterRupee
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.plus_icon
import velvet.composeapp.generated.resources.upward_trend_arrow

/**
 * The user's goals, read from `GET /user-goal/`. Velvet's goals list (header, goal cards, footer
 * button) with JN's behaviour: an overall progress card, an empty state, a "+" in the header, and a
 * footer that invests once there is something to invest towards.
 */
@Composable
fun YourGoalsScreen(
    state: YourGoalsUiData,
    handleEvent: (YourGoalsEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        BackHeader(
            title = "Your Goals",
            showBack = true,
            onBack = { handleEvent(YourGoalsEvent.OnBackClicked) },
            rightContent = {
                IconButton(onClick = { handleEvent(YourGoalsEvent.OnAddGoalClicked) }) {
                    Icon(
                        painter = painterResource(Res.drawable.plus_icon),
                        contentDescription = "Add goal",
                        tint = Primary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        )

        Box(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            if (state.goals.isEmpty()) {
                EmptyFundScreen(
                    title = "No goals yet",
                    subtitle = "Set clear financial goals and take small steps each month to achieve them!",
                    buttonText = "",
                    onActionClick = {}
                )
            } else {
                YourGoalsContent(
                    state = state,
                    onGoalClick = { handleEvent(YourGoalsEvent.OnGoalCardClicked(it)) }
                )
            }
        }

        NextButtonFooter(
            value = if (state.goals.isNotEmpty()) "Invest Now" else "Add Goal",
            onClick = {
                if (state.goals.isEmpty()) {
                    handleEvent(YourGoalsEvent.OnAddGoalClicked)
                } else {
                    handleEvent(YourGoalsEvent.OnInvestNowClicked)
                }
            }
        )
    }
}

@Composable
private fun YourGoalsContent(
    state: YourGoalsUiData,
    onGoalClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        item {
            TotalGoalProgressCard(
                progressAmount = state.totalGoalProgressAmt,
                targetAmount = state.goalTargetAmt,
                percentage = state.goalPercentage
            )
        }
        items(items = state.goals, key = { it.goalId }) { goal ->
            GoalCard(goal = goal, onClick = { onGoalClick(goal.goalId) })
        }
    }
}

/** Saved so far across every goal, against what they add up to. */
@Composable
private fun TotalGoalProgressCard(
    progressAmount: String,
    targetAmount: String,
    percentage: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .genericDropShadow()
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White, RoundedCornerShape(10.dp))
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Total Goal Progress",
                style = titlesStyle,
                color = titleColor
            )
            Text(
                text = "₹ $progressAmount".withInterRupee(),
                style = MaterialTheme.typography.headlineLarge,
                color = Primary
            )
        }

        Column(
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "Target: ₹ $targetAmount".withInterRupee(),
                style = titlesStyle,
                color = titleColor,
                textAlign = TextAlign.End
            )
            Row(
                modifier = Modifier
                    .clip(CircleShape)
                    .background(Secondary.copy(alpha = 0.15f))
                    .padding(vertical = 4.dp, horizontal = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    painter = painterResource(Res.drawable.upward_trend_arrow),
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(12.dp)
                )
                Text(
                    text = "$percentage%",
                    style = titlesStyle,
                    fontWeight = FontWeight.SemiBold,
                    color = Primary
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun YourGoalsScreenPreview() {
    val goals = listOf(
        GoalsSummaryDomain(
            goalId = "goal_1",
            goalTypes = goalOptionFor(1, "Aarav's Education"),
            title = "Aarav's Education",
            amount = 100_000,
            targetAmount = 1_790_847,
            progressPercent = 6
        ),
        GoalsSummaryDomain(
            goalId = "goal_2",
            goalTypes = goalOptionFor(3, "My Dream Villa"),
            title = "My Dream Villa",
            amount = 500_000,
            targetAmount = 7_035_502,
            progressPercent = 7
        ),
        GoalsSummaryDomain(
            goalId = "goal_3",
            goalTypes = goalOptionFor(5, "Emergency Cushion"),
            title = "Emergency Cushion",
            amount = 200_000,
            targetAmount = 1_000_000,
            progressPercent = 20
        )
    )
    VelvetTheme {
        YourGoalsScreen(
            state = YourGoalsUiData(
                totalGoalProgressAmt = "8,00,000",
                goalTargetAmt = "98,26,349",
                goalPercentage = "8",
                goals = goals
            ),
            handleEvent = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun YourGoalsEmptyPreview() {
    VelvetTheme {
        YourGoalsScreen(state = YourGoalsUiData(), handleEvent = {})
    }
}
