package org.sharad.velvetinvestment.presentation.tradingaccount.compose

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.velvetinvestment.presentation.onboarding.compose.OnBoardingTextField
import org.sharad.velvetinvestment.presentation.onboarding.compose.personaldetails.NextButtonFooter
import org.sharad.velvetinvestment.presentation.tradingaccount.viewmodel.TradingAccountViewModel
import org.sharad.velvetinvestment.shared.compose.BackHeader
import org.sharad.velvetinvestment.shared.compose.GenericDropDownField
import org.sharad.velvetinvestment.utils.theme.Poppins

@Composable
fun TAScreen6(
    pv: PaddingValues,
    onClick: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: TradingAccountViewModel
) {
    val state by viewModel.formState.collectAsStateWithLifecycle()

    val kycType = listOf("C-CKYC Compliant","K-KRA COMPLIANT","B-BIOMETRIC KYC","E-AADHAAR EKYC","P-PAN")
    val stateList = listOf("UttarPradesh","Pune","Bihar","Delhi","Goa")
    val countryList = listOf("India","Dubai","US","UK","France","Germany","Japan")
    val investorList = listOf("P-PAPER","Z-PAPERLESS")

    Column(modifier = Modifier.fillMaxSize()) {

        BackHeader(
            "Trading account",
            showBack = true,
            onBackClick = onBackClick
        )

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

            // ----------- Indian Address -----------

            item {
                OnBoardingTextField(
                    value = state.data.address_1,
                    onValueChange = viewModel::onAddress1Change,
                    placeHolder = "House/Flat No.,Building Name",
                    label = "Address Line 1",
                    mandatory = true
                )
            }

            item {
                OnBoardingTextField(
                    value = state.data.address_2,
                    onValueChange = viewModel::onAddress2Change,
                    placeHolder = "Street Name,Area",
                    label = "Address Line 2 (Optional)",
                    mandatory = false
                )
            }

            item {
                OnBoardingTextField(
                    value = state.data.address_3,
                    onValueChange = viewModel::onAddress3Change,
                    placeHolder = "Landmark",
                    label = "Address Line 3 (Optional)",
                    mandatory = false
                )
            }

            item {
                OnBoardingTextField(
                    value = state.data.city,
                    onValueChange = viewModel::onCityChange,
                    placeHolder = "Enter City",
                    label = "City",
                    mandatory = true
                )
            }

            item {
                GenericDropDownField(
                    value = state.data.state,
                    onValueChange = viewModel::onStateChange,
                    placeHolder = "Select State",
                    label = "State",
                    mandatory = true,
                    modifier = Modifier.fillMaxWidth(),
                    list = stateList
                )
            }

            item {
                OnBoardingTextField(
                    value = state.data.pincode,
                    onValueChange = viewModel::onPincodeChange,
                    placeHolder = "6-digit pincode",
                    label = "Pincode",
                    mandatory = true,
                    keyboardType = KeyboardType.Number
                )
            }

            item {
                GenericDropDownField(
                    value = state.data.country,
                    onValueChange = viewModel::onCountryChange,
                    placeHolder = "Select Country",
                    label = "Country",
                    mandatory = true,
                    modifier = Modifier.fillMaxWidth(),
                    list = countryList
                )
            }

            item {
                OnBoardingTextField(
                    value = state.data.resi_phone,
                    onValueChange = viewModel::onPhoneChange,
                    placeHolder = "Enter Phone Number",
                    label = "Phone (Optional)",
                    mandatory = false
                )
            }

            item {
                OnBoardingTextField(
                    value = state.data.resi_fax,
                    onValueChange = {},
                    placeHolder = "Enter Fax number",
                    label = "Fax Number (Optional)",
                    mandatory = false
                )
            }

            item {
                OnBoardingTextField(
                    value = state.data.office_fax,
                    onValueChange = {},
                    placeHolder = "Enter Fax number",
                    label = "Office Fax Number (Optional)",
                    mandatory = false
                )
            }

            item {
                OnBoardingTextField(
                    value = state.data.office_phone,
                    onValueChange = {},
                    placeHolder = "Enter Office Phone number",
                    label = "Office Phone Number (Optional)",
                    mandatory = false,
                    keyboardType = KeyboardType.Number
                )
            }

            item {
                OnBoardingTextField(
                    value = state.data.email,
                    onValueChange = viewModel::onEmailChange,
                    placeHolder = "Email Address",
                    label = "Email",
                    mandatory = true,
                    keyboardType = KeyboardType.Email
                )
            }

            item {
                CheckBoxComp(
                    checked = false,
                    onCheckedChange = {}
                )
            }

            // ----------- Foreign Address -----------

            item {
                OnBoardingTextField(
                    value = state.data.foreign_address_1,
                    onValueChange = {},
                    placeHolder = "Street Address",
                    label = "Foreign Address Line",
                    mandatory = true
                )
            }

            item {
                OnBoardingTextField(
                    value = state.data.foreign_address_2,
                    onValueChange = {},
                    placeHolder = "Apartment, suite, unit, etc.",
                    label = "Foreign Address Line 2 (Optional)",
                    mandatory = false
                )
            }

            item {
                OnBoardingTextField(
                    value = state.data.foreign_address_3,
                    onValueChange = {},
                    placeHolder = "Additional Info",
                    label = "Foreign Address Line 3 (Optional)",
                    mandatory = false
                )
            }

            item {
                OnBoardingTextField(
                    value = state.data.foreign_address_city,
                    onValueChange = {},
                    placeHolder = "Enter City",
                    label = "City",
                    mandatory = true
                )
            }

            item {
                OnBoardingTextField(
                    value = state.data.foreign_address_state,
                    onValueChange = {},
                    placeHolder = "Enter State/Province",
                    label = "State/Province (Optional)",
                    mandatory = false
                )
            }

            item {
                OnBoardingTextField(
                    value = state.data.foreign_address_pincode,
                    onValueChange = {},
                    placeHolder = "Enter Postal Code",
                    label = "Postal Code (Optional)",
                    mandatory = false,
                    keyboardType = KeyboardType.Number
                )
            }

            item {
                GenericDropDownField(
                    value = state.data.foreign_address_country,
                    onValueChange = {},
                    placeHolder = "Select Country",
                    label = "Country",
                    mandatory = true,
                    modifier = Modifier.fillMaxWidth(),
                    list = countryList
                )
            }

            item {
                OnBoardingTextField(
                    value = state.data.foreign_address_resi_phone,
                    onValueChange = {},
                    placeHolder = "Enter Phone",
                    label = "Phone (Optional)",
                    mandatory = false,
                    keyboardType = KeyboardType.Number
                )
            }

            // ----------- KYC -----------

            item {
                GenericDropDownField(
                    value = state.data.primary_holder_kyc_type,
                    onValueChange = viewModel::onPrimaryKycTypeChange,
                    placeHolder = "KYC type",
                    label = "KYC type",
                    mandatory = true,
                    list = kycType
                )
            }

            item {
                OnBoardingTextField(
                    value = state.data.primary_holder_ckyc_number,
                    onValueChange = viewModel::onPrimaryCkycChange,
                    placeHolder = "Enter CKYC no.",
                    label = "CKYC No",
                    mandatory = false
                )
            }

            item {
                GenericDropDownField(
                    value = state.data.paperless_flag,
                    onValueChange = viewModel::onPaperlessFlagChange,
                    placeHolder = "Investor Onboarding",
                    label = "Investor Onboarding",
                    mandatory = true,
                    list = investorList
                )
            }

            item {
                Spacer(modifier = Modifier.height(pv.calculateBottomPadding()))
            }
        }

        NextButtonFooter(
            onClick = onClick,
            pv = pv,
            value = "Next",
            enabled = true
        )
    }
}