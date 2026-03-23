package org.sharad.velvetinvestment.domain.usecases.mfkycusecases

import io.github.ismoy.imagepickerkmp.domain.models.PhotoResult
import org.sharad.velvetinvestment.domain.repository.MFKYCRepository
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

class UploadImageUseCase(
    private val repository: MFKYCRepository
) {

    suspend operator fun invoke(
        image: PhotoResult
    ): NetworkResponse<String, ErrorDomain> {
        return repository.uploadImage(image)
    }
}