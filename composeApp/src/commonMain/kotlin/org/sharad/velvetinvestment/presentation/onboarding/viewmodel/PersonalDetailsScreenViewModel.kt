package org.sharad.velvetinvestment.presentation.onboarding.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import org.sharad.velvetinvestment.presentation.onboarding.models.PersonalDetails
import org.sharad.velvetinvestment.utils.isValidEmail
import org.sharad.velvetinvestment.utils.isValidPhoneNumberInput
import org.sharad.velvetinvestment.utils.storage.AuthPrefs

class PersonalDetailsScreenViewModel(
   private val step:Int,
    private val authPrefs: AuthPrefs
): ViewModel() {

    private val _currentStep = MutableStateFlow(step)
    val currentStep = _currentStep.asStateFlow()

    private val _personalDetails=MutableStateFlow(PersonalDetails())
    val personalDetails=_personalDetails.asStateFlow()

    private val _showDateSelector=MutableStateFlow(false)
    val showDateSelector=_showDateSelector.asStateFlow()

    val buttonEnabled = combine(personalDetails) { detailsArray ->
        val details = detailsArray[0]
        details.fullName.isNotBlank() &&
                (details.email.isNotBlank() && isValidEmail(details.email)) &&
                (details.phoneNumber.isNotBlank() && details.phoneNumber.length==10) &&
                details.city.isNotBlank() &&
                details.dob != null
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )

    init {
        val phone=authPrefs.getPhoneNumber()
        if (phone!=null){
            _personalDetails.update { it.copy(phoneNumber = phone) }
        }
    }

    fun onNameChange(name:String){
        _personalDetails.update { it.copy(fullName = name) }
    }

    fun onEmailChange(email:String) {
        _personalDetails.update { it.copy(email = email) }
    }

    fun onPhoneChange(phone:String) {
        if(phone.isValidPhoneNumberInput()){
            _personalDetails.update { it.copy(phoneNumber = phone) }
        }
    }

    fun onCityChange(city:String) {
        _personalDetails.update { it.copy(city = city) }
    }

    fun onDobChange(dob: Long?) {
        if (dob==null) return
        _personalDetails.update { it.copy(dob = dob) }
    }


    fun nextStep(){
        _currentStep.value++
    }

    fun previousStep(){
        _currentStep.value--

    }

    fun onSliderChange(year:Int){
        _personalDetails.update {
            it.copy(
                retirementYear = year
            )
        }
    }

    fun showDateSelector(){
        _showDateSelector.value=true
    }

    fun hideDateSelector(){
        _showDateSelector.value=false
    }

}