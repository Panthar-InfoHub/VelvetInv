package org.sharad.velvetinvestment.presentation.onboarding.compose.insurancecoverage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.sharad.emify.core.ui.theme.Primary
import org.sharad.emify.core.ui.theme.bgColor1
import org.sharad.emify.core.ui.theme.bgColor4
import org.sharad.velvetinvestment.presentation.onboarding.compose.financialflow.ExpandableExpenseEntryField
import org.sharad.velvetinvestment.presentation.onboarding.compose.financialflow.GenericInfoHeader
import org.sharad.velvetinvestment.presentation.onboarding.viewmodel.InsuranceCoverageViewModel
import org.sharad.velvetinvestment.shared.compose.ContinueBackButtonFooter
import org.sharad.velvetinvestment.utils.formatMoneyWithUnits
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.icon_insurance

@Composable
fun InsuranceCoverageScreen(
    modifier: Modifier = Modifier,
    viewModel: InsuranceCoverageViewModel,
    onNext: () -> Unit,
    onPrev: () -> Unit,
    pv: PaddingValues
){

    val lifeInsurance by viewModel.lifeInsuranceAmount.collectAsStateWithLifecycle()
    val healthInsurance by viewModel.healthInsuranceAmount.collectAsStateWithLifecycle()
    val totalInsurance by viewModel.totalInsuranceAmount.collectAsStateWithLifecycle()



    Column(
        modifier=modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {

            item{
                GenericInfoHeader(
                    heading = "Insurance Coverage",
                    subHeading = "Protect your wealth with adequate insurance coverage"
                )
            }

            item {
                ExpandableExpenseEntryField(
                    heading = "Term Life Insurance",
                    subHeading = "Sum Insured Amount",
                    amount = if(lifeInsurance==0L) null else lifeInsurance,
                    accentColor = bgColor4,
                    icon = Res.drawable.icon_insurance,
                    onValueChange = {
                        viewModel.onLifeInsuranceAmountChange(it)
                    }
                )
            }
            item {
                ExpandableExpenseEntryField(
                    heading = "Health Insurance",
                    subHeading = "Sum Insured Amount",
                    amount = if(healthInsurance==0L) null else healthInsurance,
                    accentColor = bgColor1,
                    icon = Res.drawable.icon_insurance,
                    onValueChange = {
                        viewModel.onHealthInsuranceAmountChange(it)
                    }
                )
            }
            item {
                SummaryField(
                    lifeInsurance=lifeInsurance,
                    healthInsurance=healthInsurance,
                    totalInsurance=totalInsurance
                )
            }

        }
        ContinueBackButtonFooter(
            onContinue = onNext,
            onBack = onPrev,
            pv = pv,
        )
    }

}

@Composable
fun SummaryField(
    lifeInsurance: Long,
    healthInsurance: Long,
    totalInsurance: Long,
) {

    Box(
        modifier=Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(bgColor1.copy(alpha = 0.1f),RoundedCornerShape(15.dp)),
        contentAlignment = Alignment.Center
    ){

        Column(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal =20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ){
            Text(
                text="Total Insurance Coverage",
                style = MaterialTheme.typography.headlineSmall
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Term Life Insurance",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Primary,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "₹ ${ formatMoneyWithUnits(lifeInsurance) }",
                        style = MaterialTheme.typography.headlineSmall,
                        color = bgColor1,
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Health Insurance",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Primary,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "₹ ${ formatMoneyWithUnits(healthInsurance)}",
                        style = MaterialTheme.typography.headlineSmall,
                        color = bgColor1,
                    )
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Total Coverage",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Primary,
                        modifier = Modifier.weight(1f)
                    )
                    Text(
                        text = "₹ ${ formatMoneyWithUnits(totalInsurance)}",
                        style = MaterialTheme.typography.headlineSmall,
                        color = bgColor1,
                    )
                }
            }
        }

    }

}