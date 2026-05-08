package org.sharad.velvetinvestment.presentation.profile.compose.ProfileNew.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import org.sharad.velvetinvestment.shared.UiStateContainer
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.theme.Poppins
import org.sharad.velvetinvestment.shared.theme.titlesStyle
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.fixed_deposits
import velvet.composeapp.generated.resources.moveforward
import velvet.composeapp.generated.resources.upgrade

@Composable
fun CheckKYCScreen(
    onBackClick: () -> Unit,
    navigateToMutualFundKYC: () -> Unit,
    navigateToTradingAccountKYC: () -> Unit
) {
    val viewModel: CheckKYCViewModel = koinViewModel()
    val kycState by viewModel.kycState.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize().background(Color.White)) {
        BackHeader("Check KYC", true, onBackClick = onBackClick)

        UiStateContainer(
            uiState = kycState,
            onRetry = { viewModel.loadKycStatus() }
        ) { data ->
            LazyColumn(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(top = 16.dp)
            ) {
                item {
                    CheckKYC1Box(
                        heading = "Mutual Funds",
                        body = "Check your KYC status for mutual fund investments",
                        status = data.mutualFundKYC,
                        icon = Res.drawable.upgrade,
                        color = blueColor,
                        onClick = {
                            if (!data.mutualFundKYC) {
                                navigateToMutualFundKYC()
                            }
                        }
                    )
                }
                item {
                    CheckKYC1Box(
                        heading = "Trading Account",
                        body = "Check your trading account status for purchases.",
                        status = data.tradingAccountKYC,
                        icon = Res.drawable.fixed_deposits,
                        color = appRed,
                        onClick = {
                            if (!data.tradingAccountKYC) {
                                if (!data.mutualFundKYC) {
                                    navigateToMutualFundKYC()
                                } else {
                                    navigateToTradingAccountKYC()
                                }
                            }
                        }
                    )
                }

                item {
                    Box(
                        modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(14.dp))
                            .background(color = Color(0xffEFF6FF)).padding(16.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text(
                                "Why KYC?",
                                fontSize = 16.sp,
                                lineHeight = 16.sp,
                                fontFamily = Poppins,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                "KYC (Know Your Customer) is mandatory for all financial transactions to ensure security and compliance with regulatory requirements.",
                                fontSize = 14.sp,
                                lineHeight = 14.sp,
                                fontFamily = Poppins,
                                color = Color(0xff4A5565)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CheckKYC1Box(
    heading: String,
    body: String,
    status: Boolean,
    icon: DrawableResource,
    color: Color,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = Modifier.fillMaxWidth()
            .border(1.dp, color = Color(0xffE5E7EB), RoundedCornerShape(16.dp))
            .background(color = Color.White, shape = RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(modifier = Modifier.weight(1f)) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = heading,
                    tint = color,
                    modifier = Modifier.clip(RoundedCornerShape(14.dp))
                        .background(color = color.copy(0.1f)).padding(12.dp)
                )
                Column(
                    modifier = Modifier.weight(1f).padding(start = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        heading,
                        color = Color.Black,
                        fontFamily = Poppins,
                        fontSize = 18.sp,
                        lineHeight = 18.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        body, lineHeight = 14.sp,
                        fontSize = 14.sp,
                        fontFamily = Poppins,
                        color = titleColor
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier.size(8.dp).clip(CircleShape)
                                .background(if (status) Color(0xff00A63E) else Color(0xffF97316))
                        )
                        Text(
                            text = if (status) "KYC Completed" else "KYC Pending",
                            color = if (status) appGreen else Color(0xffF97316),
                            style = titlesStyle.copy(lineHeight = 14.sp)
                        )
                    }
                }
            }
            Icon(
                painter = painterResource(Res.drawable.moveforward),
                contentDescription = "forward icon",
                tint = Color(0xff99A1AF), modifier = Modifier.size(16.dp)
            )
        }
    }
}
