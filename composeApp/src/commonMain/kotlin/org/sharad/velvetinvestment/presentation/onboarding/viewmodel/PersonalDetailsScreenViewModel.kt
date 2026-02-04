package org.sharad.velvetinvestment.presentation.onboarding.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.sharad.velvetinvestment.presentation.onboarding.models.PersonalDetails
import org.sharad.velvetinvestment.utils.DateTimeUtils

class PersonalDetailsScreenViewModel(
   private val step:Int
): ViewModel() {

    private val _currentStep = MutableStateFlow(step)
    val currentStep = _currentStep.asStateFlow()

    private val _personalDetails=MutableStateFlow(PersonalDetails())
    val personalDetails=_personalDetails.asStateFlow()

    fun onNameChange(name:String){
        _personalDetails.update { it.copy(fullName = name) }
    }

    fun onEmailChange(email:String) {
        _personalDetails.update { it.copy(email = email) }
    }

    fun onPhoneChange(phone:String) {
        _personalDetails.update { it.copy(phoneNumber = phone) }
    }

    fun onCityChange(city:String) {
        _personalDetails.update { it.copy(city = city) }
    }

    fun onDobChange(dob:Long) {
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
                retirementYear = year,
                retirementAge = it.dob?.let {dobYear-> year -  DateTimeUtils.getYear(dobYear) },
                savingYears = year - DateTimeUtils.getCurrentYear()
            )
        }
    }
}