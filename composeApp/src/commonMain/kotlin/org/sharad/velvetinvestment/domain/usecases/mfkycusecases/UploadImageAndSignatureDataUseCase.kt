package org.sharad.velvetinvestment.domain.usecases.mfkycusecases

import org.sharad.velvetinvestment.data.remote.model.mfkyc.imageupload.UrlUploadBodyDto
import org.sharad.velvetinvestment.domain.repository.MFKYCRepository
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class UploadImageAndSignatureDataUseCase(
    private val repository: MFKYCRepository
) {

    suspend operator fun invoke(
        data: UrlUploadBodyDto
    ): NetworkResponse<Unit, ErrorDomain> {
        return repository.uploadImageAndSignatureData(data)
    }
}