package org.sharad.velvetinvestment.presentation.onboarding.compose.goals

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.sharad.velvetinvestment.domain.models.goals.GoalRequest
import org.sharad.velvetinvestment.shared.compose.ShadowCard
import org.sharad.velvetinvestment.utils.GoalUtils.GoalCalculator
import org.sharad.velvetinvestment.utils.GoalUtils.getGoalInputs
import org.sharad.velvetinvestment.utils.formatMoneyWithUnits
import org.sharad.velvetinvestment.utils.theme.Poppins


@Composable
fun GoalEntry(goalInfo: GoalRequest, onDeleteClick: (GoalRequest) -> Unit) {

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

    ShadowCard(
        modifier= Modifier.fillMaxWidth()
    ) {
        Column(
            modifier=Modifier.fillMaxWidth().padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Row(
                modifier=Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text=goalInfo.title,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )

                Box(
                    modifier=Modifier.padding(end = 8.dp)
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(Color(0xffF0F0F0))
                        .clickable(
                            onClick = { onDeleteClick(goalInfo) }
                        ),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = "x",
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.Start
                ) {
                    EntryPair("Present Value:- ", "₹${formatMoneyWithUnits(presentValue)}")
                    EntryPair("Years to goals:- ", goalInfo.yearsToGoal.toString())
                }

                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    horizontalAlignment = Alignment.End
                ){

                    EntryPair("Future value:- ", "₹${formatMoneyWithUnits(futureValue.toLong())}")
                    EntryPair("Required SIP:- ", "₹${formatMoneyWithUnits(sip.toLong())}")
                }
            }
        }
    }
}

@Composable
private fun EntryPair(
    label: String,
    value: String
){
    Row {
        Text(
            text = label,
            fontFamily = Poppins,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
        )

        Text(
            text = value,
            fontFamily = Poppins,
            fontSize = 12.sp
        )
    }
}