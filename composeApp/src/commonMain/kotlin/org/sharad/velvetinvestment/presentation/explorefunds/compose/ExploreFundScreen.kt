package org.sharad.velvetinvestment.presentation.explorefunds.compose

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.CircleShape
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
import coil3.compose.SubcomposeAsyncImage
import coil3.compose.SubcomposeAsyncImageContent
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.appGreen
import org.sharad.emify.core.ui.theme.bgColor4
import org.sharad.emify.core.ui.theme.bgColor5
import org.sharad.emify.core.ui.theme.shadowColor
import org.sharad.velvetinvestment.domain.models.fixeddeposits.FixedDepositDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundDomain
import org.sharad.velvetinvestment.presentation.explorefunds.uimodel.FixedTopPicksUiModel
import org.sharad.velvetinvestment.presentation.explorefunds.uimodel.MutualFundTopPicksUiModel
import org.sharad.velvetinvestment.presentation.explorefunds.viewmodel.ExploreFundScreenViewModel
import org.sharad.velvetinvestment.presentation.explorefunds.viewmodel.TopPickCombinedUiModel
import org.sharad.velvetinvestment.presentation.fixeddeposits.compose.FDListCard
import org.sharad.velvetinvestment.presentation.mutualfund.compose.MutualFundIcon
import org.sharad.velvetinvestment.presentation.mutualfund.compose.MutualFundListCard
import org.sharad.velvetinvestment.presentation.mutualfund.viewmodel.SelectedReturnRatePeriod
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.BarHeader
import org.sharad.velvetinvestment.shared.compose.ErrorScreen
import org.sharad.velvetinvestment.shared.compose.LoaderScreen
import org.sharad.velvetinvestment.shared.compose.ShadowCard
import org.sharad.velvetinvestment.shared.theme.buttonTextStyle
import org.sharad.velvetinvestment.shared.theme.subHeading
import org.sharad.velvetinvestment.shared.theme.titlesStyle
import org.sharad.velvetinvestment.utils.UiState
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.expenses_icon
import velvet.composeapp.generated.resources.icon_fd

@Composable
fun ExploreFundScreen(
    onMFClick: () -> Unit,
    onFDClick: () -> Unit,
    navigateToSpecificMF: (String) -> Unit,
    navigateToSpecificFD: (String) -> Unit
) {

    val viewModel: ExploreFundScreenViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()


    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        BackHeader("Explore Funds")
        ExploreFundScreenContent(
            onMFClick = onMFClick,
            onFDClick = onFDClick,
            navigateToSpecificFD = navigateToSpecificFD,
            navigateToSpecificMF = navigateToSpecificMF,
            uiState = uiState,
            retry = viewModel::loadExploreData
        )

    }
}

@Composable
fun ExploreFundScreenContent(
    onMFClick: () -> Unit,
    onFDClick: () -> Unit,
    navigateToSpecificFD: (String) -> Unit,
    navigateToSpecificMF: (String) -> Unit,
    uiState: UiState<TopPickCombinedUiModel>,
    retry: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(bottom = 20.dp)
    ) {

        item {
            BarHeader(
                heading = "Want to Invest",
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        item { InvestmentOptions(onMFClick, onFDClick) }

        when (uiState) {
            is UiState.Error -> {
                item { ErrorScreen(uiState.message, onRetryClick = retry) }
            }

            UiState.Loading -> {
                item { LoaderScreen() }
            }

            is UiState.Success -> {
                val data = uiState.data
                item {
                    BarHeader(
                        heading = "Top Picks Mutual Funds",
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }

                item{
                    MFTopPicks(data.funds, navigateToSpecificMF)
                }
                item {
                    Spacer(Modifier.height(20.dp))
                    BarHeader(
                        heading = "Top Picks Fixed Deposit",
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }

                item{
                    FDTopPicks(topFd = data.fixedDeposits, onClick = navigateToSpecificFD)
                }
            }
        }

    }
}

@Composable
fun FDTopPicks(topFd: List<FixedDepositDomain>, onClick: (String) -> Unit) {
    topFd.forEach {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            FDListCard(fd = it, onClick = { onClick(it.id) })
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .height(1.dp)
                    .clip(CircleShape)
                    .background(shadowColor)
            )
        }
    }
}

@Composable
fun MFTopPicks(
    topFunds: List<MutualFundDomain>,
    navigateToSpecificMF: (String) -> Unit,
) {
    topFunds.forEachIndexed { idx, it ->
        Column(
            modifier = Modifier.padding(horizontal = 16.dp)
        ) {
            MutualFundListCard(
                onClick = { navigateToSpecificMF(it.id) },
                fund = it,
                selectedYear = SelectedReturnRatePeriod.THREE_YEAR,
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp, bottom = if (idx==topFunds.lastIndex) 0.dp else 12.dp)
                    .height(1.dp)
                    .clip(CircleShape)
                    .background(shadowColor)
            )
        }
    }
}

