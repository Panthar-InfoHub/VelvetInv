package org.sharad.velvetinvestment.presentation.mutualfund.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.bgColor4
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.shared.compose.AppButton
import org.sharad.velvetinvestment.shared.genericDropShadow
import org.sharad.velvetinvestment.shared.theme.Poppins
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import org.sharad.velvetinvestment.shared.theme.buttonTextStyle
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.back_arrow
import velvet.composeapp.generated.resources.ic_callended_filled
import velvet.composeapp.generated.resources.ic_ruppee_filled
import velvet.composeapp.generated.resources.invest_as_lumpsum
import velvet.composeapp.generated.resources.lumpsum_description
import velvet.composeapp.generated.resources.sip_description
import velvet.composeapp.generated.resources.start_an_sip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvestmentMethodScreen(
    onStartSipClick: () -> Unit = {},
    onLumpsumClick: () -> Unit = {},
    onExistingSIPFundClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    onExistingLumpSumFundClick: () -> Unit = {}
) {
    var showSIPBottomSheet by remember { mutableStateOf(false) }
    var showLumpSumBottomSheet by remember { mutableStateOf(false) }
    val sheetStateSIP = rememberModalBottomSheetState()
    val sheetStateLumpSum = rememberModalBottomSheetState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = Color.White,
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 8.dp)
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.back_arrow),
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }
            }
        }
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            contentPadding = PaddingValues(
                start = 24.dp,
                end = 24.dp,
                top = 8.dp,
                bottom = 24.dp
            ),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {

            item {
                Column {
                    Text(
                        text = "Invest Your Way",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.SemiBold,
                        fontFamily = Poppins,
                        color = Color.Black
                    )

                    Text(
                        text = "Pick an option to start your investment journey today. ",
                        fontFamily = Poppins,
                        fontSize = 14.sp,
                        lineHeight = 16.sp,
                        color = Color.Gray
                    )
                }
            }

            item {
                InvestmentOptionCard(
                    title = stringResource(Res.string.start_an_sip),
                    description = stringResource(Res.string.sip_description),
                    buttonText = "Start SIP ",
                    icon = Res.drawable.ic_callended_filled,
                    onButtonClick = { showSIPBottomSheet = true }
                )
            }

            item {
                InvestmentOptionCard(
                    title = stringResource(Res.string.invest_as_lumpsum),
                    description = stringResource(Res.string.lumpsum_description),
                    buttonText = "Invest Lump Sum",
                    icon = Res.drawable.ic_ruppee_filled,
                    onButtonClick = {
                        showLumpSumBottomSheet = true
                    }
                )
            }
        }

        if (showSIPBottomSheet) {
            SIPSelectionBottomSheet(
                sheetState = sheetStateSIP,
                onDismiss = { showSIPBottomSheet = false },
                onExistingFundClick = {
                    showSIPBottomSheet = false
                    onExistingSIPFundClick()
                },
                onExploreNewFundsClick = {
                    showSIPBottomSheet = false
                    onStartSipClick()
                }
            )
        }
        if (showLumpSumBottomSheet) {
            SIPSelectionBottomSheet(
                sheetState = sheetStateLumpSum,
                onDismiss = { showLumpSumBottomSheet = false },
                onExistingFundClick = {
                    showLumpSumBottomSheet = false
                    onExistingLumpSumFundClick()
                },
                onExploreNewFundsClick = {
                    showLumpSumBottomSheet = false
                    onLumpsumClick()
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SIPSelectionBottomSheet(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    onExistingFundClick: () -> Unit,
    onExploreNewFundsClick: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color.White,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp)
        ) {
            BottomSheetOptionItem(
                title = "Top Up existing Funds",
                subtitle = "Invest more in existing funds",
                onClick = onExistingFundClick
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 24.dp),
                color = Color.LightGray.copy(alpha = 0.5f)
            )

            BottomSheetOptionItem(
                title = "Explore New Funds",
                subtitle = "Discover new funds and start a fresh investment.",
                onClick = onExploreNewFundsClick
            )
        }
    }
}

@Composable
fun BottomSheetOptionItem(
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 24.dp, vertical = 20.dp)
    ) {
        Text(
            text = title,
            style = buttonTextStyle,
            color = titleColor
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.titleSmall,
            color = Color(0xffAAAAAA)
        )
    }
}

@Composable
fun InvestmentOptionCard(
    title: String,
    description: String,
    buttonText: String,
    icon: DrawableResource,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .genericDropShadow(RoundedCornerShape(12.dp))
            .border(width = 1.dp, color = bgColor4.copy(alpha = 0.2f), shape = RoundedCornerShape(12.dp))
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .padding(20.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = bgColor4.copy(0.1f),
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = Primary,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = title,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = Poppins,
                color = Color.Black
            )
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = description,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            fontFamily = Poppins,
            color = Color.Gray,
            modifier = Modifier.padding(end = 40.dp)
        )
        
        Spacer(modifier = Modifier.height(24.dp))

        AppButton(
            modifier =Modifier.fillMaxWidth(),
            text = buttonText,
            onClick = onButtonClick,
            shape = RoundedCornerShape(12.dp)
        )
    }
}

@Composable
@Preview
fun InvestmentMethodScreenPreview() {
    VelvetTheme {
        InvestmentMethodScreen()
    }
}
