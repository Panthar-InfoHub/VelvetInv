package org.sharad.velvetinvestment.presentation.onboarding.compose.loan

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.bgColor4
import org.sharad.emify.core.ui.theme.titleColor
import org.sharad.velvetinvestment.presentation.onboarding.compose.financialflow.GenericInfoHeader
import org.sharad.velvetinvestment.presentation.onboarding.models.LoanInfo
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.LoanScreenViewModel
import org.sharad.velvetinvestment.shared.compose.AppButton
import org.sharad.velvetinvestment.shared.compose.ContinueBackButtonFooter
import org.sharad.velvetinvestment.utils.formatMoneyWithUnits
import org.sharad.velvetinvestment.utils.theme.Poppins
import org.sharad.velvetinvestment.utils.theme.titlesStyle

@Composable
fun OnBoardingLoanScreen(
    viewModel: LoanScreenViewModel,
    modifier: Modifier = Modifier,
    pv: PaddingValues,
    onNext: () -> Unit,
    onPrev: () -> Unit,
    onAddLoanClick: () -> Unit
){

    val loans by viewModel.loanList.collectAsStateWithLifecycle()
    val outstandingAmount by viewModel.totalOutstandingDebt.collectAsStateWithLifecycle()
    val monthlyEmi by viewModel.monthlyEMIBurden.collectAsStateWithLifecycle()
    val totalTenure by viewModel.totalTenure.collectAsStateWithLifecycle()



    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {

        Column(
            modifier=Modifier.fillMaxSize()
        )
        {
            if (loans.isEmpty()) {
                EmptyLoansScreen(
                    onAddClick = {onAddLoanClick()},
                    modifier= Modifier.weight(1f)
                )
            } else {
                LoanScreenMain(
                    onAddClick = {onAddLoanClick()},
                    modifier= Modifier.weight(1f),
                    loans=loans,
                    onLoanDeleteClick={  },
                    outstandingAmount=outstandingAmount,
                    totalTenure=totalTenure,
                    monthlyEmi=monthlyEmi
                )
            }

            ContinueBackButtonFooter(
                onContinue = onNext,
                onBack = onPrev,
                pv = pv,
            )
        }
    }
}

@Composable
fun LoanScreenMain(
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier,
    loans: List<LoanInfo>,
    onLoanDeleteClick: (LoanInfo) -> Unit,
    outstandingAmount: Long,
    totalTenure: Int,
    monthlyEmi: Long
) {
    Box(
        modifier=modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    )
    {
        LazyColumn(
            modifier = modifier.fillMaxSize().padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        )
        {

            item {
                GenericInfoHeader(
                    heading = "Your Loans & Debts",
                    subHeading = "Add your loans and debts to optimize your financial strategy"
                )
            }

            items(loans) {
                ExpandableLoanEntry(
                    loanInfo = it,
                    onDeleteClick = { onLoanDeleteClick(it) }
                )
            }

            item {
                LoanSummary(
                    outstandingAmount = outstandingAmount,
                    totalTenure = totalTenure,
                    monthlyEmi = monthlyEmi,
                    totalLoans = loans.size
                )
            }

            item {
                Spacer(
                    modifier = Modifier.height(48.dp)
                )
            }
        }

        AppButton(
            modifier=Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 8.dp),
            text = "Add Loan",
            onClick = {onAddClick()}
        )

    }
}

@Composable
fun LoanSummary(monthlyEmi: Long, totalTenure: Int, outstandingAmount: Long, totalLoans: Int) {
    Box(
        modifier = Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
            .background(bgColor4.copy(alpha = 0.1f), RoundedCornerShape(10.dp)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal =16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "Total Outstanding Debt",
                style = MaterialTheme.typography.headlineSmall,
                textAlign = TextAlign.Center
            )

            Column {
                Text(
                    text = "₹${formatMoneyWithUnits(outstandingAmount)}",
                    fontFamily = Poppins,
                    fontSize = 36.sp,
                    lineHeight = 36.sp,
                    color = Primary,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Across $totalLoans Loans",
                    style = titlesStyle,
                    color = titleColor
                )
            }

            Text(
                text = "Monthly EMI Burden",
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall,
            )

            Text(
                text = "₹${formatMoneyWithUnits(monthlyEmi)}",
                fontFamily = Poppins,
                fontSize = 36.sp,
                lineHeight = 36.sp,
                color = Primary,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold
            )

        }
    }
}

@Composable
fun EmptyLoansScreen(onAddClick: () -> Unit,modifier: Modifier=Modifier) {
    Column(
        modifier = modifier.fillMaxSize().padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        GenericInfoHeader(
            heading = "Your Loans & Debts",
            subHeading = "Add your loans and debts to optimize your financial strategy"
        )

        Box(
            modifier=Modifier.weight(1f)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ){

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
                    .padding(horizontal = 48.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ){
                    Text(
                        text = "No Loans Added yet",
                        style = MaterialTheme.typography.headlineLarge,
                        color = Primary,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = "Add your loans and debts to get a complete financial picture",
                        style = titlesStyle,
                        color = titleColor,
                        textAlign = TextAlign.Center
                    )
                }

                AppButton(
                    text = "Add Your Loans",
                    onClick = onAddClick,
                )

            }

        }

    }
}