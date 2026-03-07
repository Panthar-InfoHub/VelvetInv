package org.sharad.velvetinvestment.presentation.tradingaccount.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.goldenColor
import org.sharad.emify.core.ui.theme.grayColor
import org.sharad.emify.core.ui.theme.orangeColor
import org.sharad.emify.core.ui.theme.redColor
import org.sharad.velvetinvestment.utils.genericDropShadow
import org.sharad.velvetinvestment.utils.theme.Poppins
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.notice

@Preview(showSystemUi = true)
@Composable
fun TAScreen8() {
    LazyColumn(
        modifier = Modifier.fillMaxWidth().padding(16.dp).safeDrawingPadding()
            .navigationBarsPadding(), verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    "Guardian Details",
                    fontSize = 24.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                Text(
                    "Provide guardian information for the minor account holder",
                    fontFamily = Poppins,
                    fontSize = 14.sp,
                    color = Color(0xff4A5565)
                )

            }
        }
        item {
            Box(
                modifier = Modifier.fillMaxWidth()
                    .border(0.7.dp, shape = RoundedCornerShape(10.dp), color = Color(0xffFEE685))
                    .clip(RoundedCornerShape(24.dp)).background(
                        color = Color(0xffFFFBEB)
                    )
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.notice),
                            contentDescription = "notice icon", tint = Color(0xffE17100)
                        )
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                "Guardian PAN Required",
                                fontFamily = Poppins,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp,
                                color = Color(0xff7B3306)
                            )
                            Text(
                                "Guardian's PAN is mandatory for minor accounts",
                                fontFamily = Poppins,
                                fontSize = 14.sp,
                                color = Color(0xffBB4D00)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    "Guardian PAN Number ",
                                    fontFamily = Poppins,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 16.sp, color = Color(0xff364153)
                                )
                                Text(
                                    "*",
                                    fontFamily = Poppins,
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 16.sp, color = redColor
                                )


                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(30.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                KYCButton("ABCDE1234F", onClick = {}, selected = false)
                                KYCButton("Verify PAN", onClick = {}, selected = true)

                            }
                        }


                    }
                }
            }
        }
        item {
            Box(
                modifier = Modifier.genericDropShadow(RoundedCornerShape(24.dp))
                    .background(color = Color.White, shape = RoundedCornerShape(24.dp))
            ) {
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp)).background(
                            color =
                                orangeColor.copy(0.1f)
                        ).padding(16.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.notice),
                                contentDescription = "notice icon", tint = goldenColor
                            )
                            Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
                                Text(
                                    "Regulatory Disclosure",
                                    fontFamily = Poppins,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = grayColor
                                )
                                Text(
                                    "UCC will be rejected outright if PAN and KYC identifiers are inconsistent.",
                                    fontFamily = Poppins,
                                    fontSize = 14.sp,
                                    color = grayColor
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun KYCButton(text: String, onClick: () -> Unit, selected: Boolean) {
    Box(
        Modifier.height(41.dp).border(1.dp, Color.LightGray, shape = RoundedCornerShape(10.dp))
            .background(
                if (selected) Color.LightGray else Color.Transparent,
                shape = RoundedCornerShape(10.dp)
            ).padding(horizontal = 8.dp, vertical = 3.dp).clickable { onClick }, contentAlignment = Alignment.Center) {
        Text(
            text = text,
            lineHeight = 20.sp,
            fontFamily = Poppins,
            color = if (selected) Color.White else Color.LightGray,
            fontSize = if (selected) 18.sp else 14.sp,
            fontWeight = if (selected) {
                FontWeight.Medium
            } else {
                FontWeight.Normal
            }
        )
    }
}