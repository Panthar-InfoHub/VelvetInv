package org.sharad.velvetinvestment.presentation.profile.compose.ProfileNew.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.emify.core.ui.theme.appGreen
import org.sharad.emify.core.ui.theme.appRed
import org.sharad.emify.core.ui.theme.blueColor
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.presentation.profile.compose.ProfileNew.viewModel.CheckKYCViewModel
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.utils.theme.Poppins
import org.sharad.velvetinvestment.utils.theme.titlesStyle
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.fixed_deposits
import velvet.composeapp.generated.resources.moveforward
import velvet.composeapp.generated.resources.upgrade

@Preview(showSystemUi = true)
@Composable
fun CheckKYCSScreen() {
    val viewModel :CheckKYCViewModel= koinViewModel()
    val state by viewModel.checkKYCModel.collectAsStateWithLifecycle()
    Column {
        BackHeader("Check KYC", true)
        LazyColumn(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                CheckKYC1Box("Mutual Funds","Check your KYC status for mutual fund investments",state.mutualFundKYC,icon=Res.drawable.upgrade,blueColor)
            }
            item {
                CheckKYC1Box("Fixed Deposits","Check your KYC status for fixed deposit accounts",state.fixedDepositKyc,icon=Res.drawable.fixed_deposits,appRed)
            }

            item {
                Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp)).background(color = Color(0xffEFF6FF)).padding(16.dp)){
                    Column (verticalArrangement = Arrangement.spacedBy(8.dp)){
                            Text("Why KYC?", fontSize = 16.sp, fontFamily = Poppins, fontWeight = FontWeight.Medium)
                            Text("KYC (Know Your Customer) is mandatory for all financial transactions to ensure security and compliance with regulatory requirements.", fontSize = 14.sp, fontFamily = Poppins, color = Color(0xff4A5565))
                        }
                    }
                }
            }


        }
    }


@Composable
fun CheckKYC1Box(heading:String,body:String,status:Boolean,icon: DrawableResource,color: Color){
    Box(modifier = Modifier.fillMaxWidth().border(1.dp, color = Color(0xffE5E7EB),RoundedCornerShape(16.dp)).background(color = Color.White, shape = RoundedCornerShape(16.dp)).padding(16.dp)) {
        Row(modifier = Modifier.fillMaxWidth()
            , verticalAlignment = Alignment.CenterVertically) {
            Row(modifier = Modifier.weight(1f)) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = "upgrade icon",
                    tint = color,
                    modifier = Modifier.clip(RoundedCornerShape(14.dp))
                        .background(color = color.copy(0.1f)).padding(16.dp)
                )
                Column(
                    modifier = Modifier.weight(1f).padding(start = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        heading,
                        color = Color.Black,
                        fontFamily = Poppins,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        body, lineHeight = 24.sp,
                        fontSize = 14.sp,
                        fontFamily = Poppins,
                        color = titleColor
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier.size(8.dp).clip(CircleShape)
                                .background(if (status) Color(0xff00A63E) else appRed)
                        )
                        Text(
                            text = if(status) "KYC Completed" else "KYC Pending",
                            color = if(status) appGreen else appRed,
                            style = titlesStyle
                        )
                    }
                }
            }
            Icon(
                painter = painterResource(Res.drawable.moveforward),
                contentDescription = "forward icon",
                tint = Color(0xff99A1AF), modifier= Modifier.weight(0.2f)
            )
        }

    }
}

