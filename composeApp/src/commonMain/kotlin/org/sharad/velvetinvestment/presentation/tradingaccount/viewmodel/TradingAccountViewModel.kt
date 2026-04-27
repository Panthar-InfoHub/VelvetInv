package org.sharad.velvetinvestment.presentation.tradingaccount.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.sharad.velvetinvestment.domain.models.tradingaccount.Data
import org.sharad.velvetinvestment.domain.models.tradingaccount.TradingAccountFormDomain
import org.sharad.velvetinvestment.presentation.tradingaccount.compose.Holding

class TradingAccountViewModel : ViewModel() {

    private val _formState = MutableStateFlow(
        TradingAccountFormDomain(
            data = Data()
        )
    )

    val formState: StateFlow<TradingAccountFormDomain> = _formState.asStateFlow()

    private fun updateData(update: (Data) -> Data) {
        _formState.update { current ->
            current.copy(
                data = update(current.data)
            )
        }
    }

    private val _holderNature = MutableStateFlow(Holding.SINGLE)
    val holderNature: StateFlow<Holding> = _holderNature.asStateFlow()

    private val _nomineeChecked = MutableStateFlow(false)
    val nomineeChecked: StateFlow<Boolean> = _nomineeChecked.asStateFlow()

    ///////////////////////////////////////////
    // PRIMARY HOLDER DETAILS
    ///////////////////////////////////////////

    fun onFirstNameChange(value: String) = updateData { it.copy(primary_holder_first_name = value) }
    fun onMiddleNameChange(value: String) = updateData { it.copy(primary_holder_middle_name = value) }
    fun onLastNameChange(value: String) = updateData { it.copy(primary_holder_last_name = value) }
    fun onPanChange(value: String) = updateData { it.copy(primary_holder_pan = value) }
    fun onDobChange(value: String) = updateData { it.copy(primary_holder_dob_incorporation = value) }
    fun onGenderChange(value: String) = updateData { it.copy(gender = value) }
    fun onEmailChange(value: String) = updateData { it.copy(email = value) }
    fun onPhoneChange(value: String) = updateData { it.copy(indian_mobile_no = value) }
    fun onTaxStatusChange(value: String) = updateData { it.copy(tax_status = value) }
    fun onOccupationChange(value: String) = updateData { it.copy(occupation_code = value) }
    fun onOccTypeChange(value: String) = updateData { it.copy(occ_type = value) }
    fun onHoldingNatureChange(value: String) = updateData { it.copy(holding_nature = value) }
    fun onPrimaryCkycChange(value: String) = updateData { it.copy(primary_holder_ckyc_number = value) }
    fun onPrimaryKycTypeChange(value: String) = updateData { it.copy(primary_holder_kyc_type = value) }
    fun onPrimaryPanExemptChange(value: String) = updateData { it.copy(primary_holder_pan_exempt = value) }
    fun onPrimaryKraExemptRefChange(value: String) = updateData { it.copy(primary_holder_kra_exempt_ref_no = value) }
    fun onPrimaryExemptCategoryChange(value: String) = updateData { it.copy(primary_holder_exempt_category = value) }

    ///////////////////////////////////////////
    // SECOND HOLDER DETAILS
    ///////////////////////////////////////////

    fun onSecondFirstNameChange(value: String) = updateData { it.copy(second_holder_first_name = value) }
    fun onSecondMiddleNameChange(value: String) = updateData { it.copy(second_holder_middle_name = value) }
    fun onSecondLastNameChange(value: String) = updateData { it.copy(second_holder_last_name = value) }
    fun onSecondPanChange(value: String) = updateData { it.copy(second_holder_pan = value) }
    fun onSecondDobChange(value: String) = updateData { it.copy(second_holder_dob = value) }
    fun onSecondEmailChange(value: String) = updateData { it.copy(second_holder_email = value) }
    fun onSecondMobileChange(value: String) = updateData { it.copy(second_holder_mobile = value) }
    fun onSecondCkycChange(value: String) = updateData { it.copy(second_holder_ckyc_number = value) }
    fun onSecondKycTypeChange(value: String) = updateData { it.copy(second_holder_kyc_type = value) }
    fun onSecondPanExemptChange(value: String) = updateData { it.copy(second_holder_pan_exempt = value) }
    fun onSecondExemptCategoryChange(value: String) = updateData { it.copy(second_holder_exempt_category = value) }
    fun onSecondEmailDeclChange(value: String) = updateData { it.copy(second_holder_email_declaration = value) }
    fun onSecondMobileDeclChange(value: String) = updateData { it.copy(second_holder_mobile_declaration = value) }

