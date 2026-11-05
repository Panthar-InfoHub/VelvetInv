package org.sharad.velvetinvestment.features.bottomNavigation.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.sharad.emify.core.ui.theme.appGreen
import org.sharad.emify.core.ui.theme.appRed
import org.sharad.emify.core.ui.theme.bgColor4
import org.sharad.emify.core.ui.theme.bgColor5
import org.sharad.emify.core.ui.theme.shadowColor
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.features.bottomNavigation.domain.models.FixedTopPicksUiModel
import org.sharad.velvetinvestment.features.bottomNavigation.domain.models.MutualFundTopPicksUiModel
import org.sharad.velvetinvestment.features.bottomNavigation.ui.viewmodels.ExploreFundsEvent
import org.sharad.velvetinvestment.features.bottomNavigation.ui.viewmodels.ExploreFundsUiState
import org.sharad.velvetinvestment.features.mutualfund.utils.toTitleCase
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.BarHeader
import org.sharad.velvetinvestment.shared.compose.ErrorScreen
import org.sharad.velvetinvestment.shared.compose.FundIcon
import org.sharad.velvetinvestment.shared.compose.LoaderScreen
import org.sharad.velvetinvestment.shared.compose.ShadowCard
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import org.sharad.velvetinvestment.shared.theme.subHeading
import org.sharad.velvetinvestment.shared.theme.titlesStyle
import org.sharad.velvetinvestment.utils.trimTo
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.expenses_icon
import velvet.composeapp.generated.resources.fd_placeholder
import velvet.composeapp.generated.resources.icon_fd

/**
 * The Explore tab. JN's screen (state + events from `ExploreFundsViewModel`) drawn the way
 * Velvet's old `ExploreFundScreen` was: two option cards, then the top picks as divided rows.
 */
@Composable
fun ExploreFundsScreen(
    uiState: ExploreFundsUiState,
    handleEvent: (ExploreFundsEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        BackHeader("Explore Funds")

        when {
            uiState.isLoading -> LoaderScreen(modifier = Modifier.fillMaxSize())

            uiState.showError -> ErrorScreen(
                errorMessage = uiState.error,
                onRetryClick = { handleEvent(ExploreFundsEvent.LoadInitialData) },
                modifier = Modifier.fillMaxSize()
            )

            else -> ExploreFundsContent(uiState = uiState, handleEvent = handleEvent)
        }
    }
}

@Composable
fun ExploreFundsContent(
    uiState: ExploreFundsUiState,
    handleEvent: (ExploreFundsEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(bottom = 20.dp)
    ) {
        item {
            BarHeader(
                heading = "Want to Invest",
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        item {
            InvestmentOptions(
                onMFClick = { handleEvent(ExploreFundsEvent.OnMutualFundsCategoryClick) },
                onFDClick = { handleEvent(ExploreFundsEvent.OnFixedDepositCategoryClick) }
            )
        }

        if (uiState.mutualFundList.isNotEmpty()) {
            item {
                BarHeader(
                    heading = "Top Picks Mutual Funds",
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            itemsIndexed(uiState.mutualFundList, key = { _, fund -> "mf-" + fund.id }) { index, fund ->
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    TopPickMutualFundRow(
                        fund = fund,
                        onClick = { handleEvent(ExploreFundsEvent.OnMutualFundInvestClick(fund.id)) }
                    )
                    if (index != uiState.mutualFundList.lastIndex) RowDivider()
                }
            }
        }

        if (uiState.fixedDepositList.isNotEmpty()) {
            item {
                Spacer(Modifier.height(20.dp))
                BarHeader(
                    heading = "Top Picks Fixed Deposit",
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            itemsIndexed(uiState.fixedDepositList, key = { _, fd -> "fd-" + fd.id }) { index, fd ->
                Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                    TopPickFixedDepositRow(
                        fd = fd,
                        onClick = { handleEvent(ExploreFundsEvent.OnFixedDepositClick(fd.id)) }
                    )
                    if (index != uiState.fixedDepositList.lastIndex) RowDivider()
                }
            }
        }
    }
}

@Composable
private fun RowDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .height(1.dp)
            .clip(CircleShape)
            .background(shadowColor)
    )
}

/** Velvet's fund-list row (`MutualFundListCard` look) for a top pick's UI model. */
@Composable
private fun TopPickMutualFundRow(fund: MutualFundTopPicksUiModel, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            ),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FundIcon(iconUrl = fund.icon, name = fund.name)

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = fund.name.toTitleCase(),
                style = subHeading,
                color = Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            if (fund.metadata.isNotBlank()) {
                Text(
                    text = fund.metadata,
                    style = titlesStyle,
                    color = titleColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(start = 4.dp)
        ) {
            val rate = fund.percentage
            if (rate != null) {
                Text(
                    text = "${rate.trimTo(2)} %",
                    style = subHeading,
                    color = if (rate >= 0) appGreen else appRed
                )
                Text(
                    text = "${fund.returnYears}Y",
                    style = titlesStyle,
                    color = titleColor
                )
            } else {
                Text(text = "N/A", style = subHeading, color = Color.Black)
            }
        }
    }
}

/** Velvet's `FDListCard` look for a top pick's UI model. */
@Composable
private fun TopPickFixedDepositRow(fd: FixedTopPicksUiModel, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() },
                onClick = onClick
            ),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = fd.icon,
            contentDescription = null,
            modifier = Modifier.size(40.dp),
            fallback = painterResource(Res.drawable.fd_placeholder),
            error = painterResource(Res.drawable.fd_placeholder),
            placeholder = painterResource(Res.drawable.fd_placeholder)
        )
        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = fd.name,
                    style = subHeading,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Text(
                    text = "${fd.percentage.trimTo(2)} %",
                    style = subHeading,
                    color = appGreen
                )
            }
            Spacer(Modifier.height(2.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = fd.metadata,
                    style = titlesStyle,
                    color = titleColor,
                    modifier = Modifier.weight(1f)
                )
                Text(text = "Max Returns", style = titlesStyle, color = titleColor)
            }
        }
    }
}

@Composable
private fun InvestmentOptions(onMFClick: () -> Unit, onFDClick: () -> Unit) {
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
private fun OptionCard(
    modifier: Modifier,
    onClick: () -> Unit,
    icon: DrawableResource,
    tint: Color,
    text: String
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
                modifier = Modifier
                    .size(44.dp)
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

            Text(text = text, color = Color.Black, style = subHeading, textAlign = TextAlign.Center)
        }
    }
}

@Preview
@Composable
private fun ExploreFundsScreenPreview() {
    VelvetTheme {
        ExploreFundsScreen(
            uiState = ExploreFundsUiState(
                mutualFundList = listOf(
                    MutualFundTopPicksUiModel(
                        icon = "",
                        name = "Parag Parikh Flexi Cap Fund",
                        metadata = "Equity: Flexi Cap • Very High Risk",
                        returnYears = 3,
                        percentage = 21.4,
                        id = "1"
                    ),
                    MutualFundTopPicksUiModel(
                        icon = "",
                        name = "HDFC Mid Cap Opportunities Fund",
                        metadata = "NAV 182.4",
                        returnYears = 3,
                        percentage = null,
                        id = "2"
                    )
                ),
                fixedDepositList = listOf(
                    FixedTopPicksUiModel(
                        icon = "",
                        name = "Shriram Finance",
                        metadata = "Low",
                        percentage = 8.6,
                        id = "fd1"
                    )
                )
            ),
            handleEvent = {}
        )
    }
}
