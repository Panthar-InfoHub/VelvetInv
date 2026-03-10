package org.sharad.velvetinvestment.utils

import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.MonthNames
import kotlinx.datetime.format.Padding
import kotlinx.datetime.format.char
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

fun formatDateParts(date: LocalDate): Pair<String, String> {
    val year = date.year.toString()
    val dateFormatter = LocalDate.Format {
        dayOfWeek(DayOfWeekNames.ENGLISH_ABBREVIATED); char(','); char(' ')
        day(padding = Padding.ZERO)
        char(' ')
        monthName(MonthNames.ENGLISH_ABBREVIATED)
    }

    val formattedDate = date.format(dateFormatter)
    return year to formattedDate
}

@OptIn(ExperimentalTime::class)
fun formatMillisDateParts(millis: Long?, timeZone: TimeZone = TimeZone.currentSystemDefault()): Pair<String, String>? {
    if (millis == null) return null

    val instant = Instant.fromEpochMilliseconds(millis)
    val localDate = instant.toLocalDateTime(timeZone).date

    val year = localDate.year.toString()
    val dateFormatter = LocalDate.Format {
        dayOfWeek(DayOfWeekNames.ENGLISH_ABBREVIATED); char(','); char(' ')
        day(padding = Padding.ZERO)
        char(' ')
        monthName(MonthNames.ENGLISH_ABBREVIATED)
    }

    val formattedDate = localDate.format(dateFormatter)
    return year to formattedDate
}