package org.sharad.velvetinvestment.features.search.ui.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.back_arrow
import velvet.composeapp.generated.resources.ic_clock
import velvet.composeapp.generated.resources.ic_create_goal
import velvet.composeapp.generated.resources.profile_bank
import velvet.composeapp.generated.resources.search_icon
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.Secondary
import org.sharad.emify.core.ui.theme.appGreen
import org.sharad.emify.core.ui.theme.appRed
import org.sharad.emify.core.ui.theme.backgroundGray
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.shared.compose.AppSearchBar
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import org.sharad.velvetinvestment.shared.theme.subHeading
import org.sharad.velvetinvestment.utils.AppBackHandler
import org.sharad.velvetinvestment.utils.trimTo
import org.sharad.velvetinvestment.shared.compose.FundIcon
import org.sharad.velvetinvestment.features.mutualfund.domain.models.MutualFundDomain
import org.sharad.velvetinvestment.features.mutualfund.domain.models.ReturnYearsRateDomain
import org.sharad.velvetinvestment.shared.genericDropShadow
import org.sharad.velvetinvestment.features.search.domain.model.RecentSearch
import org.sharad.velvetinvestment.features.search.ui.viewmodels.SearchOverlayEvent
import org.sharad.velvetinvestment.features.search.ui.viewmodels.SearchOverlayUiState

/**
 * The full-screen search surface.
 *
 * It is drawn as an overlay on top of whichever screen opened it rather than as its own
 * destination: the screen underneath keeps its scroll position and its loaded data, so closing
 * the search is instant and nothing is re-fetched.
 *
 * The field takes focus as soon as it appears — the user tapped a search bar to get here, so
 * making them tap a second one would be a wasted step.
 */
@Composable
fun SearchOverlay(
    state: SearchOverlayUiState,
    handleEvent: (SearchOverlayEvent) -> Unit,
    onDismiss: () -> Unit,
    onBookFdClick: () -> Unit,
    onCreateGoalClick: () -> Unit,
    /** A live result was tapped; the caller opens the fund's details screen. */
    onFundClick: (MutualFundDomain) -> Unit,
    modifier: Modifier = Modifier
) {
    AppBackHandler(enabled = true, onBack = onDismiss)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .imePadding()
    ) {
        SearchField(
            query = state.query,
            onQueryChange = { handleEvent(SearchOverlayEvent.OnQueryChange(it)) },
            onSubmit = { handleEvent(SearchOverlayEvent.OnSubmit) },
            onDismiss = onDismiss
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            if (state.showSuggestions) {
                FundSuggestionsCard(
                    funds = state.suggestions,
                    isLoading = state.isLoadingSuggestions,
                    onFundClick = onFundClick,
                    // The same as the keyboard's search action: the full results for the term.
                    onSeeAllClick = { handleEvent(SearchOverlayEvent.OnSubmit) }
                )
            }

            if (state.showRecents) {
                RecentSearchesCard(
                    searches = state.recentSearches,
                    onRecentClick = { handleEvent(SearchOverlayEvent.OnRecentClick(it)) },
                    onClearClick = { handleEvent(SearchOverlayEvent.OnClearRecents) }
                )
            }

            QuickActions(
                onBookFdClick = onBookFdClick,
                onCreateGoalClick = onCreateGoalClick
            )
        }
    }
}

/**
 * The real input: Velvet's [AppSearchBar], focused as soon as the overlay opens, with a back
 * arrow that closes the overlay.
 */
@Composable
private fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    onSubmit: () -> Unit,
    onDismiss: () -> Unit
) {
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 12.dp, end = 16.dp, top = 8.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(Res.drawable.back_arrow),
            contentDescription = "Close search",
            tint = Color.Black,
            modifier = Modifier
                .size(22.dp)
                .clickable(
                    onClick = onDismiss,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                )
        )

        AppSearchBar(
            value = query,
            onTextChange = onQueryChange,
            onSearchClick = onSubmit,
            placeholder = SEARCH_PLACEHOLDER,
            focusRequester = focusRequester,
            modifier = Modifier.weight(1f)
        )
    }
}

private const val SEARCH_PLACEHOLDER = "Search Mutual funds, FDs..."

/**
 * Funds matching the term as it is typed: a compact row each — logo, name, category and the
 * one-year return — so the list reads at a glance and tapping one goes straight to buying it.
 */
@Composable
private fun FundSuggestionsCard(
    funds: List<MutualFundDomain>,
    isLoading: Boolean,
    onFundClick: (MutualFundDomain) -> Unit,
    onSeeAllClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(backgroundGray)
            .padding(vertical = 8.dp, horizontal = 12.dp)
    ) {
        // Only the first fetch shows the loader; later ones keep the old rows until new ones land.
        if (funds.isEmpty() && isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = Primary,
                    strokeWidth = 2.dp,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        funds.forEachIndexed { index, fund ->
            if (index > 0) {
                HorizontalDivider(color = Color.White, thickness = 1.dp)
            }
            FundSuggestionRow(fund = fund, onClick = { onFundClick(fund) })
        }

        if (funds.isNotEmpty()) {
            HorizontalDivider(color = Color.White, thickness = 1.dp)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        onClick = onSeeAllClick,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    )
                    .padding(vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(6.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "See all results",
                    style = subHeading,
                    color = Secondary
                )
                Icon(
                    painter = painterResource(Res.drawable.search_icon),
                    contentDescription = null,
                    tint = Secondary,
                    modifier = Modifier.size(14.dp)
                )
            }
        }
    }
}

