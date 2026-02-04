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
    modifier:Modifier= Modifier,
    onSliderUpdate:(Int)-> Unit,
    selectedYear:Int
){

    var sliderPosition by remember{ mutableStateOf(0f) }
    val currentYear= DateTimeUtils.getCurrentYear()
    val finalYear= currentYear+50
    val sliderState= rememberSliderState(
        value = sliderPosition,
        valueRange = 0f..50f,
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
            state = sliderState,
            modifier = Modifier.fillMaxWidth(),

            thumb = {it->
                onSliderUpdate(currentYear+it.value.toInt())
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .background(Secondary)
                )
            },

            track = { sliderScope ->

                val progressFraction =
                    (sliderState.value - sliderState.valueRange.start) /
                            (sliderState.valueRange.endInclusive - sliderState.valueRange.start)

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