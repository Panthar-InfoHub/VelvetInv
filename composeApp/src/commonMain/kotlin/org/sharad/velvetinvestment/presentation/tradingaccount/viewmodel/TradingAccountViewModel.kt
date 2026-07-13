package org.sharad.velvetinvestment.presentation.tradingaccount.viewmodel

import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.toUpperCase
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.number
import kotlinx.datetime.toLocalDateTime
import org.sharad.velvetinvestment.domain.models.tradingaccount.Data
import org.sharad.velvetinvestment.domain.models.tradingaccount.TradingAccountFormDomain
import org.sharad.velvetinvestment.domain.usecases.user.GetTradingAccountPrefilledDataUseCase
import org.sharad.velvetinvestment.domain.usecases.user.SubmitTradingAccountFormUseCase
import org.sharad.velvetinvestment.domain.usecases.user.TradingAccountConfirmationUseCase
import org.sharad.velvetinvestment.domain.usecases.user.VerifyPANUseCase
import org.sharad.velvetinvestment.utils.AppEventsController
import org.sharad.velvetinvestment.utils.DateTimeUtils
import org.sharad.velvetinvestment.utils.SnackBarController
import org.sharad.velvetinvestment.utils.UiState
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess
import org.sharad.velvetinvestment.utils.tradingaccount.ClientType
import org.sharad.velvetinvestment.utils.tradingaccount.DefaultDp
import org.sharad.velvetinvestment.utils.tradingaccount.Holding
import org.sharad.velvetinvestment.utils.tradingaccount.KycType
import org.sharad.velvetinvestment.utils.tradingaccount.OccupationSourceOfWealthMapper
import org.sharad.velvetinvestment.utils.tradingaccount.StateCode
import org.sharad.velvetinvestment.utils.tradingaccount.TaxStatus
import kotlin.time.Clock
import kotlin.time.Instant

