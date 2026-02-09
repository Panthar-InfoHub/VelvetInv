package org.sharad.velvetinvestment.presentation.onboarding.compose

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.velvetinvestment.utils.theme.secondaryLight

@Composable
fun OnBoardingHeader(
    currentStep:Int,
    totalStep:Int=7,
    showSkip:Boolean=true,
    onSkip:()->Unit={},
    modifier: Modifier=Modifier
){

    val animatedWidth by animateFloatAsState(
        targetValue = currentStep.toFloat()/totalStep.toFloat()
    )

    Column(
        modifier=modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier= Modifier.fillMaxWidth()
                .height(IntrinsicSize.Min),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            Box(
                modifier=Modifier.fillMaxHeight()
                    .width(5.dp)
                    .clip(CircleShape)
                    .background(Secondary, CircleShape)
            )

            Text(
                text="Financial Onboarding",
                style = MaterialTheme.typography.headlineSmall,
                color = Primary,
                modifier=Modifier.weight(1f)
                    .fillMaxWidth()
            )

            AnimatedVisibility(showSkip) {
                Text(
                    text = "Skip",
                    style = MaterialTheme.typography.labelSmall,
                    color = Secondary,
                    modifier = Modifier.clickable(
                        onClick = onSkip,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    )
                )
            }

        }

        Box(
            modifier=Modifier.fillMaxWidth()
                .height(10.dp)
                .clip(CircleShape)
                .background(Color(0xffEBE9E9), CircleShape),
            contentAlignment = Alignment.CenterStart
        ){
            Box(
                modifier=Modifier.fillMaxHeight()
                    .fillMaxWidth(animatedWidth)
                    .clip(CircleShape)
                    .background(Secondary,CircleShape)
            )
        }
    }

}