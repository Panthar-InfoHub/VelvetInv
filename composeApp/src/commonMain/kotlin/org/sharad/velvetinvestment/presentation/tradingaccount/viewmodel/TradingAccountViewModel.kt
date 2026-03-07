package org.sharad.velvetinvestment.presentation.tradingaccount.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.sharad.velvetinvestment.presentation.portfolio.compose.BankDetailsCard
import org.sharad.velvetinvestment.presentation.tradingaccount.compose.HolderNature
import org.sharad.velvetinvestment.presentation.tradingaccount.compose.Holding
import org.sharad.velvetinvestment.presentation.tradingaccount.uimodel.AddressDetailModel
import org.sharad.velvetinvestment.presentation.tradingaccount.uimodel.BankDetailModel
import org.sharad.velvetinvestment.presentation.tradingaccount.uimodel.BasicDetailsUIModel
import org.sharad.velvetinvestment.presentation.tradingaccount.uimodel.FinancialTradingDetailsModel
import org.sharad.velvetinvestment.presentation.tradingaccount.uimodel.GuardianDetailModel
import org.sharad.velvetinvestment.presentation.tradingaccount.uimodel.PanModel
import org.sharad.velvetinvestment.presentation.tradingaccount.uimodel.TAScreen4Model

class TradingAccountViewModel : ViewModel() {
    private val _basicDetailsModel = MutableStateFlow(BasicDetailsUIModel())
    val basicDetailsModel = _basicDetailsModel.asStateFlow()

    fun onClientIdChange(clientId: String) {
        _basicDetailsModel.update { it.copy(clientId = clientId) }
    }

    fun onFirstNameChange(firstName: String) {
        _basicDetailsModel.update { it.copy(firstName = firstName) }
    }

    fun onMiddleNameChange(middleName: String) {
        _basicDetailsModel.update { it.copy(middleName = middleName) }
    }

    fun onLastNameChange(lastName: String) {
        _basicDetailsModel.update { it.copy(lastName = lastName) }
    }

    fun onTaxStatusChange(taxStatus: String) {
        _basicDetailsModel.update { it.copy(taxStatus = taxStatus) }
    }

    fun onGenderChange(gender: String) {
        _basicDetailsModel.update { it ->
            it.copy(gender = gender)
        }
    }


    fun onDobChange(dob: String) {
        _basicDetailsModel.update { it.copy(dob = dob) }
    }

    fun onEmailChange(email: String) {
        _basicDetailsModel.update { it.copy(email = email) }
    }

    fun onPhoneChange(phone: String) {
        _basicDetailsModel.update { it.copy(phone = phone) }
    }

    /////////////////////////////////
    private val _panModel = MutableStateFlow(PanModel())
    val panModel = _panModel.asStateFlow()

    fun onPanChange(panNumber: String) {
        _panModel.update { it ->
            it.copy(panNumber = panNumber)
        }
    }


    fun verifyPan() {

    }

    ////////////////////////////////////////////////////////
    private val _financialTradingDetailModel = MutableStateFlow(FinancialTradingDetailsModel())
    val financialTradingDetailsModel = _financialTradingDetailModel.asStateFlow()

    fun onOccupationChange(occupation: String) {
        _financialTradingDetailModel.update { it ->
            it.copy(occupation = occupation)
        }
    }

    fun onHolderNatureChange(HolderNature: Holding) {
        _financialTradingDetailModel.update { it ->
            it.copy(holderNature = HolderNature)
        }
    }


    // Checkbox
    fun onCheckedChange(value: Boolean) {
        _financialTradingDetailModel.update { it ->
            it.copy(checked = value)
        }
    }

    // Nomination OPT
    fun onNominationChange(value: String) {
        _financialTradingDetailModel.update { it ->
            it.copy(nominationOPT = value)
        }
    }

    // Nominee Authentication
    fun onNomineeAuthenticationChange(value: String) {
        _financialTradingDetailModel.update { it ->
            it.copy(nomineeAuthentication = value)
        }
    }

    // Nominee Name
    fun onNomineeNameChange(value: String) {
        _financialTradingDetailModel.update { it ->
            it.copy(nomineeName = value)
        }
    }

    // Nominee Relation
    fun onNomineeRelationChange(value: String) {
        _financialTradingDetailModel.update { it ->
            it.copy(nomineeRelation = value)
        }
    }

    // Nominee Identity Type
    fun onNomineeIdentityTypeChange(value: String) {
        _financialTradingDetailModel.update { it ->
            it.copy(nomineeIdentityType = value)
        }
    }

    // Nominee Aadhaar
    fun onNomineeAadharChange(value: String) {
        _financialTradingDetailModel.update { it ->
            it.copy(nomineeAadhar = value)
        }
    }

    // Nominee Email
    fun onNomineeEmailChange(value: String) {
        _financialTradingDetailModel.update {
            it.copy(nomineeEmail = value)
        }
    }

    // Nominee Mobile
    fun onNomineeMobileChange(value: String) {
        _financialTradingDetailModel.update { it ->
            it.copy(nomineeMobile = value)
        }
    }