@Composable
fun TopPicksCardMF(fund: MutualFundTopPicksUiModel, onClick: () -> Unit) {
    ShadowCard(
        modifier = Modifier.border(1.dp, bgColor4.copy(0.1f), RoundedCornerShape(15.dp))
    ) {
        Column(
            modifier = Modifier.width(300.dp).padding(16.dp),
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SubcomposeAsyncImage(
                    modifier = Modifier.size(44.dp), model = fund.icon, contentDescription = null,

                    loading = {
                        MutualFundIcon(
                            schemeName = fund.name, size = 44.dp
                        )
                    },

                    error = {
                        MutualFundIcon(
                            schemeName = fund.name, size = 44.dp
                        )
                    },

                    success = {
                        SubcomposeAsyncImageContent()
                    }
                )
                Column(
                    horizontalAlignment = Alignment.End,
                ) {

                    Text(
                        text = fund.returnYears.toString() + "Y Returns",
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 1,
                        color = Color.Black
                    )

                    Text(
                        text = fund.percentage.toString() + "%" + " p.a.",
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
                    text = fund.name,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.Black,
                    maxLines = 1,
                )
                Text(
                    text = fund.metadata, maxLines = 1, style = titlesStyle, color = Color.Black
                )
            }

            Spacer(Modifier.height(16.dp))

            Button(
                modifier = Modifier.fillMaxWidth().height(44.dp),
                onClick = {
                    onClick()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Primary
                ),
                border = BorderStroke(1.dp, Primary)
            ) {
                Text(
                    text = "Invest Now", style = buttonTextStyle, color = Primary
                )
            }
        }
    }
}

@Composable
fun TopPicksCardFD(item: FixedTopPicksUiModel, onClick: () -> Unit) {
    ShadowCard(
        modifier = Modifier.border(1.dp, bgColor4.copy(0.1f), RoundedCornerShape(15.dp))
    ) {
        Column(
            modifier = Modifier.width(300.dp).padding(16.dp),
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SubcomposeAsyncImage(
                    modifier = Modifier.size(44.dp), model = item.icon, contentDescription = null,

                    loading = {
                        MutualFundIcon(
                            schemeName = item.name, size = 44.dp
                        )
                    },

                    error = {
                        MutualFundIcon(
                            schemeName = item.name, size = 44.dp
                        )
                    },

                    success = {
                        SubcomposeAsyncImageContent()
                    })
                Column(
                    horizontalAlignment = Alignment.End,
                ) {

                    Text(
                        text = "Max Returns",
                        style = MaterialTheme.typography.labelSmall,
                        maxLines = 1,
                        color = Color.Black
                    )

                    Text(
                        text = item.percentage.toString() + "%",
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
                    text = item.name,
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.Black,
                    maxLines = 1,
                )
                Text(
                    text = item.metadata, maxLines = 1, style = titlesStyle, color = Color.Black
                )
            }

            Spacer(Modifier.height(16.dp))

            Button(
                modifier = Modifier.fillMaxWidth().height(44.dp),
                onClick = {
                    onClick()
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Primary
                ),
                border = BorderStroke(1.dp, Primary)
            ) {
                Text(
                    text = "Invest Now", style = buttonTextStyle, color = Primary
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
        OptionCard(
            modifier = Modifier.weight(1f),
            onClick = onMFClick,
            icon = Res.drawable.expenses_icon,
            tint = appGreen,
            text = "Mutual\nFunds"
        )
        OptionCard(
            modifier = Modifier.weight(1f),
            onClick = onFDClick,
            icon = Res.drawable.icon_fd,
            tint = bgColor5,
            text = "Fixed\nDeposits"
        )
    }
}

@Composable
fun OptionCard(
    modifier: Modifier, onClick: () -> Unit, icon: DrawableResource, tint: Color, text: String
) {
    ShadowCard(
        modifier = modifier.border(1.dp, bgColor4.copy(0.1f), RoundedCornerShape(15.dp)),
        onClick = onClick,
        clickable = true
    ) {
        Column(
            modifier = Modifier.padding(vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier.size(44.dp).clip(RoundedCornerShape(10.dp))
                    .background(tint.copy(alpha = 0.1f)), contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier.size(24.dp)
                )
            }

            Text(
                text = text, color = Color.Black, style = subHeading, textAlign = TextAlign.Center
            )

        }
    }
}
