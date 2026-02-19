package org.sharad.velvetinvestment.shared.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.greenColor
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.rupeesign

@Preview(showBackground = true)
@Composable
fun GoalsCard(color:Color) {
    Card(
        colors = CardDefaults.cardColors(Color.White),
        modifier = Modifier.fillMaxWidth().padding(10.dp)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                "Travel Goals",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF5A5E60)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        "Today's Cost",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = (Color(0xFF5A5E60))
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(Res.drawable.rupeesign),
                            contentDescription = "RupeesIcon",
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            "1,00000",
                            color = Color.Black,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "Target",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = (Color(0xFF5A5E60))
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "2030",
                        color = Color(0xFFFF0600),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold
                    )


                }

                Column {
                    Text(
                        "Future Value",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF5A5E60)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(Res.drawable.rupeesign),
                            contentDescription = "RupeesIcon",
                            tint = color,
                            modifier = Modifier.size(15.dp)
                        )
                        Text(
                            "133,822.558",
                            textAlign = TextAlign.End,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color =color
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "Monthly SIP",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF5A5E60)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            painter = painterResource(Res.drawable.rupeesign),
                            contentDescription = "RupeesIcon",
                            tint = greenColor,
                            modifier = Modifier.size(15.dp)
                        )

                        Text(
                            "1,665",
                            textAlign = TextAlign.End,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = greenColor
                        )

                    }

                }

            }


            Spacer(modifier = Modifier.height(10.dp))
            Text("Feasibility Score", fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Spacer(modifier = Modifier.height(10.dp))

            Box(
                modifier = Modifier.height(12.dp).fillMaxWidth()
                    .background(color = Color(0xFFD9D9D9), shape = RoundedCornerShape(14.dp))
            ) {
                Box(
                    modifier = Modifier.fillMaxHeight().fillMaxWidth(0.6f)
                        .background(color = greenColor, shape = RoundedCornerShape(12.dp))
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Progress", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        painter = painterResource(Res.drawable.rupeesign),
                        contentDescription = "RupeesIcon",
                        modifier = Modifier.size(12.dp)
                    )
                    Text(" 0 / ", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    Icon(
                        painter = painterResource(Res.drawable.rupeesign),
                        contentDescription = "RupeesIcon",
                        modifier = Modifier.size(12.dp)
                    )
                    Text(" 1L", fontSize = 16.sp, fontWeight = FontWeight.Medium)

                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier.height(12.dp).fillMaxWidth()
                    .background(color = Color(0xFFD9D9D9), shape = RoundedCornerShape(14.dp))
            ) {
                Box(
                    modifier = Modifier.fillMaxHeight().fillMaxWidth(0.0f)
                        .background(color = greenColor, shape = RoundedCornerShape(12.dp))
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalDivider()
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Increased By", fontSize = 16.sp, color = Color(0xFF5A5E60))
                    Icon(
                        painter = painterResource(Res.drawable.rupeesign),
                        contentDescription = "RupeesIcon", tint = Color(0xFF5A5E60),
                        modifier = Modifier.size(12.dp)
                    )
                    Text("5K", fontSize = 16.sp, color = Color(0xFF5A5E60))
                }

                Row(verticalAlignment = Alignment.CenterVertically) {

                    Text("Req.monthly:", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    Icon(
                        painter = painterResource(Res.drawable.rupeesign),
                        contentDescription = "RupeesIcon", tint = greenColor,
                        modifier = Modifier.size(12.dp)
                    )
                    Text("1,665", color = greenColor, fontSize = 16.sp)
                }

            }
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}