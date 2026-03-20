package org.sharad.velvetinvestment.data.remote.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import org.sharad.velvetinvestment.data.remote.model.casreport.CASResponseDto
import org.sharad.velvetinvestment.domain.repository.CASReportRepo
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.utils.networking.getUrl
import org.sharad.velvetinvestment.utils.networking.safeRequest

class CASRepo(
    private val client: HttpClient
) : CASReportRepo{
    override suspend fun uploadPdfFile(
        file: ByteArray,
        password: String,
    ): NetworkResponse<CASResponseDto, ErrorDomain> {
        val response= safeRequest<CASResponseDto> {
            client.post("https://api.casparser.in/v4/smart/parse") {
                headers {
                    append("X-API-KEY", "sandbox-with-json-responses")
                }
                setBody(
                    MultiPartFormDataContent(
                        formData {

                            append(
                                key = "pdf_file",
                                value = file,
                                headers = Headers.build {
                                    append(HttpHeaders.ContentType, "application/pdf")
                                    append(
                                        HttpHeaders.ContentDisposition,
                                        "form-data; name=\"pdf_file\"; filename=\"casfile\""
                                    )
                                }
                            )

                            append("password", password)
                        }
                    )
                )
            }
        }
        return when (response) {
            is NetworkResponse.Success -> {
                NetworkResponse.Success(
                    response.data
                )
            }
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
        }
    }
}