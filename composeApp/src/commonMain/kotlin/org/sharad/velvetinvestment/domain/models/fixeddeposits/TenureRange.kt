package org.sharad.velvetinvestment.domain.models.fixeddeposits

data class TenureRange(
    val min: TenureDuration,
    val max: TenureDuration
) {
    val minDays = min.totalDays
    val maxDays = max.totalDays
}