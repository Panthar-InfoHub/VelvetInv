package org.sharad.velvetinvestment.presentation.profile.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.blueColor
import org.sharad.emify.core.ui.theme.darkBlue
import org.sharad.emify.core.ui.theme.goldenColor
import org.sharad.emify.core.ui.theme.grayColor
import org.sharad.emify.core.ui.theme.greenColor
import org.sharad.emify.core.ui.theme.redColor
import org.sharad.velvetinvestment.shared.compose.ScreenHeader
import org.sharad.velvetinvestment.utils.genericDropShadow
import org.sharad.velvetinvestment.utils.theme.Poppins
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.calender
import velvet.composeapp.generated.resources.calender2
import velvet.composeapp.generated.resources.icon__2_
import velvet.composeapp.generated.resources.icon__3_
import velvet.composeapp.generated.resources.icon__4_
import velvet.composeapp.generated.resources.icon__5_
import velvet.composeapp.generated.resources.rectangle_18

@Preview(showSystemUi = true)
@Composable
fun BreakFixedDepositScreen() {

    LazyColumn(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            ScreenHeader("MFD Screen")
        }
        item{
            BFDBankDetail(
            amount = "₹ 2,00,000",
            interestRate = 7.80,
            date = "01 Feb 2026",
            tenure = 12,
            completedMonths = 11
            )
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            BFDPrematureWithdrawalCard()
        }
        item {
            EstimatedPayout()
            Spacer(modifier = Modifier.height(16.dp))
        }
        item{
            SelectWithdrawalMode()
    }
        item {
            OptionalBreakingFD()
        }
    }
}

@Composable
fun BFDBankDetail(
    amount:String,
    interestRate:Double,
    date:String,
    tenure:Int,
    completedMonths:Int

){
    val fill= completedMonths.toFloat()/tenure.toFloat()
    Box(
        Modifier.fillMaxWidth().genericDropShadow(RoundedCornerShape(24.dp))
            .background(color = Color.White, shape = RoundedCornerShape(24.dp)).padding(16.dp)
    )
    {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(Res.drawable.rectangle_18),
                    contentDescription = "hdfc icon", modifier = Modifier.size(52.dp)
                )
                Column {
                    Text(
                        "HDFC Bank",
                        color = darkBlue,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text("FD2035011567", color = grayColor)
                }
            }
            Spacer(modifier = Modifier.height(40.dp))
            Column (verticalArrangement = Arrangement.spacedBy(8.dp)){
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Principal Amount", color = grayColor, fontSize = 14.sp)
                    Text("Interest Rate", color = grayColor, fontSize = 14.sp)
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text=amount,
                        color = darkBlue,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        fontFamily = Poppins
                    )
                    Text(
                        "$interestRate% p.a.",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold,
                        color = goldenColor,
                        fontSize = 14.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
            Column {
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Start Date", color = grayColor, fontSize = 14.sp)
                    Text("Tenure", color = grayColor, fontSize = 14.sp)
                }
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(
                        text=date,
                        color = darkBlue,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp,
                        fontFamily = Poppins
                    )
                    Text(
                        "$tenure months",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold,
                        color = darkBlue,
                        fontSize = 14.sp
                    )
                }
            }
            Spacer(modifier = Modifier.height(20.dp))
            Box(Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(color = Color(0xffF8F9FB)).padding(16.dp)) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.calender2),
                            contentDescription = "calender",
                            tint = darkBlue
                        )
                        Text(
                            "Completed:${completedMonths}months of $tenure months",
                            fontSize = 14.sp,
                            fontFamily = Poppins,
                            color = darkBlue
                        )
                    }
                    Spacer(modifier = Modifier.height(7.dp))
                    Box(
                        modifier = Modifier.fillMaxWidth().height(6.dp)
                            .clip(RoundedCornerShape(10.dp)).background(
                                color = Color(0xffDEE2F6),
                                shape = RoundedCornerShape(10.dp)
                            )
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth(fill).height(6.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(color = darkBlue, shape = RoundedCornerShape(10.dp))
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun BFDPrematureWithdrawalCard(){
    Box(modifier= (Modifier.fillMaxWidth().clip(RoundedCornerShape(24.dp)).background(color = Color(0xffFEF2F2)).padding(16.dp)).padding(top = 16.dp, bottom = 16.dp)){
Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Icon(
            painter = painterResource(Res.drawable.icon__4_),
            contentDescription = "icon",
            tint = redColor
        )
        Text(
            "Impact of Premature \nWithdrawal",
            color = redColor,
            fontSize = 16.sp,
            fontFamily = Poppins,
            fontWeight = FontWeight.Medium
        )
    }
     Row (horizontalArrangement = Arrangement.spacedBy(8.dp)){
        Icon(
            painter = painterResource(Res.drawable.icon__3_),
            contentDescription = "down Stock",
            tint = redColor
        )
        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text("Reduced Interest Rate", color = Color(0xff82181A))
            Text("From 7.60% to 6.60% p.a. (1% penalty)", color = Color(0xffC10007))
        }
    }
     Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Icon(painter = painterResource(Res.drawable.icon__5_), contentDescription = "calculator", tint = redColor)
        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text("Loss in Returns", color = Color(0xff82181A))
            Text("Estimated loss of ₹ 250 in interest + ₹100 processing fee",color = Color(0xffC10007))
        }
    }
     Row (horizontalArrangement = Arrangement.spacedBy(8.dp)){
        Icon(painter = painterResource(Res.drawable.icon__2_), contentDescription = "icon", tint = redColor)
        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text("Processing Time",color=Color(0xff82181A))
            Text("Funds will be credited in 2-3 business days",color = Color(0xffC10007))
        }
    }

    }

    }
}