    // Address 1
    fun onNomineeAddress1Change(value: String) {
        _financialTradingDetailModel.update { it ->
            it.copy(nomineeAddress1 = value)
        }
    }

    // Address 2
    fun onNomineeAddress2Change(value: String) {
        _financialTradingDetailModel.update { it ->
            it.copy(nomineeAddress2 = value)
        }
    }

    // Address 3
    fun onNomineeAddress3Change(value: String) {
        _financialTradingDetailModel.update { it ->
            it.copy(nomineeAddress3 = value)
        }
    }

    // City
    fun onNomineeCityChange(value: String) {
        _financialTradingDetailModel.update { it ->
            it.copy(nomineeCity = value)
        }
    }

    // Pincode
    fun onNomineePincodeChange(value: String) {
        _financialTradingDetailModel.update { it ->
            it.copy(nomineePincode = value)
        }
    }

    // Country
    fun onNomineeCountryChange(value: String) {
        _financialTradingDetailModel.update { it ->
            it.copy(nomineeCountry = value)
        }
    }

    // SOA
    fun onNomineeSOAChange(value: String) {
        _financialTradingDetailModel.update { it ->
            it.copy(nomineeSOA = value)
        }
    }

    // Nominee Ref No
    fun onNomineeOptRefNoChange(value: String) {
        _financialTradingDetailModel.update { it ->
            it.copy(nomineeOptRefNo = value)
        }
    }

    fun onJointFullNameChange(value: String) {
        _financialTradingDetailModel.update {
            it.copy(jointFullName = value)
        }
    }

    fun onJointRelationshipChange(value: String) {
        _financialTradingDetailModel.update {
            it.copy(jointRelationship = value)
        }
    }

    fun onJointPanChange(value: String) {
        _financialTradingDetailModel.update {
            it.copy(jointPan = value)
        }
    }

    fun onJointEmailChange(value: String) {
        _financialTradingDetailModel.update {
            it.copy(jointEmail = value)
        }
    }

    fun onJointMobileChange(value: String) {
        _financialTradingDetailModel.update {
            it.copy(jointMobile = value)
        }
    }

    fun onJointPercentageChange(value: String) {
        _financialTradingDetailModel.update {
            it.copy(jointPercentage = value)
        }
    }

    fun onJointAddress1Change(value: String) {
        _financialTradingDetailModel.update {
            it.copy(jointAddress1 = value)
        }
    }

    fun onJointAddress2Change(value: String) {
        _financialTradingDetailModel.update {
            it.copy(jointAddress2 = value)
        }
    }

    fun onJointAddress3Change(value: String) {
        _financialTradingDetailModel.update {
            it.copy(jointAddress3 = value)
        }
    }

    fun onJointCityChange(value: String) {
        _financialTradingDetailModel.update {
            it.copy(jointCity = value)
        }
    }

    fun onJointPincodeChange(value: String) {
        _financialTradingDetailModel.update {
            it.copy(jointPincode = value)
        }
    }

    fun onJointCountryChange(value: String) {
        _financialTradingDetailModel.update {
            it.copy(jointCountry = value)
        }
    }

    private val _ClientFormModel = MutableStateFlow(TAScreen4Model())
    val ClientFormModel = _ClientFormModel.asStateFlow()

    /////////////////////////////////////////
    fun onClientTypeChange(ClientType: String) {
        _ClientFormModel.update {
            it.copy(clientType = ClientType)
        }
    }

    fun onPMSChange(PMS: String) {
        _ClientFormModel.update {
            it.copy(PMS = PMS)
        }
    }

    fun onDefaultDPChange(DefaultDP: String) {
        _ClientFormModel.update {
            it.copy(defaultDP = DefaultDP)
        }
    }

    fun onCDSLIDChange(CDSLID: String) {
        _ClientFormModel.update {
            it.copy(CDSLID = CDSLID)
        }
    }

    fun onCDSLClientIDChange(CDSLClientID: String) {
        _ClientFormModel.update {
            it.copy(CDSLClientID = CDSLClientID)
        }
    }

    fun onCMBPIDChange(CMBPID: String) {
        _ClientFormModel.update {
            it.copy(CMBPID = CMBPID)
        }
    }

    fun onNSDLIDChange(NSDLID: String) {
        _ClientFormModel.update {
            it.copy(NSDLID = NSDLID)
        }
    }

    fun onNSDLClientIDChange(NSDLClientID: String) {
        _ClientFormModel.update {
            it.copy(NSDLClientID = NSDLClientID)
        }

    }

    /////////////////////////////////////////////////
    private val _bankDetailModel = MutableStateFlow(BankDetailModel())
    val bankDetailModel = _bankDetailModel.asStateFlow()
    fun onAccountTypeChange(accountType:String){
        _bankDetailModel.update {it.copy(accountType = accountType)
        }

    }

    fun onAccountNumberChange(number:String){
        _bankDetailModel.update {
            it.copy(accountNumber = number)
        }
    }
    fun onIFSCchange(ifsc:String){
        _bankDetailModel.update {
            it.copy(ifscCode = ifsc)
        }

    }

