package org.sharad.velvetinvestment.domain.models.fire

data class EmiValueDomain<T>(
    val includeEmi: T,
    val excludeEmi: T
)