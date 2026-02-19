package org.sharad.velvetinvestment.presentation.profile.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import org.sharad.emify.core.ui.theme.darkBlue
import org.sharad.emify.core.ui.theme.goldenColor
import org.sharad.emify.core.ui.theme.grayColor
import org.sharad.emify.core.ui.theme.greenColor
import org.sharad.emify.core.ui.theme.redColor
import org.sharad.velvetinvestment.shared.compose.ScreenHeader
import org.sharad.velvetinvestment.utils.genericDropShadow
import org.sharad.velvetinvestment.utils.theme.Poppins
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.sbi
import velvet.composeapp.generated.resources.sbi_tree
import velvet.composeapp.generated.resources.up_stock
@Preview(showBackground = true)
@Composable
fun SIPdetailScreen(){
    LazyColumn(modifier=Modifier.padding(16.dp)) {
        item {
            Row {
                Box(modifier = Modifier.weight(1f)) {
                    ScreenHeader("SIP Details")
                }
                Text("Cancel SIP", color = goldenColor, fontFamily = Poppins, fontSize = 16.sp)
            }
        }
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier.genericDropShadow(RoundedCornerShape(10.dp))
                    .clip(RoundedCornerShape(10.dp))
                    .background(color = Color.White, shape = RoundedCornerShape(10.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Box(
                        modifier = Modifier.fillMaxWidth()
                            .height(80.dp)
                            .background(color = Color.White),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = painterResource(Res.drawable.sbi_tree),
                                contentDescription = "sbi icon",
                                modifier = Modifier.size(44.dp)
                            )
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    "SBI Gold Fund",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 16.sp,
                                    fontFamily = Poppins
                                )
                                Text("Commodities Gold", color = Color.Gray)


                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                                Text(
                                    "₹ 5,000",
                                    fontSize = 16.sp,
                                    fontFamily = Poppins,
                                    fontWeight = FontWeight.SemiBold
                                )

                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        painter = painterResource(Res.drawable.up_stock),
                                        contentDescription = "upstock icon",
                                        tint = greenColor
                                    )
                                    Text("22.4%", fontSize = 14.sp, color = greenColor)
                                }


                            }

                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    PortfolioDetailList()
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))

            Box(
                Modifier.fillMaxWidth().genericDropShadow(RoundedCornerShape(10.dp))
                    .clip(RoundedCornerShape(10.dp))
                    .background(color = Color.White, shape = RoundedCornerShape(10.dp))
                    .padding(16.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(32.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Next Installment", color = grayColor, fontSize = 14.sp)
                        Text(
                            "05 Feb 2026",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            fontFamily = Poppins
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("SIP ID", color = grayColor, fontSize = 14.sp)
                        Text(
                            "SIP12345678",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            fontFamily = Poppins
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Autopay ID", color = grayColor, fontSize = 14.sp)
                        Text(
                            "A7F9C2D4E1B8M90",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            fontFamily = Poppins
                        )
                    }
                }

            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier.fillMaxWidth().genericDropShadow(RoundedCornerShape(10.dp))
                    .clip(RoundedCornerShape(10.dp)).background(color = Color.White).padding(16.dp)
            ) {
                Column {
                    Text(
                        "Back Account",
                        color = grayColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = Poppins
                    )

                    Row(
                        modifier = Modifier.padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.sbi),
                            contentDescription = "upi image icon",
                            modifier = Modifier.size(59.dp)
                        )

                        Text(
                            "STATE BANK OF INDIA....1234",
                            color = Color.DarkGray,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = Poppins
                        )

                    }
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier.genericDropShadow(RoundedCornerShape(10.dp))
                    .clip(RoundedCornerShape(10.dp)).background(color = Color.White).padding(16.dp)
            ) {
                Column(
                    Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    Text(
                        "Transection History",
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = Poppins,
                        fontSize = 16.sp,
                        color = darkBlue
                    )
                    Column {
                        Text(
                            "6th Installment",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = Poppins,
                            color = Color.Black
                        )
                        Text(
                            "20 Jan 2026 at 10:30 am",
                            fontSize = 14.sp,
                            fontFamily = Poppins,
                            color = grayColor
                        )
                    }

                    Column {
                        Text(
                            "Failed",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            fontFamily = Poppins,
                            color = Color.Black
                        )
                        Text(
                            "20 Jan 2026 at 10:30 am",
                            fontSize = 14.sp,
                            fontFamily = Poppins,
                            color = grayColor
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                }
            }
        }
        }

    }


@Composable
fun PortfolioDetailList()
{

    // 1. Data structure to keep things organized
    val details = listOf(
        DetailItem("Return", "- 60 (6.00%)", redColor),
        DetailItem("Day returns", "- 40.05 (4.09%)", redColor),
        DetailItem("XIRR", "- 19.11%", Color.Black),
        DetailItem("Current NAV", "137.42", Color.Black),
        DetailItem("Avg NAV", "146.32", Color.Black),
        DetailItem("Folio no.", "22265704/95", Color.Black),
        DetailItem("Balance Units", "6.834", Color.Black)
    )

    // 2. Parent Column
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp) // Gap between rows
    ) {
        details.forEach { item ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically // Ensures 14sp and 16sp align nicely
            ) {
                Text(
                    text = item.label,
                    fontFamily = Poppins,
                    fontSize = 14.sp,
                    color = grayColor
                )
                Text(
                    text = item.value,
                    color = item.valueColor,
                    fontSize = 16.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

data class DetailItem(
    val label: String,
    val value: String,
    val valueColor: Color = Color.Black // Default color black rahega
)