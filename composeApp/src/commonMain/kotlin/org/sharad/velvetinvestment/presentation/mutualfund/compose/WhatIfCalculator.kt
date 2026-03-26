package org.sharad.velvetinvestment.presentation.mutualfund.compose

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.appGreen
import org.sharad.emify.core.ui.theme.appRed
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.data.remote.model.mfdetails.Metrics
import org.sharad.velvetinvestment.presentation.mutualfund.CalculatorInputState
import org.sharad.velvetinvestment.presentation.mutualfund.viewmodel.getPreferredReturn
import org.sharad.velvetinvestment.shared.compose.TwoWaySwitch
import org.sharad.velvetinvestment.utils.formatMoneyWithUnits
import org.sharad.velvetinvestment.utils.theme.Poppins
import org.sharad.velvetinvestment.utils.theme.titlesStyle
import kotlin.math.pow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WhatIfCalculator(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onExpandToggle: () -> Unit,
    input: CalculatorInputState,
    metrics: Metrics?,
    onModeChange: (Boolean) -> Unit,
    onInvestmentChange: (Long) -> Unit,
    onTimeChange: (Int) -> Unit,

) {

    val expectedReturn = metrics?.getPreferredReturn() ?: 12f

    val monthlyRate = expectedReturn / 100f / 12f

    val totalInvested = if (input.isSip) {
        input.monthlyInvestment * 12 * input.timeInYears
    } else {
        input.monthlyInvestment
    }

    val totalValue = if (input.isSip) {
        val months = input.timeInYears * 12

        (input.monthlyInvestment *
                (((1 + monthlyRate).toDouble().pow(months.toDouble()) - 1) / monthlyRate) *
                (1 + monthlyRate)).toLong()
    } else {
        (input.monthlyInvestment *
                (1 + expectedReturn / 100).toDouble()
                    .pow(input.timeInYears.toDouble())).toLong()
    }

    val returns = totalValue - totalInvested

    ExpandableContent(
        modifier=Modifier.padding(horizontal = 16.dp),
        expanded = expanded,
        heading = "What if Calculator",
        onIconClick = onExpandToggle
    ) {

        Column(
            modifier = modifier
                .fillMaxWidth()
        ) {

            TwoWaySwitch(
                isFirstSelected = input.isSip,
                onFirstClick = { onModeChange(true) },
                onSecondClick = { onModeChange(false) },
                firstText = "Monthly SIP",
                secondText = "One-Time"
            )

            Spacer(Modifier.height(16.dp))

            Row(
                modifier=Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ){
                Text("Investment Amount",
                    fontSize = 14.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    color =Color(0xff4A5565)
                )
                Text(
                    "₹ ${input.monthlyInvestment}",
                    fontSize = 14.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )
            }

            Slider(
                value = input.monthlyInvestment.toFloat(),
                onValueChange = { onInvestmentChange(it.toLong()) },
                valueRange = 1000f..100000f,
                modifier = Modifier.fillMaxWidth(),
                thumb = {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Secondary)
                    )
                },
                track = { sliderScope ->
                    val progress =
                        (sliderScope.value - sliderScope.valueRange.start) /
                                (sliderScope.valueRange.endInclusive - sliderScope.valueRange.start)

                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(CircleShape)
                            .background(Color(0xff307FE2).copy(alpha = 0.3f))
                    ) {
                        Box(
                            Modifier
                                .fillMaxWidth(progress)
                                .height(8.dp)
                                .clip(CircleShape)
                                .background(Secondary)
                        )
                    }
                }
            )

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    "Time Period", fontSize = 14.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xff4A5565)
                )
                Text(
                    "${input.timeInYears} Years",
                    fontSize = 14.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )
            }

            Slider(
                value = input.timeInYears.toFloat(),
                onValueChange = { onTimeChange(it.toInt()) },
                valueRange = 1f..30f,
                modifier = Modifier.fillMaxWidth(),
                thumb = {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(Secondary)
                    )
                },
                track = { sliderScope ->
                    val progress =
                        (sliderScope.value - sliderScope.valueRange.start) /
                                (sliderScope.valueRange.endInclusive - sliderScope.valueRange.start)

                    Box(
                        Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(CircleShape)
                            .background(Color(0xff307FE2).copy(alpha = 0.3f))
                    ) {
                        Box(
                            Modifier
                                .fillMaxWidth(progress)
                                .height(8.dp)
                                .clip(CircleShape)
                                .background(Secondary)
                        )
                    }
                }
            )

            Spacer(Modifier.height(20.dp))


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xffF3F4F6))
                    .padding(16.dp)
            ) {

                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total Invested",
                        fontSize = 14.sp,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Medium,
                        color = titleColor)
                    Text("₹ ${formatMoneyWithUnits(totalInvested)}",
                        fontSize = 14.sp,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Medium)
                }

                Spacer(Modifier.height(12.dp))

                Row(
                    Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Est. Returns" ,
                        fontSize = 14.sp,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Medium,
                        color = titleColor)
                    Text(
                        "₹ ${formatMoneyWithUnits(returns)}",
                        color = if (returns>0) appGreen else appRed,
                        fontSize = 14.sp,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(Modifier.height(12.dp))

                HorizontalDivider(color = titleColor.copy(0.3f))

                Spacer(Modifier.height(12.dp))

                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Total Value",
                        fontSize = 16.sp,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold,
                        color = Primary
                    )
                    Text(
                        "₹ ${formatMoneyWithUnits(totalValue)}",
                        fontSize = 18.sp,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold,
                        color = Primary
                    )
                }
            }
        }
    }
}

