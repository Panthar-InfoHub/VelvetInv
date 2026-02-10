package org.sharad.velvetinvestment.presentation.onboarding.compose.loan

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.sharad.velvetinvestment.presentation.onboarding.compose.OnBoardingTextField
import org.sharad.velvetinvestment.presentation.onboarding.compose.financialflow.MoneyTextField
import org.sharad.velvetinvestment.presentation.onboarding.compose.personaldetails.NextButtonFooter
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.LoanScreenViewModel

@Composable
fun AddLoanScreen(
    modifier: Modifier = Modifier,
    viewModel: LoanScreenViewModel,
    onBack: () -> Unit,
    pv: PaddingValues
){
    
    val addLoanState by viewModel.addLoanDetails.collectAsStateWithLifecycle()

    Column(
        modifier=modifier.fillMaxSize()
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
                    value = addLoanState.loanType,
                    onValueChange = {it->viewModel.onLoanTypeUpdate(it)},
                    placeHolder = "Choose Type",
                    label = "Loan Type"
                )
            }

            item {
                MoneyTextField(
                    value = addLoanState.outstandingAmount?.toString()?:"",
                    onValueChange = {
                        viewModel.onOutstandingAmountUpdate(it)
                    },
                    placeHolder = "Amount",
                    label = "Outstanding Amount"
                )
            }
            item {
                MoneyTextField(
                    value = addLoanState.monthlyEmi?.toString()?:"",
                    onValueChange = {
                        viewModel.onMonthlyEmiUpdate(it)
                    },
                    placeHolder = "Amount",
                    label = "Monthly EMI"
                )
            }
            item {
                OnBoardingTextField(
                    value = addLoanState.tenure?.toString()?:"",
                    onValueChange = {
                        viewModel.onTenureUpdate(it)
                    },
                    placeHolder = "0",
                    label = "Tenure",
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
                viewModel.addLoan()
                onBack()
            },
            pv = pv
        )
    }

}