package org.sharad.velvetinvestment.presentation.profile.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.ktor.client.request.invoke
import org.jetbrains.compose.resources.painterResource
import org.sharad.velvetinvestment.utils.theme.Poppins
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.arrowback_elements
import velvet.composeapp.generated.resources.penvector

@Preview(showSystemUi = true)
@Composable
fun PersonalInformationScreen(){
    var name by remember { mutableStateOf("Pooja Sharma") }
    var dateOfBirth by remember { mutableStateOf("**/**/2002")}
    var mobileNumber by remember { mutableStateOf("*****90909")}
    var emailAddress by remember { mutableStateOf("poo*****01@gmail.com") }
    var panNumber by remember { mutableStateOf("******701H")}
    var incomeRange by remember { mutableStateOf("Below 1 Lac")}
    Column (modifier = Modifier.padding(16.dp)){
        Spacer(modifier = Modifier.height(30.dp))
        Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(Res.drawable.arrowback_elements),
                contentDescription = "ArrowBackIcon"
            )
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Text(
                    text = "Personal Information",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF273E71)
                )
            }
        }
            Spacer(modifier=Modifier.height(22.dp))


        Row {
    Text("Full Name (as on PAN card)", fontWeight = FontWeight.Medium, fontSize = 20.sp)

}
        Spacer(modifier=Modifier.height(18.dp))
        TextField(value = name,
            onValueChange = {name=it},
            textStyle = TextStyle(
                fontWeight = FontWeight.Medium, fontSize = 18.sp
            ),
            colors= TextFieldDefaults.colors(focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent),
            modifier=Modifier.fillMaxWidth().border(1.dp, color = Color(0xFFD2B077), shape = RoundedCornerShape(12.dp)).clip(RoundedCornerShape(12.dp)))
Spacer(modifier = Modifier.height(18.dp))

        Row {
            Text("Date of Birth", fontWeight = FontWeight.Medium, fontSize = 20.sp)

        }
        Spacer(modifier=Modifier.height(18.dp))
        TextField(value = dateOfBirth,
            onValueChange = {dateOfBirth=it},
            textStyle = TextStyle(
                fontWeight = FontWeight.Medium, fontSize = 18.sp
            ),
            colors= TextFieldDefaults.colors(focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent),
            modifier=Modifier.fillMaxWidth().border(1.dp, color = Color(0xFFD2B077), shape = RoundedCornerShape(12.dp)).clip(RoundedCornerShape(12.dp)))

        Spacer(modifier = Modifier.height(18.dp))

        Row {
            Text("Mobile Number", fontWeight = FontWeight.Medium, fontSize = 20.sp)

        }
        Spacer(modifier=Modifier.height(18.dp))
        TextField(value = mobileNumber,
            onValueChange = {mobileNumber=it},
            textStyle = TextStyle(
                fontWeight = FontWeight.Medium, fontSize = 18.sp, fontFamily = Poppins
            ),
            colors= TextFieldDefaults.colors(focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent),
            trailingIcon = {Icon(painter = painterResource(Res.drawable.penvector), contentDescription = "penvector Icon")},

            modifier=Modifier.fillMaxWidth().border(1.dp, color = Color(0xFFD2B077), shape = RoundedCornerShape(12.dp)).clip(RoundedCornerShape(12.dp)))

        Spacer(modifier = Modifier.height(18.dp))

        Row {
            Text("Email Address", fontWeight = FontWeight.Medium, fontSize = 20.sp)

        }
        Spacer(modifier=Modifier.height(18.dp))
        TextField(value = emailAddress,
            onValueChange = {emailAddress=it},
            textStyle = TextStyle(
                fontWeight = FontWeight.Medium, fontSize = 18.sp
            ),
            colors= TextFieldDefaults.colors(focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent),
            trailingIcon = {Icon(painter = painterResource(Res.drawable.penvector), contentDescription = "penvector Icon")},

            modifier=Modifier.fillMaxWidth().border(1.dp, color = Color(0xFFD2B077), shape = RoundedCornerShape(12.dp)).clip(RoundedCornerShape(12.dp)))

        Spacer(modifier = Modifier.height(18.dp))

        Row {
            Text("PAN Number", fontWeight = FontWeight.Medium, fontSize = 20.sp)

        }
        Spacer(modifier=Modifier.height(18.dp))
        TextField(value = panNumber,
            onValueChange = {panNumber=it},
            textStyle = TextStyle(
                fontWeight = FontWeight.Medium, fontSize = 18.sp
            ),
            colors= TextFieldDefaults.colors(focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent),
            modifier=Modifier.fillMaxWidth().border(1.dp, color = Color(0xFFD2B077), shape = RoundedCornerShape(12.dp)).clip(RoundedCornerShape(12.dp)))


        Spacer(modifier = Modifier.height(18.dp))

        Row {
            Text("Income Range", fontWeight = FontWeight.Medium, fontSize = 20.sp)

        }
        Spacer(modifier=Modifier.height(18.dp))
        TextField(value = incomeRange,
            onValueChange = {incomeRange=it},
            textStyle = TextStyle(
                fontWeight = FontWeight.Medium, fontSize = 18.sp
            ),
            colors= TextFieldDefaults.colors(focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent),
            trailingIcon = {Icon(painter = painterResource(Res.drawable.penvector), contentDescription = "penvector Icon")},
            modifier=Modifier.fillMaxWidth().border(1.dp, color = Color(0xFFD2B077), shape = RoundedCornerShape(12.dp)).clip(RoundedCornerShape(12.dp)))

    }}