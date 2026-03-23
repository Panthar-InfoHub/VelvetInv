package org.sharad.velvetinvestment.data.remote.repository

import io.github.ismoy.imagepickerkmp.domain.extensions.loadBytes
import io.github.ismoy.imagepickerkmp.domain.models.PhotoResult
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import org.sharad.velvetinvestment.data.remote.mapper.toDomain
import org.sharad.velvetinvestment.data.remote.mapper.toDto
import org.sharad.velvetinvestment.data.remote.model.mfkyc.imageupload.ImageUploadDto
import org.sharad.velvetinvestment.data.remote.model.mfkyc.digilockerdetails.DigiLockerDetailsDto
import org.sharad.velvetinvestment.data.remote.model.mfkyc.imageupload.UrlUploadBodyDto
import org.sharad.velvetinvestment.data.remote.model.mfkyc.imageupload.UrlUploadResultDto
import org.sharad.velvetinvestment.data.remote.model.mfkyc.init.InitiateKycDto
import org.sharad.velvetinvestment.domain.models.mfkyc.DigiLockerDetailsDomain
import org.sharad.velvetinvestment.domain.models.mfkyc.KYCInitInfo
import org.sharad.velvetinvestment.domain.models.mfkyc.KycFormDataDomain
import org.sharad.velvetinvestment.domain.repository.MFKYCRepository
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.utils.networking.getUrl
import org.sharad.velvetinvestment.utils.networking.safeRequest
import org.sharad.velvetinvestment.utils.networking.safeUnitRequest

class MFKYCRepo(
    private val client: HttpClient
): MFKYCRepository {
    override suspend fun initiateKyc(): NetworkResponse<KYCInitInfo, ErrorDomain> {
        val response= safeRequest<InitiateKycDto> {
            client.post(getUrl("/kyc/mf-initiate")) {
            }
        }
        return when (response) {
            is NetworkResponse.Success -> {
                NetworkResponse.Success(
                    response.data.toDomain()
                )
            }
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
        }
    }

    override suspend fun getDigiLockerDetails(): NetworkResponse<DigiLockerDetailsDomain, ErrorDomain> {
        val response= safeRequest<DigiLockerDetailsDto> {
            client.post(getUrl("/kyc/mf-details")) {
            }
        }
        return when (response) {
            is NetworkResponse.Success -> {
                NetworkResponse.Success(
                    response.data.toDomain()
                )
            }
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
        }
    }

    override suspend fun uploadKYCFormData(data: KycFormDataDomain): NetworkResponse<Unit, ErrorDomain> {
        val response= safeUnitRequest {
            client.post(getUrl("/kyc/mf-update")) {
                setBody(data.toDto())
            }
        }
        return response
    }

    override suspend fun uploadImage(image: PhotoResult): NetworkResponse<String, ErrorDomain> {
        val mimeType= image.mimeType?:"image.jpeg"
        val response= safeRequest<ImageUploadDto> {
            client.post("https://persist.signzy.tech/api/files/upload") {
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            append(
                                key = "file",
                                value = image.loadBytes(),
                                headers = Headers.build {
                                    append(HttpHeaders.ContentType, mimeType)
                                    append(
                                        HttpHeaders.ContentDisposition,
                                        "form-data; name=\"file\"; filename=\"image.jpg\""
                                    )
                                }
                            )
                            append("ttl", "infinity")
                        }
                    )
                )
            }
        }
        when(response){
            is NetworkResponse.Error -> {
               return NetworkResponse.Error(response.error)
            }
            is NetworkResponse.Success->{
                val url= response.data.file.directURL
                return NetworkResponse.Success(url)
            }
        }
    }

    override suspend fun uploadSignature(signature: PhotoResult): NetworkResponse<String, ErrorDomain> {
        val mimeType= signature.mimeType?:"image.jpeg"
        val response= safeRequest<ImageUploadDto> {
            client.post("https://persist.signzy.tech/api/files/upload") {
                setBody(
                    MultiPartFormDataContent(
                        formData {
                            append(
                                key = "file",
                                value = signature.loadBytes(),
                                headers = Headers.build {
                                    append(HttpHeaders.ContentType, mimeType)
                                    append(
                                        HttpHeaders.ContentDisposition,
                                        "form-data; name=\"file\"; filename=\"image.jpg\""
                                    )
                                }
                            )
                            append("ttl", "infinity")
                        }
                    )
                )
            }
        }
        when(response){
            is NetworkResponse.Error -> {
                return NetworkResponse.Error(response.error)
            }
            is NetworkResponse.Success->{
                val url= response.data.file.directURL
                return NetworkResponse.Success(url)
            }
        }
    }

    override suspend fun uploadImageAndSignatureData(data: UrlUploadBodyDto): NetworkResponse<Unit, ErrorDomain> {
        val response= safeRequest<UrlUploadResultDto> {
            client.patch(getUrl("/kyc/mf-doc")) {
                setBody(data)
            }
        }
        return when(response){
            is NetworkResponse.Error ->{
                NetworkResponse.Error(response.error)
            }
            is NetworkResponse.Success->{
                NetworkResponse.Success(Unit)
            }
        }
    }
}