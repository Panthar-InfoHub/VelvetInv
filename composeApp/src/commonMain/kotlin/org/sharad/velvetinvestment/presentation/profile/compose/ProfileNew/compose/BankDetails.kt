package org.sharad.velvetinvestment.presentation.profile.compose.ProfileNew.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.greenColor
import org.sharad.velvetinvestment.presentation.tradingaccount.compose.AddMoreNominiee
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.genericDropShadow
import org.sharad.velvetinvestment.utils.theme.Poppins
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.sbi_tree

@Composable
fun BankDetailNew() {
    Column(modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp).navigationBarsPadding(), verticalArrangement = Arrangement.spacedBy(2.dp)) {
        BackHeader("Bank Details", true)
        LazyColumn(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().genericDropShadow(RoundedCornerShape(24.dp))
                        .clip(RoundedCornerShape(24.dp)).background(color = Color.White)
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.sbi_tree),
                            contentDescription = "sbi tree", modifier = Modifier.size(38.dp)
                        )
                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                "State bank of India...1234",
                                fontFamily = Poppins,
                                fontWeight = FontWeight.Medium,
                                fontSize = 18.sp
                            )
                            Text(
                                "Primary",
                                color = greenColor,
                                fontFamily = Poppins,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            item {

                AddMoreNominiee("Add new Bank")
            }
        }
    }
}

//@Preview(showSystemUi = true)
//@Composable
//fun PreviewBankDetails(){
//    VelvetTheme{
//        BankDetailNew()
//    }
//}