    ///////////////////////////////////////////
    // THIRD HOLDER DETAILS
    ///////////////////////////////////////////

    fun onThirdFirstNameChange(value: String) = updateData { it.copy(third_holder_first_name = value) }
    fun onThirdMiddleNameChange(value: String) = updateData { it.copy(third_holder_middle_name = value) }
    fun onThirdLastNameChange(value: String) = updateData { it.copy(third_holder_last_name = value) }
    fun onThirdPanChange(value: String) = updateData { it.copy(third_holder_pan = value) }
    fun onThirdDobChange(value: String) = updateData { it.copy(third_holder_dob = value) }
    fun onThirdEmailChange(value: String) = updateData { it.copy(third_holder_email = value) }
    fun onThirdMobileChange(value: String) = updateData { it.copy(third_holder_mobile = value) }
    fun onThirdCkycChange(value: String) = updateData { it.copy(third_holder_ckyc_number = value) }
    fun onThirdKycTypeChange(value: String) = updateData { it.copy(third_holder_kyc_type = value) }
    fun onThirdPanExemptChange(value: String) = updateData { it.copy(third_holder_pan_exempt = value) }
    fun onThirdExemptCategoryChange(value: String) = updateData { it.copy(third_holder_exempt_category = value) }
    fun onThirdEmailDeclChange(value: String) = updateData { it.copy(third_holder_email_declaration = value) }
    fun onThirdMobileDeclChange(value: String) = updateData { it.copy(third_holder_mobile_declaration = value) }

    ///////////////////////////////////////////
    // GUARDIAN DETAILS
    ///////////////////////////////////////////

    fun onGuardianRelationChange(value: String) = updateData { it.copy(guardian_relation = value) }
    fun onGuardianFirstNameChange(value: String) = updateData { it.copy(guardian_first_name = value) }
    fun onGuardianMiddleNameChange(value: String) = updateData { it.copy(guardian_middle_name = value) }
    fun onGuardianLastNameChange(value: String) = updateData { it.copy(guardian_last_name = value) }
    fun onGuardianPanChange(value: String) = updateData { it.copy(guardian_pan = value) }
    fun onGuardianDobChange(value: String) = updateData { it.copy(guardian_dob = value) }
    fun onGuardianCkycChange(value: String) = updateData { it.copy(guardian_ckyc_number = value) }
    fun onGuardianKycTypeChange(value: String) = updateData { it.copy(guardian_kyc_type = value) }
    fun onGuardianPanExemptChange(value: String) = updateData { it.copy(guardian_pan_exempt = value) }
    fun onGuardianExemptCategoryChange(value: String) = updateData { it.copy(guardian_exempt_category = value) }
    fun onGuardianExemptRefNoChange(value: String) = updateData { it.copy(guardian_exempt_ref_no = value) }

    ///////////////////////////////////////////
    // NOMINEE 1 DETAILS
    ///////////////////////////////////////////

    fun onNominee1NameChange(value: String) = updateData { it.copy(nominee_1_name = value) }
    fun onNominee1RelationChange(value: String) = updateData { it.copy(nominee_1_relationship = value) }
    fun onNominee1DobChange(value: String) = updateData { it.copy(nominee_1_dob = value) }
    fun onNominee1EmailChange(value: String) = updateData { it.copy(nominee_1_email = value) }
    fun onNominee1MobileChange(value: String) = updateData { it.copy(nominee_1_mobile = value) }
    fun onNominee1IdentityTypeChange(value: String) = updateData { it.copy(nominee_1_identity_type = value) }
    fun onNominee1IdentityNumberChange(value: String) = updateData { it.copy(nominee_1_identity_number = value) }
    fun onNominee1Address1Change(value: String) = updateData { it.copy(nominee_1_address1 = value) }
    fun onNominee1Address2Change(value: String) = updateData { it.copy(nominee_1_address2 = value) }
    fun onNominee1Address3Change(value: String) = updateData { it.copy(nominee_1_address3 = value) }
    fun onNominee1CityChange(value: String) = updateData { it.copy(nominee_1_city = value) }
    fun onNominee1PincodeChange(value: String) = updateData { it.copy(nominee_1_pin = value) }
    fun onNominee1CountryChange(value: String) = updateData { it.copy(nominee_1_country = value) }
    fun onNominee1MinorFlagChange(value: String) = updateData { it.copy(nominee_1_minor_flag = value) }
    fun onNominee1GuardianChange(value: String) = updateData { it.copy(nominee_1_guardian = value) }
    fun onNominee1GuardianPanChange(value: String) = updateData { it.copy(nominee_1_guardian_pan = value) }
    fun onNominee1ApplicableChange(value: String) = updateData { it.copy(nominee_1_applicable = value) }
    fun onNomineeSoaChange(value: String) = updateData { it.copy(nominee_soa = value) }

