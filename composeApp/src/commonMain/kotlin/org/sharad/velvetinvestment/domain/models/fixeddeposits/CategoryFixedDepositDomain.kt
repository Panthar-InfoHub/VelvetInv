package org.sharad.velvetinvestment.domain.models.fixeddeposits

data class CategoryFixedDepositDomain(
    val categoryName: String,
    val categorySearchReference: String,
    val fixedDeposits: List<FixedDepositDomain>
)