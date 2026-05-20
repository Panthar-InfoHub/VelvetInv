package org.sharad.velvetinvestment.data.remote.model.loan

import kotlinx.serialization.Serializable
import org.sharad.velvetinvestment.domain.models.loan.LoanDomain

@Serializable
data class UserLoanDto(
    val success: Boolean,
    val message: String,
    val data: LoanDataDto
)

@Serializable
data class LoanDataDto(
    val loans: List<LoanDomain>,
    val pagination: LoanPaginationDto
)

@Serializable
data class LoanPaginationDto(
    val total_loans: Int,
    val current_page: Int,
    val limit: Int,
    val total_pages: Int
)
