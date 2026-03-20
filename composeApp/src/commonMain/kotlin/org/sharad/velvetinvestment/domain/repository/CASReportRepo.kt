package org.sharad.velvetinvestment.domain.repository

import org.sharad.velvetinvestment.data.remote.model.casreport.CASResponseDto
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

interface CASReportRepo {
    suspend fun uploadPdfFile(file: ByteArray, password:String): NetworkResponse<CASResponseDto, ErrorDomain>
}