package org.sharad.velvetinvestment.domain.models.fire

data class FirePercentagePointDomain(
    val year: Int,
    val percentage: EmiValueDomain<Double>
)