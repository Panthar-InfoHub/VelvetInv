package org.sharad.velvetinvestment.presentation.firereport.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.emify.core.ui.theme.bgColor1
import org.sharad.emify.core.ui.theme.bgColor4
import org.sharad.velvetinvestment.presentation.firereport.viewmodel.InsuranceCoverageEditViewModel
import org.sharad.velvetinvestment.presentation.onboarding.compose.financialflow.ExpandableExpenseEntryField
import org.sharad.velvetinvestment.presentation.onboarding.compose.financialflow.GenericInfoHeader
import org.sharad.velvetinvestment.presentation.onboarding.compose.insurancecoverage.SummaryField
import org.sharad.velvetinvestment.presentation.onboarding.compose.personaldetails.NextButtonFooter
import org.sharad.velvetinvestment.shared.UiStateContainer
import org.sharad.velvetinvestment.shared.compose.BackHeader
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.icon_insurance

@Composable
fun InsuranceCoverageEditScreen(
    onBackClick: () -> Unit,
) {
    val viewModel: InsuranceCoverageEditViewModel = koinViewModel()

    val state by viewModel.insuranceInfo.collectAsStateWithLifecycle()
    val totalInsurance by viewModel.totalInsuranceAmount.collectAsStateWithLifecycle()
    val loading by viewModel.loading.collectAsStateWithLifecycle()

    UiStateContainer(
        uiState = state,
        onRetry = { viewModel.loadData() },
        modifier = Modifier.fillMaxSize()
    ) { data ->
        val lifeInsurance = data.lifeInsurance ?: 0L
        val healthInsurance = data.healthInsurance ?: 0L

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

            // 🔹 Top Bar
            BackHeader(
                heading = "Update Insurance",
                onBackClick = onBackClick,
                showBack = true
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(bottom = 28.dp)
            ) {

                item {
                    GenericInfoHeader(
                        heading = "Insurance Coverage",
                        subHeading = "Protect your wealth with adequate insurance coverage"
                    )
                }

                // -------------------------------
                // LIFE INSURANCE
                // -------------------------------
                item {
                    ExpandableExpenseEntryField(
                        heading = "Term Life Insurance",
                        subHeading = "Sum Insured Amount",
                        amount = if (lifeInsurance == 0L) null else lifeInsurance,
                        accentColor = bgColor4,
                        icon = Res.drawable.icon_insurance,
                        onValueChange = {
                            viewModel.onLifeInsuranceAmountChange(it)
                        }
                    )
                }

                // -------------------------------
                // HEALTH INSURANCE
                // -------------------------------
                item {
                    ExpandableExpenseEntryField(
                        heading = "Health Insurance",
                        subHeading = "Sum Insured Amount",
                        amount = if (healthInsurance == 0L) null else healthInsurance,
                        accentColor = bgColor1,
                        icon = Res.drawable.icon_insurance,
                        onValueChange = {
                            viewModel.onHealthInsuranceAmountChange(it)
                        }
                    )
                }

                // -------------------------------
                // SUMMARY
                // -------------------------------
                item {
                    SummaryField(
                        lifeInsurance = lifeInsurance,
                        healthInsurance = healthInsurance,
                        totalInsurance = totalInsurance
                    )
                }
            }

            // -------------------------------
            // FOOTER
            // -------------------------------
            NextButtonFooter(
                onClick = { viewModel.onSubmit { onBackClick() } },
                value = "Submit Changes",
                loading = loading,
                enabled = true
            )
        }
    }
}