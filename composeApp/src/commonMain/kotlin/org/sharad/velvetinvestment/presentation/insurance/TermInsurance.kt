package org.sharad.velvetinvestment.presentation.insurance

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.emify.core.ui.theme.termColor
import org.sharad.velvetinvestment.shared.UiStateContainer
import org.sharad.velvetinvestment.utils.formatWithCommas
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.banner_term_1
import velvet.composeapp.generated.resources.banner_term_2
import velvet.composeapp.generated.resources.banner_term_3
import velvet.composeapp.generated.resources.nav_icon_incurance

@Composable
fun TermInsuranceScreen(
    onBackClick: () -> Unit,
){
    val viewModel: InsuranceScreenViewModel = koinViewModel()
    val state by viewModel.insuranceInfo.collectAsStateWithLifecycle()
    val images = listOf(
        Res.drawable.banner_term_3,
        Res.drawable.banner_term_1,
        Res.drawable.banner_term_2
    )


    UiStateContainer(
        uiState = state,
        onRetry = { viewModel.loadData() }
    ) { data ->
        val recommended by viewModel.recommendedLife.collectAsStateWithLifecycle()
        val remaining = (recommended - (data.lifeInsurance ?: 0L)).coerceIn(0, recommended)
        SharedInsuranceScreen(
            heading = "Term Insurance",
            onBack = onBackClick,
            subHeading = "Life Coverage Progress",
            icon = Res.drawable.nav_icon_incurance,
            tint = termColor,
            status = "Active",
            coverage = "₹ " + formatWithCommas(data.lifeInsurance ?: 0),
            recommended = "₹ " + formatWithCommas(recommended),
            gap = "₹ " + formatWithCommas(remaining),
            infoText = "Term life cover is estimated using Annual Income and an age-based multiplier.\n" +
                    "\n" +
                    "Age Multiplier:\n" +
                    "20–30 years: 30× Annual Income\n" +
                    "31–40 years: 20× Annual Income\n" +
                    "41–50 years: 15× Annual Income\n" +
                    "Above 50 years: 10× Annual Income",
            image = images
        )
    }
}