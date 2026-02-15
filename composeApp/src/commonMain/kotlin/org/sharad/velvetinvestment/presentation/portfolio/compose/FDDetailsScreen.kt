package org.sharad.velvetinvestment.presentation.portfolio.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.appGreen
import org.sharad.emify.core.ui.theme.appRed
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.presentation.portfolio.models.FDDetailsUiModel
import org.sharad.velvetinvestment.presentation.portfolio.models.FDNomineeUiModel
import org.sharad.velvetinvestment.presentation.portfolio.viewmodel.FDDetailsViewModel
import org.sharad.velvetinvestment.shared.compose.BarHeader
import org.sharad.velvetinvestment.shared.compose.ErrorScreen
import org.sharad.velvetinvestment.shared.compose.LoaderScreen
import org.sharad.velvetinvestment.shared.compose.ShadowCard
import org.sharad.velvetinvestment.utils.UIState
import org.sharad.velvetinvestment.utils.theme.buttonTextStyle
import org.sharad.velvetinvestment.utils.theme.subHeading
import org.sharad.velvetinvestment.utils.theme.subHeadingMedium
import org.sharad.velvetinvestment.utils.theme.titlesStyle
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.back_arrow

@Composable
fun FDDetailsScreen(
    onBackClick: () -> Unit,
    id: String,
    pv: PaddingValues
){

    val viewModel: FDDetailsViewModel = koinViewModel{ parametersOf(id) }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val details by viewModel.fdDetails.collectAsStateWithLifecycle()

    when(uiState){
        is UIState.Error -> {
            ErrorScreen((uiState as UIState.Error).error)
        }
        UIState.Loading -> {
            LoaderScreen()
        }
        UIState.Success ->{
            if (details != null)
                FDDetailsMain(
                    details = details!!,
                    onBackClick = onBackClick,
                    pv=pv
                )
            else
                ErrorScreen("Something went wrong")
        }
    }

}

@Composable
fun FDDetailsMain(details: FDDetailsUiModel, onBackClick: () -> Unit, pv: PaddingValues) {
    Box(modifier = Modifier.fillMaxSize()){
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            stickyHeader {
                FDDetailsHeader(
                    onBackClick = onBackClick,
                    bankName = details.bankName,
                    fdId = details.fdAccountNumber
                )
            }
            item {
                InvestmentDetailsCard(details)
            }
            item { BarHeader(heading = "Nominee Details") }
            items(details.nominees, key = { it.fullName }) {
                NomineeDetailsCard(nominee = it)
            }
            item {
                OrderTimelineCard(details.startDate, details.maturityDate, details.daysRemaining)
            }
            item {
                Spacer(Modifier.height(pv.calculateBottomPadding() + 64.dp))
            }
        }

        BreakFDButton(onClick={}, modifier = Modifier.align(Alignment.BottomCenter).fillMaxWidth(0.9f).padding(bottom = pv.calculateBottomPadding()+16.dp))
    }
}

@Composable
fun BreakFDButton( onClick: () -> Unit,modifier:Modifier=Modifier) {
    Button(
        onClick=onClick,
        modifier=modifier.fillMaxWidth().height(50.dp),
        enabled = true,
        colors = ButtonDefaults.buttonColors(
            containerColor = appRed,
            contentColor = Color.White,
            disabledContentColor = Color.White ,
            disabledContainerColor = appRed.copy(alpha = 0.5f)
        )
    ){
        Text(
            text = "Break FD",
            style = buttonTextStyle
        )
    }
}

