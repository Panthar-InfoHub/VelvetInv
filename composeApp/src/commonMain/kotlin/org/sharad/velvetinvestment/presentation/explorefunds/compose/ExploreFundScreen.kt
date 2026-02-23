package org.sharad.velvetinvestment.presentation.explorefunds.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.appGreen
import org.sharad.emify.core.ui.theme.bgColor5
import org.sharad.velvetinvestment.presentation.explorefunds.uimodel.FixedTopPicksUiModel
import org.sharad.velvetinvestment.presentation.explorefunds.uimodel.MutualFundTopPicksUiModel
import org.sharad.velvetinvestment.presentation.explorefunds.viewmodel.ExploreFundScreenViewModel
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.BarHeader
import org.sharad.velvetinvestment.shared.compose.ErrorScreen
import org.sharad.velvetinvestment.shared.compose.LoaderScreen
import org.sharad.velvetinvestment.shared.compose.ShadowCard
import org.sharad.velvetinvestment.utils.UIState
import org.sharad.velvetinvestment.utils.theme.buttonTextStyle
import org.sharad.velvetinvestment.utils.theme.subHeading
import org.sharad.velvetinvestment.utils.theme.titlesStyle
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.expenses_icon
import velvet.composeapp.generated.resources.icon_fd
import velvet.composeapp.generated.resources.sbi_placeholder

@Composable
fun ExploreFundScreen(
    pv: PaddingValues,
    onMFClick: () -> Unit,
    onFDClick: () -> Unit,
    navigateToSpecificMF: (String) -> Unit,
    navigateToSpecificFD: (String) -> Unit
) {

    val viewModel: ExploreFundScreenViewModel = koinViewModel()
    val topFunds by viewModel.mutualFunds.collectAsStateWithLifecycle()
    val topFixedDeposits by viewModel.fixedDeposits.collectAsStateWithLifecycle()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()



    Column(
        modifier=Modifier.fillMaxSize()
    ) {

        BackHeader("Explore Funds")
        ExploreFundScreenContent(topFunds, topFixedDeposits, onMFClick, onFDClick,pv,navigateToSpecificFD,navigateToSpecificMF, uiState)

    }
}

@Composable
fun ExploreFundScreenContent(
    topFunds: List<MutualFundTopPicksUiModel>,
    topFixedDeposits: List<FixedTopPicksUiModel>,
    onMFClick: () -> Unit,
    onFDClick: () -> Unit,
    pv: PaddingValues,
    navigateToSpecificFD: (String) -> Unit,
    navigateToSpecificMF: (String) -> Unit,
    uiState: UIState
) {
    LazyColumn(
        modifier=Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {

        item { BarHeader(heading ="Want to Invest", modifier = Modifier.padding(horizontal = 16.dp)) }

        item { InvestmentOptions(onMFClick, onFDClick) }

        when(uiState){
            is UIState.Error -> {
                item { ErrorScreen(uiState.error, onRetryClick = {}) }
            }
            UIState.Loading -> {
                item { LoaderScreen() }
            }
            UIState.Success -> {
                item { BarHeader(heading ="Top Picks Mutual Funds", modifier = Modifier.padding(horizontal = 16.dp)) }

                item { MFTopPicks(topFunds,navigateToSpecificMF) }

                item { BarHeader(heading ="Top Picks Fixed Deposit", modifier = Modifier.padding(horizontal = 16.dp)) }

                item { FDTopPicks(topFd =topFixedDeposits, onClick =navigateToSpecificFD) }

                item { Spacer(modifier = Modifier.padding(bottom = pv.calculateBottomPadding())) }
            }
        }

    }
}

@Composable
fun FDTopPicks(topFd: List<FixedTopPicksUiModel>, onClick: (String) -> Unit) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(topFd, key = {it.id}){
            TopPicksCardFD(it, onClick = {onClick(it.id)})
        }
    }
}

@Composable
fun MFTopPicks(
    topFunds: List<MutualFundTopPicksUiModel>,
    navigateToSpecificMF: (String) -> Unit,
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        items(topFunds, key = {it.id}){
            TopPicksCardMF(it, onClick = {navigateToSpecificMF(it.id)})
        }
    }
}

