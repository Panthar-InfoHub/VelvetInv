package org.sharad.velvetinvestment.presentation.mutualfund.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.velvetinvestment.presentation.mutualfund.viewmodel.AllBundlesViewModel
import org.sharad.velvetinvestment.shared.UiStateContainer
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.utils.CartInfo
import org.sharad.velvetinvestment.utils.formatMoneyAfterL

@Composable
fun AllBundlesScreen(
    onBackClick: () -> Unit,
    onBundleClick: (String) -> Unit,
    onCartClick: () -> Unit,
    pv: PaddingValues
) {
    val viewModel: AllBundlesViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val cartAmount by CartInfo.fundAmount.collectAsStateWithLifecycle()


    Scaffold(
        topBar = {
            BackHeader(
                heading = "Curated Bundles",
                showBack = true,
                onBackClick = onBackClick
            )
        },
        floatingActionButton = {
            CartFab(
                onClick = { onCartClick() },
                cartAmount = cartAmount,
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        UiStateContainer(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            uiState = uiState,
            onRetry = { viewModel.loadBundles() }
        ) { bundles ->
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(bundles) { bundle ->
                    BundleCard(
                        title = bundle.categoryName,
                        minAmount = "₹" + formatMoneyAfterL(bundle.minAmount.toLong()),
                        onClick = { onBundleClick(bundle.key) }
                    )
                }
                item(span = { GridItemSpan(2) }) {
                    Spacer(modifier = Modifier.height(pv.calculateBottomPadding()))
                }
            }
        }
    }
}
