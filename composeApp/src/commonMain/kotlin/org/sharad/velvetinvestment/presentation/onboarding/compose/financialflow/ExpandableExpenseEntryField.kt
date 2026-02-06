package org.sharad.velvetinvestment.presentation.onboarding.compose.financialflow

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.shadowColor
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.utils.formatMoneyWithK
import org.sharad.velvetinvestment.utils.theme.titlesStyle
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.arrow_down
import velvet.composeapp.generated.resources.circle_arrow
import kotlin.math.exp

@Composable
fun ExpandableExpenseEntryField(
    heading:String,
    subHeading:String,
    amount:Long?,
    percentage:Int?=null,
    accentColor: Color,
    icon: DrawableResource,
    onValueChange:(String)->Unit,
){

    var expended by remember { mutableStateOf(false) }

    Box(
        modifier=Modifier
            .fillMaxWidth()
            .dropShadow(
                shadow = Shadow(
                    radius = 24.dp,
                    color = shadowColor,
                ),
                shape = RoundedCornerShape(10.dp)
            )
            .clip(RoundedCornerShape(10.dp))
            .background(Color.White, RoundedCornerShape(10.dp))
            .animateContentSize()
    ){
        Column(
            modifier=Modifier.fillMaxWidth()
                .padding(vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            UnExtendedPart(
                heading = heading,
                subHeading = subHeading,
                amount = amount?:0,
                percentage = percentage,
                expended=expended,
                accentColor=accentColor,
                onIconClick={expended=!expended},
                icon=icon
            )

            if (expended){
                ExtendedPart(
                    value = amount?.toString()?:"",
                    onValueChange = onValueChange,
                    accentColor = accentColor,
                    onIconClick = { expended = !expended }
                )
            }
        }
    }

}

@Composable
fun ExtendedPart(
    onValueChange: (String) -> Unit,
    value: String,
    accentColor: Color,
    onIconClick: () -> Unit
) {
    Column(
        modifier=Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        HorizontalDivider(
            modifier = Modifier.fillMaxWidth(),
            thickness = 1.dp,
            color = accentColor
        )

        IconMoneyTextField(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            value = value,
            onValueChange = onValueChange,
            placeHolder = "",
            icon = {
                Icon(
                    painter = painterResource(Res.drawable.circle_arrow),
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(28.dp)
                        .clickable(
                            onClick = {onIconClick()}
                        )
                )
            }
        )

    }
}

@Composable
fun UnExtendedPart(
    heading: String,
    subHeading: String,
    amount: Long,
    percentage: Int?,
    expended: Boolean,
    onIconClick: () -> Unit,
    accentColor: Color,
    icon: DrawableResource
) {
    Row(
        modifier=Modifier.fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Box(
            modifier = Modifier.size(44.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(accentColor.copy(alpha = 0.1f),RoundedCornerShape(10.dp)),
            contentAlignment = Alignment.Center
        ){
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = accentColor
            )
        }

        Column(
            modifier=Modifier.weight(1f)
                .fillMaxWidth()
        ) {
            Text(
                text = heading,
                style = org.sharad.velvetinvestment.utils.theme.subHeading,
                maxLines = 1
            )
            Text(
                text = subHeading,
                style = titlesStyle,
                color = titleColor,
                maxLines = if (expended) 2 else 1
            )
        }

        if (expended){
            Column(
                horizontalAlignment = Alignment.End,
            ) {
                Text(
                    text = "₹ ${formatMoneyWithK(amount)}",
                    style = org.sharad.velvetinvestment.utils.theme.subHeading,
                )
                percentage?.let{
                    Text(
                        text = "$percentage%",
                        style = titlesStyle,
                        color = titleColor
                    )
                }
            }
        }
        else{
            Icon(
                painter = painterResource(Res.drawable.arrow_down),
                contentDescription=null,
                tint= accentColor,
                modifier=Modifier
                    .padding(end = 16.dp)
                    .clickable(
                    onClick = {
                        onIconClick()
                    }
                )
            )
        }

    }
}