@Composable
fun TopPicksCardMF(fund: MutualFundTopPicksUiModel, onClick: () -> Unit) {
    ShadowCard {
        Column(
            modifier = Modifier.width(300.dp).padding(16.dp),
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AsyncImage(
                    modifier = Modifier.size(44.dp),
                    model = fund.icon,
                    contentDescription = null,
                    placeholder = painterResource(Res.drawable.sbi_placeholder),
                    error = painterResource(Res.drawable.sbi_placeholder),
                    fallback = painterResource(Res.drawable.sbi_placeholder)
                )
                Column(
                    horizontalAlignment = Alignment.End,
                ) {

                    Text(
                        text= fund.returnYears.toString()+"Y Returns",
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 1,
                        color = Color.Black
                    )

                    Text(
                        text= fund.percentage.toString()+"%"+" p.a.",
                        style = MaterialTheme.typography.headlineSmall,
                        maxLines = 1,
                        color = appGreen
                    )

                }
            }

            Spacer(Modifier.height(28.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text= fund.name,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.Black,
                    maxLines = 1,
                )
                Text(
                    text= fund.metadata,
                    maxLines = 1,
                    style = titlesStyle,
                    color = Color.Black
                )
            }

            Spacer(Modifier.height(16.dp))

            Button(
                modifier=Modifier.fillMaxWidth()
                    .height(44.dp),
                onClick={
                    onClick()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Primary),
                border = BorderStroke(1.dp, Primary)
            ){
                Text(
                    text="Invest Now",
                    style = buttonTextStyle,
                    color = Primary
                )
            }
        }
    }
}

@Composable
fun TopPicksCardFD(item: FixedTopPicksUiModel, onClick: () -> Unit) {
    ShadowCard {
        Column(
            modifier = Modifier.width(300.dp).padding(16.dp),
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AsyncImage(
                    modifier = Modifier.size(44.dp),
                    model = item.icon,
                    contentDescription = null,
                    placeholder = painterResource(Res.drawable.sbi_placeholder),
                    error = painterResource(Res.drawable.sbi_placeholder),
                    fallback = painterResource(Res.drawable.sbi_placeholder)
                )
                Column(
                    horizontalAlignment = Alignment.End,
                ) {

                    Text(
                        text= item.returnYears.toString()+"Y Returns",
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 1,
                        color = Color.Black
                    )

                    Text(
                        text= item.percentage.toString()+"%",
                        style = MaterialTheme.typography.headlineSmall,
                        maxLines = 1,
                        color = appGreen
                    )

                }
            }

            Spacer(Modifier.height(28.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text= item.name,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.Black,
                    maxLines = 1,
                )
                Text(
                    text= item.metadata,
                    maxLines = 1,
                    style = titlesStyle,
                    color = Color.Black
                )
            }

            Spacer(Modifier.height(16.dp))

            Button(
                modifier=Modifier.fillMaxWidth()
                    .height(44.dp),
                onClick={
                    onClick()
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = Primary),
                border = BorderStroke(1.dp, Primary)
            ){
                Text(
                    text="Invest Now",
                    style = buttonTextStyle,
                    color = Primary
                )
            }
        }
    }
}

@Composable
fun InvestmentOptions(onMFClick: () -> Unit, onFDClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        OptionCard(modifier = Modifier.weight(1f), onClick = onMFClick, icon= Res.drawable.expenses_icon,tint= appGreen,text="Mutual\nFunds")
        OptionCard(modifier = Modifier.weight(1f), onClick = onFDClick, icon= Res.drawable.icon_fd,tint= bgColor5,text="Fixed\nDeposits")
    }
}

@Composable
fun OptionCard(
    modifier: Modifier,
    onClick: () -> Unit,
    icon: DrawableResource,
    tint: Color,
    text: String
) {
    ShadowCard(
        modifier=modifier,
        onClick = onClick,
        clickable = true
    ) {
        Column(
            modifier= Modifier.padding(vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier=Modifier.size(44.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(tint.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier.size(24.dp)
                )
            }

            Text(
                text = text,
                color = Color.Black,
                style = subHeading,
                textAlign = TextAlign.Center
            )

        }
    }
}