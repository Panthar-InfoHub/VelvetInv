package org.sharad.velvetinvestment.presentation.loans.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.velvetinvestment.features.fireonboarding.data.mapper.toLoanInfo
import org.sharad.velvetinvestment.shared.domain.models.UserLoanDomain
import org.sharad.velvetinvestment.presentation.loans.viewmodel.LoanInfoViewModel
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.compose.loan.ExpandableLoanEntry
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.compose.loan.LoanSummary
import org.sharad.velvetinvestment.shared.UiStateContainer
import org.sharad.velvetinvestment.shared.compose.AppButton
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.utils.AppEvent
import org.sharad.velvetinvestment.utils.AppEventsController

@Composable
fun LoansScreen(
    onBack: () -> Unit,
    onAddClick: () -> Unit,
    onEditClick: (String) -> Unit,
) {
    val viewModel: LoanInfoViewModel = koinViewModel()
    val uiState by viewModel.loans.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        AppEventsController.appEvent.collect {
            when (it) {
                AppEvent.LoanEventRefresh -> {
                    viewModel.loadLoans()
                    AppEventsController.clear()
                    AppEventsController.sendFireRefreshEvent()
                }
                else -> {}
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        BackHeader(title = "Your Loans", showBack = true, onBack = onBack)
        Box(
            modifier = Modifier.weight(1f).fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            UiStateContainer(
                uiState = uiState,
                onRetry = { viewModel.loadLoans() }
            ) { data ->
                LoansScreenContent(
                    data = data,
                    onDelete = { viewModel.deleteLoan(it.id) },
                    onEdit = { onEditClick(it.id) }
                )
            }
        }
        Box(modifier = Modifier.padding(16.dp)) {
            AppButton(
                text = "Add Loan",
                onClick = onAddClick,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun LoansScreenContent(
    data: List<UserLoanDomain>,
    onDelete: (UserLoanDomain) -> Unit,
    onEdit: (UserLoanDomain) -> Unit
) {
    val totalOutstanding = data.sumOf { it.outstandingAmount }
    val totalEmi = data.sumOf { it.monthlyEmi }
    val totalTenure = data.sumOf { it.tenureMonths }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(data, key = { it.id }) { loan ->
            ExpandableLoanEntry(
                loanInfo = loan.toLoanInfo(),
                onDeleteClick = { onDelete(loan) },
                onEditClick = { onEdit(loan) }
            )
        }

        item {
            LoanSummary(
                outstandingAmount = totalOutstanding,
                totalTenure = totalTenure,
                monthlyEmi = totalEmi,
                totalLoans = data.size
            )
        }
    }
}
