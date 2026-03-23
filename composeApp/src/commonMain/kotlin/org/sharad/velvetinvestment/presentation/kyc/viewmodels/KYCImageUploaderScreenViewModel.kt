package org.sharad.velvetinvestment.presentation.kyc.viewmodels

import androidx.compose.material3.Snackbar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.ismoy.imagepickerkmp.domain.models.PhotoResult
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.data.remote.model.mfkyc.imageupload.UrlUploadBodyDto
import org.sharad.velvetinvestment.domain.usecases.mfkycusecases.UploadImageAndSignatureDataUseCase
import org.sharad.velvetinvestment.domain.usecases.mfkycusecases.UploadImageUseCase
import org.sharad.velvetinvestment.domain.usecases.mfkycusecases.UploadSignatureUseCase
import org.sharad.velvetinvestment.utils.Log
import org.sharad.velvetinvestment.utils.SnackBarController
import org.sharad.velvetinvestment.utils.SnackBarType
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class KYCImageUploaderScreenViewModel(
    private val uploadImageUseCase: UploadImageUseCase,
    private val uploadSignatureUseCase: UploadSignatureUseCase,
    private val uploadImageAndSignatureDataUseCase: UploadImageAndSignatureDataUseCase
): ViewModel() {

    private val _signature = MutableStateFlow<PhotoResult?>(null)
    val signature = _signature.asStateFlow()

    private val _photo = MutableStateFlow<PhotoResult?>(null)
    val photo = _photo.asStateFlow()

    private val _showSignatureSelector = MutableStateFlow(false)
    val showSignatureSelector = _showSignatureSelector.asStateFlow()

    private val _showPhotoSelector = MutableStateFlow(false)
    val showPhotoSelector = _showPhotoSelector

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    val buttonEnabled= combine(signature, photo) { signature, photo ->
        signature != null && photo != null
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false
    )


    fun onSignatureSelected(photoResult: PhotoResult){
        _signature.value = photoResult
        hideSignatureSelector()
    }

    fun onPhotoSelected(photoResult: PhotoResult) {
        _photo.value = photoResult
        hidePhotoSelector()
    }

    fun showSignatureSelector() {
        _showSignatureSelector.value = true
    }

    fun showPhotoSelector() {
        _showPhotoSelector.value = true
    }

    fun hideSignatureSelector() {
        _showSignatureSelector.value = false
    }

    fun hidePhotoSelector() {
        _showPhotoSelector.value = false
    }

    fun uploadImages(
        onSuccessfulUpload: () -> Unit
    ) {

        val photo = _photo.value ?: return
        val signature = _signature.value ?: return

        _loading.value = true

        viewModelScope.launch {

            val imageDeferred = async { uploadImageUseCase(photo) }
            val signatureDeferred = async { uploadSignatureUseCase(signature) }

            val imageResult = imageDeferred.await()
            val signatureResult = signatureDeferred.await()

            if (imageResult is NetworkResponse.Error ||
                signatureResult is NetworkResponse.Error
            ) {
                _loading.value = false
                SnackBarController.showSnackBar(
                    SnackBarType.Error("Error uploading images")
                )
                return@launch
            }

            val imageUrl = (imageResult as NetworkResponse.Success).data
            val signatureUrl = (signatureResult as NetworkResponse.Success).data

            Log("KYC", "ImageResult = $imageResult")
            Log("KYC", "SignatureResult = $signatureResult")

            val photoDto = UrlUploadBodyDto(
                type = "photo",
                img_url = imageUrl
            )

            val signatureDto = UrlUploadBodyDto(
                type = "signature",
                img_url = signatureUrl
            )


            val photoUploadResult = uploadImageAndSignatureDataUseCase(photoDto)
            val signatureUploadResult = uploadImageAndSignatureDataUseCase(signatureDto)

            if (photoUploadResult is NetworkResponse.Error ||
                signatureUploadResult is NetworkResponse.Error
            ) {
                _loading.value = false
                SnackBarController.showSnackBar(
                    SnackBarType.Error("Error submitting KYC data")
                )
                return@launch
            }

            _loading.value = false

            SnackBarController.showSnackBar(
                SnackBarType.Success("KYC uploaded successfully")
            )

            onSuccessfulUpload()
        }
    }

}