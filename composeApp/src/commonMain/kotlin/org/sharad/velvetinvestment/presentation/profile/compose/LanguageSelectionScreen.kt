package org.sharad.velvetinvestment.presentation.profile.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.sharad.velvetinvestment.shared.compose.BarHeader
import org.sharad.velvetinvestment.utils.theme.Poppins
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.arrowback_elements
import velvet.composeapp.generated.resources.custom
import velvet.composeapp.generated.resources.education
import velvet.composeapp.generated.resources.elements
import velvet.composeapp.generated.resources.marriage
import velvet.composeapp.generated.resources.retirement

@Preview(showSystemUi = true)
@Composable
fun LanguageSelectionScreen(){
    Column(Modifier.padding(16.dp)){
        Spacer(modifier = Modifier.height(30.dp))
       Row (modifier=Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically){
           Icon(painter = painterResource(Res.drawable.arrowback_elements), contentDescription = "Arrow Back Icon")
           Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
               Text("Financial Goals", fontWeight = FontWeight.Bold, fontSize = 24.sp, color = Color(0xFF273E71))

           }
       }
        Spacer(modifier = Modifier.height(30.dp))
        Text(text="What are you Saving For?", fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
        Spacer(modifier = Modifier.height(15.dp))
        Text("Select the goal to personalize your investment strategy", fontSize = 16.sp,color=Color.Gray)
        Box(modifier=Modifier.height(70.dp).fillMaxWidth().background(color = Color.White, shape = RoundedCornerShape(10.dp)).shadow(1.dp,RoundedCornerShape(10.dp), ambientColor = Color.LightGray, spotColor = Color.LightGray), contentAlignment = Alignment.CenterStart){
            Row(modifier = Modifier.padding(16.dp)) {
                Icon(painter = painterResource(Res.drawable.elements), contentDescription = "Wealth icon", tint = Color(0xFFFF9D00), modifier = Modifier.background(color = Color(0xFFFF9D00).copy(0.1f), shape = RoundedCornerShape(8.dp)).padding(8.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Column{
                    Text("Wealth Building", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                    Text("Saving for a down payment", color = Color.Gray)

            }


            }

        }
        Spacer(modifier = Modifier.height(15.dp))

        Box(modifier=Modifier.height(70.dp).fillMaxWidth().background(color = Color.White, shape = RoundedCornerShape(10.dp)).shadow(1.dp,RoundedCornerShape(10.dp), ambientColor = Color(0xFF9DBCD4), spotColor = Color(0xFF9DBCD4)), contentAlignment = Alignment.CenterStart){
            Row(modifier = Modifier.padding(16.dp)) {
                Icon(painter = painterResource(Res.drawable.retirement), contentDescription = "retirement", tint = Color(0xFF4881FF), modifier = Modifier.background(color = Color(0xFF4881FF).copy(0.1f), shape = RoundedCornerShape(8.dp)).padding(8.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Column{
                    Text("Retirement", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                    Text("Building long-term financial security", color = Color.Gray)

                }


            }

        }
        Spacer(modifier = Modifier.height(15.dp))

        Box(modifier=Modifier.height(70.dp).fillMaxWidth().background(color = Color.White, shape = RoundedCornerShape(10.dp)).shadow(1.dp,RoundedCornerShape(10.dp), ambientColor = Color(0xFF9DBCD4), spotColor = Color(0xFF9DBCD4)), contentAlignment = Alignment.CenterStart){
            Row(modifier = Modifier.padding(16.dp)) {
                Icon(painter = painterResource(Res.drawable.education), contentDescription = "education icon", tint = Color(0xFF008E23), modifier = Modifier.background(color = Color(0xFF008E23).copy(0.1f), shape = RoundedCornerShape(8.dp)).padding(8.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Column{
                    Text("Education", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                    Text("Investing in the future", color = Color.Gray)

                }


            }

        }
        Spacer(modifier = Modifier.height(15.dp))

        Box(modifier=Modifier.height(70.dp).fillMaxWidth().background(color = Color.White, shape = RoundedCornerShape(10.dp)).shadow(1.dp,RoundedCornerShape(10.dp), ambientColor = Color(0xFF9DBCD4), spotColor = Color(0xFF9DBCD4)), contentAlignment = Alignment.CenterStart){
            Row(modifier = Modifier.padding(16.dp)) {
                Icon(painter = painterResource(Res.drawable.marriage), contentDescription = "marriage icon", tint = Color(0xFFD2B077), modifier = Modifier.background(color = Color(0xFFD2B077).copy(0.1f), shape = RoundedCornerShape(8.dp)).padding(8.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Column{
                    Text("Marriage", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                    Text("Plainning for your dream wedding", color = Color.Gray)

                }


            }

        }
Spacer(modifier = Modifier.height(15.dp))
        Box(modifier=Modifier.height(70.dp).fillMaxWidth().background(color = Color.White, shape = RoundedCornerShape(10.dp)).shadow(1.dp,RoundedCornerShape(10.dp), ambientColor = Color(0xFF9DBCD4), spotColor = Color(0xFF9DBCD4)), contentAlignment = Alignment.CenterStart){
            Row(modifier = Modifier.padding(16.dp)) {
                Icon(painter = painterResource(Res.drawable.custom), contentDescription = "custom icon", tint = Color(0xFF273E71), modifier = Modifier.background(color = Color(0xFF273E71).copy(0.1f), shape = RoundedCornerShape(8.dp)).padding(8.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Column{
                    Text("Custom Goal", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                    Text("Create your own personalized goal", color = Color.Gray)

                }


            }

        }
    }
}