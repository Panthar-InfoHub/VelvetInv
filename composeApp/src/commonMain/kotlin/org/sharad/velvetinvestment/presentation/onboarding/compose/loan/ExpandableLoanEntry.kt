package org.sharad.velvetinvestment.presentation.onboarding.compose.loan

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.velvetinvestment.presentation.onboarding.models.LoanInfo
import org.sharad.velvetinvestment.utils.formatMoneyWithUnits
import org.sharad.velvetinvestment.utils.genericDropShadow
import org.sharad.velvetinvestment.utils.theme.Poppins
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.arrow_right
import velvet.composeapp.generated.resources.icon_download

@Composable
fun ExpandableLoanEntry(
    loanInfo: LoanInfo,
    onDeleteClick: () -> Unit
) {

    var extended by remember { mutableStateOf(false) }


    Box(
        modifier=Modifier.fillMaxWidth()
            .genericDropShadow(RoundedCornerShape(15.dp))
            .clip(RoundedCornerShape(15.dp))
            .background(Color.White,RoundedCornerShape(10.dp))
            .animateContentSize()
    ){

        Column(
            modifier= Modifier.fillMaxWidth()
        ) {

            StablePart(
                title=loanInfo.loanType?.displayName?:"",
                extended=extended,
                onClick={extended=!extended},
                onDeleteClick={ onDeleteClick()}
            )

            if (extended){ ExtendingPart(loanInfo = loanInfo) }

        }
    }
}

@Composable
fun ExtendingPart(loanInfo: LoanInfo) {
    Column(
        modifier=Modifier.fillMaxWidth().padding(start = 20.dp, end = 20.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text="Outstanding Amount",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Primary,
                fontFamily = Poppins
            )
            Text(
                text="₹${formatMoneyWithUnits(loanInfo.outstandingAmount)}",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                fontFamily = Poppins,
                color = Secondary
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text="EMI",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Primary,
                fontFamily = Poppins
            )
            Text(
                text="₹${formatMoneyWithUnits(loanInfo.monthlyEmi)}",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                fontFamily = Poppins,
                color = Secondary
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text="Tenure",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = Primary,
                fontFamily = Poppins
            )
            Text(
                text="${loanInfo.tenure?:0} months",
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                fontFamily = Poppins,
                color = Secondary
            )
        }

    }
}

@Composable
fun StablePart(title: String, extended: Boolean, onClick: () -> Unit, onDeleteClick: () -> Unit) {
    Row(
        modifier=Modifier.fillMaxWidth()
            .height(72.dp)
            .clip(RoundedCornerShape(15.dp))
            .clickable(
                onClick = { onClick() }
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {

        Text(
            text=title,
            modifier=Modifier.weight(1f).padding(start = 20.dp),
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            fontFamily = Poppins
        )

        if (extended){
            Icon(
                painter = painterResource(Res.drawable.icon_download),
                contentDescription = null,
                modifier = Modifier.padding(end = 20.dp)
                    .size(16.dp)
                    .clickable(
                        onClick={onDeleteClick}
                    ),
                tint = Primary
            )
        }
        else{
            Icon(
                painter = painterResource(Res.drawable.arrow_right),
                contentDescription = null,
                modifier = Modifier.padding(horizontal = 16.dp)
                    .size(24.dp),
                tint = Primary
            )
        }

    }
}