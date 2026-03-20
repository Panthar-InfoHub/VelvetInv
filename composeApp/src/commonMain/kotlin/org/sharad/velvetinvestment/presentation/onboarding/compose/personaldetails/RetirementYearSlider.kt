package org.sharad.velvetinvestment.presentation.onboarding.compose.personaldetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSliderState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.utils.DateTimeUtils
import org.sharad.velvetinvestment.utils.theme.subHeadingMedium
import org.sharad.velvetinvestment.utils.theme.titlesStyle

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun RetirementYearSlider(
    modifier: Modifier = Modifier,
    onSliderUpdate: (Int) -> Unit,
    selectedYear: Int?
){

    val currentYear= DateTimeUtils.getCurrentYear()
    val sliderPosition = selectedYear
        ?.let { (it - currentYear).coerceIn(0, 60).toFloat() }
        ?: 0f
    val finalYear= currentYear+60
    val sliderState= rememberSliderState(
        value = sliderPosition,
        valueRange = 0f..60f,
        onValueChangeFinished = {
            onSliderUpdate(sliderPosition.toInt()+ currentYear)
        }
    )

    Column(
        modifier=modifier.fillMaxWidth()
    ){

        Row{
            Text(
                text = "Planned Retirement Year ",
                style = subHeadingMedium
            )

            Text(
                text = "*",
                style = subHeadingMedium,
                color = Color.Red
            )
        }
        Slider(
            value = sliderPosition,
            onValueChange = {
                onSliderUpdate(it.toInt() + currentYear)
            },
            modifier = Modifier.fillMaxWidth(),
            valueRange = 0f..60f,


            thumb = {it->
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Secondary)
                )
            },

            track = { sliderScope ->

                val progressFraction =
                    (sliderScope.value - sliderScope.valueRange.start) /
                            (sliderScope.valueRange.endInclusive - sliderScope.valueRange.start)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(CircleShape)
                        .background(Color(0xff307FE2).copy(alpha = 0.3f))
                ) {

                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progressFraction)
                            .height(8.dp)
                            .clip(CircleShape)
                            .background(Secondary)
                    )
                }
            }
        )

        Row(
            modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text=currentYear.toString(),
                color = titleColor,
                style = titlesStyle
            )

            Text(
                text=selectedYear.toString(),
                color = titleColor,
                style = titlesStyle
            )


            Text(
                text=finalYear.toString(),
                color = titleColor,
                style = titlesStyle
            )

        }
    }
}