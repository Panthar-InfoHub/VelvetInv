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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.darkBlue
import org.sharad.velvetinvestment.shared.compose.GoalsCard
import org.sharad.velvetinvestment.shared.compose.ScreenHeader
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.arrowback_elements
import velvet.composeapp.generated.resources.calender
import velvet.composeapp.generated.resources.elements
import velvet.composeapp.generated.resources.rupeesign
import velvet.composeapp.generated.resources.vector__5_
import velvet.composeapp.generated.resources.vector__6_

@Preview(showSystemUi = true)
@Composable
fun CustomGoalScreen(){
    var textFieldValue by remember{mutableStateOf("")}
    val graycolor = Color(0xFF5A5E60)
    LazyColumn {
        item {
            Column(modifier = Modifier.padding(16.dp)) {
                ScreenHeader("Custom Goal")

                Text("Category", fontWeight = FontWeight.SemiBold, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(20.dp))
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .border(1.dp, color = Color(0xFF5A5E60), shape = RoundedCornerShape(12.dp))
                        .height(70.dp)
                        .background(color = Color.White, shape = RoundedCornerShape(12.dp))
                        .padding(15.dp), contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.elements),
                            contentDescription = "wealth icon",
                            tint = Color(0xFF5A5E60),
                            modifier = Modifier.background(
                                color = Color(0xFF5a5e60).copy(0.1f),
                                shape = RoundedCornerShape(8.dp)
                            ).padding(8.dp)
                        )
                        Spacer(Modifier.width(15.dp))
                        Text("Wealth Building", fontWeight = FontWeight.Medium, fontSize = 16.sp)

                    }
                }
Spacer(modifier = Modifier.height(20.dp))

               Text("Goal Name", fontWeight = FontWeight.SemiBold, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(20.dp))
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .border(1.dp, color = Color(0xFF5A5E60), shape = RoundedCornerShape(12.dp))
                        .height(70.dp)
                        .background(color = Color.White, shape = RoundedCornerShape(12.dp))
                        .padding(15.dp), contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.elements),
                            contentDescription = "wealth icon",
                            tint = Color(0xFF5A5E60),
                            modifier = Modifier.background(
                                color = Color(0xFF5A5E60).copy(0.1f),
                                shape = RoundedCornerShape(8.dp)
                            ).padding(8.dp)
                        )
                        Spacer(Modifier.width(15.dp))
                        Text("Wealth Building", fontWeight = FontWeight.Medium, fontSize = 16.sp)

                    }
                }
                TextFieldComposable("Target Year *",value="2030",icon=Res.drawable.calender,color=graycolor)

                TextFieldComposable("Target Amount *",value="100000",icon=Res.drawable.rupeesign,color=graycolor)


                Spacer(modifier = Modifier.height(20.dp))

                Text("Default Inflation Rate(% per annum)", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)

                Spacer(modifier = Modifier.height(20.dp))
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .border(1.dp, color = graycolor, shape = RoundedCornerShape(12.dp))
                        .height(70.dp)
                        .background(color = Color.White, shape = RoundedCornerShape(12.dp))
                        .padding(15.dp), contentAlignment = Alignment.CenterStart
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.vector__6_),
                            contentDescription = "default Inflation icon",
                            tint = graycolor,
                            modifier = Modifier.background(
                                color = graycolor.copy(0.1f),
                                shape = RoundedCornerShape(8.dp)
                            ).padding(8.dp)
                        )
                        Spacer(Modifier.width(15.dp))
                        Text(
                            "6 %",
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            color = graycolor
                        )

                    }
                }
                Spacer(modifier = Modifier.height(30.dp))
                HorizontalDivider(thickness = 2.dp, color = graycolor)
                Spacer(modifier = Modifier.height(15.dp))
                Text("Projected Impact", fontSize = 20.sp, fontWeight = FontWeight.W600)
                Spacer(modifier = Modifier.height(15.dp))
                GoalsCard(color = graycolor)
                Spacer(modifier = Modifier.height(20.dp))
                Row(modifier = Modifier.padding(horizontal = 30.dp)) {
                    Button(onClick = {}, colors = ButtonDefaults.buttonColors(darkBlue),modifier=Modifier.fillMaxWidth()){
                        Text("Set Your First Goal", fontSize = 18.sp)
                    }
                }
                Spacer(modifier = Modifier.height(30.dp))

            }
        }
    }
}

@Composable
fun TextFieldComposable(text:String,value:String,icon: DrawableResource, color:Color){
    var textFieldValue by remember{mutableStateOf("")}
Spacer(modifier=Modifier.height(20.dp))
    Text(text, fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
    Spacer(modifier = Modifier.height(20.dp))
    TextField(
        value = textFieldValue,
        onValueChange = { textFieldValue = it },
        leadingIcon = {
            Box(
                modifier = Modifier.padding(12.dp).background(
                    color = color.copy(0.1f),
                    shape = RoundedCornerShape(12.dp)
                ).padding(8.dp)
            ) {
                Icon(
                    painter = painterResource(resource = icon),
                    contentDescription = "textfield icon",
                    tint = color
                )
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        placeholder = {
            Text(text=value, modifier = Modifier.padding(top = 8.dp))
                      },
        modifier = Modifier.height(70.dp).fillMaxWidth()
            .border(1.dp, color = color, shape = RoundedCornerShape(12.dp))
    )
}


