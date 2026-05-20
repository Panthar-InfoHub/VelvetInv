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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.velvetinvestment.domain.LoanTypes
import org.sharad.velvetinvestment.domain.models.loan.LoanDomain
import org.sharad.velvetinvestment.presentation.loans.viewmodel.LoanInfoViewModel
import org.sharad.velvetinvestment.presentation.onboarding.compose.loan.ExpandableLoanEntry
import org.sharad.velvetinvestment.presentation.onboarding.compose.loan.LoanSummary
import org.sharad.velvetinvestment.presentation.onboarding.models.LoanInfo
import org.sharad.velvetinvestment.shared.UiStateContainer
import org.sharad.velvetinvestment.shared.compose.AppButton
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.PaginationEffect
import org.sharad.velvetinvestment.shared.compose.PaginationFooter
import org.sharad.velvetinvestment.utils.AppEvent
import org.sharad.velvetinvestment.utils.AppEventsController

@Composable
fun LoansScreen(
    onBack: () -> Unit,
    onAddClick: () -> Unit,
    onEditClick: (String) -> Unit,
    pv: PaddingValues
) {
    val viewModel: LoanInfoViewModel = koinViewModel()
    val uiState by viewModel.loans.collectAsStateWithLifecycle()
    val hasNextPage by viewModel.hasNextPage.collectAsStateWithLifecycle()
    val isLoadingNext by viewModel.isLoadingNext.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        AppEventsController.appEvent.collect {
            when (it) {
                AppEvent.LoanEventRefresh -> {
                    viewModel.loadLoans()
                    AppEventsController.clear()
                }
                else -> {}
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        BackHeader(heading = "Your Loans", showBack = true, onBackClick = onBack)
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
                    hasNextPage = hasNextPage,
                    isLoadingNext = isLoadingNext,
                    onLoadNext = { viewModel.loadNext() },
                    onDelete = { viewModel.deleteLoan(it.id) },
                    onEdit = { onEditClick(it.id) }
                )
            }
        }
        Box(modifier = Modifier.padding(pv).padding(16.dp)) {
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
    data: List<LoanDomain>,
    hasNextPage: Boolean,
    isLoadingNext: Boolean,
    onLoadNext: () -> Unit,
    onDelete: (LoanDomain) -> Unit,
    onEdit: (LoanDomain) -> Unit
) {
    val totalOutstanding = data.sumOf { it.outstanding_amount.toLongOrNull() ?: 0L }
    val totalEmi = data.sumOf { it.monthly_emi.toLongOrNull() ?: 0L }
    val totalTenure = data.sumOf { it.tenure_months }

    val lazyListState = rememberLazyListState()

    PaginationEffect(
        lazyListState = lazyListState,
        onLoadMore = onLoadNext
    )

    LazyColumn(
        state = lazyListState,
        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(data) { loan ->
            val loanInfo = LoanInfo(
                loanType = LoanTypes.entries.find { it.displayName == loan.loan_type } ?: LoanTypes.OTHER,
                outstandingAmount = loan.outstanding_amount.toLongOrNull() ?: 0L,
                monthlyEmi = loan.monthly_emi.toLongOrNull() ?: 0L,
                tenure = loan.tenure_months
            )
            ExpandableLoanEntry(
                loanInfo = loanInfo,
                onDeleteClick = { onDelete(loan) },
                onEditClick = { onEdit(loan) }
            )
        }

        item {
            PaginationFooter(hasNextPage = hasNextPage || isLoadingNext)
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
