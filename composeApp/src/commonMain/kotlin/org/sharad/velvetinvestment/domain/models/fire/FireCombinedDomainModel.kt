package org.sharad.velvetinvestment.domain.models.fire

data class FireCombinedDomainModel(
    val emiIncluded: FireReportDomainModel,
    val emiExcluded: FireReportDomainModel
)