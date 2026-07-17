package org.sharad.velvetinvestment.presentation.firereport.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import org.sharad.velvetinvestment.shared.UiStateContainer
import org.sharad.velvetinvestment.shared.compose.BackHeader

@Composable
fun FinancialFlowEditScreen(
    onBackClick: () -> Unit,
) {
    val viewModel: FinancialFlowEditScreenViewModel = koinViewModel()

    val loading by viewModel.loading.collectAsStateWithLifecycle()

    val state by viewModel.financialInfo.collectAsStateWithLifecycle()
    val expensePercent by viewModel.expensePercentages.collectAsStateWithLifecycle()
    val financialSummary by viewModel.financialSummary.collectAsStateWithLifecycle()

    UiStateContainer(
        uiState = state,
        onRetry = { viewModel.loadData() },
        modifier = Modifier.fillMaxSize().imePadding()
    ) { financialInfo ->
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            BackHeader("Update Financial Info", onBackClick = onBackClick, showBack = true)
            LazyColumn(
                modifier = Modifier.weight(1f).fillMaxSize().padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(bottom = 28.dp)
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

            }
            NextButtonFooter(
                onClick = { viewModel.onSubmit { onBackClick() } },
                value = "Submit Changes",
                enabled = true,
                loading = loading
            )
        }
    }


}