    fun onMicroNumberChange(number: String){
        _bankDetailModel.update {
            it.copy(microNumber = number)
        }

    }
    fun onPaymentModeChange(mode:String){
        _bankDetailModel.update {
            it.copy(paymentMethod = mode)
        }
    }
    //////////////////////////////////////////////////////
    private val _addressDetailModel = MutableStateFlow(AddressDetailModel())
    val addressDetailModel = _addressDetailModel.asStateFlow()

    fun onAddress1Change(value: String) {
        _addressDetailModel.update { it.copy(Address1 = value) }
    }

    fun onAddress2Change(value: String) {
        _addressDetailModel.update { it.copy(Address2 = value) }
    }

    fun onAddress3Change(value: String) {
        _addressDetailModel.update { it.copy(Address3 = value) }
    }

    fun onCity1Change(value: String) {
        _addressDetailModel.update { it.copy(City1 = value) }
    }

    fun onStatesChange(value: String) {
        _addressDetailModel.update { it.copy(states = value) }
    }

    fun onPincodeChange(value: String) {
        _addressDetailModel.update { it.copy(Pincode = value) }
    }

    fun onCountry1Change(value: String) {
        _addressDetailModel.update { it.copy(Country1 = value) }
    }
    fun onPhone1Change(value: String) {
        _addressDetailModel.update { it.copy(phone1 = value) }
    }
    fun onPhone3Change(value: String) {
        _addressDetailModel.update { it.copy(phone3 = value) }
    }

    fun onFaxChange(value: String) {
       _addressDetailModel.update { it.copy(fax = value) }
    }

    fun onOfficeFaxChange(value: String) {
        _addressDetailModel.update { it.copy(officeFax = value) }
    }

    fun onEmailAddressChange(value: String) {
        _addressDetailModel.update { it.copy(email1 = value) }
    }

    fun onCheckedChanged(value: Boolean) {
        _addressDetailModel.update { it.copy(checked = value) }
    }
    fun onForeignAddressChange(value: String) {
        _addressDetailModel.update { it.copy(foreignAddress = value) }
    }

    fun onForeignAddressLine2Change(value: String) {
        _addressDetailModel.update { it.copy(foreignAddressLine2 = value) }
    }

    fun onForeignAddressLine3Change(value: String) {
        _addressDetailModel.update { it.copy(foreignAddressLine3 = value) }
    }

    fun onCity2Change(value: String) {
        _addressDetailModel.update { it.copy(City2 = value) }
    }

    fun onStates2Change(value: String) {
        _addressDetailModel.update { it.copy(states2 = value) }
    }

    fun onPostalCodeChange(value: String) {
        _addressDetailModel.update { it.copy(postalCode = value) }
    }

    fun onCountry2Change(value: String) {
        _addressDetailModel.update { it.copy(Country2 = value) }
    }

    fun onFax2Change(value: String) {
        _addressDetailModel.update { it.copy(fax2 = value) }
    }

    fun onOfficeFax2Change(value: String) {
        _addressDetailModel.update { it.copy(officeFax2 = value) }
    }

    fun onOfficePhoneNumberChange(value: String) {
        _addressDetailModel.update { it.copy(officePhoneNumber = value) }
    }
    fun onOfficePhoneNumber2Change(value: String) {
        _addressDetailModel.update { it.copy(officePhoneNumber2 = value) }
    }
    fun onKycTypeChange(value: String) {
        _addressDetailModel.update { it.copy(kycType = value) }
    }

    fun onCKycChange(value: String) {
        _addressDetailModel.update { it.copy(ckycType = value) }
    }

    fun onPanExemptChange(value: String) {
        _addressDetailModel.update { it.copy(panExempt = value) }
    }

    fun onAdharUpdatedChange(value: String) {
        _addressDetailModel.update { it.copy(adharUpdated = value) }
    }

    fun onMapinChange(value: String) {
        _addressDetailModel.update { it.copy(mapin = value) }
    }

    fun onInvestorChange(value: String) {
        _addressDetailModel.update { it.copy(investor = value) }
    }

    fun onLeiNumberChange(value: String) {
        _addressDetailModel.update { it.copy(leiNumber = value) }
    }
    //////////////////////////////////////////////////
    private val _guardianDetailModel =MutableStateFlow(GuardianDetailModel())
    val gurdianDetailModel = _guardianDetailModel.asStateFlow()

    fun onMinorRelationChange(value:String) {
        _guardianDetailModel.update {
            it.copy(minorRelation = value)
        }
    }
        fun onGurdianEmailChange(value:String) {
            _guardianDetailModel.update {
                it.copy(guardianEmail = value)
            }
        }
            fun onGurdianNumberChange(value:String) {
                _guardianDetailModel.update {
                    it.copy(guardianNumber = value)
                }
            }
                fun onGurdianNameChange(value:String) {
                    _guardianDetailModel.update {
                        it.copy(guardianName = value)
                    }
                }


    }

