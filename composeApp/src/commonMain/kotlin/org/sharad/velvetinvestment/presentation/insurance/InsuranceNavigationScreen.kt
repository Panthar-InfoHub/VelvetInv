package org.sharad.velvetinvestment.presentation.insurance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.appGreen
import org.sharad.emify.core.ui.theme.healthColor
import org.sharad.emify.core.ui.theme.otherColor
import org.sharad.emify.core.ui.theme.termColor
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.BarHeader
import org.sharad.velvetinvestment.shared.compose.ShadowCard
import org.sharad.velvetinvestment.utils.theme.Poppins
import org.sharad.velvetinvestment.utils.theme.titlesStyle
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.arrow_right
import velvet.composeapp.generated.resources.icon_arrow_right
import velvet.composeapp.generated.resources.icon_heart
import velvet.composeapp.generated.resources.icon_insurance
import velvet.composeapp.generated.resources.nav_icon_incurance

@Composable
fun InsuranceNavigationScreen(
    navigateToHealthInsurance:()->Unit,
    navigateToTermInsurance:()->Unit,
    navigateToOtherInsurance:()->Unit
) {





    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        BackHeader("Insurance")
        LazyColumn(
            modifier = Modifier.weight(1f)
                .padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                BarHeader(heading = "Explore Insurance Options")
            }

            item {
                InsuranceEntry(
                    title = "Health Insurance",
                    subTitle = "Health Coverage Progress",
                    color = healthColor,
                    icon = Res.drawable.icon_heart,
                    onClick = navigateToHealthInsurance
                )
            }

            item {
                InsuranceEntry(
                    title = "Term Insurance",
                    subTitle = "Life Coverage Progress",
                    color = termColor,
                    icon = Res.drawable.nav_icon_incurance,
                    onClick = navigateToTermInsurance
                )
            }

            item {
                InsuranceEntry(
                    title = "Other Insurance",
                    subTitle = "Other Coverage Progress",
                    color = otherColor,
                    icon = Res.drawable.nav_icon_incurance,
                    onClick = navigateToOtherInsurance
                )
            }
        }
    }
}

@Composable
fun InsuranceEntry(
    title:String,
    subTitle: String,
    color: Color,
    icon: DrawableResource,
    onClick:()->Unit
){
    ShadowCard(
        clickable = true,
        onClick = onClick
    ) {
        Row(
            modifier=Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(modifier=Modifier.size(48.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(color.copy(0.1f)),
                contentAlignment = Alignment.Center
            ){
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = color,
                    modifier = Modifier.size(24.dp)
                )
            }

            Column(
                modifier= Modifier.weight(1f)
            ) {
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = subTitle,
                    style = titlesStyle,
                    color= titleColor
                )
            }

            Icon(
                painter = painterResource(Res.drawable.arrow_right),
                contentDescription = null,
                tint = titleColor,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}