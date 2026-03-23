package org.sharad.velvetinvestment.presentation.onboarding.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.data.remote.model.casreport.CASResponseDto
import org.sharad.velvetinvestment.data.remote.repository.CASRepo
import org.sharad.velvetinvestment.utils.SharedDocument
import org.sharad.velvetinvestment.utils.SnackBarController
import org.sharad.velvetinvestment.utils.SnackBarType
import org.sharad.velvetinvestment.utils.networking.onError
import org.sharad.velvetinvestment.utils.networking.onSuccess

class CASParserViewModel(
    private val casRepo: CASRepo

) : ViewModel(){

    private val _casFile= MutableStateFlow<SharedDocument?>(null)
    val casFile=_casFile.asStateFlow()

    private val _password= MutableStateFlow("")
    val password=_password.asStateFlow()

    private val _casLoading= MutableStateFlow(false)
    val casLoadingState= _casLoading.asStateFlow()

    fun onFileChange(value:SharedDocument?){
        _casFile.value=value
        _password.value=""
    }

    fun onPasswordChange(value:String){
        _password.value=value
    }


    fun uploadPdfFile(onSuccess: (CASResponseDto)->Unit){
        val byteArray= casFile.value?.toByteArray()
        if (byteArray==null){
            viewModelScope.launch{
                SnackBarController.showSnackBar(SnackBarType.Error("No File Selected"))
            }
            return
        }
        if (_password.value.isEmpty()) return
        viewModelScope.launch {
            _casLoading.value=true
            casRepo.uploadPdfFile(byteArray, _password.value.trim())
                .onSuccess {
                    _casLoading.value=false
                    _casFile.value=null
                    _password.value=""
                    onSuccess(it)
                    SnackBarController.showSnackBar(SnackBarType.Success("Data Updated Successfully"))
                }
                .onError {
                    _casLoading.value=false
                    SnackBarController.showSnackBar(SnackBarType.Error("Incorrect Password"))
                }
        }
    }

}