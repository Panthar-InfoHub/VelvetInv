package org.sharad.velvetinvestment.presentation.loans.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import org.sharad.velvetinvestment.presentation.loans.viewmodel.AddLoanViewModel
import org.sharad.velvetinvestment.presentation.onboarding.compose.OnBoardingTextField
import org.sharad.velvetinvestment.presentation.onboarding.compose.financialflow.MoneyTextField
import org.sharad.velvetinvestment.presentation.onboarding.compose.loan.LoanSelectionDropDown
import org.sharad.velvetinvestment.presentation.onboarding.compose.personaldetails.NextButtonFooter
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.utils.clearFocusOnTap

@Composable
fun AddLoanScreen(
    loanId: String? = null,
    pv: PaddingValues,
    onBack: () -> Unit
) {
    val viewModel: AddLoanViewModel = koinViewModel { parametersOf(loanId) }

    val loanType by viewModel.loanType.collectAsStateWithLifecycle()
    val outstandingAmount by viewModel.outstandingAmount.collectAsStateWithLifecycle()
    val monthlyEmi by viewModel.monthlyEmi.collectAsStateWithLifecycle()
    val tenure by viewModel.tenure.collectAsStateWithLifecycle()
    val loading by viewModel.loading.collectAsStateWithLifecycle()

    LaunchedEffect(loanId) {
        if (loanId != null) {
            viewModel.loadLoan(loanId)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
            .clearFocusOnTap()
    ) {
        BackHeader(
            heading = if (loanId == null) "Add Loan" else "Edit Loan",
            showBack = true,
            onBackClick = onBack
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                LoanSelectionDropDown(
                    value = loanType,
                    onValueChange = { viewModel.onLoanTypeUpdate(it) },
                    placeHolder = "Choose Type",
                    label = "Loan Type"
                )
            }

            item {
                MoneyTextField(
                    value = outstandingAmount?.toString() ?: "",
                    onValueChange = {
                        viewModel.onOutstandingAmountUpdate(it)
                    },
                    placeHolder = "Amount",
                    label = "Outstanding Amount"
                )
            }
            item {
                MoneyTextField(
                    value = monthlyEmi?.toString() ?: "",
                    onValueChange = {
                        viewModel.onMonthlyEmiUpdate(it)
                    },
                    placeHolder = "Amount",
                    label = "Monthly EMI"
                )
            }
            item {
                OnBoardingTextField(
                    value = tenure?.toString() ?: "",
                    onValueChange = {
                        viewModel.onTenureUpdate(it)
                    },
                    placeHolder = "0",
                    label = "Tenure(months)",
                    keyboardType = KeyboardType.Number
                )
            }

            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
        }

        NextButtonFooter(
            value =if (loanId==null) "Add Loan" else "Update Loan",
            onClick = {
                viewModel.submit {
                    onBack()
                }
            },
            pv = pv,
            loading = loading
        )
    }
}
