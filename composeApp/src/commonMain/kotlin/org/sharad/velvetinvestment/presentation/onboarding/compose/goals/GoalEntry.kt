package org.sharad.velvetinvestment.presentation.onboarding.compose.goals

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.velvetinvestment.domain.models.goals.GoalRequest
import org.sharad.velvetinvestment.shared.compose.ShadowCard
import org.sharad.velvetinvestment.utils.DateTimeUtils
import org.sharad.velvetinvestment.utils.GoalUtils.GoalCalculator
import org.sharad.velvetinvestment.utils.GoalUtils.getGoalInputs
import org.sharad.velvetinvestment.utils.Log
import org.sharad.velvetinvestment.utils.formatMoneyWithUnits
import org.sharad.velvetinvestment.shared.theme.Poppins
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.delete_box
import velvet.composeapp.generated.resources.info_icon


data class RetirementGoalResult(
    val monthlyExpense: Double,
    val retirementCorpus: Double,
    val retirementAge: Int,
    val sip: Double
)

@Composable
fun GoalEntry(
    goalInfo: GoalRequest,
    onDeleteClick: (GoalRequest) -> Unit,
    showDelete: Boolean = true,
    showInfoIcon: Boolean=true,
    dob: Long?
) {
    val dobYear = dob?.let { DateTimeUtils.getYear(it) }
        ?: DateTimeUtils.getCurrentYear()

    when (goalInfo) {
        is GoalRequest.Retirement -> {
            RetirementGoalCard(
                goalInfo = goalInfo,
                dobYear = dobYear,
                onDeleteClick = onDeleteClick,
                showDelete = showDelete,
                showInfoIcon=showInfoIcon
            )
        }

        else -> {
            NormalGoalCard(
                goalInfo = goalInfo,
                dobYear = dobYear,
                onDeleteClick = onDeleteClick,
                showDelete = showDelete
            )
        }
    }
}
@Composable
fun RetirementGoalCard(
    goalInfo: GoalRequest.Retirement,
    dobYear: Int,
    onDeleteClick: (GoalRequest) -> Unit,
    showDelete: Boolean,
    showInfoIcon: Boolean
) {

    val result = remember(goalInfo) {
        GoalCalculator.calculateRetirementFromInputs(
            currentAge = DateTimeUtils.getCurrentYear() - dobYear,
            retirementAge = goalInfo.retirementAge,
            lifeExpectancy = goalInfo.lifeExpectancy,
            monthlyExpense = goalInfo.currentMonthlyExpense.toDouble(),
            inflationRate = goalInfo.inflationRate.toDouble()/100,
            postRetirementReturn = goalInfo.postRetirementReturn.toDouble()/100,
            preRetirementReturn = goalInfo.returnRate.toDouble()/100
        )
    }

    LaunchedEffect(result) {

        Log(
            "RETIREMENT_DEBUG",
            """
        -------- RETIREMENT DEBUG --------

        📌 RAW INPUT (GoalRequest):
        currentMonthlyExpense: ${goalInfo.currentMonthlyExpense}
        retirementAge: ${goalInfo.retirementAge}
        lifeExpectancy: ${goalInfo.lifeExpectancy}
        inflationRate: ${goalInfo.inflationRate}
        returnRate: ${goalInfo.returnRate}

        📌 USER:
        dobYear: $dobYear
        currentAge: ${DateTimeUtils.getCurrentYear() - dobYear}

        📌 RESULT (Calculator Output):
        monthlyExpense: ${result.monthlyExpense}
        retirementCorpus: ${result.retirementCorpus}
        retirementAge: ${result.retirementAge}
        sip: ${result.sip}

        ----------------------------------
        """.trimIndent()
        )
    }
    ShadowCard(modifier = Modifier.fillMaxWidth()) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Retirement Goals",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = Primary
                )

                Row {
                    if (showInfoIcon){ ClickableIcon({}, Res.drawable.info_icon) }
                    if (showDelete){ ClickableIcon({ onDeleteClick(goalInfo) }, Res.drawable.delete_box) }
                }
            }

            HorizontalDivider()

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Column(verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.weight(1f),
                    ) {

                    EntryPair(
                        "Current Monthly Expenses",
                        "₹${formatMoneyWithUnits(result.monthlyExpense.toLong())}"
                    )

                    EntryPair(
                        "Retirement Age",
                        result.retirementAge.toString()
                    )
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
//                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.weight(1f),

                ) {

                    EntryPair(
                        "Retirement Corpus Required",
                        "₹${formatMoneyWithUnits(result.retirementCorpus.toLong())}"
                    )

                    EntryPair(
                        "Required SIP",
                        "₹${formatMoneyWithUnits(result.sip.toLong())}/month"
                    )
                }
            }
        }
    }
}

