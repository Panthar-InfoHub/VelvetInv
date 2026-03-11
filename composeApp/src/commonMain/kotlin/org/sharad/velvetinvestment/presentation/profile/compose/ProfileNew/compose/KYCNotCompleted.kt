package org.sharad.velvetinvestment.presentation.profile.compose.ProfileNew.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.appGreen
import org.sharad.emify.core.ui.theme.blueColor
import org.sharad.emify.core.ui.theme.goldenColor
import org.sharad.emify.core.ui.theme.grayColor
import org.sharad.velvetinvestment.utils.theme.Poppins
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.arrowback_elements
import velvet.composeapp.generated.resources.camera
import velvet.composeapp.generated.resources.document_kyc_icon
import velvet.composeapp.generated.resources.kyc_not_complete_icon
import velvet.composeapp.generated.resources.kyc_person_icon
import velvet.composeapp.generated.resources.tick_inside_circle
import velvet.composeapp.generated.resources.upload_verification


@Composable

fun KYCNotCompletedScreen() {
    Column(
        modifier = Modifier.fillMaxSize().background(color = Color.White)
            .navigationBarsPadding().statusBarsPadding()
    ) {
        Row {
            Icon(
                painter = painterResource(Res.drawable.arrowback_elements),
                contentDescription = "profile icon",
                tint = Color.Black,
                modifier = Modifier.padding(16.dp)
            )
        }
        LazyColumn(
            modifier = Modifier.fillMaxWidth().weight(1f).padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Box(
                    modifier = Modifier.background(
                        color = Color(0xffFFEDD4), shape = CircleShape
                    ).padding(20.dp)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.kyc_not_complete_icon),
                        contentDescription = "tick icon",
                        tint = Color(0xffF54900)
                    )
                }
            }
            item {
                Text(
                    "KYC Not Completed",
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 24.sp
                )

            }

            item {
                Text(
                    "Complete your KYC verification to start investing in Fixed Deposits and other financial products.",
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
                        ).padding(12.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            "Complete These Steps",
                            color = grayColor,
                            fontFamily = Poppins,
                            fontSize = 14.sp,
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Icon(
                                painter = painterResource(Res.drawable.document_kyc_icon),
                                contentDescription = "document icon",
                                tint = Color(0xffF54900),
                                modifier = Modifier.border(
                                    2.dp, color = Color(0xffFFEDD4),
                                    shape = RoundedCornerShape(10.dp)
                                ).background(color = Color.White, shape = RoundedCornerShape(10.dp))
                                    .padding(8.dp)
                            )
                            Column {
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                     Text(
                                        "Personal Details",
                                        color = Color.Black,
                                        fontFamily = Poppins,
                                        fontSize = 14.sp
                                    )
                                    PendingText()
                                }
                                Text(
                                    "Provide your basic information",
                                    fontSize = 12.sp,
                                    fontFamily = Poppins,
                                    color = grayColor
                                )
                            }
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Icon(
                                painter = painterResource(Res.drawable.camera),
                                contentDescription = "camera icon",
                                tint = Color(0xffF54900),
                                modifier = Modifier.border(
                                    2.dp, color = Color(0xffFFEDD4),
                                    shape = RoundedCornerShape(10.dp)
                                ).background(color = Color.White, shape = RoundedCornerShape(10.dp))
                                    .padding(8.dp)

                            )
                            Column {
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Text(
                                        "Document Upload",
                                        color = Color.Black,
                                        fontFamily = Poppins,
                                        fontSize = 14.sp
                                    )
                                    PendingText()
                                }
                                Text("Upload Aadhar/PAN card", fontSize = 12.sp, fontFamily = Poppins, color = grayColor)

                            }
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Icon(
                                painter = painterResource(Res.drawable.upload_verification),
                                contentDescription = "verification icon",
                                tint = Color(0xffF54900),
                                modifier = Modifier.border(
                                    2.dp, color = Color(0xffFFEDD4),
                                    shape = RoundedCornerShape(10.dp)
                                ).background(color = Color.White, shape = RoundedCornerShape(10.dp))
                                    .padding(8.dp)

                            )
                            Column {
                                Column {
                                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        Text(
                                            "Personal Details",
                                            color = Color.Black,
                                            fontFamily = Poppins,
                                            fontSize = 14.sp
                                        )
                                        PendingText()
                                    }
                                    Text("Provide your basic information", fontSize = 12.sp, fontFamily = Poppins, color = grayColor)
                                }
                            }

                        }
                    }
                }
            }


            item {
                Box(modifier = Modifier.padding(16.dp).fillMaxWidth().background(color = Color(0xffEFF6FF), shape = RoundedCornerShape(16.dp)).padding(16.dp)) {
                    Column (verticalArrangement = Arrangement.spacedBy(8.dp)){
                        Text("Document Required", fontFamily = Poppins, fontSize = 14.sp)
                        DocumentRequiredListText("PAN Card")
                        DocumentRequiredListText("Aadhar Card")
                        DocumentRequiredListText("Driving Licence")

                    }
                }
            }
        }

        ButtonKYC()

    }
}

@Composable
fun PendingText() {
    Text(
        "Pending",
        fontSize = 12.sp,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        fontFamily = Poppins,
        color = Color(0xffF54900),
        modifier = Modifier.background(Color(0xffFFEDD4), shape = RoundedCornerShape(23552200.dp))
            .padding(vertical = 2.dp, horizontal = 6.dp)
    )

}

@Composable
fun DocumentRequiredListText(text:String){
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(painter = painterResource(Res.drawable.tick_inside_circle), contentDescription = "Tick", tint = Color(0xff155DFC), modifier = Modifier.size(15.dp))
        Text(text, fontSize = 14.sp, fontFamily = Poppins)
    }
}

@Composable
fun ButtonKYC(){

    Box(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(horizontal = 12.dp, vertical = 20.dp).fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            TextButton(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Primary,
                    contentColor = Color.White
                ),
                modifier = Modifier.height(50.dp).fillMaxWidth()
            ) {
                Text(
                    "Start KYC Process",
                    fontFamily = Poppins,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
            TextButton(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = goldenColor
                ),
                modifier = Modifier.height(50.dp).fillMaxWidth()
                    .border(0.7.dp, color = goldenColor, shape = RoundedCornerShape(50.dp))
            ) {
                Text(
                    "I'll Do It Later",
                    fontFamily = Poppins,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}