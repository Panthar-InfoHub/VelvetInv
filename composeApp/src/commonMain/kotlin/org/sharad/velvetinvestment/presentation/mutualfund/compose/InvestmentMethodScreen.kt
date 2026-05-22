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
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import org.sharad.velvetinvestment.shared.compose.AppButton
import org.sharad.velvetinvestment.shared.genericDropShadow
import org.sharad.velvetinvestment.shared.theme.Poppins
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.back_arrow
import velvet.composeapp.generated.resources.choose_how_to_invest
import velvet.composeapp.generated.resources.ic_callended_filled
import velvet.composeapp.generated.resources.ic_ruppee_filled
import velvet.composeapp.generated.resources.invest_as_lumpsum
import velvet.composeapp.generated.resources.lumpsum_description
import velvet.composeapp.generated.resources.select_investment_method
import velvet.composeapp.generated.resources.sip_description
import velvet.composeapp.generated.resources.start_an_sip

@Composable
fun InvestmentMethodScreen(
    onStartSipClick: () -> Unit = {},
    onLumpsumClick: () -> Unit = {},
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
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
                    buttonText ="Start SIP ",
                    icon = Res.drawable.ic_callended_filled,
                    onButtonClick = onStartSipClick
                )
            }

            item {
                InvestmentOptionCard(
                    title = stringResource(Res.string.invest_as_lumpsum),
                    description = stringResource(Res.string.lumpsum_description),
                    buttonText = "Invest Lump Sum",
                    icon = Res.drawable.ic_ruppee_filled,
                    onButtonClick = onLumpsumClick
                )
            }
        }
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
