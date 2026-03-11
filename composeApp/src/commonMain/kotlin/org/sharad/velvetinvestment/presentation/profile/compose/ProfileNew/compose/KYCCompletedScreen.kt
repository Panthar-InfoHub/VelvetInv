package org.sharad.velvetinvestment.presentation.profile.compose.ProfileNew.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.appGreen
import org.sharad.emify.core.ui.theme.appOrange
import org.sharad.emify.core.ui.theme.blueColor
import org.sharad.emify.core.ui.theme.darkBlue
import org.sharad.emify.core.ui.theme.grayColor
import org.sharad.velvetinvestment.utils.theme.Poppins
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.arrowback_elements
import velvet.composeapp.generated.resources.document_kyc_icon
import velvet.composeapp.generated.resources.kyc_person_icon
import velvet.composeapp.generated.resources.nav_icon_profile
import velvet.composeapp.generated.resources.profile_icon
import velvet.composeapp.generated.resources.tick_inside_circle

@Preview(showSystemUi = true)
@Composable
fun KYCCompletedScreen() {
    Column (modifier = Modifier.fillMaxSize().padding(16.dp).background(color = Color.White).navigationBarsPadding().statusBarsPadding()){
        Row {
            Icon(
                painter = painterResource(Res.drawable.arrowback_elements),
                contentDescription = "profile icon",
                tint = Color.Black
            )
        }
        LazyColumn(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Box(
                    modifier = Modifier.background(
                        color = Color(0xff00C950), shape = CircleShape
                    ).padding(16.dp)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.tick_inside_circle),
                        contentDescription = "tick icon",
                        tint = Color.White
                    )
                }
            }
                  item {
                    Text(
                        "KYC Completed!",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 24.sp
                    )

            }

            item {
                Text(
                    "Your KYC verification has been successfully completed and approved.",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = grayColor,
                    fontFamily = Poppins,
                    textAlign = TextAlign.Center
                )
            }

            item {
                Box(
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                        .background(
                            color = Color(0xffF9FAFB),
                            shape = RoundedCornerShape(16.dp)
                        ).padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            "Verification Details",
                            color = grayColor,
                            fontFamily = Poppins,
                            fontSize = 14.sp,
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Icon(
                                painter = painterResource(Res.drawable.kyc_person_icon),
                                contentDescription = "person icon",
                                tint = Color(0xff155DFC),
                                modifier = Modifier.background(
                                    color = Color(0xff155DFC).copy(0.1f),
                                    shape = RoundedCornerShape(10.dp)
                                ).padding(8.dp)
                            )
                            Column {
                                Text(
                                    "Full Name",
                                    color = grayColor,
                                    fontFamily = Poppins,
                                    fontSize = 12.sp
                                )
                                Text("Rajesh Kumar", fontSize = 14.sp, fontFamily = Poppins)
                            }
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Icon(
                                painter = painterResource(Res.drawable.document_kyc_icon),
                                contentDescription = "document icon",
                                tint = Color(0xff9810FA),
                                modifier = Modifier.background(
                                    color = Color(0xff9810FA).copy(0.1f),
                                    shape = RoundedCornerShape(10.dp)
                                ).padding(8.dp)
                            )
                            Column {
                                Text(
                                    "Document Type",
                                    color = grayColor,
                                    fontFamily = Poppins,
                                    fontSize = 12.sp
                                )
                                Text("Adhar Card", fontSize = 14.sp, fontFamily = Poppins)
                            }
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Icon(
                                painter = painterResource(Res.drawable.kyc_person_icon),
                                contentDescription = "person icon",
                                tint = Color(0xffF54900),
                                modifier = Modifier.background(
                                    color = Color(0xffF54900).copy(0.1f),
                                    shape = RoundedCornerShape(10.dp)
                                ).padding(8.dp)
                            )
                            Column {
                                Text(
                                    "Verified On",
                                    color = grayColor,
                                    fontFamily = Poppins,
                                    fontSize = 12.sp
                                )
                                Text(
                                    "15 Feb 2026", fontSize = 14.sp, fontFamily = Poppins
                                )
                            }
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Icon(
                                painter = painterResource(Res.drawable.kyc_person_icon),
                                contentDescription = "person icon",
                                tint = appGreen,
                                modifier = Modifier.background(
                                    color = appGreen.copy(0.1f),
                                    shape = RoundedCornerShape(10.dp)
                                ).padding(8.dp)
                            )
                            Column {
                                Text(
                                    "Status",
                                    color = grayColor,
                                    fontFamily = Poppins,
                                    fontSize = 12.sp
                                )
                                Text(
                                    "Active & Verified",
                                    fontSize = 14.sp,
                                    fontFamily = Poppins,
                                    color = appGreen
                                )
                            }

                        }
                    }
                }
            }
                item {
                    TextButton(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor = darkBlue, contentColor = Color.White), modifier = Modifier.padding(vertical = 16.dp).fillMaxWidth()) {
                        Text(
                            "Start Investing",
                            fontSize = 18.sp,
                            fontFamily = Poppins,
                            color = Color.White, fontWeight = FontWeight.Medium, modifier = Modifier.padding(4.dp)
                        )
                    }
                }
            }
        }
    }
