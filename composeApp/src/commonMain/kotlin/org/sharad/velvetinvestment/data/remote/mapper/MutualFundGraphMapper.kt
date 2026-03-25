package org.sharad.velvetinvestment.data.remote.mapper

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.sharad.velvetinvestment.data.remote.model.mfgraph.MFGraphDto
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundGraphDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundGraphPointsDomain
import kotlin.time.Instant

fun MFGraphDto.toDomain(): MutualFundGraphDomain{
    val d= this.data
    return MutualFundGraphDomain(
        graphPoints = d.reversed().map {
            val navDouble = it.nav.toDouble()
            val formattedNav = navDouble.toTwoDecimalString()
            val formattedDate = it.nav_date.toReadableDate()

            MutualFundGraphPointsDomain(
                navValue = navDouble,
                date = it.nav_date,
                label = "NAV $formattedNav | $formattedDate"
            )
        }
    )
}

fun Double.toTwoDecimalString(): String {
    val scaled = (this * 100).toInt()
    val integerPart = scaled / 100
    val decimalPart = scaled % 100

    return if (decimalPart == 0) {
        integerPart.toString()
    } else {
        "$integerPart.${decimalPart.toString().padStart(2, '0')}"
    }
}

fun String.toReadableDate(): String {
    val instant = Instant.parse(this)
    val date = instant.toLocalDateTime(TimeZone.UTC).date

    val day = date.day
    val month = date.month.name.lowercase().replaceFirstChar { it.uppercase() }.take(3)
    val year = date.year

    return "${day.withSuffix()} $month $year"
}

fun Int.withSuffix(): String {
    return when {
        this % 100 in 11..13 -> "${this}th"
        this % 10 == 1 -> "${this}st"
        this % 10 == 2 -> "${this}nd"
        this % 10 == 3 -> "${this}rd"
        else -> "${this}th"
    }
}