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
import org.sharad.emify.core.ui.theme.bgColor1
import org.sharad.emify.core.ui.theme.bgColor4
import org.sharad.velvetinvestment.features.fireonboarding.presentation.firereport.viewmodel.InsuranceCoverageEditEvent
import org.sharad.velvetinvestment.features.fireonboarding.presentation.firereport.viewmodel.InsuranceCoverageEditUiState
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.compose.financialflow.ExpandableExpenseEntryField
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.compose.financialflow.GenericInfoHeader
import org.sharad.velvetinvestment.features.fireonboarding.presentation.onboarding.compose.insurancecoverage.SummaryField
import org.sharad.velvetinvestment.shared.compose.NextButtonFooter
import org.sharad.velvetinvestment.shared.UiStateContainer
import org.sharad.velvetinvestment.shared.compose.BackHeader
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.icon_insurance

@Composable
fun InsuranceCoverageEditScreen(
    state: InsuranceCoverageEditUiState,
    onEvent: (InsuranceCoverageEditEvent) -> Unit
) {
    UiStateContainer(
        uiState = state.insuranceInfo,
        onRetry = { onEvent(InsuranceCoverageEditEvent.Retry) },
        modifier = Modifier.fillMaxSize().imePadding()
    ) { data ->
        val lifeInsurance = data.lifeInsurance ?: 0L
        val healthInsurance = data.healthInsurance ?: 0L

        Column(
            modifier = Modifier.fillMaxSize()
        ) {


            BackHeader(
                title = "Update Insurance",
                onBack = { onEvent(InsuranceCoverageEditEvent.OnBackClicked) },
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
                            onEvent(InsuranceCoverageEditEvent.OnLifeInsuranceChanged(it))
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
                            onEvent(InsuranceCoverageEditEvent.OnHealthInsuranceChanged(it))
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
                        totalInsurance = state.totalInsuranceAmount
                    )
                }
            }

            // -------------------------------
            // FOOTER
            // -------------------------------
            NextButtonFooter(
                onClick = { onEvent(InsuranceCoverageEditEvent.OnSubmitClicked) },
                value = "Submit Changes",
                loading = state.isSubmitting,
                enabled = true
            )
        }
    }
}