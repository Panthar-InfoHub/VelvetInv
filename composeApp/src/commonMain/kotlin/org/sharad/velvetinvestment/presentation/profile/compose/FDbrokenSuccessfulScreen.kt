package org.sharad.velvetinvestment.presentation.profile.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.blueColor
import org.sharad.emify.core.ui.theme.darkBlue
import org.sharad.emify.core.ui.theme.goldenColor
import org.sharad.emify.core.ui.theme.grayColor
import org.sharad.emify.core.ui.theme.greenColor
import org.sharad.emify.core.ui.theme.lightGray
import org.sharad.emify.core.ui.theme.orangeColor
import org.sharad.velvetinvestment.utils.genericDropShadow
import org.sharad.velvetinvestment.utils.theme.Poppins
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.calender
import velvet.composeapp.generated.resources.calender2
import velvet.composeapp.generated.resources.icon__2_
import velvet.composeapp.generated.resources.image__hdfc_bank_
import velvet.composeapp.generated.resources.tick_inside_circle

@Preview(showSystemUi = true)
@Composable
fun FDbrokenSuccessfulScreen() {
    LazyColumn(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {

        item {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Spacer(modifier = Modifier.height(50.dp))
                Box(
                    modifier = Modifier.clip(CircleShape).background(greenColor.copy(0.1f))
                        .padding(20.dp)
                ) {
                    Icon(
                        painter = painterResource(
                            Res.drawable.tick_inside_circle
                        ), contentDescription = "Tick Icon", tint = greenColor
                    )
                }
            }
        }
        item {
            Text(
                "FD Broken Successfully!",
                color = darkBlue,
                fontFamily = Poppins,
                fontWeight = FontWeight.SemiBold,
                fontSize = 24.sp
            )
        }
        item {
            Text("Your withdrawal request has been \n processed", fontSize = 14.sp, fontFamily = Poppins, textAlign = TextAlign.Center)
        }
        item {
            Row {
                Text("Transaction ID:", fontSize = 14.sp, fontFamily = Poppins,color=grayColor)
                Text("TXN20350123456", color = darkBlue, fontFamily = Poppins, fontSize = 14.sp)
            }
            Spacer(modifier = Modifier.height(30.dp))
        }
        item {


            Box(
                Modifier.fillMaxWidth().height(189.dp).genericDropShadow(RoundedCornerShape(24.dp))
                    .clip(RoundedCornerShape(24.dp))
                    .background(color = darkBlue, shape = RoundedCornerShape(24.dp)).padding(24.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        "Amount to be Credited",
                        fontFamily = Poppins,
                        fontSize = 14.sp,
                        color = Color.White.copy(0.8f)
                    )
                    Column {
                        Text(
                            "₹ 1,01,550",
                            color = Color.White,
                            fontFamily = Poppins,
                            fontSize = 36.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(10.dp))
                                .background(color = Color.White.copy(0.1f)).padding(10.dp)
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.calender2),
                                contentDescription = "Calender 2 icon",
                                tint = Color.White
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                "Estimated credit by 14 Feb 2026",
                                fontFamily = Poppins,
                                color = Color.White.copy(0.9f)
                            )

                        }
                    }
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))


            Box(
                modifier = Modifier.fillMaxWidth().genericDropShadow(RoundedCornerShape(24.dp))
                    .clip(RoundedCornerShape(24.dp))
                    .background(color = Color.White, shape = RoundedCornerShape(24.dp))
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        "Transaction Details",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("FD Number", color = grayColor, fontFamily = Poppins, fontSize = 14.sp, modifier = Modifier.padding(bottom = 4.dp))
                        Text(
                            "FD2025012345",
                            fontFamily = Poppins,
                            color = darkBlue,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    HorizontalDivider(color = Color(0xFFDEE2F6).copy(0.75f))
                    Spacer(Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Bank", color = grayColor, fontFamily = Poppins, fontSize = 14.sp, modifier = Modifier.padding(bottom = 4.dp))
                        Row (verticalAlignment = Alignment.CenterVertically){
                            Image(
                                painter = painterResource(Res.drawable.image__hdfc_bank_),
                                contentDescription = "hdfc icon2",
                                modifier = Modifier.size(24.dp)
                            )
                            Text(
                                "HDFC Bank",
                                fontFamily = Poppins,
                                color = darkBlue,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    HorizontalDivider(color = Color(0xFFDEE2F6).copy(0.75f))
                    Spacer(Modifier.height(12.dp))
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(
                            "Principal Amount",
                            color = grayColor,
                            fontFamily = Poppins,
                            fontSize = 14.sp, modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Text(
                            "₹ 1,00,000",
                            fontFamily = Poppins,
                            color = darkBlue,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    HorizontalDivider(color = Color(0xFFDEE2F6).copy(0.75f))
                    Spacer(Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Interest Rate",
                            color = grayColor,
                            fontFamily = Poppins,
                            fontSize = 14.sp, modifier = Modifier.padding(bottom = 4.dp)
                        )
                        Text(
                            "₹1,900",
                            fontFamily = Poppins,
                            color = greenColor,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    HorizontalDivider(color = Color(0xFFDEE2F6).copy(0.75f))
                    Spacer(Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Charges & Penalty",
                            color = grayColor,
                            fontFamily = Poppins,
                            fontSize = 14.sp,modifier= Modifier.padding(4.dp)
                        )
                        Text(
                            "- ₹ 350",
                            fontFamily = Poppins,
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    HorizontalDivider(color = Color(0xFFDEE2F6).copy(0.75f))
                    Spacer(Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Duration Completed",
                            color = grayColor,
                            fontFamily = Poppins,
                            fontSize = 14.sp,modifier=Modifier.padding(4.dp)
                        )
                        Text(
                            "3 of 12 months",
                            fontFamily = Poppins,
                            color = darkBlue,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    HorizontalDivider(color = Color(0xFFDEE2F6).copy(0.75f))
                    Spacer(Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Break Date",
                            color = grayColor,
                            fontFamily = Poppins,
                            fontSize = 14.sp,modifier=Modifier.padding(bottom = 4.dp)
                        )
                        Text(
                            "11 Feb 2026",
                            fontFamily = Poppins,
                            color = darkBlue,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    HorizontalDivider(color = Color(0xFFDEE2F6).copy(0.75f))
                    Spacer(Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Credit To", color = grayColor, fontFamily = Poppins, fontSize = 14.sp)
                        Text(
                            "HDFC Bank....4567",
                            fontFamily = Poppins,
                            color = darkBlue,
                            fontWeight = FontWeight.Bold
                        )
                    }

                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(24.dp))
                    .background(color = Color.White, shape = RoundedCornerShape(24.dp))
                    .padding(16.dp),
            ) {
                Column {
                    Text(
                        "Calculation Breakdown",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp,
                        fontFamily = Poppins
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(1.dp)){
                            Text(
                                "Original Interest Rate",
                                fontFamily = Poppins,
                                color = grayColor,
                                fontSize = 14.sp
                            )
                            Text(
                                "7.60% p.a. for 12 months",
                                fontFamily = Poppins,
                                fontSize = 11.sp,
                                color = grayColor
                            )
                        }
                        Text(
                            "₹ 7,600",
                            fontFamily = Poppins,
                            color = darkBlue,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column (verticalArrangement = Arrangement.spacedBy(1.dp)){
                            Text(
                                "Revised Interest Rate",
                                fontFamily = Poppins,
                                color = grayColor,
                                fontSize = 14.sp
                            )
                            Text(
                                "6.60% p.a. for 3 months",
                                fontFamily = Poppins,
                                fontSize = 11.sp,
                                color = grayColor
                            )
                        }
                        Text(
                            "₹ 1,900",
                            fontFamily = Poppins,
                            color = greenColor,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column (verticalArrangement = Arrangement.spacedBy(1.dp)){
                            Text(
                                "Penalty (1%)",
                                fontFamily = Poppins,
                                color = grayColor,
                                fontSize = 14.sp
                            )
                            Text(
                                "Premature withdrawal charge",
                                fontFamily = Poppins,
                                fontSize = 11.sp,
                                color = grayColor
                            )
                        }
                        Text(
                            "- ₹ 250",
                            fontFamily = Poppins,
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(1.dp)) {
                            Text(
                                "Processing Fee",
                                fontFamily = Poppins,
                                color = grayColor,
                                fontSize = 14.sp
                            )
                            Text(
                                "Administrative charges",
                                fontFamily = Poppins,
                                fontSize = 11.sp,
                                color = grayColor
                            )
                        }
                        Text(
                            "- ₹ 100",
                            fontFamily = Poppins,
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(modifier=Modifier.height(10.dp))

                    HorizontalDivider()
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Net Payout",
                            color = darkBlue,
                            fontFamily = Poppins,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            "₹ 1,01,550",
                            color = goldenColor,
                            fontFamily = Poppins,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

                item{
                    Spacer(Modifier.height(16.dp))
                    Box(
                        modifier = Modifier.fillMaxWidth()
                            .background(color = orangeColor.copy(0.1f), shape = RoundedCornerShape(16.dp)).padding(16.dp)
                    ) {

                            Row {
                                Icon(
                                    painter = painterResource(Res.drawable.icon__2_),
                                    contentDescription = "icon", tint = goldenColor
                                )
                                Spacer(Modifier.width(5.dp))
                                Column {
                                    Text(
                                        "What happens next?",
                                        fontWeight = FontWeight.SemiBold,
                                        color = darkBlue,
                                        fontSize = 13.sp
                                    )

                                    Text(
                                        "Amount will be credited to your bank account in 2-3 business days",
                                        fontSize = 12.sp,
                                        fontFamily = Poppins,
                                        color = grayColor
                                    )
                                    Text(
                                        "You will receive an SMS and email confirmation",
                                        fontSize = 12.sp,
                                        fontFamily = Poppins,
                                        color = grayColor
                                    )
                                }
                            }
                    }
                    Spacer(modifier=Modifier.height(60.dp))

                }
            }

        }

