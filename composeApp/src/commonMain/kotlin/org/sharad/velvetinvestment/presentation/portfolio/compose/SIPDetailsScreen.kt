package org.sharad.velvetinvestment.presentation.portfolio.compose

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.domain.models.portfolio.BankDetails
import org.sharad.velvetinvestment.domain.models.portfolio.SIPDetailsDomain
import org.sharad.velvetinvestment.domain.models.portfolio.TransactionHistoryDomain
import org.sharad.velvetinvestment.presentation.portfolio.viewmodel.SIPDetailsViewModel
import org.sharad.velvetinvestment.shared.compose.ContinueBackButtonFooter
import org.sharad.velvetinvestment.shared.compose.ErrorScreen
import org.sharad.velvetinvestment.shared.compose.LoaderScreen
import org.sharad.velvetinvestment.shared.compose.ShadowCard
import org.sharad.velvetinvestment.shared.compose.SidedBackHeader
import org.sharad.velvetinvestment.utils.UIState
import org.sharad.velvetinvestment.utils.theme.subHeading
import org.sharad.velvetinvestment.utils.theme.titlesStyle
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.rectangle_19
import velvet.composeapp.generated.resources.sbi_placeholder

@Composable
fun SIPDetailsScreen(
    onBackClick: () -> Unit,
    id: String,
    onCancelClick: (String) -> Unit,
    pv: PaddingValues,
){

    val viewModel: SIPDetailsViewModel= koinViewModel()
    val screenState by viewModel.uiState.collectAsStateWithLifecycle()
    val sipData by viewModel.sipDetails.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        SidedBackHeader(
            heading = "Cancel SIP",
            showBack = true,
            onBackClick = onBackClick,
            textIcon = "Cancel SIP",
            onTextClick = { onCancelClick(id) }
        )
        Box(modifier = Modifier.weight(1f)){
            when (screenState) {
                is UIState.Error -> {
                    ErrorScreen("")
                }

                UIState.Loading -> {
                    LoaderScreen()
                }

                UIState.Success -> {
                    if (sipData!=null)
                        SIPDetailsLoadedScreen(sipData!!, pv=pv)
                    else
                        ErrorScreen("No Data")
                }
            }
        }
    }

}

@Composable
fun SIPDetailsLoadedScreen(sipData: SIPDetailsDomain, pv: PaddingValues) {
    Column(
        modifier=Modifier.fillMaxSize()
    ){
        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxSize().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item { Spacer(modifier = Modifier) }
            item { SIPDetailsCard(sipData) }
            item { InstallmentDetailsCard(sipData) }
            item { BankDetailsCard(sipData.bankDetails) }
            item { TransactionHistoryCard(history = sipData.transactionHistory) }

            item { Spacer(Modifier.height(16.dp)) }
        }

        ContinueBackButtonFooter(
            continueText = "Edit",
            backText = "Withdraw",
            onContinue = {},
            onBack = {},
            pv = pv
        )

    }
}

@Composable
fun TransactionHistoryCard(history: List<TransactionHistoryDomain>) {
    ShadowCard {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Text(
                text = "Transaction History",
                style = MaterialTheme.typography.labelSmall,
                color = Primary
            )
            Column {
                history.forEachIndexed { idx,item->
                    HistoryTimelineCard(item, showNext= idx!=history.lastIndex)
                }
            }
        }
    }
}



@Composable
fun BankDetailsCard(bankDetails: BankDetails) {
    ShadowCard {
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ){
            Text("Bank Account", style=subHeading, color = titleColor)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    modifier = Modifier.size(56.dp),
                    contentDescription = null,
                    model = bankDetails.bankIcon,
                    fallback = painterResource(Res.drawable.sbi_placeholder),
                    error = painterResource(Res.drawable.sbi_placeholder),
                    placeholder = painterResource(Res.drawable.sbi_placeholder)
                )

                Text(
                    text = bankDetails.bankName+ " " + bankDetails.accountNumber,
                    style = titlesStyle.copy(fontWeight = FontWeight.SemiBold),
                    color = titleColor,
                    maxLines = 2
                )
            }
        }
    }
}

@Composable
fun InstallmentDetailsCard(sipData: SIPDetailsDomain) {
    ShadowCard {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ){
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Next Installment",
                    style = titlesStyle,
                    color = titleColor
                )
                Text(
                    text = sipData.nextInstallment,
                    style = subHeading,
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "SIP ID",
                    style = titlesStyle,
                    color = titleColor
                )
                Text(
                    text = sipData.sipId,
                    style = subHeading,
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Autopay ID",
                    style = titlesStyle,
                    color = titleColor
                )
                Text(
                    text = sipData.autopayId,
                    style = subHeading,
                )
            }
        }
    }
}

@Composable
fun SIPDetailsCard(sipData: SIPDetailsDomain) {
    ShadowCard {
        Column(modifier = Modifier.fillMaxWidth()
            .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ){
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                AsyncImage(
                    modifier = Modifier.size(44.dp)
                        .clip(RoundedCornerShape(15.dp)),
                    model = sipData.icon,
                    contentDescription = null,
                    fallback = painterResource(Res.drawable.rectangle_19),
                    error = painterResource(Res.drawable.rectangle_19),
                    placeholder = painterResource(Res.drawable.rectangle_19)
                )

                Column(
                    modifier = Modifier.weight(1f).padding(end = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = sipData.fundName,
                            color = Color.Black,
                            style = subHeading
                        )

                        Text(
                            text = (sipData.amount),
                            color = Color.Black,
                            style = subHeading
                        )
                    }

                    Text(
                        text = sipData.fundCategory,
                        color = titleColor,
                        style = titlesStyle
                    )

                }

            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                sipData.metadata?.forEach { dataPair ->
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text=dataPair.first,
                            color = titleColor,
                            style = titlesStyle
                        )
                        Text(
                            text=dataPair.second,
                            color = Color.Black,
                            style = subHeading
                        )
                    }
                }
            }
        }
    }
}