@Composable
fun NormalGoalCard(
    goalInfo: GoalRequest,
    dobYear: Int,
    onDeleteClick: (GoalRequest) -> Unit,
    showDelete: Boolean
) {

    val (presentValue, inflation, years) = goalInfo.getGoalInputs()

    val futureValue = remember(goalInfo) {
        GoalCalculator.calculateFutureValue(
            presentValue,
            inflation,
            years
        )
    }

    val sip = remember(goalInfo) {
        GoalCalculator.calculateSip(
            futureValue,
            goalInfo.returnRate,
            years
        )
    }

    ShadowCard(modifier = Modifier.fillMaxWidth()) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = goalInfo.title,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                    color = Primary
                )

                Row {
                    ClickableIcon({}, Res.drawable.info_icon)
                    if (showDelete){ ClickableIcon({ onDeleteClick(goalInfo) }, Res.drawable.delete_box) }
                }
            }

            HorizontalDivider()

            Text(
                text = when (goalInfo) {
                    is GoalRequest.ChildEducation -> "${goalInfo.childName.split(" ").first()}’s Education Goal"
                    is GoalRequest.ChildMarriage -> "${goalInfo.childName.split(" ").first()}’s Marriage Goal"
                    is GoalRequest.WealthBuildingGoal -> "${goalInfo.goalItemName} Goal"
                    else -> ""
                },
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Primary
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    EntryPair("Present Value", "₹${formatMoneyWithUnits(presentValue)}")
                    EntryPair("Years to goals", years.toString())
                }

                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.weight(1f),
                    ) {
                    EntryPair("Future value", "₹${formatMoneyWithUnits(futureValue.toLong())}", horizontal = Alignment.End)
                    EntryPair("Required SIP", "₹${formatMoneyWithUnits(sip.toLong())}/month", horizontal = Alignment.End)
                }
            }

            ExpenseAge(
                value = DateTimeUtils.getCurrentYear() - dobYear + years
            )
        }
    }
}

@Composable
fun ExpenseAge(value: Int) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(15.dp))
            .border(width = 0.8.dp, Secondary.copy(0.25f), RoundedCornerShape(15.dp))
            .background(Color(0xff273E71).copy(0.05f)),
    ){
        Row(
            modifier = Modifier.fillMaxWidth().height(54.dp).padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Expense Required at Age",
                fontFamily = Poppins,
                fontSize = 12.sp,
                color = Color(0xff6A7282)
            )
            Text(
                text = value.toString(),
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Primary
            )
        }
    }
}

@Composable
private fun ClickableIcon(
    onClick: () -> Unit,
    icon: DrawableResource,

    ){
    Box(
        modifier = Modifier.padding(end = 8.dp)
            .size(32.dp)
            .clip(CircleShape)
            .background(Color(0xffF9FAFB))
            .clickable(
                onClick = { onClick() }
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            painter = painterResource(icon),
            contentDescription = null,
            modifier = Modifier.fillMaxSize().padding(8.dp),
            tint = Primary
        )
    }
}

@Composable
private fun EntryPair(
    label: String,
    value: String,
    horizontal: Alignment.Horizontal= Alignment.Start
){
    Column(
        horizontalAlignment = horizontal,
    ) {
        Text(
            text = label,
            fontFamily = Poppins,
            fontSize = 12.sp,
            lineHeight = 13.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xff6A7282)
        )

        Text(
            text = value,
            fontFamily = Poppins,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = Primary
        )
    }
}