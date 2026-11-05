package org.sharad.velvetinvestment.features.portfolio.domain.models

data class FDDetailsDomain(
    val bankInfo: FDBankInfoDomain,
    val investmentDetails: FDInvestmentDetailsDomain,
    val nomineeDetails: List<FDNomineeDomain>,
    val timelineDetails: FDTimelineDomain
)
