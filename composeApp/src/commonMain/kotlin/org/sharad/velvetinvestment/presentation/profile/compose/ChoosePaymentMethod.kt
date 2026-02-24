package org.sharad.velvetinvestment.presentation.profile.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonShapes
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PaintingStyle.Companion.Stroke
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.serialization.builtins.ArraySerializer
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.blueColor
import org.sharad.emify.core.ui.theme.greenColor
import org.sharad.velvetinvestment.shared.compose.ScreenHeader
import org.sharad.velvetinvestment.utils.theme.Poppins
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources._151upi_main_image_1
import velvet.composeapp.generated.resources.googlepay
import velvet.composeapp.generated.resources.phonepay
import velvet.composeapp.generated.resources.sbi

@Preview(showSystemUi = true)
@Composable
fun ChoosePaymentMethod() {
    var buttonstate by remember { mutableStateOf(true) }
    Column(modifier = Modifier.padding(16.dp)) {
        ScreenHeader("Choose Payment Method")
        Row(
            modifier = Modifier.height(60.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Image(
                painter = painterResource(Res.drawable._151upi_main_image_1),
                contentDescription = "upi image icon",
                modifier = Modifier.height(60.dp).width(60.dp)
            )

            Text("Pay using any UPI app", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth().height(60.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Image(
                    painter = painterResource(Res.drawable.googlepay),
                    contentDescription = "upi image icon",
                    modifier = Modifier.height(60.dp).width(60.dp).padding(10.dp)
                )

                Text("Gpay", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)

            }
            RadioButton(
                selected = buttonstate,
                onClick = { buttonstate != buttonstate },
                enabled = true,
                colors = RadioButtonDefaults.colors(
                    blueColor
                )
            )
        }

        if (buttonstate) {
            Column(modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(start=30.dp).fillMaxWidth()) {
                Box(Modifier.fillMaxWidth().drawBehind {
                    drawRect(
                        color = Color.LightGray, style = Stroke(
                            width = 2.dp.toPx(),
                            pathEffect = PathEffect.dashPathEffect(
                                intervals = floatArrayOf(
                                    15f,
                                    15f
                                ), phase = 0f
                            )
                        )
                    )
                }.align(Alignment.End))
                {
                    Row(
                        Modifier.padding(5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                         Image(
                            painter = painterResource(Res.drawable.sbi),
                            contentDescription = "sbi image icon", modifier = Modifier.size(60.dp)
                        )
                        Text(
                            "STATE BANK OF INDIA....1234",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            fontFamily = Poppins
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                    }
                }
                    Spacer(modifier=Modifier.height(10.dp))
                Button(
                    onClick = {},
                    shape = RoundedCornerShape(4.dp),
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White,
                        containerColor = greenColor
                    ),
                    modifier = Modifier.fillMaxWidth().height(65.dp)
                ) {
                    Text(text = "Pay Rupee 5000", fontSize = 16.sp)

                }
            }
        }
            }
        Spacer(modifier = Modifier.height(10.dp))
        Row(
            modifier = Modifier.fillMaxWidth().height(60.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Image(
                    painter = painterResource(Res.drawable.phonepay),
                    contentDescription = "upi image icon",
                    modifier = Modifier.height(60.dp).width(60.dp).padding(10.dp)
                )

                Text("Phone pay", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)

            }
            RadioButton(
                selected = buttonstate,
                onClick = { buttonstate != buttonstate },
                enabled = true,
                colors = RadioButtonDefaults.colors(
                    blueColor
                )
            )
        }

        Row(
            modifier = Modifier.height(60.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {

            Image(
                painter = painterResource(Res.drawable._151upi_main_image_1),
                contentDescription = "upi image icon",
                modifier = Modifier.height(60.dp).width(60.dp)
            )

            Text("Other UPI apps", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }

        Spacer(modifier=Modifier.height(20.dp))
        Text(text="Net Banking", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, fontFamily = Poppins)
        Row(
            modifier = Modifier.fillMaxWidth().height(60.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Image(
                    painter = painterResource(Res.drawable.sbi),
                    contentDescription = "upi image icon",
                    modifier = Modifier.height(60.dp).width(60.dp).padding(10.dp)
                )

                Text("STATE BANK OF INDIA....1234",color=Color.DarkGray, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)

            }
            RadioButton(
                selected = buttonstate,
                onClick = { buttonstate = !buttonstate },
                enabled = true,
                colors = RadioButtonDefaults.colors(
                    blueColor
                )
            )
        }



    }
}