class TradingAccountViewModel(
    private val getTradingAccountPrefilledDataUseCase: GetTradingAccountPrefilledDataUseCase,
    private val verifyPANUseCase: VerifyPANUseCase,
    private val submitTradingAccountFormUseCase: SubmitTradingAccountFormUseCase,
    private val tradingAccountConfirmationUseCase: TradingAccountConfirmationUseCase
) : ViewModel() {

    // GLOBAL STATE
    private val _formState = MutableStateFlow<UiState<TradingAccountFormDomain>>(UiState.Loading)
    val     formState = _formState.asStateFlow()

    init {
        getUserData()
    }

    private fun updateData(update: (Data) -> Data) {
        _formState.update { currentState ->
            when (currentState) {
                is UiState.Success -> {
                    currentState.copy(
                        data = currentState.data.copy(
                            data = update(currentState.data.data)
                        )
                    )
                }
                else -> currentState
            }
        }
    }

    fun getUserData() {
        viewModelScope.launch {
            _formState.value = UiState.Loading
            getTradingAccountPrefilledDataUseCase()
                .onSuccess { response ->
                    _formState.value = UiState.Success(
                        TradingAccountFormDomain(data = Data())
                    )
                    val userData = response
                    updateData {
                        it.copy(
                            primary_holder_first_name = userData.fullName,
                            email = userData.email,
                            primary_holder_dob_incorporation = DateTimeUtils.isoUtcToSlashDate(userData.dob),
                            indian_mobile_no = userData.phoneNo,
                            gender = userData.gender.toUpperCase(Locale.current).take(1),
                            primary_holder_pan = userData.panNo,
                            po_bir_inc = userData.placeOfBirth,
                            address_1 = userData.fullAddress,
                            pincode = userData.pinCode,
                            city = userData.city,
                            state = StateCode.fromDisplayName(userData.state)?.code?: "",
                            country = userData.country,
                        )
                    }
                    _isMinor.value = isMinor(userData.dob)
                    if (isMinor(userData.dob)){
                        onGuardianPanExemptChange("N")
                        onPrimaryPanExemptChange("Y")
                    }
                }
                .onError {
                    _formState.value = UiState.Error(it.message)
                    SnackBarController.showError("Failed to load data.")
                }
        }
    }

    fun submitForm(
        onLaunchWebView: (String) -> Unit
    ) {
        viewModelScope.launch {

            val previousState = _formState.value
            val successState = previousState as? UiState.Success ?: return@launch

            _formState.value = UiState.Loading
            var currentRetry = 0
            val maxRetries = 2

            while (currentRetry <= maxRetries) {

                var success = false

                submitTradingAccountFormUseCase(successState.data)
                    .onSuccess { response ->
                        _formState.value = previousState
                        onLaunchWebView(response)
                        success = true
                    }
                    .onError { error ->
                        if (currentRetry == maxRetries) {
                            _formState.value = previousState
                            SnackBarController.showError(
                                error.message.ifBlank {
                                    "Failed to submit trading account form"
                                }
                            )
                        }
                    }

                if (success) break

                currentRetry++

                if (currentRetry <= maxRetries) {
                    delay(1000)
                }
            }
        }
    }

    fun confirmAccount(
        onSuccessfulSubmit: () -> Unit
    ) {
        viewModelScope.launch {

            val previousState = _formState.value
            val successState = previousState as? UiState.Success ?: return@launch

            _formState.value = UiState.Loading

            tradingAccountConfirmationUseCase(
                taxStatus = successState.data.data.tax_status,
                holdingNature = successState.data.data.holding_nature,
                jointHolderName1 = successState.data.data.second_holder_first_name+ " "+successState.data.data.second_holder_last_name,
                jointHolderName2 = successState.data.data.third_holder_first_name+ " "+successState.data.data.third_holder_last_name,
                guardianName = successState.data.data.guardian_first_name+ " "+successState.data.data.guardian_last_name,
                isMinor = isMinor.value
            )
                .onSuccess { response ->
                    _formState.value = previousState
                    SnackBarController.showSuccess("Account Created Successfully")
                    AppEventsController.sendHomeRefreshEvent()
                    onSuccessfulSubmit()
                }
                .onError { error ->

                    _formState.value = previousState

                    SnackBarController.showError(
                        error.message.ifBlank {
                            "Failed to confirm trading account"
                        }
                    )
                }
        }
    }

    // PRIMARY HOLDER DETAILS
    private val _isMinor = MutableStateFlow(false)
    val isMinor: StateFlow<Boolean> = _isMinor.asStateFlow()

    private val _panVerified = MutableStateFlow(false)
    val panVerified: StateFlow<Boolean> = _panVerified.asStateFlow()

    private val _verifiedPanNumber = MutableStateFlow("")
    val verifiedPanNumber: StateFlow<String> = _verifiedPanNumber.asStateFlow()

    val basicDetailsNextEnabled = combine(_formState) { flow ->
        val data = flow[0]
        if (data is UiState.Success) {
            val form = data.data.data
            form.primary_holder_first_name.isNotEmpty() &&
                    form.primary_holder_dob_incorporation.isNotEmpty() &&
                    form.indian_mobile_no.isNotEmpty() &&
                    form.email.isNotEmpty() &&
                    form.po_bir_inc.isNotEmpty() &&
                    form.tax_status.isNotEmpty()
        } else false
    }.stateIn(scope = viewModelScope, started = WhileSubscribed(5000), initialValue = false)

    fun verifyPan(pan: String) {
        viewModelScope.launch {
            val previousState = _formState.value
            if (previousState !is UiState.Success) return@launch
            _formState.value = UiState.Loading
            verifyPANUseCase(pan)
                .onSuccess { response ->
                    _formState.value = previousState
                    SnackBarController.showInfo(response.message)
                    _panVerified.value = response.status
                    _verifiedPanNumber.value = pan
                }
                .onError { error ->
                    _formState.value = previousState
                    SnackBarController.showError(error.message)
                }
        }
    }

    fun onFirstNameChange(value: String) = updateData { it.copy(primary_holder_first_name = value.toUpperCase(Locale.current)) }
    fun onMiddleNameChange(value: String) = updateData { it.copy(primary_holder_middle_name = value.toUpperCase(Locale.current)) }
    fun onLastNameChange(value: String) = updateData { it.copy(primary_holder_last_name = value.toUpperCase(Locale.current)) }
    fun onPanChange(value: String) = updateData { it.copy(primary_holder_pan = value.trim().toUpperCase(Locale.current)) }
    fun onDobChange(value: String) {
        updateData { it.copy(primary_holder_dob_incorporation = value.trim().toUpperCase(Locale.current)) }
        _isMinor.value = isMinor(value)
    }
    fun onGenderChange(value: String) = updateData { it.copy(gender = value.trim().toUpperCase(Locale.current)) }
    fun onEmailChange(value: String) = updateData { it.copy(email = value.trim()) }
    fun onPhoneChange(value: String) {
        if (value.length> 10) return
        updateData { it.copy(indian_mobile_no = value.trim().toUpperCase(Locale.current)) }
    }
    fun onTaxStatusChange(value: String) = updateData { it.copy(tax_status = value.trim().toUpperCase(Locale.current)) }
    fun onOccupationChange(value: String) {
        val sourceOfWealth =
            OccupationSourceOfWealthMapper.getSourceOfWealthCode(value) ?: ""

        updateData {
            it.copy(
                occupation_code = value,
                srce_wealt = sourceOfWealth,
                occ_type = OccupationSourceOfWealthMapper.getFatcaOccupationTypeCode(sourceOfWealth)
            )
        }
    }
    fun onOccTypeChange(value: String) = updateData { it.copy(occ_type = value.trim().toUpperCase(Locale.current)) }
    fun onPlaceOfBirthChange(value: String) = updateData { it.copy(po_bir_inc = value.toUpperCase(Locale.current)) }
    fun onPrimaryCkycChange(value: String) = updateData { it.copy(primary_holder_ckyc_number = value.trim().toUpperCase(Locale.current)) }
    fun onPrimaryKycTypeChange(value: String) = updateData { it.copy(primary_holder_kyc_type = value.trim().toUpperCase(Locale.current)) }
    fun onPrimaryPanExemptChange(value: String) = updateData { it.copy(primary_holder_pan_exempt = value.trim().toUpperCase(Locale.current)) }
    fun onPrimaryKraExemptRefChange(value: String) = updateData { it.copy(primary_holder_kra_exempt_ref_no = value.trim().toUpperCase(Locale.current)) }
    fun onPrimaryExemptCategoryChange(value: String) = updateData { it.copy(primary_holder_exempt_category = value.trim().toUpperCase(Locale.current)) }

    // JOINT HOLDER DETAILS
    private val _holderNature = MutableStateFlow(Holding.SINGLE)
    val holderNature: StateFlow<Holding> = _holderNature.asStateFlow()

    private val _enableThirdHolder = MutableStateFlow(false)
    val enableThirdHolder: StateFlow<Boolean> = _enableThirdHolder.asStateFlow()

    fun onHoldingNatureChange(value: String) = updateData { it.copy(holding_nature = value) }

    fun onHolderNatureChangeUi(value: Holding) {
        _holderNature.value = value
        onHoldingNatureChange(value.code)
        if (value == Holding.SINGLE) {
            resetJointHolderData()
        }
        if (value == Holding.JOINT) {
            updateData { it.copy(second_holder_pan_exempt = "N") }
        }
    }

    fun addThirdHolder() {
        _enableThirdHolder.value = true
        updateData { it.copy(third_holder_pan_exempt = "N") }
    }

    fun removeThirdHolder() {
        _enableThirdHolder.value = false
        resetThirdHolder()
    }

    private fun resetJointHolderData() {
        resetSecondHolder()
        resetThirdHolder()
    }

    private fun resetSecondHolder() {
        updateData {
            it.copy(
                second_holder_first_name = "", second_holder_middle_name = "", second_holder_last_name = "",
                second_holder_pan = "", second_holder_dob = "", second_holder_email = "", second_holder_mobile = "",
                second_holder_ckyc_number = "", second_holder_kyc_type = "", second_holder_pan_exempt = "",
                second_holder_exempt_category = "", second_holder_email_declaration = "", second_holder_mobile_declaration = "",
                second_holder_kra_exempt_ref_no = ""
            )
        }
    }

    private fun resetThirdHolder() {
        updateData {
            it.copy(
                third_holder_first_name = "", third_holder_middle_name = "", third_holder_last_name = "",
                third_holder_pan = "", third_holder_dob = "", third_holder_email = "", third_holder_mobile = "",
                third_holder_ckyc_number = "", third_holder_kyc_type = "", third_holder_pan_exempt = "",
                third_holder_exempt_category = "", third_holder_email_declaration = "", third_holder_mobile_declaration = "",
                third_holder_kra_exempt_ref_no = ""
            )
        }
    }

    fun onSecondFirstNameChange(value: String) = updateData { it.copy(second_holder_first_name = value.trim().toUpperCase(Locale.current)) }
    fun onSecondMiddleNameChange(value: String) = updateData { it.copy(second_holder_middle_name = value.trim().toUpperCase(Locale.current)) }
    fun onSecondLastNameChange(value: String) = updateData { it.copy(second_holder_last_name = value.trim().toUpperCase(Locale.current)) }
    fun onSecondPanChange(value: String) = updateData { it.copy(second_holder_pan = value.trim().toUpperCase(Locale.current)) }
    fun onSecondDobChange(value: String) = updateData { it.copy(second_holder_dob = value.trim().toUpperCase(Locale.current)) }
    fun onSecondEmailChange(value: String) = updateData { it.copy(second_holder_email = value.trim().toUpperCase(Locale.current)) }
    fun onSecondMobileChange(value: String) = updateData { it.copy(second_holder_mobile = value.trim().toUpperCase(Locale.current)) }
    fun onSecondCkycChange(value: String) = updateData { it.copy(second_holder_ckyc_number = value.trim().toUpperCase(Locale.current)) }
    fun onSecondKycTypeChange(value: String) = updateData { it.copy(second_holder_kyc_type = value.trim().toUpperCase(Locale.current)) }
    fun onSecondPanExemptChange(value: String) = updateData { it.copy(second_holder_pan_exempt = value.trim().toUpperCase(Locale.current)) }
    fun onSecondExemptCategoryChange(value: String) = updateData { it.copy(second_holder_exempt_category = value.trim().toUpperCase(Locale.current)) }
    fun onSecondEmailDeclChange(value: String) = updateData { it.copy(second_holder_email_declaration = value.trim().toUpperCase(Locale.current)) }
    fun onSecondMobileDeclChange(value: String) = updateData { it.copy(second_holder_mobile_declaration = value.trim().toUpperCase(Locale.current)) }

    fun onThirdFirstNameChange(value: String) = updateData { it.copy(third_holder_first_name = value.trim().toUpperCase(Locale.current)) }
    fun onThirdMiddleNameChange(value: String) = updateData { it.copy(third_holder_middle_name = value.trim().toUpperCase(Locale.current)) }
    fun onThirdLastNameChange(value: String) = updateData { it.copy(third_holder_last_name = value.trim().toUpperCase(Locale.current)) }
    fun onThirdPanChange(value: String) = updateData { it.copy(third_holder_pan = value.trim().toUpperCase(Locale.current)) }
    fun onThirdDobChange(value: String) = updateData { it.copy(third_holder_dob = value.trim().toUpperCase(Locale.current)) }
    fun onThirdEmailChange(value: String) = updateData { it.copy(third_holder_email = value.trim().toUpperCase(Locale.current)) }
    fun onThirdMobileChange(value: String) = updateData { it.copy(third_holder_mobile = value.trim().toUpperCase(Locale.current)) }
    fun onThirdCkycChange(value: String) = updateData { it.copy(third_holder_ckyc_number = value.trim().toUpperCase(Locale.current)) }
    fun onThirdKycTypeChange(value: String) = updateData { it.copy(third_holder_kyc_type = value.trim().toUpperCase(Locale.current)) }
    fun onThirdPanExemptChange(value: String) = updateData { it.copy(third_holder_pan_exempt = value.trim().toUpperCase(Locale.current)) }
    fun onThirdExemptCategoryChange(value: String) = updateData { it.copy(third_holder_exempt_category = value.trim().toUpperCase(Locale.current)) }
    fun onThirdEmailDeclChange(value: String) = updateData { it.copy(third_holder_email_declaration = value.trim().toUpperCase(Locale.current)) }
    fun onThirdMobileDeclChange(value: String) = updateData { it.copy(third_holder_mobile_declaration = value.trim().toUpperCase(Locale.current)) }

    // GUARDIAN DETAILS
    fun onGuardianRelationChange(value: String) = updateData { it.copy(guardian_relation = value.trim().toUpperCase(Locale.current)) }
    fun onGuardianFirstNameChange(value: String) = updateData { it.copy(guardian_first_name = value.trim().toUpperCase(Locale.current)) }
    fun onGuardianMiddleNameChange(value: String) = updateData { it.copy(guardian_middle_name = value.trim().toUpperCase(Locale.current)) }
    fun onGuardianLastNameChange(value: String) = updateData { it.copy(guardian_last_name = value.trim().toUpperCase(Locale.current)) }
    fun onGuardianPanChange(value: String) = updateData { it.copy(guardian_pan = value.trim().toUpperCase(Locale.current)) }
    fun onGuardianDobChange(value: String) = updateData { it.copy(guardian_dob = value.trim().toUpperCase(Locale.current)) }
    fun onGuardianCkycChange(value: String) = updateData { it.copy(guardian_ckyc_number = value.trim().toUpperCase(Locale.current)) }
    fun onGuardianKycTypeChange(value: String) = updateData { it.copy(guardian_kyc_type = value.trim().toUpperCase(Locale.current)) }
    fun onGuardianPanExemptChange(value: String) = updateData { it.copy(guardian_pan_exempt = value.trim().toUpperCase(Locale.current)) }
    fun onGuardianExemptCategoryChange(value: String) = updateData { it.copy(guardian_exempt_category = value.trim().toUpperCase(Locale.current)) }
    fun onGuardianExemptRefNoChange(value: String) = updateData { it.copy(guardian_exempt_ref_no = value.trim().toUpperCase(Locale.current)) }

    // NOMINEE DETAILS
    private val _nomineeChecked = MutableStateFlow(false)
    val nomineeChecked: StateFlow<Boolean> = _nomineeChecked.asStateFlow()

    private val _showCalender = MutableStateFlow(false)
    val showCalender: StateFlow<Boolean> = _showCalender.asStateFlow()

    fun onCheckedChange(value: Boolean) {
        _nomineeChecked.value = value
        onNominationOptChange(if (value) "Y" else "N")
        onNominationAuthChange("")
    }

    fun showCalender() = run { _showCalender.value = true }
    fun hideCalender() = run { _showCalender.value = false }

    fun onDobChange(dob: Long) {
        onNominee1DobChange(DateTimeUtils.epochMillisToSlashDate(dob))
        val isMinor = DateTimeUtils.epochMillisToIsoUtc(dob)?.let { isMinor(it) }
        onNominee1MinorFlagChange(if (isMinor == true) "Y" else "N")
    }

    fun onNominee1NameChange(value: String) = updateData { it.copy(nominee_1_name = value.toUpperCase(Locale.current)) }
    fun onNominee1RelationChange(value: String) = updateData { it.copy(nominee_1_relationship = value.trim().toUpperCase(Locale.current)) }
    fun onNominee1DobChange(value: String) = updateData { it.copy(nominee_1_dob = value.trim().toUpperCase(Locale.current)) }
    fun onNominee1EmailChange(value: String) = updateData { it.copy(nominee_1_email = value.trim()) }
    fun onNominee1MobileChange(value: String) {
        if (value.length> 10) return
        updateData { it.copy(nominee_1_mobile = value.trim().toUpperCase(Locale.current)) }
    }
    fun onNominee1IdentityTypeChange(value: String) = updateData { it.copy(nominee_1_identity_type = value.trim().toUpperCase(Locale.current)) }
    fun onNominee1IdentityNumberChange(value: String) = updateData { it.copy(nominee_1_identity_number = value.trim().toUpperCase(Locale.current)) }
    fun onNominee1Address1Change(value: String) = updateData { it.copy(nominee_1_address1 = value.toUpperCase(Locale.current)) }
    fun onNominee1Address2Change(value: String) = updateData { it.copy(nominee_1_address2 = value.toUpperCase(Locale.current)) }
    fun onNominee1Address3Change(value: String) = updateData { it.copy(nominee_1_address3 = value.toUpperCase(Locale.current)) }
    fun onNominee1CityChange(value: String) = updateData { it.copy(nominee_1_city = value.toUpperCase(Locale.current)) }
    fun onNominee1PincodeChange(value: String) {
        if (value.length>6) return
        updateData { it.copy(nominee_1_pin = value.trim().toUpperCase(Locale.current)) }
    }
    fun onNominee1CountryChange(value: String) = updateData { it.copy(nominee_1_country = value.trim().toUpperCase(Locale.current)) }
    fun onNominee1MinorFlagChange(value: String) = updateData { it.copy(nominee_1_minor_flag = value.trim().toUpperCase(Locale.current)) }
    fun onNominee1GuardianChange(value: String) = updateData { it.copy(nominee_1_guardian = value.toUpperCase(Locale.current)) }
    fun onNominee1GuardianPanChange(value: String) = updateData { it.copy(nominee_1_guardian_pan = value.trim().toUpperCase(Locale.current)) }
    fun onNominee1ApplicableChange(value: String) = updateData { it.copy(nominee_1_applicable = value.trim().toUpperCase(Locale.current)) }
    fun onNomineeSoaChange(value: String) = updateData { it.copy(nominee_soa = value.trim().toUpperCase(Locale.current)) }

    // BANK DETAILS
    private val _visibleBankAccounts = MutableStateFlow(listOf(1))
    val visibleBankAccounts = _visibleBankAccounts.asStateFlow()
    private val _reEnteredAccountNumbers =
        MutableStateFlow(List(5) { "" })

    val reEnteredAccountNumbers = _reEnteredAccountNumbers.asStateFlow()

    val bankScreenButtonEnabled = combine(_formState, visibleBankAccounts,reEnteredAccountNumbers) { formState, visibleAccounts,reEntered ->
        println("Visible Accounts = $visibleAccounts")
        val data = (formState as? UiState.Success)?.data?.data ?: return@combine false
        data.div_pay_mode.isNotBlank() && visibleAccounts.all { index ->
            val accountValid=when (index) {
                1 -> data.account_type_1.isNotBlank() && data.account_no_1.isNotBlank() && data.ifsc_code_1.isNotBlank() && data.default_bank_flag_1.isNotBlank()
                2 -> data.account_type_2.isNotBlank() && data.account_no_2.isNotBlank() && data.ifsc_code_2.isNotBlank() && data.default_bank_flag_2.isNotBlank()
                3 -> data.account_type_3.isNotBlank() && data.account_no_3.isNotBlank() && data.ifsc_code_3.isNotBlank() && data.default_bank_flag_3.isNotBlank()
                4 -> data.account_type_4.isNotBlank() && data.account_no_4.isNotBlank() && data.ifsc_code_4.isNotBlank() && data.default_bank_flag_4.isNotBlank()
                5 -> data.account_type_5.isNotBlank() && data.account_no_5.isNotBlank() && data.ifsc_code_5.isNotBlank() && data.default_bank_flag_5.isNotBlank()
                else -> false
            }
            accountValid && !isBankAccountMismatch(index, data)
        }
    }.stateIn(scope = viewModelScope, started = WhileSubscribed(5000), initialValue = false)

    fun addBankAccount() {
        val current = _visibleBankAccounts.value
        if (current.size >= 5) return
        val next = (1..5).firstOrNull { it !in current } ?: return
        _visibleBankAccounts.value = current + next
    }

    fun removeBankAccount(index: Int) {
        if (index == 1) return
        _visibleBankAccounts.value -= index
        _reEnteredAccountNumbers.update { list ->
            list.toMutableList().apply {
                this[index - 1] = ""
            }
        }
        resetBankAccount(index)
    }

    fun isBankAccountMismatch(
        index: Int,
        data: Data
    ): Boolean {

        val original = getAccountNumber(index, data)
        val reEntered = getReEnteredAccountNumber(index)

        if (reEntered.isBlank()) return false

        return original != reEntered
    }

    fun getReEnteredAccountNumber(index: Int) =
        _reEnteredAccountNumbers.value[index - 1]

    fun onReEnteredAccountNumberChange(index: Int, value: String) {
        _reEnteredAccountNumbers.update { list ->
            list.toMutableList().apply {
                this[index - 1] = value
            }
        }
    }
    private fun resetBankAccount(index: Int) {
        updateData {
            when (index) {
                2 -> it.copy(account_type_2 = "", account_no_2 = "", ifsc_code_2 = "", micr_no_2 = "", default_bank_flag_2 = "")
                3 -> it.copy(account_type_3 = "", account_no_3 = "", ifsc_code_3 = "", micr_no_3 = "", default_bank_flag_3 = "")
                4 -> it.copy(account_type_4 = "", account_no_4 = "", ifsc_code_4 = "", micr_no_4 = "", default_bank_flag_4 = "")
                5 -> it.copy(account_type_5 = "", account_no_5 = "", ifsc_code_5 = "", micr_no_5 = "", default_bank_flag_5 = "")
                else -> it
            }
        }
    }

    fun getAccountType(index: Int, data: Data): String = when (index) {
        1 -> data.account_type_1; 2 -> data.account_type_2; 3 -> data.account_type_3; 4 -> data.account_type_4; 5 -> data.account_type_5; else -> ""
    }
    fun getAccountNumber(index: Int, data: Data): String = when (index) {
        1 -> data.account_no_1; 2 -> data.account_no_2; 3 -> data.account_no_3; 4 -> data.account_no_4; 5 -> data.account_no_5; else -> ""
    }
    fun getIfsc(index: Int, data: Data): String = when (index) {
        1 -> data.ifsc_code_1; 2 -> data.ifsc_code_2; 3 -> data.ifsc_code_3; 4 -> data.ifsc_code_4; 5 -> data.ifsc_code_5; else -> ""
    }
    fun getMicr(index: Int, data: Data): String = when (index) {
        1 -> data.micr_no_1; 2 -> data.micr_no_2; 3 -> data.micr_no_3; 4 -> data.micr_no_4; 5 -> data.micr_no_5; else -> ""
    }
    fun getDefaultBank(index: Int, data: Data): String = when (index) {
        1 -> data.default_bank_flag_1; 2 -> data.default_bank_flag_2; 3 -> data.default_bank_flag_3; 4 -> data.default_bank_flag_4; 5 -> data.default_bank_flag_5; else -> ""
    }

    fun onAccountTypeChange(index: Int, value: String) {
        when (index) { 1 -> onAccountType1Change(value); 2 -> onAccountType2Change(value); 3 -> onAccountType3Change(value); 4 -> onAccountType4Change(value); 5 -> onAccountType5Change(value) }
    }
    fun onAccountNumberChange(index: Int, value: String) {
        when (index) { 1 -> onAccountNumber1Change(value); 2 -> onAccountNumber2Change(value); 3 -> onAccountNumber3Change(value); 4 -> onAccountNumber4Change(value); 5 -> onAccountNumber5Change(value) }
    }
    fun onIfscChange(index: Int, value: String) {
        when (index) { 1 -> onIfscCode1Change(value); 2 -> onIfscCode2Change(value); 3 -> onIfscCode3Change(value); 4 -> onIfscCode4Change(value); 5 -> onIfscCode5Change(value) }
    }
    fun onMicrChange(index: Int, value: String) {
        when (index) { 1 -> onMicrNo1Change(value); 2 -> onMicrNo2Change(value); 3 -> onMicrNo3Change(value); 4 -> onMicrNo4Change(value); 5 -> onMicrNo5Change(value) }
    }
    fun onDefaultBankChange(index: Int, value: String) {
        when (index) { 1 -> onDefaultBankFlag1Change(value); 2 -> onDefaultBankFlag2Change(value); 3 -> onDefaultBankFlag3Change(value); 4 -> onDefaultBankFlag4Change(value); 5 -> onDefaultBankFlag5Change(value) }
    }

    fun onAccountType1Change(value: String) = updateData { it.copy(account_type_1 = value.trim().toUpperCase(Locale.current)) }
    fun onAccountNumber1Change(value: String) = updateData { it.copy(account_no_1 = value.trim().toUpperCase(Locale.current)) }
    fun onIfscCode1Change(value: String) = updateData { it.copy(ifsc_code_1 = value.trim().toUpperCase(Locale.current)) }
    fun onMicrNo1Change(value: String) = updateData { it.copy(micr_no_1 = value.trim().toUpperCase(Locale.current)) }
    fun onDefaultBankFlag1Change(value: String) = updateData { it.copy(default_bank_flag_1 = value.trim().toUpperCase(Locale.current)) }
    fun onAccountType2Change(value: String) = updateData { it.copy(account_type_2 = value.trim().toUpperCase(Locale.current)) }
    fun onAccountType3Change(value: String) = updateData { it.copy(account_type_3 = value.trim().toUpperCase(Locale.current)) }
    fun onAccountType4Change(value: String) = updateData { it.copy(account_type_4 = value.trim().toUpperCase(Locale.current)) }
    fun onAccountType5Change(value: String) = updateData { it.copy(account_type_5 = value.trim().toUpperCase(Locale.current)) }
    fun onAccountNumber2Change(value: String) = updateData { it.copy(account_no_2 = value.trim().toUpperCase(Locale.current)) }
    fun onAccountNumber3Change(value: String) = updateData { it.copy(account_no_3 = value.trim().toUpperCase(Locale.current)) }
    fun onAccountNumber4Change(value: String) = updateData { it.copy(account_no_4 = value.trim().toUpperCase(Locale.current)) }
    fun onAccountNumber5Change(value: String) = updateData { it.copy(account_no_5 = value.trim().toUpperCase(Locale.current)) }
    fun onIfscCode2Change(value: String) = updateData { it.copy(ifsc_code_2 = value.trim().toUpperCase(Locale.current)) }
    fun onIfscCode3Change(value: String) = updateData { it.copy(ifsc_code_3 = value.trim().toUpperCase(Locale.current)) }
    fun onIfscCode4Change(value: String) = updateData { it.copy(ifsc_code_4 = value.trim().toUpperCase(Locale.current)) }
    fun onIfscCode5Change(value: String) = updateData { it.copy(ifsc_code_5 = value.trim().toUpperCase(Locale.current)) }
    fun onMicrNo2Change(value: String) = updateData { it.copy(micr_no_2 = value.trim().toUpperCase(Locale.current)) }
    fun onMicrNo3Change(value: String) = updateData { it.copy(micr_no_3 = value.trim().toUpperCase(Locale.current)) }
    fun onMicrNo4Change(value: String) = updateData { it.copy(micr_no_4 = value.trim().toUpperCase(Locale.current)) }
    fun onMicrNo5Change(value: String) = updateData { it.copy(micr_no_5 = value.trim().toUpperCase(Locale.current)) }
    fun onDefaultBankFlag2Change(value: String) = updateData { it.copy(default_bank_flag_2 = value.trim().toUpperCase(Locale.current)) }
    fun onDefaultBankFlag3Change(value: String) = updateData { it.copy(default_bank_flag_3 = value.trim().toUpperCase(Locale.current)) }
    fun onDefaultBankFlag4Change(value: String) = updateData { it.copy(default_bank_flag_4 = value.trim().toUpperCase(Locale.current)) }
    fun onDefaultBankFlag5Change(value: String) = updateData { it.copy(default_bank_flag_5 = value.trim().toUpperCase(Locale.current)) }

    // ADDRESS DETAILS
    fun onAddress1Change(value: String) = updateData { it.copy(address_1 = value.trim().toUpperCase(Locale.current)) }
    fun onAddress2Change(value: String) = updateData { it.copy(address_2 = value.trim().toUpperCase(Locale.current)) }
    fun onAddress3Change(value: String) = updateData { it.copy(address_3 = value.trim().toUpperCase(Locale.current)) }
    fun onCityChange(value: String) = updateData { it.copy(city = value.trim().toUpperCase(Locale.current)) }
    fun onStateChange(value: String) = updateData { it.copy(state = value.trim().toUpperCase(Locale.current)) }
    fun onPincodeChange(value: String) = updateData { it.copy(pincode = value.trim().toUpperCase(Locale.current)) }
    fun onCountryChange(value: String) = updateData { it.copy(country = value.trim().toUpperCase(Locale.current)) }

    // CLIENT TYPE & DEMAT DETAILS
    val clientScreenButtonEnabled = _formState.map { formState ->
        val data = (formState as? UiState.Success)?.data?.data ?: return@map false
        val clientTypeValid = data.client_type.isNotBlank()
        val isDemat = data.client_type == ClientType.DEMAT.code
        val dematFieldsValid = if (isDemat) {
            val pmsValid = data.pms.isNotBlank()
            val defaultDpValid = data.default_dp.isNotBlank()
            val dpBranchValid = when (data.default_dp) {
                DefaultDp.CDSL.code -> data.cdsl_dpid.isNotBlank() && data.cdslcltid.isNotBlank()
                DefaultDp.NSDL.code -> data.cmbp_id.isNotBlank() && data.nsdldpid.isNotBlank() && data.nsdlcltid.isNotBlank()
                else -> false
            }
            pmsValid && defaultDpValid && dpBranchValid
        } else true
        clientTypeValid && dematFieldsValid
    }.stateIn(scope = viewModelScope, started = WhileSubscribed(5000), initialValue = false)

    fun onClientTypeChangeUi(clientType: ClientType) {
        onClientTypeChange(clientType.code)
        resetClientTypeDependentFields(clientType)
    }

    fun onDefaultDpChangeUi(defaultDp: DefaultDp) {
        onDefaultDpChange(defaultDp.code)
        resetDefaultDpDependentFields(defaultDp)
    }

    private fun resetClientTypeDependentFields(clientType: ClientType) {
        updateData { data ->
            when (clientType) {
                ClientType.PHYSICAL -> data.copy(pms = "", default_dp = "", cdsl_dpid = "", cdslcltid = "", cmbp_id = "", nsdldpid = "", nsdlcltid = "")
                ClientType.DEMAT -> data
            }
        }
    }

    private fun resetDefaultDpDependentFields(defaultDp: DefaultDp) {
        updateData { data ->
            when (defaultDp) {
                DefaultDp.CDSL -> data.copy(cmbp_id = "", nsdldpid = "", nsdlcltid = "")
                DefaultDp.NSDL -> data.copy(cdsl_dpid = "", cdslcltid = "")
            }
        }
    }

    fun onClientTypeChange(value: String) = updateData { it.copy(client_type = value.trim().toUpperCase(Locale.current)) }
    fun onPmsChange(value: String) = updateData { it.copy(pms = value.trim().toUpperCase(Locale.current)) }
    fun onDefaultDpChange(value: String) = updateData { it.copy(default_dp = value.trim().toUpperCase(Locale.current)) }
    fun onCdslDpidChange(value: String) = updateData { it.copy(cdsl_dpid = value.trim().toUpperCase(Locale.current)) }
    fun onCdslCltidChange(value: String) = updateData { it.copy(cdslcltid = value.trim().toUpperCase(Locale.current)) }
    fun onNsdlDpidChange(value: String) = updateData { it.copy(nsdldpid = value.trim().toUpperCase(Locale.current)) }
    fun onNsdlCltidChange(value: String) = updateData { it.copy(nsdlcltid = value.trim().toUpperCase(Locale.current)) }
    fun onCmbpIdChange(value: String) = updateData { it.copy(cmbp_id = value.trim().toUpperCase(Locale.current)) }

    // DECLARATIONS & FINANCE DETAILS
    val financeScreenButtonEnabled = _formState.map { formState ->
        val data = (formState as? UiState.Success)?.data?.data ?: return@map false
        val occupationValid = data.occupation_code.isNotEmpty()
        val holdingNature = data.holding_nature
        val secondHolderValid = when (holdingNature) {
            Holding.JOINT.code -> data.second_holder_first_name.isNotBlank() && data.second_holder_pan.isNotBlank() && data.second_holder_email.isNotBlank() && data.second_holder_mobile.isNotBlank() && data.second_holder_dob.isNotBlank()
            else -> true
        }
        val thirdHolderStarted = data.third_holder_first_name.isNotBlank() || data.third_holder_pan.isNotBlank() || data.third_holder_email.isNotBlank() || data.third_holder_mobile.isNotBlank() || data.third_holder_dob.isNotBlank()
        val thirdHolderValid = when (holdingNature) {
            Holding.JOINT.code -> if (!thirdHolderStarted) true else data.third_holder_first_name.isNotBlank() && data.third_holder_pan.isNotBlank() && data.third_holder_email.isNotBlank() && data.third_holder_mobile.isNotBlank() && data.third_holder_dob.isNotBlank()
            else -> true
        }
        val nominee1IsMinor = data.nominee_1_dob.isNotBlank() && isMinor(DateTimeUtils.slashDateToIsoUtc(data.nominee_1_dob))
        val nomineeGuardianValid = if (nominee1IsMinor) data.nominee_1_guardian.isNotBlank() && data.nominee_1_guardian_pan.isNotBlank() else true
        val nominationEnabled = data.nomination_opt == "Y"
        val nominationValid = if (nominationEnabled) {
            val nomineeFilled = data.nominee_1_name.isNotBlank() && data.nominee_1_relationship.isNotBlank() && data.nominee_1_identity_type.isNotBlank() && data.nominee_1_identity_number.isNotBlank() && data.nominee_1_dob.isNotBlank() && data.nominee_1_email.isNotBlank() && data.nominee_1_mobile.isNotBlank() && data.nominee_1_address1.isNotBlank() && data.nominee_1_city.isNotBlank() && data.nominee_1_pin.isNotBlank() && data.nominee_1_country.isNotBlank() && nomineeGuardianValid
            val authValid = data.nomination_authentication in listOf("W", "E", "O")
            nomineeFilled && authValid
        } else data.nomination_authentication in listOf("O", "V")
        secondHolderValid && thirdHolderValid && nominationValid && occupationValid
    }.stateIn(scope = viewModelScope, started = WhileSubscribed(5000), initialValue = false)

    fun onNominationOptChange(value: String) = updateData { it.copy(nomination_opt = value.trim().toUpperCase(Locale.current)) }
    fun onNominationAuthChange(value: String) = updateData { it.copy(nomination_authentication = value.trim().toUpperCase(Locale.current)) }
    fun onDivPayModeChange(value: String) = updateData { it.copy(div_pay_mode = value.trim().toUpperCase(Locale.current)) }
    fun onAadhaarUpdatedChange(value: String) = updateData { it.copy(aadhaar_updated = value.trim().toUpperCase(Locale.current)) }
    fun onChequeNameChange(value: String) = updateData { it.copy(cheque_name = value.trim().toUpperCase(Locale.current)) }
    fun onCommunicationModeChange(value: String) = updateData { it.copy(communication_mode = value.trim().toUpperCase(Locale.current)) }
    fun onEmailDeclFlagChange(value: String) = updateData { it.copy(email_declaration_flag = value.trim().toUpperCase(Locale.current)) }
    fun onMobileDeclFlagChange(value: String) = updateData { it.copy(mobile_declaration_flag = value.trim().toUpperCase(Locale.current)) }
    fun onPaperlessFlagChange(value: String) = updateData { it.copy(paperless_flag = value.trim().toUpperCase(Locale.current)) }
    fun onLeiNoChange(value: String) = updateData { it.copy(lei_no = value.trim().toUpperCase(Locale.current)) }
    fun onLeiValidityChange(value: String) = updateData { it.copy(lei_validity = value.trim().toUpperCase(Locale.current)) }
    fun onMapinIdChange(value: String) = updateData { it.copy(mapin_id = value.trim().toUpperCase(Locale.current)) }
    fun onSourceWealthChange(value: String) {
        updateData {
            it.copy(
                srce_wealt = value.trim(),
                occ_type = OccupationSourceOfWealthMapper.getFatcaOccupationTypeCode(value)
            )
        }
    }

    val addressScreenButtonEnabled = _formState
        .map { formState ->

            val data = (formState as? UiState.Success)?.data?.data
                ?: return@map false

            val isForeignAddress =
                TaxStatus.fromCode(data.tax_status)
                    ?.isResident
                    ?.not()
                    ?: false

            ///////////////////////////////////////////
            // ADDRESS VALIDATION
            ///////////////////////////////////////////

            val addressValid =
                if (isForeignAddress) {

                    data.foreign_address_1.isNotBlank() &&
                            data.foreign_address_city.isNotBlank() &&
                            data.foreign_address_state.isNotBlank() &&
                            data.foreign_address_pincode.isNotBlank() &&
                            data.foreign_address_country.isNotBlank()

                } else {

                    data.address_1.isNotBlank() &&
                            data.city.isNotBlank() &&
                            data.state.isNotBlank() &&
                            data.pincode.isNotBlank() &&
                            data.country.isNotBlank()
                }

            ///////////////////////////////////////////
            // KYC VALIDATION
            ///////////////////////////////////////////

            val kycValid =
                when (data.primary_holder_kyc_type) {

                    KycType.CKYC_COMPLIANT.code -> {
                        data.primary_holder_ckyc_number.isNotBlank()
                    }

                    else -> {
                        data.primary_holder_kyc_type.isNotBlank()
                    }
                }

            ///////////////////////////////////////////
            // INVESTOR ONBOARDING
            ///////////////////////////////////////////

            val onboardingValid =
                data.paperless_flag.isNotBlank()

            ///////////////////////////////////////////
            // FINAL
            ///////////////////////////////////////////

            addressValid &&
                    kycValid &&
                    onboardingValid
        }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5000),
            initialValue = false
        )
    fun onForeignAddress1Change(value: String) =
        updateData { it.copy(foreign_address_1 = value.trim().toUpperCase(Locale.current)) }

    fun onForeignAddress2Change(value: String) =
        updateData { it.copy(foreign_address_2 = value.trim().toUpperCase(Locale.current)) }

    fun onForeignAddress3Change(value: String) =
        updateData { it.copy(foreign_address_3 = value.trim().toUpperCase(Locale.current)) }

    fun onForeignCityChange(value: String) =
        updateData { it.copy(foreign_address_city = value.trim().toUpperCase(Locale.current)) }

    fun onForeignStateChange(value: String) =
        updateData { it.copy(foreign_address_state = value.trim().toUpperCase(Locale.current)) }

    fun onForeignPincodeChange(value: String) =
        updateData { it.copy(foreign_address_pincode = value.trim().toUpperCase(Locale.current)) }

    fun onForeignCountryChange(value: String) =
        updateData { it.copy(foreign_address_country = value.trim().toUpperCase(Locale.current)) }

    fun onForeignPhoneChange(value: String) =
        updateData { it.copy(foreign_address_resi_phone = value.trim().toUpperCase(Locale.current)) }

    fun onForeignFaxChange(value: String) =
        updateData { it.copy(foreign_address_fax = value.trim().toUpperCase(Locale.current)) }

    fun onForeignOfficePhoneChange(value: String) =
        updateData { it.copy(foreign_address_off_phone = value.trim().toUpperCase(Locale.current)) }

    fun onForeignOfficeFaxChange(value: String) =
        updateData { it.copy(foreign_address_off_fax = value.trim().toUpperCase(Locale.current)) }
    fun onResiPhoneChange(value: String) =
        updateData { it.copy(resi_phone = value.trim().toUpperCase(Locale.current)) }

    fun onResiFaxChange(value: String) =
        updateData { it.copy(resi_fax = value.trim().toUpperCase(Locale.current)) }

    fun onOfficePhoneChange(value: String) =
        updateData { it.copy(office_phone = value.trim().toUpperCase(Locale.current)) }

    fun onOfficeFaxChange(value: String) =
        updateData { it.copy(office_fax = value.trim().toUpperCase(Locale.current)) }

    val guardianScreenButtonEnabled = _formState
        .map { formState ->

            val data = (formState as? UiState.Success)?.data?.data
                ?: return@map false

            data.guardian_first_name.isNotBlank() &&
                    data.guardian_relation.isNotBlank() &&
                    data.guardian_dob.isNotBlank() &&
                    data.guardian_pan.isNotBlank()
        }
        .stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5000),
            initialValue = false
        )
}

fun isMinor(dobIsoUtc: String): Boolean {
    return try {
        val birthDate = Instant.parse(dobIsoUtc).toLocalDateTime(TimeZone.UTC).date
        val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
        val age = calculateAge(birthDate, today)
        age < 18
    } catch (_: Exception) { false }
}

private fun calculateAge(birthDate: LocalDate, today: LocalDate): Int {
    var age = today.year - birthDate.year
    if (today.month.number < birthDate.month.number || (today.month.number == birthDate.month.number && today.day < birthDate.day)) { age-- }
    return age
}