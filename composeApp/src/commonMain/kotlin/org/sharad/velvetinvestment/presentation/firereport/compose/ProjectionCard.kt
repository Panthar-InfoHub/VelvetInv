package org.sharad.velvetinvestment.presentation.firereport.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.appGreen
import org.sharad.emify.core.ui.theme.appYellow
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.presentation.firereport.uimodels.FireProjectionRowUiModel
import org.sharad.velvetinvestment.shared.compose.ShadowCard
import org.sharad.velvetinvestment.utils.formatMoneyWithUnits
import org.sharad.velvetinvestment.utils.theme.Poppins
import org.sharad.velvetinvestment.utils.trimTo
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.arrow_down

@Composable
fun ProjectionCard(projection: FireProjectionRowUiModel) {
    val portfolioText = remember(projection.portfolioValue) {
        "₹ ${formatMoneyWithUnits(projection.portfolioValue)}"
    }
    val fireText = remember(projection.firePercentage) {
        "${projection.firePercentage.trimTo(1)}%"
    }
    var extended by rememberSaveable { mutableStateOf(false) }
    ShadowCard() {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            UnExtendedProjectionPart(
                year = projection.year,
                portfolio = portfolioText,
                fire = fireText,
                extended = extended,
                onClick = { extended = !extended }
            )

            AnimatedVisibility(
                visible = extended,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                ExtendedProjectionPart(
                    income = projection.income,
                    expenses = projection.expenses,
                    yearlyGoalSip = projection.yearlyGoalSip,
                    surplusMoney = projection.surplusMoney,
                    goalsFutureValue = projection.goalsFutureValue,
                    fireNumber = projection.fireNumber
                )
            }
        }
    }
}

@Composable
fun UnExtendedProjectionPart(
    fire: String,
    portfolio: String,
    year: Int,
    extended: Boolean,
    onClick: () -> Unit
) {
    val rotation by animateFloatAsState(
        if (!extended) 0f else 180f
    )
    Row(
        modifier=Modifier.fillMaxWidth()
            .height(74.dp)
            .clickable(onClick=onClick)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xffF1F5F9)),
            contentAlignment = Alignment.Center
        ){
            Text(
                text = year.toString(),
                style = MaterialTheme.typography.bodySmall,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)
            )
        }

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = "Portfolio",
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                lineHeight = 12.sp,
                color = titleColor
            )
            Text(
                text = portfolio,
                fontFamily = Poppins,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.Black
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "FIRE %",
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                lineHeight = 12.sp,
                color = titleColor
            )
            Text(
                text = fire,
                fontFamily = Poppins,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = appYellow
            )
        }
        Icon(
            painter = painterResource(Res.drawable.arrow_down),
            contentDescription=null,
            tint= Primary,
            modifier=Modifier
                .padding(end = 8.dp)
                .rotate(rotation)
        )
    }
}

@Composable
fun ExtendedProjectionPart(
    income: Long,
    expenses: Long,
    yearlyGoalSip: Long,
    surplusMoney: Long,
    goalsFutureValue: Long,
    fireNumber: Long
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        HorizontalDivider(color = titleColor)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                DataCard(
                    modifier = Modifier.weight(1f),
                    title = "Income",
                    value = formatMoneyWithUnits(income),
                )

                DataCard(
                    modifier = Modifier.weight(1f),
                    title = "Expenses",
                    value = formatMoneyWithUnits(expenses),
                    valueColor = Color(0xffEF4444)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                DataCard(
                    modifier = Modifier.weight(1f),
                    title = "Yearly Goal SIPs",
                    value = formatMoneyWithUnits(yearlyGoalSip),
                    valueColor = Secondary
                )

                DataCard(
                    modifier = Modifier.weight(1f),
                    title = "Surplus Money",
                    value = formatMoneyWithUnits(surplusMoney),
                    valueColor = appGreen
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                DataCard(
                    modifier = Modifier.weight(1f),
                    title = "Goals (FV)",
                    value = formatMoneyWithUnits(goalsFutureValue),
                    valueColor = Color(0xff8B5CF6)
                )

                DataCard(
                    modifier = Modifier.weight(1f),
                    title = "FIRE No.",
                    value = formatMoneyWithUnits(fireNumber),
                    valueColor = Primary
                )
            }
        }
    }
}

@Composable
fun DataCard(value: String, title: String, modifier: Modifier=Modifier, valueColor: Color=Color(0xff1D293D)) {
    Box(
        modifier = modifier.fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xffF8FAFC)),
        contentAlignment = Alignment.CenterStart,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
        ) {
            Text(
                text = title,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                fontSize = 12.sp,
                lineHeight = 12.sp,
                color = Color(0xff62748E)
            )
            Text(
                text = "₹ $value",
                fontFamily = Poppins,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = valueColor
            )
        }

    }
}