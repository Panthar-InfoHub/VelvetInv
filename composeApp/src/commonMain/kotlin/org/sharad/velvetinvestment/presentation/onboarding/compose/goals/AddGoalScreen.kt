package org.sharad.velvetinvestment.presentation.onboarding.compose.goals

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
import org.sharad.velvetinvestment.presentation.onboarding.compose.OnBoardingTextField
import org.sharad.velvetinvestment.presentation.onboarding.compose.financialflow.MoneyTextField
import org.sharad.velvetinvestment.presentation.onboarding.compose.loan.LoanSelectionDropDown
import org.sharad.velvetinvestment.presentation.onboarding.compose.personaldetails.NextButtonFooter
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.GoalScreenViewModel

@Composable
fun AddGoalScreen(
    modifier: Modifier = Modifier,
    viewModel: GoalScreenViewModel,
    onBack: () -> Unit,
    pv: PaddingValues
){

    val addGoalState by viewModel.addGoalDetails.collectAsStateWithLifecycle()

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
                OnBoardingTextField(
                    value = addGoalState.name,
                    onValueChange = { viewModel.onGoalNameUpdate(it) },
                    placeHolder = "e.g. Children's, Education, Dream House, travel",
                    label = "Goal Name"
                )
            }

            item {
                GoalSelectionDropDown(
                    value = addGoalState.category,
                    onValueChange = { it -> viewModel.onGoalCategoryUpdate(it) },
                    placeHolder = "Category",
                    label = "Category"
                )
            }

            item {
                OnBoardingTextField(
                    value = addGoalState.targetAmount?.toString()?:"",
                    onValueChange = { viewModel.onTargetAmountUpdate(it) },
                    placeHolder = "Enter Target Amount",
                    label = "Target Amount",
                    keyboardType = KeyboardType.Number
                )
            }

            item {
                OnBoardingTextField(
                    value = addGoalState.targetYear?.toString()?:"",
                    onValueChange = { viewModel.onTargetYearUpdate(it) },
                    placeHolder = "Enter Target Year",
                    label = "Target Year",
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
                viewModel.addGoal()
                onBack()
            },
            pv = pv
        )
    }

}