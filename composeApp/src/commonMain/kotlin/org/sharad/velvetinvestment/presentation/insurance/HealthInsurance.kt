package org.sharad.velvetinvestment.presentation.insurance

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.emify.core.ui.theme.healthColor
import org.sharad.velvetinvestment.shared.compose.ErrorScreen
import org.sharad.velvetinvestment.shared.compose.LoaderScreen
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.formatWithCommas
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.icon_heart
import velvet.composeapp.generated.resources.pl3

@Composable
fun HealthInsuranceScreen(
    onBackClick: () -> Unit,
){
    val viewModel:InsuranceScreenViewModel= koinViewModel()
    val state by viewModel.insuranceInfo.collectAsStateWithLifecycle()
    when(state){
        is UiState.Error -> {
            ErrorScreen(
                errorMessage = (state as UiState.Error).message,
                onRetryClick = { viewModel.loadData() }
            )
        }
        UiState.Loading -> {
            LoaderScreen()
        }
        is UiState.Success-> {
            val data = (state as UiState.Success).data
            val recommended = viewModel.recommendedHealth
            val remaining= (recommended- (data.healthInsurance?:0L)).coerceIn(0,recommended)

            SharedInsuranceScreen(
                heading = "Health Insurance",
                onBack = onBackClick,
                subHeading = "Coverage Progress",
                icon = Res.drawable.icon_heart,
                tint = healthColor,
                status = "Active",
                coverage = "₹ " + formatWithCommas(data.healthInsurance?:0),
                recommended = "₹ " + formatWithCommas(recommended),
                gap =  "₹ " + formatWithCommas(remaining),
                infoText = "A CAS (Consolidate Account Statement) Provides an automated way to track all your Mutual Fund holdings across different brokers in one place.",
                image = Res.drawable.pl3
            )
        }
    }

}