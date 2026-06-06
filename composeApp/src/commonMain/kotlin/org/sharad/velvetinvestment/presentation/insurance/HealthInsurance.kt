package org.sharad.velvetinvestment.presentation.insurance

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.emify.core.ui.theme.healthColor
import org.sharad.velvetinvestment.shared.UiStateContainer
import org.sharad.velvetinvestment.utils.formatWithCommas
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.banner_health_1
import velvet.composeapp.generated.resources.banner_health_2
import velvet.composeapp.generated.resources.banner_health_3
import velvet.composeapp.generated.resources.icon_heart

@Composable
fun HealthInsuranceScreen(
    onBackClick: () -> Unit,
){
    val viewModel: InsuranceScreenViewModel = koinViewModel()
    val state by viewModel.insuranceInfo.collectAsStateWithLifecycle()
    val images = listOf(
        Res.drawable.banner_health_3,
        Res.drawable.banner_health_1,
        Res.drawable.banner_health_2
    )

    UiStateContainer(
        uiState = state,
        onRetry = { viewModel.loadData() }
    ) { data ->
        val recommended by viewModel.recommendedHealth.collectAsStateWithLifecycle()
        val remaining = (recommended - (data.healthInsurance ?: 0L)).coerceIn(0, recommended)

        SharedInsuranceScreen(
            heading = "Health Insurance",
            onBack = onBackClick,
            subHeading = "Coverage Progress",
            icon = Res.drawable.icon_heart,
            tint = healthColor,
            status = "Active",
            coverage = "₹ " + formatWithCommas(data.healthInsurance ?: 0),
            recommended = "₹ " + formatWithCommas(recommended),
            gap = "₹ " + formatWithCommas(remaining),
            infoText = "Health insurance cover is estimated using an age-based coverage benchmark.\n" +
                    "\n" +
                    "Age-Based Cover:\n" +
                    "20–30 years: ₹10 Lakhs\n" +
                    "31–40 years: ₹15 Lakhs\n" +
                    "41–50 years: ₹25 Lakhs\n" +
                    "Above 50 years: ₹40 Lakhs",
            image = images
        )
    }
}