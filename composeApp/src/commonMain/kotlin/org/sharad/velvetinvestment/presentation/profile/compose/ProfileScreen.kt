package org.sharad.velvetinvestment.presentation.profile.compose

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.ktor.client.plugins.observer.ResponseObserver
import org.jetbrains.compose.resources.painterResource
import org.sharad.velvetinvestment.shared.compose.BarHeader
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.arrowback_elements
import velvet.composeapp.generated.resources.back_icon
import velvet.composeapp.generated.resources.fd_icon
import velvet.composeapp.generated.resources.notification_icon
import velvet.composeapp.generated.resources.profile_icon
import velvet.composeapp.generated.resources.security
import velvet.composeapp.generated.resources.settings_icon
import velvet.composeapp.generated.resources.signupelement
import velvet.composeapp.generated.resources.termcondition
import velvet.composeapp.generated.resources.verification

@Preview( showSystemUi = true)
@Composable
fun ProfileScreen(){
    LazyColumn {
        item {   Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(30.dp))
            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically){
                Icon(painter = painterResource(Res.drawable.arrowback_elements), contentDescription = "ArrowBackIcon",Modifier.size(24.dp))
                Text("Profile", fontWeight = FontWeight.Bold, fontSize = 24.sp,color=Color(0xFF273E71))
                Box(modifier=Modifier.background(color=Color.White, shape = CircleShape).padding(10.dp)){ Icon(painter = painterResource(Res.drawable.settings_icon), contentDescription = "Setting Icon",tint=Color(0xFFD8AF6B))
                }
            }
            Spacer(modifier=Modifier.height(40.dp))
            Box(modifier = Modifier.size(120.dp).background(color = Color.LightGray, shape = CircleShape).align(
                Alignment.CenterHorizontally), contentAlignment = Alignment.Center){}
            Spacer(modifier=Modifier.height(15.dp))
            Text("Pooja Sharma", fontWeight = FontWeight.SemiBold, fontSize = 26.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Box(modifier=Modifier.padding(2.dp).background(color = Color(0xFF008E23).copy(0.1f), shape = RoundedCornerShape(20.dp)).padding(horizontal = 15.dp, vertical = 10.dp)){
                Row( horizontalArrangement = Arrangement.spacedBy(4.dp)){
                    Icon(painter = painterResource(Res.drawable.verification), contentDescription = "Kyc icon", tint =Color(0xFF008E23) )
                    Text(text="KYC VERIFIED", fontWeight = FontWeight.Bold, fontSize = 12.sp,color = Color(0xFF008E23))

                }
            }

            Spacer(modifier=Modifier.height(50.dp))
            BarHeader(modifier = Modifier.fillMaxWidth(),"Account Setting")
            Spacer(modifier = Modifier.height(15.dp))
            Card(colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation( defaultElevation = 2.dp)) {
                Column(modifier = Modifier.padding(16.dp)){
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically){
                            Icon(painter = painterResource(Res.drawable.profile_icon), contentDescription = "Personal Information Icon",modifier=Modifier.background(color = Color(0xFFDEE2F6), shape = RoundedCornerShape(8.dp)).padding(8.dp))
                            Spacer(modifier=Modifier.width(24.dp))
                            Column{
                                Text("Personal Information", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                                Text("Edit your details and contact info", color = Color.DarkGray)
                            }


                        }
                        Icon(painter = painterResource(Res.drawable.back_icon), contentDescription = "Forward Icon",tint=Color(0xFFD2B077))


                    }
                    Spacer(modifier=Modifier.height(8.dp))
                    HorizontalDivider(color=Color.LightGray)
                    Spacer(modifier=Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically){
                            Icon(painter = painterResource(Res.drawable.fd_icon), contentDescription = "Personal Information Icon",modifier=Modifier.background(color = Color(0xFFDEE2F6), shape = RoundedCornerShape(8.dp)).padding(7.dp))
                            Spacer(modifier=Modifier.width(24.dp))
                            Column{
                                Text("Bank Account", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                                Text("Manage Linked funding source", color = Color.DarkGray)
                            }


                        }
                        Icon(painter = painterResource(Res.drawable.back_icon), contentDescription = "Forward Icon",tint=Color(0xFFD2B077))


                    }
                }

            }
            Spacer(modifier=Modifier.height(20.dp))
            BarHeader(modifier = Modifier.fillMaxWidth(),"Preferences")
            Spacer(modifier=Modifier.height(15.dp))

            Card(colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation( defaultElevation = 2.dp)) {
                Column(modifier = Modifier.padding(16.dp)){
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically){
                            Icon(painter = painterResource(Res.drawable.notification_icon), contentDescription = "Personal Information Icon",modifier=Modifier.background(color = Color(0xFFDEE2F6), shape = RoundedCornerShape(8.dp)).padding(8.dp))
                            Spacer(modifier=Modifier.width(24.dp))
                            Column{
                                Text("Notifications", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                                Text("Alert,updates & news", color = Color.DarkGray)
                            }


                        }
                        Icon(painter = painterResource(Res.drawable.back_icon), contentDescription = "Forward Icon",tint=Color(0xFFD2B077))


                    }
                }

            }

            Spacer(modifier=Modifier.height(20.dp))
            BarHeader(modifier=Modifier.fillMaxWidth(),"Legal")
            Spacer(modifier=Modifier.height(15.dp))

            Card(colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation( defaultElevation = 2.dp)) {
                Column(modifier = Modifier.padding(16.dp)){
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically){
                            Icon(painter = painterResource(Res.drawable.termcondition), contentDescription = "Personal Information Icon",modifier=Modifier.background(color = Color(0xFFDEE2F6), shape = RoundedCornerShape(8.dp)).padding(8.dp))
                            Spacer(modifier=Modifier.width(24.dp))
                            Column{
                                Text("Terms & Condiftion", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                                }


                        }
                        Icon(painter = painterResource(Res.drawable.back_icon), contentDescription = "Forward Icon",tint=Color(0xFFD2B077))


                    }
                    Spacer(modifier=Modifier.height(8.dp))
                    HorizontalDivider(color=Color.LightGray)
                    Spacer(modifier=Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically){
                            Icon(painter = painterResource(Res.drawable.security), contentDescription = "Personal Information Icon",modifier=Modifier.background(color = Color(0xFFDEE2F6), shape = RoundedCornerShape(8.dp)).padding(7.dp))
                            Spacer(modifier=Modifier.width(24.dp))
                            Column{
                                Text("Privacy Policy", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                                }


                        }
                        Icon(painter = painterResource(Res.drawable.back_icon), contentDescription = "Forward Icon",tint=Color(0xFFD2B077))


                    }
                }

            }


            Spacer(modifier=Modifier.height(40.dp))

            Row(modifier=Modifier.fillMaxWidth().padding(horizontal = 35.dp)) {
                Button(onClick = {}, colors = ButtonDefaults.buttonColors(containerColor =Color( 0xFFFF0600).copy(0.1f),contentColor = Color(0xFFFF0600)),modifier=Modifier.weight(1f).height(50.dp)){
                    Icon(painter = painterResource(Res.drawable.signupelement), contentDescription = "Signup Button Icon")
                    Spacer(modifier=Modifier.width(5.dp
                    ))
                    Text("Sign Out", fontSize = 20.sp)
                }

            }
            Spacer(modifier=Modifier.height(60.dp))
          }

        }
       }
  }