    ///////////////////////////////////////////
    // BANK DETAILS (ACCOUNT 1)
    ///////////////////////////////////////////

    fun onAccountType1Change(value: String) = updateData { it.copy(account_type_1 = value) }
    fun onAccountNumber1Change(value: String) = updateData { it.copy(account_no_1 = value) }
    fun onIfscCode1Change(value: String) = updateData { it.copy(ifsc_code_1 = value) }
    fun onMicrNo1Change(value: String) = updateData { it.copy(micr_no_1 = value) }
    fun onDefaultBankFlag1Change(value: String) = updateData { it.copy(default_bank_flag_1 = value) }

    ///////////////////////////////////////////
    // ADDRESS DETAILS
    ///////////////////////////////////////////

    fun onAddress1Change(value: String) = updateData { it.copy(address_1 = value) }
    fun onAddress2Change(value: String) = updateData { it.copy(address_2 = value) }
    fun onAddress3Change(value: String) = updateData { it.copy(address_3 = value) }
    fun onCityChange(value: String) = updateData { it.copy(city = value) }
    fun onStateChange(value: String) = updateData { it.copy(state = value) }
    fun onPincodeChange(value: String) = updateData { it.copy(pincode = value) }
    fun onCountryChange(value: String) = updateData { it.copy(country = value) }

    ///////////////////////////////////////////
    // MISCELLANEOUS / SYSTEM FIELDS
    ///////////////////////////////////////////

    fun onNominationOptChange(value: String) = updateData { it.copy(nomination_opt = value) }
    fun onNominationAuthChange(value: String) = updateData { it.copy(nomination_authentication = value) }
    fun onClientTypeChange(value: String) = updateData { it.copy(client_type = value) }
    fun onPmsChange(value: String) = updateData { it.copy(pms = value) }
    fun onDefaultDpChange(value: String) = updateData { it.copy(default_dp = value) }
    fun onCdslDpidChange(value: String) = updateData { it.copy(cdsl_dpid = value) }
    fun onCdslCltidChange(value: String) = updateData { it.copy(cdslcltid = value) }
    fun onNsdlDpidChange(value: String) = updateData { it.copy(nsdldpid = value) }
    fun onNsdlCltidChange(value: String) = updateData { it.copy(nsdlcltid = value) }
    fun onCmbpIdChange(value: String) = updateData { it.copy(cmbp_id = value) }
    fun onDivPayModeChange(value: String) = updateData { it.copy(div_pay_mode = value) }
    fun onAadhaarUpdatedChange(value: String) = updateData { it.copy(aadhaar_updated = value) }
    fun onChequeNameChange(value: String) = updateData { it.copy(cheque_name = value) }
    fun onCommunicationModeChange(value: String) = updateData { it.copy(communication_mode = value) }
    fun onEmailDeclFlagChange(value: String) = updateData { it.copy(email_declaration_flag = value) }
    fun onMobileDeclFlagChange(value: String) = updateData { it.copy(mobile_declaration_flag = value) }
    fun onPaperlessFlagChange(value: String) = updateData { it.copy(paperless_flag = value) }
    fun onLeiNoChange(value: String) = updateData { it.copy(lei_no = value) }
    fun onLeiValidityChange(value: String) = updateData { it.copy(lei_validity = value) }
    fun onMapinIdChange(value: String) = updateData { it.copy(mapin_id = value) }
    fun onSourceWealthChange(value: String) = updateData { it.copy(srce_wealt = value) }

    // (Note: There are similar fields for Account 2-5 and Nominee 2-3 in the Data model
    // which can be added following the same pattern if required by the UI.)

    fun getFormData(): TradingAccountFormDomain {
        return _formState.value
    }

    fun onHolderNatureChange(value: Holding) {
        _holderNature.value = value
    }

    fun onCheckedChange(value: Boolean) {
        _nomineeChecked.value = value
    }

    fun verifyPan() {

    }
}