@Composable
fun OrderTimelineCard(startDate: String, maturityDate: String, remainingDays: String) {
    ShadowCard {
        Column(
            modifier=Modifier.fillMaxWidth().padding(16.dp),
        ) {
            Text("Order Timeline", style = MaterialTheme.typography.labelSmall, color = Primary)
            Spacer(modifier = Modifier.height(16.dp))
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ){
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(modifier = Modifier.size(24.dp).clip(CircleShape).background(appGreen))
                    Box(modifier = Modifier.width(1.dp).height(52.dp).background(titleColor))
                }
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(text = "Start Date", style = subHeading, color = titleColor)
                    Text(text = startDate, style = titlesStyle.copy(fontWeight = FontWeight.Bold), color = Primary)
                }
            }
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ){
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(modifier = Modifier.size(24.dp).clip(CircleShape).background(appGreen))
                    Box(modifier = Modifier.width(1.dp).height(52.dp).background(titleColor))
                }
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(text = "Maturity Date", style = subHeading, color = titleColor)
                    Text(text = maturityDate, style = titlesStyle.copy(fontWeight = FontWeight.Bold), color = Secondary)
                }
            }
            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ){
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(modifier = Modifier.size(24.dp).clip(CircleShape).background(appGreen))
                }
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(text = "Days Remaining", style = subHeading, color = titleColor)
                    Text(text = remainingDays, style = titlesStyle.copy(fontWeight = FontWeight.Bold), color = appGreen)
                }
            }
        }
    }
}

@Composable
fun NomineeDetailsCard(nominee: FDNomineeUiModel) {
    ShadowCard {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
               Text("Nominee Info", style = subHeadingMedium, color = Color.Black)
                Text("Edit", style = MaterialTheme.typography.labelSmall, color = Primary, modifier = Modifier.clickable(onClick={}))
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                Column(
                    modifier=Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text("Full Name", style = subHeading, color = titleColor)
                    Text(nominee.fullName, style = MaterialTheme.typography.headlineSmall, color = Secondary)
                }
                Column(
                    modifier=Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text("Relationship", style = subHeading, color = titleColor)
                    Text(nominee.relationship, style = MaterialTheme.typography.headlineSmall, color = Secondary)
                }
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text("DOB", style = subHeading, color = titleColor)
                Text(nominee.dateOfBirth, style = MaterialTheme.typography.headlineSmall, color = Secondary)
            }
        }
    }
}

@Composable
fun InvestmentDetailsCard(details: FDDetailsUiModel) {
    ShadowCard {
        Column(
            modifier=Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            Text("Investment Details", style = MaterialTheme.typography.labelSmall, color = Primary)

            InfoTextColumn(
                title = "Principal Amount",
                value = details.principalAmount,
                valueColor = Primary,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                InfoTextColumn(
                    title = "Interest Rate",
                    value = details.interestRate,
                    valueColor = Secondary,
                    modifier = Modifier.weight(1f)
                )

                InfoTextColumn(
                    title = "Maturity Amount",
                    value = details.maturityAmount,
                    valueColor = appGreen,
                    modifier = Modifier.weight(1f),
                )
            }

            InfoTextColumn(
                title = "Tenure",
                value = details.tenure,
                valueColor = Primary,
                modifier = Modifier.fillMaxWidth()
            )

            InfoTextColumn(
                title = "Interest Earned Till Date",
                value = details.interestEarnedTillDate,
                valueColor = appGreen,
                modifier = Modifier.fillMaxWidth()
            )

        }
    }
}

@Composable
fun FDDetailsHeader(onBackClick: () -> Unit, bankName: String, fdId: String) {
    Box(modifier = Modifier.fillMaxWidth().background(Color.White)){
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
        ) {

            Icon(
                painter = painterResource(Res.drawable.back_arrow),
                contentDescription = null,
                modifier = Modifier.padding(top = 8.dp).size(20.dp).clickable(
                    onClick = onBackClick,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                )
            )
            Column(
                modifier = Modifier.weight(1f)
                    .fillMaxWidth(),
            ) {
                Text(
                    text = bankName,
                    style = MaterialTheme.typography.headlineLarge,
                    color = Primary,
                )
                Text(
                    text = fdId,
                    style = titlesStyle,
                    color = titleColor
                )
            }

        }
    }
}

@Composable
fun InfoTextColumn(
    title: String,
    value: String,
    valueColor: Color,
    modifier: Modifier = Modifier,
    spacing: Dp = 4.dp
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(spacing)
    ) {
        Text(
            text = title,
            style = titlesStyle,
            color = titleColor
        )

        Text(
            text = value,
            style = subHeading,
            color = valueColor
        )
    }
}
