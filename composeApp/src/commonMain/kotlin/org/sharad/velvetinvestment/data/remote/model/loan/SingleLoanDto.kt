package org.sharad.velvetinvestment.data.remote.model.loan

import kotlinx.serialization.Serializable
import org.sharad.velvetinvestment.domain.models.loan.LoanDomain

@Serializable
data class SingleLoanDto(
    val success: Boolean,
    val message: String,
    val data: LoanDomain
)
