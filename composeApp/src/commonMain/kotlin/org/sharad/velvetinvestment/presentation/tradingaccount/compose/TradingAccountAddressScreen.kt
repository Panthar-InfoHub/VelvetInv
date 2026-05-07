package org.sharad.velvetinvestment.presentation.tradingaccount.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.sharad.velvetinvestment.presentation.onboarding.compose.OnBoardingTextField
import org.sharad.velvetinvestment.presentation.onboarding.compose.personaldetails.NextButtonFooter
import org.sharad.velvetinvestment.presentation.tradingaccount.viewmodel.TradingAccountViewModel
import org.sharad.velvetinvestment.shared.DropDownSelector
import org.sharad.velvetinvestment.shared.UiStateContainer
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.utils.theme.Poppins
import org.sharad.velvetinvestment.utils.tradingaccount.Country
import org.sharad.velvetinvestment.utils.tradingaccount.InvestorOnboarding
import org.sharad.velvetinvestment.utils.tradingaccount.KycType
import org.sharad.velvetinvestment.utils.tradingaccount.StateCode
import org.sharad.velvetinvestment.utils.tradingaccount.TaxStatus

@Composable
fun TradingAccountAddressScreen(
    pv: PaddingValues,
    onClick: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: TradingAccountViewModel
) {
    val state by viewModel.formState.collectAsStateWithLifecycle()
    var showCountryDialog by rememberSaveable { mutableStateOf(false) }
    var showStateDialog by remember { mutableStateOf(false) }
    val buttonEnabled by viewModel.addressScreenButtonEnabled.collectAsStateWithLifecycle()


    Column(modifier = Modifier.fillMaxSize()) {

        BackHeader(
            "Trading account",
            showBack = true,
            onBackClick = { onBackClick() }
        )

        UiStateContainer(
            uiState = state,
            onRetry = { viewModel.getUserData() },
            modifier = Modifier.fillMaxSize()
        ) { uiData ->

            val data = uiData.data

            val showForeignAddress =
                isForeignAddressRequired(data.tax_status)

            Column(modifier = Modifier.fillMaxSize()) {

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {

                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(2.dp)
                        ) {
                            Text(
                                "Address Details",
                                style = MaterialTheme.typography.headlineLarge
                            )

                            Text(
                                "Provide your residential and contact information",
                                fontFamily = Poppins,
                                fontSize = 14.sp,
                                color = Color(0xff4A5565)
                            )
                        }
                    }

                    ///////////////////////////////////////////////////////
                    // INDIAN ADDRESS
                    ///////////////////////////////////////////////////////

                    if (!showForeignAddress) {

                        item {
                            OnBoardingTextField(
                                value = data.address_1,
                                onValueChange = viewModel::onAddress1Change,
                                placeHolder = "House/Flat No.,Building Name",
                                label = "Address Line 1",
                                mandatory = true
                            )
                        }

                        item {
                            OnBoardingTextField(
                                value = data.address_2,
                                onValueChange = viewModel::onAddress2Change,
                                placeHolder = "Street Name,Area",
                                label = "Address Line 2 (Optional)",
                                mandatory = false
                            )
                        }

                        item {
                            OnBoardingTextField(
                                value = data.address_3,
                                onValueChange = viewModel::onAddress3Change,
                                placeHolder = "Landmark",
                                label = "Address Line 3 (Optional)",
                                mandatory = false
                            )
                        }

                        item {
                            OnBoardingTextField(
                                value = data.city,
                                onValueChange = viewModel::onCityChange,
                                placeHolder = "Enter City",
                                label = "City",
                                mandatory = true
                            )
                        }

                        item {
                            OnBoardingTextField(
                                value = StateCode.getDisplayName(data.state),
                                onValueChange = viewModel::onStateChange,
                                placeHolder = "Select State",
                                label = "State",
                                mandatory = true,
                                modifier = Modifier.fillMaxWidth()
                                    .clickable(
                                        onClick = { showStateDialog = true },
                                        indication = null,
                                        interactionSource = remember { MutableInteractionSource() }
                                    ),
                                enabled = false
                            )
                        }

                        item {
                            OnBoardingTextField(
                                value = data.pincode,
                                onValueChange = viewModel::onPincodeChange,
                                placeHolder = "6-digit pincode",
                                label = "Pincode",
                                mandatory = true,
                                keyboardType = KeyboardType.Number
                            )
                        }

                        item {
                            OnBoardingTextField(
                                value = data.country,
                                onValueChange = viewModel::onCountryChange,
                                placeHolder = "Select Country",
                                label = "Country",
                                mandatory = true,
                                modifier = Modifier.fillMaxWidth(),
                            )
                        }

                        item {
                            OnBoardingTextField(
                                value = data.resi_phone,
                                onValueChange = viewModel::onResiPhoneChange,
                                placeHolder = "Enter Phone Number",
                                label = "Phone (Optional)",
                                mandatory = false,
                                keyboardType = KeyboardType.Phone
                            )
                        }

                        item {
                            OnBoardingTextField(
                                value = data.resi_fax,
                                onValueChange = viewModel::onResiFaxChange,
                                placeHolder = "Enter Fax number",
                                label = "Fax Number (Optional)",
                                mandatory = false,
                                keyboardType = KeyboardType.Number
                            )
                        }

                        item {
                            OnBoardingTextField(
                                value = data.office_phone,
                                onValueChange = viewModel::onOfficePhoneChange,
                                placeHolder = "Enter Office Phone number",
                                label = "Office Phone Number (Optional)",
                                mandatory = false,
                                keyboardType = KeyboardType.Phone
                            )
                        }

                        item {
                            OnBoardingTextField(
                                value = data.office_fax,
                                onValueChange = viewModel::onOfficeFaxChange,
                                placeHolder = "Enter Office Fax number",
                                label = "Office Fax Number (Optional)",
                                mandatory = false,
                                keyboardType = KeyboardType.Number
                            )
                        }
                    }

                    ///////////////////////////////////////////////////////
                    // FOREIGN ADDRESS
                    ///////////////////////////////////////////////////////

                    if (showForeignAddress) {

                        item {
                            OnBoardingTextField(
                                value = data.foreign_address_1,
                                onValueChange = viewModel::onForeignAddress1Change,
                                placeHolder = "Street Address",
                                label = "Foreign Address Line 1",
                                mandatory = true
                            )
                        }

                        item {
                            OnBoardingTextField(
                                value = data.foreign_address_2,
                                onValueChange = viewModel::onForeignAddress2Change,
                                placeHolder = "Apartment, suite, unit, etc.",
                                label = "Foreign Address Line 2 (Optional)",
                                mandatory = false
                            )
                        }

                        item {
                            OnBoardingTextField(
                                value = data.foreign_address_3,
                                onValueChange = viewModel::onForeignAddress3Change,
                                placeHolder = "Additional Info",
                                label = "Foreign Address Line 3 (Optional)",
                                mandatory = false
                            )
                        }

                        item {
                            OnBoardingTextField(
                                value = data.foreign_address_city,
                                onValueChange = viewModel::onForeignCityChange,
                                placeHolder = "Enter City",
                                label = "City",
                                mandatory = true
                            )
                        }

                        item {
                            OnBoardingTextField(
                                value = data.foreign_address_state,
                                onValueChange = viewModel::onForeignStateChange,
                                placeHolder = "Enter State",
                                label = "State/Province",
                                mandatory = true
                            )
                        }

                        item {
                            OnBoardingTextField(
                                value = data.foreign_address_pincode,
                                onValueChange = viewModel::onForeignPincodeChange,
                                placeHolder = "Postal Code",
                                label = "Postal Code",
                                mandatory = true,
                                keyboardType = KeyboardType.Number
                            )
                        }

                        item {
                            OnBoardingTextField(
                                value = Country.getDisplayNameFromCode(data.foreign_address_country)
                                    ?: "",
                                onValueChange = {},
                                enabled = false,
                                placeHolder = "Select Country",
                                label = "Country",
                                mandatory = true,
                                modifier = Modifier.fillMaxWidth().clickable(
                                    onClick = {
                                        showCountryDialog = true
                                    },
                                    indication = null,
                                    interactionSource = remember { MutableInteractionSource() }
                                ),
                            )
                        }

                        item {
                            OnBoardingTextField(
                                value = data.foreign_address_resi_phone,
                                onValueChange = viewModel::onForeignPhoneChange,
                                placeHolder = "Phone Number",
                                label = "Phone",
                                mandatory = false,
                                keyboardType = KeyboardType.Phone
                            )
                        }
                    }

                    ///////////////////////////////////////////////////////
                    // KYC
                    ///////////////////////////////////////////////////////

                    item {
                        DropDownSelector(
                            value = KycType.getDisplayName(data.primary_holder_kyc_type),
                            onValueChange = { viewModel.onPrimaryKycTypeChange(it.code) },
                            placeHolder = "KYC Type",
                            label = "KYC Type",
                            mandatory = true,
                            list = KycType.entries,
                            textConvertor = { it.displayName }
                        )
                    }

                    if (data.primary_holder_kyc_type == KycType.CKYC_COMPLIANT.code) {
                        item {
                            OnBoardingTextField(
                                value = data.primary_holder_ckyc_number,
                                onValueChange = viewModel::onPrimaryCkycChange,
                                placeHolder = "Enter CKYC no.",
                                label = "CKYC No",
                                mandatory = true,
                                keyboardType = KeyboardType.Number
                            )
                        }
                    }

                    item {
                        DropDownSelector(
                            value = InvestorOnboarding.getDisplayName(data.paperless_flag),
                            onValueChange = { viewModel.onPaperlessFlagChange(it.code) },
                            placeHolder = "Investor Onboarding",
                            label = "Investor Onboarding",
                            mandatory = true,
                            list = InvestorOnboarding.entries,
                            textConvertor = { it.displayName }
                        )
                    }

                    item {
                        Spacer(
                            modifier = Modifier.height(
                                pv.calculateBottomPadding()
                            )
                        )
                    }
                }

                NextButtonFooter(
                    onClick = onClick,
                    pv = pv,
                    value = "Submit Form",
                    enabled = buttonEnabled
                )
            }
            if (showCountryDialog) {
                CountrySelectorDialog(
                    showDialog = showCountryDialog,
                    selectedCode = data.foreign_address_country,
                    onDismiss = {
                        showCountryDialog = false
                    },
                    onCountrySelected = {
                        viewModel.onForeignCountryChange(it.code)
                    }
                )
            }

            if (showStateDialog) {
                StatePickerDialog(
                    showDialog = showStateDialog,
                    selectedState = data.state,
                    onDismiss = { showStateDialog = false },
                    onStateSelected = {
                        viewModel.onStateChange(it.code)
                    }
                )
            }
        }
    }
}


fun isForeignAddressRequired(taxStatusCode: String): Boolean {
    return TaxStatus.fromCode(taxStatusCode)
        ?.isResident
        ?.not()
        ?: false
}