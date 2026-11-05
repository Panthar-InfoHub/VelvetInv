package org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.compose.loan

import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel.LoanUiState

import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel.LoanEvent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.sharad.velvetinvestment.shared.compose.OnBoardingTextField
import org.sharad.velvetinvestment.shared.compose.MoneyTextField
import org.sharad.velvetinvestment.shared.compose.NextButtonFooter
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.viewmodel.LoanScreenViewModel
import org.sharad.velvetinvestment.utils.clearFocusOnTap

@Composable
fun AddLoanScreen(
    modifier: Modifier = Modifier,
    state: LoanUiState,
    onEvent: (LoanEvent) -> Unit,
    onBack: () -> Unit,
    pv: PaddingValues
){

    val loanType = state.draft.loanType
    val outstandingAmount = state.draft.outstandingAmount
    val monthlyEmi = state.draft.monthlyEmi
    val tenure = state.draft.tenure

    Column(
        modifier=modifier.fillMaxSize()
            .imePadding()
            .clearFocusOnTap()
    ){
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        )
        {

            item {
                LoanSelectionDropDown(
                    value = loanType,
                    onValueChange = { onEvent(LoanEvent.OnLoanTypeSelected(it)) },
                    placeHolder = "Choose Type",
                    label = "Loan Type"
                )
            }

            item {
                MoneyTextField(
                    value =outstandingAmount?.toString()?:"",
                    onValueChange = {
                        onEvent(LoanEvent.OnOutstandingAmountChanged(it))
                    },
                    placeHolder = "Amount",
                    label = "Outstanding Amount"
                )
            }
            item {
                MoneyTextField(
                    value = monthlyEmi?.toString()?:"",
                    onValueChange = {
                        onEvent(LoanEvent.OnMonthlyEmiChanged(it))
                    },
                    placeHolder = "Amount",
                    label = "Monthly EMI"
                )
            }
            item {
                OnBoardingTextField(
                    value = tenure?.toString()?:"",
                    onValueChange = {
                        onEvent(LoanEvent.OnTenureChanged(it))
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
            value = "Add Loan",
            onClick = {
                onEvent(LoanEvent.OnAddLoanClicked)
                onBack()
            },
            pv = pv
        )
    }

}