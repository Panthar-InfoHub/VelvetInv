package org.sharad.velvetinvestment.presentation.settingscreens.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.goldenColor
import org.sharad.velvetinvestment.presentation.tradingaccount.compose.AddMoreNominiee
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.genericDropShadow
import org.sharad.velvetinvestment.utils.theme.Poppins
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.icon__2_
@Preview(showBackground = true)
@Composable
fun NomineeScreen() {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        BackHeader("Nominee",true)
        LazyColumn(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item {
                AddMoreNominiee("Add Nominee")
            }

            item {
                Box(modifier = Modifier.fillMaxWidth().genericDropShadow(RoundedCornerShape(24.dp)).clip(RoundedCornerShape(24.dp)).background(color = Color.White)
                ){
                    Row(modifier = Modifier.fillMaxWidth().background(Color(0xffFF9D00).copy(0.1f)).padding(vertical = 24.dp, horizontal = 16.dp)) {
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Icon(
                                painter = painterResource(Res.drawable.icon__2_),
                                contentDescription = "Icon",
                                tint = goldenColor, modifier = Modifier.size(25.dp)
                            )
                            Text("You can add up to 3 nominees to your account. Added nominees can’t be removed", color = Primary, fontSize = 12.sp, fontFamily = Poppins)
                        }
                    }

                }
            }

            item {
                Spacer(Modifier.height(16.dp))
            }
        }
    }
}