package org.sharad.velvetinvestment.presentation.profile.compose

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.darkBlue
import org.sharad.emify.core.ui.theme.grayColor
import org.sharad.emify.core.ui.theme.greenColor
import org.sharad.emify.core.ui.theme.orangeColor
import org.sharad.emify.core.ui.theme.redColor
import org.sharad.velvetinvestment.shared.compose.ScreenHeader
import org.sharad.velvetinvestment.utils.genericDropShadow
import org.sharad.velvetinvestment.utils.theme.Poppins
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.failed
import velvet.composeapp.generated.resources.icon_clock
import velvet.composeapp.generated.resources.successicon

@Preview(showSystemUi = true)
@Composable
fun WithdrawalDetailScreen(){
    val investmentDetail = InvestmentDetails(
        fundName = "SBI Gold Fund",
        investmentType = "Lumpsum",
        redemptionAmount = 5000,
        nav =29.189,
        unitsAlloted = 171.30,
        folioNumber = "SBI123456789"
    )
    var status by remember { mutableStateOf<TransactionStatus>(TransactionStatus.PENDING) }
    LazyColumn (modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(20.dp)){
       item {
           CancellationHeader("Withdrawal Details","MF1703345678901")

       }
        item {
            when (status) {
                TransactionStatus.PENDING -> {
                    PendingComposable()
                }

                TransactionStatus.SUCCESSFUL -> {
                    SuccessfulComposable()
                }

                TransactionStatus.FAILED -> {
                    FailedComposable()
                }
            }
        }
        item {
            InvestmentDetailsCard(
                fundName = investmentDetail.fundName,
                investmentType = investmentDetail.investmentType,
                redemptionAmount = investmentDetail.redemptionAmount,
                nav = investmentDetail.nav,
                unitsAlloted = investmentDetail.unitsAlloted,
                folioNumber = investmentDetail.folioNumber
            )
        }
        item{
            TransactionDetail()
        }
    }
}


@Composable
fun PendingComposable(){
    Column(modifier = Modifier.clip(RoundedCornerShape(20.dp)).background(Color(0xffFFF5E5)).padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)){
            Icon(painter = painterResource(Res.drawable.icon_clock), contentDescription = "clock icon",tint= orangeColor, modifier = Modifier.size(38.dp))
            Column {
                Text("Pending", color = orangeColor, fontWeight = FontWeight.Medium, fontSize = 16.sp, fontFamily = Poppins)
                Text("Withdrawal is being processed", fontSize = 14.sp, fontFamily = Poppins)
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            HorizontalDivider(color = orangeColor.copy(0.24f))
            Text("Expected Allotment", fontFamily = Poppins, color = grayColor)
        }
        Text("Within 2-3 business days", fontFamily = Poppins, color = darkBlue, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun SuccessfulComposable(){
    Column(modifier = Modifier.clip(RoundedCornerShape(20.dp)).background(Color(0xff008E23).copy(0.1f)).padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
       Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.successicon),
                contentDescription = "clock icon",
                tint = greenColor,
                modifier = Modifier.size(38.dp)
            )
            Column {
                Text(
                    "Successful",
                    color = greenColor,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    fontFamily = Poppins
                )
                Text("Withdrawal completed successfully", fontSize = 14.sp, fontFamily = Poppins)
            }
        }
    }
}

@Composable
fun FailedComposable(){
    Column(modifier = Modifier.clip(RoundedCornerShape(20.dp)).background(redColor.copy(0.1f)).padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                painter = painterResource(Res.drawable.failed),
                contentDescription = "clock icon",
                tint = redColor,
                modifier = Modifier.size(38.dp)
            )
            Column {
                Text(
                    "Failed",
                    color = redColor,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp,
                    fontFamily = Poppins
                )
                Text("Withdrawal completed successfully", fontSize = 14.sp, fontFamily = Poppins)
            }
        }
    }
}

@Composable
fun InvestmentDetailsCard(fundName:String,
                          investmentType: String,
                          redemptionAmount:Long,
                          nav: Double,
                          unitsAlloted:Double,
                          folioNumber: String
                          ){
    Box(modifier=Modifier.fillMaxWidth().genericDropShadow(RoundedCornerShape(24.dp)).clip(RoundedCornerShape(24.dp)).background(color = Color.White).padding(16.dp)){
        Column (verticalArrangement = Arrangement.spacedBy(16.dp)){
            Text("Investment Details", color = darkBlue, fontSize = 16.sp, fontWeight = FontWeight.Medium, fontFamily = Poppins)

            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text("Fund Name", fontFamily = Poppins, fontSize = 14.sp, color = grayColor)
                Text(text=fundName, fontFamily = Poppins, fontSize = 16.sp, fontWeight = FontWeight.SemiBold )
            }
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Investment Type", fontFamily = Poppins, fontSize = 14.sp, color = grayColor)
                    Text("Redemption Amount", fontFamily = Poppins, fontSize = 14.sp, color = grayColor)

                }
                Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                    Text(
                        investmentType,
                        fontFamily = Poppins,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text=redemptionAmount.toString(),
                        fontFamily = Poppins,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("NAV", fontFamily = Poppins, fontSize = 14.sp, color = grayColor)
                    Text("Units Alloted", fontFamily = Poppins, fontSize = 14.sp, color = grayColor)

                }
                Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                    Text(
                        text=nav.toString(),
                        fontFamily = Poppins,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text=unitsAlloted.toString(),
                        fontFamily = Poppins,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text("Folio Number", fontFamily = Poppins, fontSize = 14.sp, color = grayColor)
                Text(folioNumber, fontFamily = Poppins, fontSize = 16.sp, fontWeight = FontWeight.SemiBold )
            }

        }
    }
}


@Composable
fun TransactionDetail(){
    Box(modifier=Modifier.fillMaxWidth().genericDropShadow(RoundedCornerShape(24.dp)).clip(RoundedCornerShape(24.dp)).background(color=Color.White).padding(16.dp)){
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Text("Transaction Details", color = darkBlue, fontSize = 16.sp, fontWeight = FontWeight.Medium, fontFamily = Poppins)
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Text("Order Date", fontFamily = Poppins, fontSize = 14.sp, color = grayColor)
                Text("20 Dec 2025", fontFamily = Poppins, fontSize = 16.sp, fontWeight = FontWeight.SemiBold )
            }
            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Order ID", fontFamily = Poppins, fontSize = 14.sp, color = grayColor)
                    Text("Allotment Date", fontFamily = Poppins, fontSize = 14.sp, color = grayColor)

                }
                Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                    Text(
                        "MF1703345678901",
                        fontFamily = Poppins,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        "₹ 5000",
                        fontFamily = Poppins,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Transaction ID", fontFamily = Poppins, fontSize = 14.sp, color = grayColor)
                    Text("Status Date", fontFamily = Poppins, fontSize = 14.sp, color = grayColor)

                }
                Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
                    Text(
                        "TXN45678901234",
                        fontFamily = Poppins,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        "03 Feb 2026",
                        fontFamily = Poppins,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

        }
    }
}

enum class TransactionStatus{
    PENDING,
    SUCCESSFUL,
    FAILED
}

data class InvestmentDetails(
    val fundName:String,
    val investmentType: String,
    val redemptionAmount:Long,
    val nav: Double,
    val unitsAlloted:Double,
    val folioNumber:String
)