@Composable
fun EstimatedPayout(){
    Box(modifier = Modifier.fillMaxWidth().genericDropShadow(RoundedCornerShape(24.dp)).clip(RoundedCornerShape(24.dp)).background(color = Color.White, shape = RoundedCornerShape(24.dp)).padding(16.dp)) {
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(
                "Estimated Payout",
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp
            )
            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                Text("Principal Amount", color = grayColor, fontSize = 14.sp)
                Text("₹ 1,00,000", color = darkBlue, fontWeight = FontWeight.Bold, fontFamily = Poppins)
            }
            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                Text("Interest Earned(Till Date)", fontSize = 14.sp, color = grayColor)
                Text("₹ 1,900", color =greenColor, fontWeight = FontWeight.Bold, fontFamily = Poppins)
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Penalty & Charges", fontSize = 14.sp, color =grayColor
                )
                Text("- ₹ 350", color = redColor, fontWeight = FontWeight.Bold, fontFamily = Poppins)
            }
            HorizontalDivider(color = Color(0xffDEE2F6))
            Spacer(Modifier.height(2.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(
                    "Total Payout",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
                Text("₹1,01,550", fontFamily = Poppins, color = goldenColor, fontWeight = FontWeight.Bold, fontSize =22.sp )

            }
        }
    }
}

@Composable
fun SelectWithdrawalMode(){
    Box(modifier=Modifier.fillMaxWidth().genericDropShadow(RoundedCornerShape(24.dp)).background(color = Color.White, shape = RoundedCornerShape(24.dp)).padding(26.dp)){
        Column {
            Text("Select Withdrawal Mode", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(20.dp))
            Row(modifier = Modifier.fillMaxWidth().border(1.dp, color = Color(0xffDEE2F6), shape = RoundedCornerShape(12.dp)).padding(16.dp)){
                Text("Bank\nAccount", fontWeight = FontWeight.SemiBold, color = darkBlue)
                Text("HDFC Bank....4567 \n (2-3 business days)", color = grayColor)
            }
        }
    }
}

@Composable
fun OptionalBreakingFD(){
    Box(modifier = Modifier.fillMaxWidth().background(color = Color.White).padding(16.dp)){

       Column {
           Text(
               "Reason for Breaking FD (Optional)",
               fontSize = 16.sp,
               fontFamily = Poppins,
               fontWeight = FontWeight.SemiBold
           )
           Spacer(modifier = Modifier.height(26.dp))
           Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(color = Color(0xffF3F3F5))) {
               TextField(
                   value = "",
                   onValueChange = {},
                   modifier = Modifier.height(100.dp).fillMaxWidth(),
                   placeholder = {
                       Text(
                           "Please tell us why you're withdrawing early...",
                           fontSize = 14.sp
                       )
                   },
                   colors = TextFieldDefaults.colors(
                       unfocusedContainerColor = Color(0xffF3F3F5),
                       focusedContainerColor = Color(0xffF3F3F5),
                       unfocusedIndicatorColor = Color.Transparent,
                       focusedIndicatorColor = Color.Transparent
                   )
               )
           }
       }

    }
}