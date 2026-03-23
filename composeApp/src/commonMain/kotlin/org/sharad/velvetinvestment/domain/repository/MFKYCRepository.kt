package org.sharad.velvetinvestment.domain.repository

import io.github.ismoy.imagepickerkmp.domain.models.PhotoResult
import org.sharad.velvetinvestment.data.remote.model.mfkyc.imageupload.UrlUploadBodyDto
import org.sharad.velvetinvestment.domain.models.mfkyc.DigiLockerDetailsDomain
import org.sharad.velvetinvestment.domain.models.mfkyc.KYCInitInfo
import org.sharad.velvetinvestment.domain.models.mfkyc.KycFormDataDomain
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

interface MFKYCRepository {
    suspend fun initiateKyc(): NetworkResponse<KYCInitInfo, ErrorDomain>
    suspend fun getDigiLockerDetails(): NetworkResponse<DigiLockerDetailsDomain, ErrorDomain>
    suspend fun uploadKYCFormData(data: KycFormDataDomain): NetworkResponse<Unit, ErrorDomain>
    suspend fun uploadImage(image: PhotoResult): NetworkResponse<String, ErrorDomain>
    suspend fun uploadSignature(signature: PhotoResult): NetworkResponse<String, ErrorDomain>
    suspend fun uploadImageAndSignatureData(data: UrlUploadBodyDto): NetworkResponse<Unit, ErrorDomain>
}
