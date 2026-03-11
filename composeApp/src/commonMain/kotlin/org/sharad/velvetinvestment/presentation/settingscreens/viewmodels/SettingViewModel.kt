package org.sharad.velvetinvestment.presentation.settingscreens.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.sharad.velvetinvestment.presentation.settingscreens.compose.ChangePasswordScreen
import org.sharad.velvetinvestment.presentation.settingscreens.compose.NomineeDetail2Screen
import org.sharad.velvetinvestment.presentation.settingscreens.modelUI.ChangePasswordModel
import org.sharad.velvetinvestment.presentation.settingscreens.modelUI.NomineeDetail2Model
import org.sharad.velvetinvestment.presentation.settingscreens.modelUI.VerifyOTPModel

class SettingViewModel: ViewModel() {
    private val _nomineeDetail2Model = MutableStateFlow(NomineeDetail2Model())
    val nomineeDetail2Model = _nomineeDetail2Model.asStateFlow()
    fun updateFullName(value:String){
        _nomineeDetail2Model.update {
            it.copy(fullName = value)
        }
    }
    fun updateRelationship(value:String){
        _nomineeDetail2Model.update {
            it.copy(relationship = value)
        }
    }
    fun updateDob(value:String){
        _nomineeDetail2Model.update {
            it.copy(dob = value)
        }
    }
    fun updateNomineeId(value:String){
        _nomineeDetail2Model.update {
            it.copy(nomineeId = value)
        }
    }
    fun updateMobileNumber(value:String){
        _nomineeDetail2Model.update {
            it.copy(mobileNumber = value)
        }
    }
    fun updateEmail(value:String){
        _nomineeDetail2Model.update {
            it.copy(email = value)
        }
    }

    private val _verifyOtpModel =MutableStateFlow(VerifyOTPModel())
    val verifyOtpModel =_verifyOtpModel.asStateFlow()

    fun verifyOTP(value:Int){
        _verifyOtpModel.update {
            it.copy(verifyOtp = value)
        }
    }

    private val _changePasswordModel = MutableStateFlow(ChangePasswordModel())
    val changePasswordModel =_changePasswordModel.asStateFlow()
    fun updateNewPassword(value:String){
        _changePasswordModel.update {
            it.copy(newPassword = value)
        }
    }
    fun updateConfirmPassword(value:String){
        _changePasswordModel.update {
            it.copy(confirmPassword = value)
        }
    }
}