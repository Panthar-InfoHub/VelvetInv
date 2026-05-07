package org.sharad.velvetinvestment.data.remote.mapper

import org.sharad.velvetinvestment.data.remote.model.getfds.FdProduct
import org.sharad.velvetinvestment.data.remote.model.getfds.FixedDepositListDto
import org.sharad.velvetinvestment.data.remote.model.getfds.InterestRate
import org.sharad.velvetinvestment.data.remote.model.getfds.Tag
import org.sharad.velvetinvestment.domain.models.PaginatedData
import org.sharad.velvetinvestment.domain.models.fixeddeposits.FixedDepositDomain
import org.sharad.velvetinvestment.domain.models.fixeddeposits.FixedDepositTenureDomain
import org.sharad.velvetinvestment.domain.models.fixeddeposits.RiskLevel

fun FixedDepositListDto.toDomain(): PaginatedData<FixedDepositDomain> {

    val paginationData=this.data.pagination
    return PaginatedData(
        items = data.fd_products.map { it.toDomain() },

        page = paginationData.page,
        pageSize = paginationData.limit,
        totalItems = paginationData.total,
        totalPages = paginationData.totalPages,

        hasNextPage = paginationData.page < paginationData.totalPages
    )
}

fun FdProduct.toDomain(): FixedDepositDomain {
    val interestList = interest_rates.map { it.toDomain() }

    return FixedDepositDomain(
        id = id,
        bankName = issuer.full_name,
        bankLogoUrl = issuer.logo_url,
        riskLevel = tags.toRiskLevel(),
        baseInterest = interestList.maxOfOrNull { it.interestRate } ?: 0.0,
        tenures = interestList,
        bankTag = tags.firstOrNull()?.text ?: "",
        tags = tags.map { it.text }
    )
}

fun InterestRate.toDomain(): FixedDepositTenureDomain {
    return FixedDepositTenureDomain(
        tenure = TenureRangeList.fromDays(tenure_days),
        tenureDays = tenure_days,
        interestRate = interest_rate.toDoubleOrNull() ?: 0.0,
        receiveMin = 0L, // not provided
        receiveMax = 0L
    )
}
sealed class TenureRangeList {
    data class Days(val days: Int) : TenureRangeList()
    data class Years(val years: Int) : TenureRangeList()

    companion object {
        fun fromDays(days: Int): TenureRangeList {
            return if (days % 365 == 0) {
                Years(days / 365)
            } else {
                Days(days)
            }
        }
    }
}

fun List<Tag>.toRiskLevel(): RiskLevel {
    val ratingText = this.joinToString { it.text }.uppercase()

    return when {
        "AAA" in ratingText || "A1+" in ratingText -> RiskLevel.LOW
        "AA" in ratingText || "A+" in ratingText -> RiskLevel.MODERATE
        else -> RiskLevel.HIGH
    }
}