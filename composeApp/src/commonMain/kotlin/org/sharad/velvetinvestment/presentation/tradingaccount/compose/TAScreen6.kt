package org.sharad.velvetinvestment.presentation.tradingaccount.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import org.koin.compose.viewmodel.koinViewModel
import org.sharad.velvetinvestment.presentation.onboarding.compose.OnBoardingTextField
import org.sharad.velvetinvestment.presentation.tradingaccount.viewmodel.TradingAccountViewModel
import org.sharad.velvetinvestment.shared.compose.GenericDropDownField
import org.sharad.velvetinvestment.utils.theme.Poppins
@Preview(showSystemUi = true)
@Composable
fun TAscreen6() {
    val viewModel: TradingAccountViewModel = koinViewModel()
    val state by viewModel.addressDetailModel.collectAsStateWithLifecycle()
    val kycType = listOf("C-CKYC Compliant","K-KRA COMPLIANT","B-BIOMETRIC KYC","E-AADHAAR EKYC","P-PAN")
    val statelist = listOf("UttarPradesh","Pune","Bihar","Delhi","Goa")
    val Countrylist = listOf("India","Dubai","US","UK","France","Germany","Japan")
    val InvestorList = listOf("P-PAPER","Z-PAPERLESS")
    LazyColumn(
        modifier = Modifier.fillMaxWidth().padding(16.dp).safeDrawingPadding(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column(
                modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    "Address Details",
                    fontSize = 24.sp,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                Text(
                    "Provide your residential and contact information",
                    fontFamily = Poppins,
                    fontSize = 14.sp,
                    color = Color(0xff4A5565)
                )
            }
        }
        item {
            OnBoardingTextField(
                value = state.Address1,
                onValueChange = viewModel::onAddress1Change,
                placeHolder = "House/Flat No.,Building Name",
                label = "Address Line 1",
                mandatory = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Text,
            )
        }
        item {

            OnBoardingTextField(
                value = state.Address2,
                onValueChange = viewModel::onAddress2Change,
                placeHolder = "Street Name,Area",
                label = "Address Line 2 (Optonal)",
                mandatory = false,
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Text,
            )
        }
item {
    OnBoardingTextField(
        value = state.Address3,
        onValueChange = viewModel::onAddress3Change,
        placeHolder = "Landmark",
        label = "Address Line 3 (Optional)",
        mandatory = false,
        modifier = Modifier.fillMaxWidth(),
        keyboardType = KeyboardType.Text,
    )
}
        item {
            OnBoardingTextField(
                value = state.City1,
                onValueChange = viewModel::onCity1Change,
                placeHolder = "Enter City",
                label = "City",
                mandatory = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Text,
            )
        }
        item {
            GenericDropDownField(
                value = state.states,
                onValueChange = viewModel::onStatesChange,
                placeHolder = "Select State",
                label = "State",
                mandatory = true,
                list = statelist
            )
        }
        item {

            OnBoardingTextField(
                value = state.Pincode, onValueChange = viewModel::onPincodeChange,
                placeHolder = "6-digit pincode",
                label = "Pincode",
                mandatory = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Number,
            )
        }

        item {

            GenericDropDownField(
                value = state.Country1, onValueChange = viewModel::onCountry1Change,
                placeHolder = "Select Country",
                mandatory = true,
                label = "Country",
                modifier = Modifier.fillMaxWidth(),
                list = Countrylist
            )
        }
        item {
            OnBoardingTextField(
                value = state.phone1,
                onValueChange = viewModel::onPhone1Change,
                placeHolder = "Enter Phone Number",
                label = "Phone (Optional)",
                mandatory = false
            )

        }
        item {
            OnBoardingTextField(value = state.fax,
                onValueChange = viewModel::onFaxChange,
                placeHolder = "Enter Fax number",
                label = "Fax Number (Optional)",
                mandatory = false
            )
        }

        item {
            OnBoardingTextField(value = state.officeFax,
                onValueChange = viewModel::onOfficeFaxChange,
                placeHolder = "Enter Fax number",
                label = "Office Fax Number (Optional)",
                mandatory = false
            )
        }
        item {
            OnBoardingTextField(value = state.officePhoneNumber,
                onValueChange = viewModel::onOfficePhoneNumberChange,
                placeHolder = "Enter Office Phone number",
                label = "Office Phone Number (Optional)",
                mandatory = false,
                keyboardType = KeyboardType.Number
            )
        }
        item {
            OnBoardingTextField(value = state.email1,
                onValueChange = viewModel::onEmailChange,
                placeHolder = "Email Address",
                label = "Email",
                mandatory = true,
                keyboardType = KeyboardType.Email
            )

        }

        item {
            CheckBoxComp(checked = state.checked, onCheckedChange = viewModel::onCheckedChanged)


        }

        item {
            OnBoardingTextField(
                value = state.foreignAddress,
                onValueChange = viewModel::onForeignAddressChange,
                placeHolder = "Street Address",
                label = "Foreign Address Line",
                mandatory = true,
            )
        }

        item {
            OnBoardingTextField(
                value = state.foreignAddressLine2,
                onValueChange = viewModel::onForeignAddressLine2Change,
                placeHolder = "Apartment,suite,unit,etc.",
                label = "Foreign Address Line 2 (Optional)",
                mandatory = false
            )
        }
        item {
            OnBoardingTextField(
                value = state.foreignAddressLine3,
                onValueChange = viewModel::onForeignAddressLine3Change,
                placeHolder = "Additional Info",
                label = "Foreign Address Line 3 (Optional)",
                mandatory = false
            )
        }
        item {
            OnBoardingTextField(
                value = state.City2,
                onValueChange = viewModel::onCity2Change,
                placeHolder = "Enter City",
                label = "City",
                mandatory = true,
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Text,
            )
        }
        item {
            OnBoardingTextField(
                value = state.states2,
                onValueChange = viewModel::onStates2Change,
                placeHolder = "Enter State/province",
                label = "State/Province(Optional)",
                mandatory = false,

            )
        }
        item {

            OnBoardingTextField(
                value = state.postalCode, onValueChange = viewModel::onPostalCodeChange,
                placeHolder = "Enter Postal Code",
                label = "Postal Code (Optional)",
                mandatory = false,
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Number,
            )
        }

        item {

            GenericDropDownField(
                value = state.Country2, onValueChange = viewModel::onCountry2Change,
                placeHolder = "Select Country",
                mandatory = true,
                label = "Country",
                modifier = Modifier.fillMaxWidth(),
                list = Countrylist
            )
        }
        item {

            OnBoardingTextField(
                value = state.phone3, onValueChange = viewModel::onPhone3Change,
                placeHolder = "Enter Phone",
                label = "Phone (Optional)",
                mandatory = false,
                modifier = Modifier.fillMaxWidth(),
                keyboardType = KeyboardType.Number,
            )
        }
        item {
            OnBoardingTextField(value = state.fax2,
                onValueChange = viewModel::onFax2Change,
                placeHolder = "Enter Fax number",
                label = "Fax Number (Optional)",
                mandatory = false
            )
        }

        item {
            OnBoardingTextField(value = state.officeFax2,
                onValueChange = viewModel::onOfficeFax2Change,
                placeHolder = "Enter Fax number",
                label = "Office Fax Number (Optional)",
                mandatory = false
            )
        }
        item {
            OnBoardingTextField(value = state.officePhoneNumber2,
                onValueChange = viewModel::onOfficePhoneNumber2Change,
                placeHolder = "Enter Office Phone number",
                label = "Office Phone Number (Optional)",
                mandatory = false,
                keyboardType = KeyboardType.Number
            )
        }
        item {
           GenericDropDownField(value = state.kycType,
                onValueChange = viewModel::onKycTypeChange,
                placeHolder = "KYC type",
                label = "KYC type",
                mandatory = true,
               list = kycType
            )
        }
        item {
            OnBoardingTextField(value = state.ckycType,
                onValueChange = viewModel::onCKycChange,
                placeHolder = "Enter CKYC no.",
                label = "CKYC No",
                mandatory = false
            )
        }

        item {
            OnBoardingTextField(value = state.panExempt,
                onValueChange = viewModel::onPanExemptChange,
                placeHolder = "Enter PAN Exempt",
                label = "PAN EXEMPT",
                mandatory = false
            )
        }

        item {
            OnBoardingTextField(value = state.adharUpdated,
                onValueChange = viewModel::onAdharUpdatedChange,
                placeHolder = "Y/N",
                label = "Aadhaar Updated(Optional)",
                mandatory = false,
                keyboardType = KeyboardType.Number
            )
        }

        item {
            OnBoardingTextField(value = state.mapin,
                onValueChange = viewModel::onMapinChange,
                placeHolder = "Y/N",
                label = "Mapin(Optional)",
                mandatory = false
            )
        }

        item {
            GenericDropDownField(value = state.investor,
                onValueChange = viewModel::onInvestorChange,
                placeHolder = "Investor Onboarding",
                label = "Investor Onboarding",
                mandatory = true,
                list = InvestorList
            )
        }

        item {
            OnBoardingTextField(value = state.leiNumber,
                onValueChange = viewModel::onLeiNumberChange,
                placeHolder = "Lei Number",
                label = "Lei Number(Optional)",
                mandatory = false
            )
        }

    }
}