@Composable
private fun FundSuggestionRow(
    fund: MutualFundDomain,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        FundIcon(
            iconUrl = fund.icon,
            name = fund.name,
            size = 32.dp,
            cornerRadius = 8.dp
        )

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = fund.name,
                style = MaterialTheme.typography.displaySmall.copy(fontWeight = FontWeight.SemiBold),
                color = Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            val subtitle = fund.category.ifBlank { fund.type }
            if (subtitle.isNotBlank()) {
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.displaySmall,
                    color = titleColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        fund.returnYearsRate.year3?.let { year3 ->
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = (if (year3 >= 0) "+" else "") + "${year3.trimTo(1)}%",
                    style = subHeading,
                    color = if (year3 >= 0) appGreen else appRed
                )
                Text(
                    text = "3Y",
                    style = MaterialTheme.typography.displaySmall,
                    color = titleColor
                )
            }
        }
    }
}

/** Terms the user has actually searched, newest first. */
@Composable
private fun RecentSearchesCard(
    searches: List<RecentSearch>,
    onRecentClick: (String) -> Unit,
    onClearClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(backgroundGray)
            .padding(vertical = 20.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(Res.drawable.ic_clock),
                contentDescription = null,
                tint = Primary,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = "Recent Searches",
                style = subHeading,
                color = Primary,
                modifier = Modifier.weight(1f)
            )
            Text(
                // A recents list the user cannot empty is a list they are stuck with.
                text = "Clear",
                style = subHeading,
                color = Secondary,
                modifier = Modifier.clickable(
                    onClick = onClearClick,
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                )
            )
        }

        searches.forEach { search ->
            RecentSearchRow(
                query = search.query,
                onClick = { onRecentClick(search.query) }
            )
        }
    }
}

@Composable
private fun RecentSearchRow(
    query: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                onClick = onClick,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(Res.drawable.search_icon),
            contentDescription = null,
            tint = titleColor,
            modifier = Modifier.size(16.dp)
        )
        Text(
            text = query,
            style = MaterialTheme.typography.displayMedium,
            color = Color.Black,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/**
 * Shortcuts for users who came to the search without a term in mind. JN's Daily ₹10 and
 * Monthly ₹100 cards are left out: Velvet doesn't offer those starters.
 */
@Composable
private fun QuickActions(
    onBookFdClick: () -> Unit,
    onCreateGoalClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = "Quick Actions",
            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
            color = Primary
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            QuickActionCard(
                icon = Res.drawable.profile_bank,
                iconBackground = Primary.copy(alpha = 0.1f),
                title = "Book FD",
                subtitle = "Secure returns",
                onClick = onBookFdClick,
                modifier = Modifier.weight(1f)
            )
            QuickActionCard(
                icon = Res.drawable.ic_create_goal,
                iconBackground = Secondary.copy(alpha = 0.15f),
                iconTint = Secondary,
                title = "Create Goal",
                subtitle = "Plan for what matters",
                onClick = onCreateGoalClick,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun QuickActionCard(
    icon: DrawableResource,
    iconBackground: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    iconTint: Color = Primary
) {
    Column(
        modifier = modifier
            .genericDropShadow(RoundedCornerShape(15.dp))
            .clip(RoundedCornerShape(15.dp))
            .background(Color.White)
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .background(iconBackground),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )
        }

        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                text = title,
                style = subHeading,
                color = Color.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.displaySmall,
                color = titleColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 900)
@Composable
private fun SearchOverlayPreview() {
    VelvetTheme {
        SearchOverlay(
            state = SearchOverlayUiState(
                query = "hdfc",
                suggestions = listOf(
                    MutualFundDomain(
                        id = "1",
                        name = "HDFC Mid Cap Fund - Growth Plan",
                        icon = "",
                        category = "Equity: Mid Cap",
                        returnYearsRate = ReturnYearsRateDomain(null, null, year1 = 14.3, year3 = null)
                    ),
                    MutualFundDomain(
                        id = "2",
                        name = "HDFC Flexi Cap Fund - Growth Plan",
                        icon = "",
                        category = "Equity: Flexi Cap",
                        returnYearsRate = ReturnYearsRateDomain(null, null, year1 = -6.6, year3 = null)
                    )
                ),
                recentSearches = listOf(
                    RecentSearch("SBI Fixed Deposit", 4),
                    RecentSearch("HDFC Mid Cap Fund", 3),
                    RecentSearch("Top ELSS Funds 2024", 2),
                    RecentSearch("Gold ETF", 1)
                )
            ),
            handleEvent = {},
            onDismiss = {},
            onBookFdClick = {},
            onCreateGoalClick = {},
            onFundClick = {}
        )
    }
}
