package org.sharad.velvetinvestment.presentation.bundle.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.velvetinvestment.domain.models.mutualfunds.BundledMutualFundDomain
import org.sharad.velvetinvestment.presentation.cart.compose.CartFab
import org.sharad.velvetinvestment.presentation.bundle.viewmodel.AllBundlesViewModel
import org.sharad.velvetinvestment.shared.UiStateContainer
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import org.sharad.velvetinvestment.utils.CartInfo
import org.sharad.velvetinvestment.utils.UiState

@Composable
fun AllBundlesScreen(
    onBackClick: () -> Unit,
    onBundleClick: (String) -> Unit,
    onCartClick: () -> Unit,
) {
    val viewModel: AllBundlesViewModel = koinViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val cartAmount by CartInfo.fundAmount.collectAsStateWithLifecycle()

    AllBundlesContent(
        uiState = uiState,
        cartAmount = cartAmount,
        onBackClick = onBackClick,
        onBundleClick = onBundleClick,
        onCartClick = onCartClick,
        onRetry = { viewModel.loadBundles() }
    )
}

@Composable
fun AllBundlesContent(
    uiState: UiState<List<BundledMutualFundDomain>>,
    cartAmount: Int,
    onBackClick: () -> Unit,
    onBundleClick: (String) -> Unit,
    onCartClick: () -> Unit,
    onRetry: () -> Unit,
) {
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
            onRetry = onRetry
        ) { bundles ->
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(bundles) { bundle ->
                    BundleCardExtended(
                        onClick = { onBundleClick(bundle.id) },
                        bundleData = bundle,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun AllBundlesPreview() {
    VelvetTheme {
        AllBundlesContent(
            uiState = UiState.Success(
                listOf(
                    BundledMutualFundDomain(
                        id = "1",
                        bundleName = "Velvet Preserve",
                        bundleDescription = "Balanced wealth with lower volatility",
                        equityPercentage = 70,
                        hybridPercentage = 15,
                        debtPercentage = 10,
                        commodityPercentage = 5,
                        riskLevel = "Moderate",
                        investmentTime = "5+ Years",
                        investmentGrowth = "Balanced"
                    ),
                    BundledMutualFundDomain(
                        id = "2",
                        bundleName = "Velvet Growth",
                        bundleDescription = "High growth potential with equity focus",
                        equityPercentage = 90,
                        hybridPercentage = 5,
                        debtPercentage = 5,
                        commodityPercentage = 0,
                        riskLevel = "High",
                        investmentTime = "7+ Years",
                        investmentGrowth = "Aggressive"
                    )
                )
            ),
            cartAmount = 5000,
            onBackClick = {},
            onBundleClick = {},
            onCartClick = {},
            onRetry = {}
        )
    }
}
