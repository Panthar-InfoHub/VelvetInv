package org.sharad.velvetinvestment.presentation.profile.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.greenColor
import org.sharad.emify.core.ui.theme.redColor
import org.sharad.velvetinvestment.shared.compose.BarHeader
import org.sharad.velvetinvestment.shared.compose.ScreenHeader
import org.sharad.velvetinvestment.utils.genericDropShadow
import org.sharad.velvetinvestment.utils.theme.Poppins
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.axis_bank
import velvet.composeapp.generated.resources.down_stock
import velvet.composeapp.generated.resources.sbi_tree
import velvet.composeapp.generated.resources.up_stock

@Preview(showSystemUi = true)
@Composable
fun DashboardScreen(){
    Column(modifier=Modifier.fillMaxSize().padding(16.dp)) {
ScreenHeader("Mutual Funds")
        BarHeader(modifier = Modifier.fillMaxWidth(),"Dashboard")
        Spacer(modifier=Modifier.height(20.dp))
    Box(
        modifier = Modifier.fillMaxWidth().height(225.dp).genericDropShadow(RoundedCornerShape(10.dp)).clip(
            RoundedCornerShape(10.dp)
        )
            .background(Color.White, shape = RoundedCornerShape(10.dp))
            .padding(16.dp)
    ){
        Column {
        Text("Total Investment(2)", fontSize = 16.sp, color = Color(0xFF5A5E60), fontFamily = Poppins, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(20.dp))

        Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween){
            Column{

                Text("Current Value", fontSize = 14.sp, color = Color(0xFF5A5E60), fontFamily = Poppins)
                Spacer(Modifier.height(10.dp))
                Spacer(modifier = Modifier.height(10.dp))
                Text("₹ 20,000", color = Color.Black, fontSize = 16.sp, fontFamily = Poppins, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(20.dp))
                Text("Invested Amount", fontFamily = Poppins, color = Color(0xFF5A5E60), fontSize = 14.sp)
                Spacer(modifier = Modifier.height(10.dp))
                Text("₹ 15,000", color = Color.Black, fontSize = 16.sp, fontFamily = Poppins, fontWeight = FontWeight.SemiBold)
            }
            Column {
                Text("Total Returns", fontFamily = Poppins, color = Color(0xFF5A5E60), fontSize = 14.sp, modifier = Modifier.align(
                    Alignment.End))
                Spacer(Modifier.height(10.dp))

                Text("+ ₹ 5000 (22.4%)", color = greenColor, fontWeight = FontWeight.SemiBold, fontSize = 16.sp, fontFamily = Poppins, modifier = Modifier.align(
                    Alignment.End))
                Spacer(modifier=Modifier.height(16.dp))
                Text("ID returns", fontFamily = Poppins, fontSize = 14.sp, color = Color(0xFF5A5E60), modifier = Modifier.align(
                    Alignment.End))
                Spacer(modifier = Modifier.height(10.dp))
                Text("+ ₹ 15,500(22.4%)", color = greenColor, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, fontFamily = Poppins, modifier = Modifier.align(
                    Alignment.End))

            }
        }
        }
    }
Spacer(modifier = Modifier.height(20.dp))
        BarHeader(modifier = Modifier.fillMaxWidth(),"Your Investments")
        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier = Modifier.fillMaxWidth()
                .height(101.dp)

                .genericDropShadow()
                .clip(shape = RoundedCornerShape(10.dp))
                .background(color = Color.White),
            contentAlignment = Alignment.Center
        ) {
            Column {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 16.dp),
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
                            "₹ 5000",
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
                Spacer(modifier = Modifier.height(10.dp))
                Row (modifier = Modifier.fillMaxWidth().padding(horizontal = 10.dp), verticalAlignment = Alignment.CenterVertically) {
                    Spacer(modifier = Modifier.width(58.dp))
                    Text(
                        "Next due on 05 Feb 2026", modifier = Modifier.weight(1f),
                        fontFamily = Poppins,
                        color = Color.Gray,
                        fontSize = 14.sp
                    )




                    Text(
                        "SIP",
                        fontFamily = Poppins,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
                    }






        }
        Spacer(modifier=Modifier.height(16.dp))


           MFholdingSummary(
               Res.drawable.axis_bank,
               "Axis Small Cap Fund",
               "Small Cap",
               "10000",
               "2.4%",
               Res.drawable.down_stock,
               redColor
           )
       }
}

