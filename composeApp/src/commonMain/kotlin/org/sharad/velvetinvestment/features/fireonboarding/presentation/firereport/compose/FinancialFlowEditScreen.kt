package org.sharad.velvetinvestment.features.fireonboarding.presentation.firereport.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.sharad.velvetinvestment.features.fireonboarding.presentation.firereport.viewmodel.FinancialFlowEditEvent
import org.sharad.velvetinvestment.features.fireonboarding.presentation.firereport.viewmodel.FinancialFlowEditUiState
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.compose.financialflow.GenericInfoHeader
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.compose.financialflow.IncomeEntry
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.compose.financialflow.MonthlyExpenses
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.compose.financialflow.Summary
import org.sharad.velvetinvestment.shared.compose.NextButtonFooter
import org.sharad.velvetinvestment.shared.UiStateContainer
import org.sharad.velvetinvestment.shared.compose.BackHeader

@Composable
fun FinancialFlowEditScreen(
    state: FinancialFlowEditUiState,
    onEvent: (FinancialFlowEditEvent) -> Unit
) {
    UiStateContainer(
        uiState = state.financialInfo,
        onRetry = { onEvent(FinancialFlowEditEvent.Retry) },
        modifier = Modifier.fillMaxSize().imePadding()
    ) { financialInfo ->
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            BackHeader("Update Financial Info", onBack = { onEvent(FinancialFlowEditEvent.OnBackClicked) }, showBack = true)
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
                        onAnnualIncomeUpdate = { onEvent(FinancialFlowEditEvent.OnAnnualIncomeChanged(it)) },
                    )
                }

                item {
                    MonthlyExpenses(
                        financialInfo = financialInfo,
                        expensePercent = state.expensePercentages,
                        onHouseExpenseUpdate = { onEvent(FinancialFlowEditEvent.OnHouseExpenseChanged(it)) },
                        onFoodExpenseUpdate = { onEvent(FinancialFlowEditEvent.OnFoodExpenseChanged(it)) },
                        onTransportExpenseUpdate = { onEvent(FinancialFlowEditEvent.OnTransportExpenseChanged(it)) },
                        onOtherExpenseUpdate = { onEvent(FinancialFlowEditEvent.OnOtherExpenseChanged(it)) }
                    )
                }

                item {
                    Summary(
                        summary = state.summary
                    )
                }

            }
            NextButtonFooter(
                onClick = { onEvent(FinancialFlowEditEvent.OnSubmitClicked) },
                value = "Submit Changes",
                enabled = true,
                loading = state.isSubmitting
            )
        }
    }


}