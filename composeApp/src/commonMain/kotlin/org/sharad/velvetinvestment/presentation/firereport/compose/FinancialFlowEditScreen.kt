package org.sharad.velvetinvestment.presentation.firereport.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.velvetinvestment.presentation.firereport.viewmodel.FinancialFlowEditScreenViewModel
import org.sharad.velvetinvestment.presentation.onboarding.compose.financialflow.GenericInfoHeader
import org.sharad.velvetinvestment.presentation.onboarding.compose.financialflow.IncomeEntry
import org.sharad.velvetinvestment.presentation.onboarding.compose.financialflow.MonthlyExpenses
import org.sharad.velvetinvestment.presentation.onboarding.compose.financialflow.Summary
import org.sharad.velvetinvestment.presentation.onboarding.compose.personaldetails.NextButtonFooter
import org.sharad.velvetinvestment.presentation.onboarding.models.FinancialFlowDetails
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.FinancialFlowScreenViewModel
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.ErrorScreen
import org.sharad.velvetinvestment.shared.compose.LoaderScreen
import org.sharad.velvetinvestment.utils.UiState

@Composable
fun FinancialFlowEditScreen(
    onBackClick: () -> Unit,
    pv: PaddingValues,
) {
    val viewModel: FinancialFlowEditScreenViewModel = koinViewModel()

    val loading by viewModel.loading.collectAsStateWithLifecycle()

    val state by viewModel.financialInfo.collectAsStateWithLifecycle()
    val expensePercent by viewModel.expensePercentages.collectAsStateWithLifecycle()
    val financialSummary by viewModel.financialSummary.collectAsStateWithLifecycle()

    when(state){
        is UiState.Error -> {
            ErrorScreen(
                errorMessage = (state as UiState.Error).message,
                onRetryClick = {
                    viewModel.loadData()
                }
            )
        }
        is UiState.Loading -> {
            LoaderScreen()
        }
        is UiState.Success -> {
            val financialInfo= (state as UiState.Success<FinancialFlowDetails>).data
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                BackHeader("Update Financial Info", onBackClick = onBackClick, showBack = true)
                LazyColumn(
                    modifier = Modifier.weight(1f).fillMaxSize().padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                )
                {

                    item {
                        GenericInfoHeader(
                            heading = "Your Financial Flow",
                            subHeading = "Understanding your income and expenses helps us create a personalized investment plan"
                        )
                    }

                    item {
                        IncomeEntry(
                            annualIncome = financialInfo.annualIncome,
                            onAnnualIncomeUpdate = { viewModel.onAnnualIncomeUpdate(it) },
                        )
                    }

                    item {
                        MonthlyExpenses(
                            financialInfo = financialInfo,
                            expensePercent = expensePercent,
                            onHouseExpenseUpdate = { viewModel.onHouseExpenseUpdate(it) },
                            onFoodExpenseUpdate = { viewModel.onFoodExpenseUpdate(it) },
                            onTransportExpenseUpdate = { viewModel.onTransportExpenseUpdate(it) },
                            onOtherExpenseUpdate = { viewModel.onOtherExpenseUpdate(it) }
                        )
                    }

                    item {
                        Summary(
                            summary = financialSummary
                        )
                    }

                    item {
                        Spacer(
                            modifier = Modifier.height(80.dp + pv.calculateBottomPadding())
                        )
                    }

                }
                NextButtonFooter(
                    onClick = { viewModel.onSubmit { onBackClick() } },
                    pv = pv,
                    value = "Submit Changes",
                    enabled = true,
                    loading = loading
                )
            }
        }
    }


}