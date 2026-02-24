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
import androidx.compose.ui.layout.ModifierLocalBeyondBoundsLayout
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.ktor.client.request.invoke
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.darkBlue
import org.sharad.emify.core.ui.theme.orangeColor
import org.sharad.velvetinvestment.shared.compose.GoalsCard
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.arrowback_elements
import velvet.composeapp.generated.resources.calender
import velvet.composeapp.generated.resources.elements
import velvet.composeapp.generated.resources.vector__5_
import velvet.composeapp.generated.resources.vector__6_

@Preview(showSystemUi = true)
@Composable
fun FinancialWealthScreen(){
    var textFieldValue by remember{mutableStateOf("")}
    LazyColumn {
        item {
            Column(modifier = Modifier.padding(16.dp)) {
                Spacer(modifier = Modifier.height(30.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.arrowback_elements),
                        contentDescription = "Arrow Back Icon"
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            "Financial Goals",
                            fontWeight = FontWeight.Bold,
                            fontSize = 24.sp,
                            color = Color(0xFF273E71)
                        )

                    }
                }
                Spacer(modifier = Modifier.height(30.dp))
                Text("Goal Name", fontWeight = FontWeight.SemiBold, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(20.dp))
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .border(1.dp, color = Color(0xFFFF9D00), shape = RoundedCornerShape(12.dp))
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
                            tint = Color(0xFFFF9D00),
                            modifier = Modifier.background(
                                color = Color(0xFFFF9D00).copy(0.1f),
                                shape = RoundedCornerShape(8.dp)
                            ).padding(8.dp)
                        )
                        Spacer(Modifier.width(15.dp))
                        Text("Wealth Building", fontWeight = FontWeight.Medium, fontSize = 16.sp)

                    }
                }
                Spacer(modifier = Modifier.height(20.dp))
                Text("Target Year *", fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
                TextField(
                    value = textFieldValue,
                    onValueChange = { textFieldValue = it },
                    leadingIcon = {
                        Box(
                            modifier = Modifier.padding(12.dp).background(
                                color = Color(0xFFFF9D00).copy(0.1f),
                                shape = RoundedCornerShape(12.dp)
                            ).padding(8.dp)
                        ) {
                            Icon(
                                painter = painterResource(Res.drawable.calender),
                                contentDescription = "calender icon",
                                tint = Color(0xFFFF9D00)
                            )
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    placeholder = { Text("2030", modifier = Modifier.padding(top = 6.dp)) },
                    modifier = Modifier.height(70.dp).fillMaxWidth()
                        .border(1.dp, color = Color(0xFFFF9D00), shape = RoundedCornerShape(12.dp))
                )

                Spacer(modifier = Modifier.height(20.dp))
                Text("Target Amount *", fontSize = 20.sp)
                TextField(
                    value = textFieldValue,
                    onValueChange = { textFieldValue = it },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(Res.drawable.vector__5_),
                            contentDescription = "calender icon",
                            tint = Color(0xFFFF9D00)
                        )
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    placeholder = { Text("100000", modifier = Modifier.padding(top = 8.dp)) },
                    modifier = Modifier.height(70.dp).fillMaxWidth()
                        .border(1.dp, color = Color(0xFFFF9D00), shape = RoundedCornerShape(12.dp))
                )
                Text("Default Inflation Rate(% per annum)")

                Spacer(modifier = Modifier.height(20.dp))
                Box(
                    modifier = Modifier.fillMaxWidth()
                        .border(1.dp, color = Color(0xFFFF9D00), shape = RoundedCornerShape(12.dp))
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
                            tint = Color(0xFFFF9D00),
                            modifier = Modifier.background(
                                color = Color(0xFFFF9D00).copy(0.1f),
                                shape = RoundedCornerShape(8.dp)
                            ).padding(8.dp)
                        )
                        Spacer(Modifier.width(15.dp))
                        Text(
                            "6 %",
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            color = orangeColor
                        )

                    }
                }
                Spacer(modifier = Modifier.height(30.dp))
                HorizontalDivider(thickness = 2.dp, color = orangeColor)
                Spacer(modifier = Modifier.height(15.dp))
                Text("Projected Impact", fontSize = 20.sp, fontWeight = FontWeight.W600)
                Spacer(modifier = Modifier.height(15.dp))
                GoalsCard(color = orangeColor)
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
