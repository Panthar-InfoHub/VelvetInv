package org.sharad.velvetinvestment.presentation.mutualfund.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.velvetinvestment.presentation.mutualfund.viewmodel.AllBundlesViewModel
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.ErrorScreen
import org.sharad.velvetinvestment.shared.compose.LoaderScreen
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.formatMoneyAfterL

@Composable
fun AllBundlesScreen(
    onBackClick: () -> Unit,
    onBundleClick: (String) -> Unit,
    pv: PaddingValues
) {
    val viewModel: AllBundlesViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            BackHeader(
                heading = "Curated Bundles",
                showBack = true,
                onBackClick = onBackClick
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val state = uiState) {
                is UiState.Loading -> {
                    LoaderScreen()
                }
                is UiState.Error -> {
                    ErrorScreen(
                        errorMessage = state.message,
                        onRetryClick = { viewModel.loadBundles() }
                    )
                }
                is UiState.Success -> {
                    val bundles = state.data
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
    }
}
