package org.sharad.velvetinvestment.presentation.onboarding.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import org.sharad.velvetinvestment.utils.DateTimeUtils

@Composable
fun RetirementYearSlider(
    modifier:Modifier= Modifier,
    onSliderUpdate:(Int)-> Unit
){

    val sliderPosition by remember{ mutableStateOf(0f) }
    val currentYear= DateTimeUtils.getCurrentYear()
    val finalYear= currentYear+50



}