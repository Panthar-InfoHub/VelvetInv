package org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.compose.goals

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.bgColor4
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.compose.financialflow.GenericInfoHeader
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel.FireGoalDraft
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel.FireGoalsEvent
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel.FireGoalsUiState
import org.sharad.velvetinvestment.features.goals.domain.models.CreateGoalRequest
import org.sharad.velvetinvestment.features.goals.domain.models.goalOptionFor
import org.sharad.velvetinvestment.shared.compose.AppButton
import org.sharad.velvetinvestment.shared.compose.ContinueBackButtonFooter
import org.sharad.velvetinvestment.shared.compose.ShadowCard
import org.sharad.velvetinvestment.shared.domain.GoalType
import org.sharad.velvetinvestment.shared.theme.Poppins
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import org.sharad.velvetinvestment.shared.theme.largeTextStyle
import org.sharad.velvetinvestment.shared.theme.titlesStyle
import org.sharad.velvetinvestment.utils.AppBackHandler
import org.sharad.velvetinvestment.utils.DateTimeUtils
import org.sharad.velvetinvestment.utils.formatMoneyWithUnits
import org.sharad.velvetinvestment.utils.withInterRupee
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.delete_box

/**
 * The FIRE goals step: the goals added so far (none is created until Continue sends them all in
 * one `POST /onboarding/goals`), Add Goal, and the Continue / Back footer.
 */
@Composable
fun OnBoardingGoalScreen(
    state: FireGoalsUiState,
    onEvent: (FireGoalsEvent) -> Unit,
    modifier: Modifier = Modifier,
    pv: PaddingValues,
    onPrev: () -> Unit,
    onAddGoalClick: () -> Unit,
    /** The flow is skipping or moving on; the footer waits. */
    busy: Boolean = false
) {
    AppBackHandler(true) {
        onPrev()
    }

    Box(
        modifier = modifier.fillMaxSize().imePadding(),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            if (state.goals.isEmpty()) {
                EmptyGoalsScreen(
                    onAddClick = onAddGoalClick,
                    modifier = Modifier.weight(1f)
                )
            } else {
                GoalScreenMain(
                    onAddClick = onAddGoalClick,
                    modifier = Modifier.weight(1f),
                    goals = state.goals,
                    onDeleteClick = { onEvent(FireGoalsEvent.OnDeleteGoalClicked(it.id)) },
                    totalGoalAmount = state.totalTargetAmount
                )
            }

            ContinueBackButtonFooter(
                onContinue = { onEvent(FireGoalsEvent.OnSubmitClicked) },
                onBack = onPrev,
                pv = pv,
                enabled = state.canSubmit && !busy,
                loading = state.isSubmitting || busy
            )
        }
    }
}

@Composable
fun GoalScreenMain(
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier,
    goals: List<FireGoalDraft>,
    onDeleteClick: (FireGoalDraft) -> Unit,
    totalGoalAmount: Long
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                GenericInfoHeader(
                    heading = "Financial Goals",
                    subHeading = "Define your key financial aspirations so we can create a roadmap to achieve them"
                )
            }

            item { GoalSummary(totalGoals = goals.size, totalAmount = totalGoalAmount) }

            items(goals, key = { it.id }) {
                FireGoalDraftCard(goal = it, onDeleteClick = { onDeleteClick(it) })
            }

            item { Spacer(modifier = Modifier.height(64.dp)) }
        }

        AppButton(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp),
            text = "Add Goal",
            onClick = onAddClick
        )
    }
}

/** One goal waiting to be sent, in the look of Velvet's onboarding goal card. */
@Composable
fun FireGoalDraftCard(
    goal: FireGoalDraft,
    onDeleteClick: () -> Unit
) {
    val request = goal.request
    val presentValue = (request.targetAmount ?: request.currentCost ?: 0.0).toLong()
    val projection = goal.projection
    val targetYear = DateTimeUtils.getCurrentYear() + request.yearsRemaining

    ShadowCard(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = goal.title,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        color = Primary
                    )
                    Text(
                        text = goal.option.title,
                        style = titlesStyle.copy(fontSize = 12.sp),
                        color = titleColor
                    )
                }
                ClickableIcon(onClick = onDeleteClick, icon = Res.drawable.delete_box)
            }

            HorizontalDivider()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    EntryPair(
                        if (request.targetAmount != null) "Target Amount" else "Present Value",
                        "₹${formatMoneyWithUnits(presentValue)}"
                    )
                    EntryPair("Target Year", targetYear.toString())
                }
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.weight(1f)
                ) {
                    EntryPair(
                        "Future Value",
                        projection?.let { "₹${formatMoneyWithUnits(it.futureTargetAmount.toLong())}" } ?: "—",
                        horizontal = Alignment.End
                    )
                    EntryPair(
                        "Required SIP",
                        projection?.let { "₹${formatMoneyWithUnits(it.requiredMonthlySip.toLong())}/month" } ?: "—",
                        horizontal = Alignment.End
                    )
                }
            }
        }
    }
}

@Composable
fun GoalSummary(totalGoals: Int, totalAmount: Long) {
    Box(
        modifier = Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(bgColor4.copy(alpha = 0.1f), RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = if (totalGoals == 1) "Total Goal Value (1 goal)" else "Total Goal Value ($totalGoals goals)",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )
            Text(
                text = "₹${formatMoneyWithUnits(totalAmount)}".withInterRupee(),
                style = largeTextStyle,
                color = Primary,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun EmptyGoalsScreen(onAddClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        GenericInfoHeader(
            heading = "Financial Goals",
            subHeading = "Define your key financial aspirations so we can create a roadmap to achieve them"
        )

        Box(
            modifier = Modifier.weight(1f).fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 48.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "No Goals Added yet",
                        style = MaterialTheme.typography.headlineLarge,
                        color = Primary,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "Add your goals to get a complete financial picture",
                        style = titlesStyle,
                        color = titleColor,
                        textAlign = TextAlign.Center
                    )
                }

                AppButton(
                    text = "Add Goals",
                    onClick = onAddClick,
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xffffff)
@Composable
private fun OnBoardingGoalScreenPreview() {
    VelvetTheme {
        OnBoardingGoalScreen(
            state = FireGoalsUiState(
                goals = listOf(
                    FireGoalDraft(
                        id = 1,
                        request = CreateGoalRequest(
                            goalType = GoalType.BuyVehicle,
                            goalName = "Buy a Car",
                            assetSubtype = "CAR",
                            yearsRemaining = 3,
                            currentCost = 1_200_000.0,
                            currentSavings = 100_000.0
                        ),
                        option = goalOptionFor(GoalType.BuyVehicle.id),
                        title = "Buy a Car",
                        projection = null
                    )
                )
            ),
            onEvent = {},
            pv = PaddingValues(0.dp),
            onPrev = {},
            onAddGoalClick = {}
        )
    }
}
