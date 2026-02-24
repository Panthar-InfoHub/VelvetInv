package org.sharad.velvetinvestment.presentation.profile.compose

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.ktor.client.request.invoke
import io.ktor.util.sha1
import org.jetbrains.compose.resources.painterResource
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.arrowback_elements

@Preview(showSystemUi = true)
@Composable
fun AnnualIcome(){
    Column(modifier = Modifier.padding(16.dp)) {
        Spacer(modifier = Modifier.height(30.dp))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(Res.drawable.arrowback_elements),
                contentDescription = "ArrowBackIcon"
            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text(
                    text = "Annual Income",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF273E71)
                )
            }
        }
        Spacer(modifier=Modifier.height(25.dp))
        Text("What's your annual Income", fontWeight = FontWeight.Medium, fontSize = 20.sp)
        Spacer(modifier=Modifier.height(30.dp))
        Box(modifier=Modifier.height(60.dp).border(1.dp, color = Color(0xFFD2B077), shape = RoundedCornerShape(12.dp)).fillMaxWidth().background(color = Color.White, shape = RoundedCornerShape(12.dp)), contentAlignment = Alignment.CenterStart){
            Text("More than 5 Crores", fontWeight = FontWeight.Medium, fontSize = 18.sp, modifier = Modifier.padding(16.dp))
        }
        Spacer(modifier = Modifier.height(20.dp))


        Box(modifier=Modifier.height(60.dp).border(1.dp, color = Color(0xFFD2B077), shape = RoundedCornerShape(12.dp)).fillMaxWidth().background(color = Color.White, shape = RoundedCornerShape(12.dp)), contentAlignment = Alignment.CenterStart){
            Text("More than 5 Crores", fontWeight = FontWeight.Medium, fontSize = 18.sp, modifier = Modifier.padding(16.dp))
        }
        Spacer(modifier = Modifier.height(20.dp))


        Box(modifier=Modifier.height(60.dp).border(1.dp, color = Color(0xFFD2B077), shape = RoundedCornerShape(12.dp)).fillMaxWidth().background(color = Color.White, shape = RoundedCornerShape(12.dp)), contentAlignment = Alignment.CenterStart){
            Text("More than 5 Crores", fontWeight = FontWeight.Medium, fontSize = 18.sp, modifier = Modifier.padding(16.dp))
        }
        Spacer(modifier = Modifier.height(20.dp))


        Box(modifier=Modifier.height(60.dp).border(1.dp, color = Color(0xFFD2B077), shape = RoundedCornerShape(12.dp)).fillMaxWidth().background(color = Color.White, shape = RoundedCornerShape(12.dp)), contentAlignment = Alignment.CenterStart){
            Text("1 Crore - 5 Crore", fontWeight = FontWeight.Medium, fontSize = 18.sp, modifier = Modifier.padding(16.dp))
        }
        Spacer(modifier = Modifier.height(20.dp))


        Box(modifier=Modifier.height(60.dp).border(1.dp, color = Color(0xFFD2B077), shape = RoundedCornerShape(12.dp)).fillMaxWidth().background(color = Color.White, shape = RoundedCornerShape(12.dp)), contentAlignment = Alignment.CenterStart){
            Text("25 Lack - 50 Lack", fontWeight = FontWeight.Medium, fontSize = 18.sp, modifier = Modifier.padding(16.dp))
        }
        Spacer(modifier = Modifier.height(20.dp))


        Box(modifier=Modifier.height(60.dp).border(1.dp, color = Color(0xFFD2B077), shape = RoundedCornerShape(12.dp)).fillMaxWidth().background(color = Color.White, shape = RoundedCornerShape(12.dp)), contentAlignment = Alignment.CenterStart){
            Text("5 Lack - 10 Lakh", fontWeight = FontWeight.Medium, fontSize = 18.sp, modifier = Modifier.padding(16.dp))
        }
        Spacer(modifier = Modifier.height(20.dp))


        Box(modifier=Modifier.height(60.dp).border(1.dp, color = Color(0xFFD2B077), shape = RoundedCornerShape(12.dp)).fillMaxWidth().background(color = Color.White, shape = RoundedCornerShape(12.dp)), contentAlignment = Alignment.CenterStart){
            Text("1 Lack - 5 Lakh", fontWeight = FontWeight.Medium, fontSize = 18.sp, modifier = Modifier.padding(16.dp))
        }
        Spacer(modifier = Modifier.height(20.dp))


        Box(modifier=Modifier.height(60.dp).border(1.dp, color = Color(0xFFD2B077), shape = RoundedCornerShape(12.dp)).fillMaxWidth().background(color = Color.White, shape = RoundedCornerShape(12.dp)), contentAlignment = Alignment.CenterStart){
            Text("Upto 1 Lakh", fontWeight = FontWeight.Medium, fontSize = 18.sp, modifier = Modifier.padding(16.dp))
        }
        Spacer(modifier = Modifier.height(20.dp))

    }
}