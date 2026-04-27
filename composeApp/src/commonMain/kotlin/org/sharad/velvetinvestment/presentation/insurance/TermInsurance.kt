package org.sharad.velvetinvestment.presentation.insurance

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.emify.core.ui.theme.termColor
import org.sharad.velvetinvestment.shared.compose.ErrorScreen
import org.sharad.velvetinvestment.shared.compose.LoaderScreen
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.formatWithCommas
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.nav_icon_incurance
import velvet.composeapp.generated.resources.pl2

@Composable
fun TermInsuranceScreen(
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
            val recommended by viewModel.recommendedLife.collectAsStateWithLifecycle()
            val remaining= recommended - (data.lifeInsurance?:0L).coerceIn(0,recommended)
            SharedInsuranceScreen(
                heading = "Term Insurance",
                onBack = onBackClick,
                subHeading = "Life Coverage Progress",
                icon = Res.drawable.nav_icon_incurance,
                tint = termColor,
                status = "Active",
                coverage = "₹ "+formatWithCommas(data.lifeInsurance?:0),
                recommended ="₹ "+ formatWithCommas(recommended),
                gap = "₹ "+ formatWithCommas(remaining),
                infoText = "A CAS (Consolidate Account Statement) Provides an automated way to track all your Mutual Fund holdings across different brokers in one place.",
                image = Res.drawable.pl2
            )
        }
    }

}