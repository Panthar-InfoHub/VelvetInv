package org.sharad.velvetinvestment.domain.models.portfolio

data class FDDetailsDomain(
    val bankInfo: FDBankInfoDomain,
    val investmentDetails: FDInvestmentDetailsDomain,
    val nomineeDetails: List<FDNomineeDomain>,
    val timelineDetails: FDTimelineDomain
)
