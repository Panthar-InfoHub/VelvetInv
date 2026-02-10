package org.sharad.velvetinvestment.presentation.onboarding.compose.goals

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.bgColor4
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.presentation.onboarding.compose.financialflow.GenericInfoHeader
import org.sharad.velvetinvestment.presentation.onboarding.models.GoalInfo
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.GoalScreenViewModel
import org.sharad.velvetinvestment.shared.compose.AppButton
import org.sharad.velvetinvestment.shared.compose.ContinueBackButtonFooter
import org.sharad.velvetinvestment.utils.DateTimeUtils
import org.sharad.velvetinvestment.utils.formatMoneyWithUnits
import org.sharad.velvetinvestment.utils.genericDropShadow
import org.sharad.velvetinvestment.utils.theme.largeTextStyle
import org.sharad.velvetinvestment.utils.theme.subHeading
import org.sharad.velvetinvestment.utils.theme.subHeadingMedium
import org.sharad.velvetinvestment.utils.theme.titlesStyle
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.icon_cross

@Composable
fun OnBoardingGoalScreen(
    viewModel: GoalScreenViewModel,
    modifier: Modifier = Modifier,
    pv: PaddingValues,
    onNext: () -> Unit,
    onPrev: () -> Unit,
    onAddGoalClick: () -> Unit
){

    val goals by viewModel.goalList.collectAsStateWithLifecycle()
    val totalGoalAmount by viewModel.totalTargetAmount.collectAsStateWithLifecycle()


    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {

        Column(
            modifier=Modifier.fillMaxSize()
        )
        {
            if (goals.isEmpty()) {
                EmptyGoalsScreen(
                    onAddClick = {onAddGoalClick()},
                    modifier= Modifier.weight(1f)
                )
            } else {
                GoalScreenMain(
                    onAddClick = {onAddGoalClick()},
                    modifier = Modifier.weight(1f),
                    goals =goals,
                    onLoanDeleteClick ={ viewModel.deleteGoal(it)  },
                    totalGoalAmount=totalGoalAmount
                )
            }

            ContinueBackButtonFooter(
                onContinue = onNext,
                onBack = onPrev,
                pv = pv,
            )
        }
    }
}

@Composable
fun GoalScreenMain(
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier,
    goals: List<GoalInfo>,
    onLoanDeleteClick: (GoalInfo) -> Unit,
    totalGoalAmount: Long,
) {
    Box(
        modifier=modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    )
    {
        LazyColumn(
            modifier = modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        )
        {

            item {
                GenericInfoHeader(
                    heading = "Financial Goals",
                    subHeading = "Define your key financial aspirations so we can create a roadmap to achieve them"
                )
            }

            items(goals) {
                GoalEntry(
                    goalInfo = it,
                    onDeleteClick = {goal-> onLoanDeleteClick(goal) }
                )
            }

            item {
                GoalSummary(
                    totalGoals = goals.size,
                    totalAmount=totalGoalAmount
                )
            }

            item {
                Spacer(
                    modifier = Modifier.height(48.dp)
                )
            }
        }

        AppButton(
            modifier=Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp),
            text = "Add Loan",
            onClick = {onAddClick()}
        )

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
            modifier = Modifier
                .padding(horizontal =16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Total Goal Value",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )

            Text(
                text= "₹${formatMoneyWithUnits(totalAmount)}",
                style = largeTextStyle,
                color = Primary,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun EmptyGoalsScreen(onAddClick: () -> Unit, modifier: Modifier=Modifier) {
    Column(
        modifier = modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        GenericInfoHeader(
            heading = "Financial Goals",
            subHeading = "Define your key financial aspirations so we can create a roadmap to achieve them"
        )

        Box(
            modifier=Modifier.weight(1f)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ){

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 48.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ){
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