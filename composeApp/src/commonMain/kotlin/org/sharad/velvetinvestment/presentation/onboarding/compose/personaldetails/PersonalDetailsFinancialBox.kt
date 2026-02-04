package org.sharad.velvetinvestment.presentation.onboarding.compose.personaldetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.velvetinvestment.utils.theme.subHeadingMedium

@Composable
fun PersonalDetailsFinancialBox(
    label:String,
    value:String,
    icon: DrawableResource,
    backgroundColor: Color,
    labelColor: Color,
    modifier: Modifier = Modifier
){

    Box(
        modifier=modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(backgroundColor.copy(alpha = 0.1f), RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ){
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(vertical = 24.dp, horizontal = 16.dp)
        ){
            Box(
                modifier=Modifier.size(44.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(backgroundColor.copy(alpha = 0.2f),RoundedCornerShape(10.dp))
            ){
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize().padding(12.dp),
                    tint = labelColor
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Text(
                    text = label,
                    style = subHeadingMedium,
                    color = labelColor,
                )

                Text(
                    text = value,
                    style = MaterialTheme.typography.headlineMedium,
                    color = Primary
                )
            }
        }
    }

}