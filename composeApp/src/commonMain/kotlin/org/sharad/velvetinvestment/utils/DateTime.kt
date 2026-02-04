package org.sharad.velvetinvestment.utils

import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours
import kotlin.time.Instant


object DateTimeUtils {

    /**
     * Get the current year as an integer
     * @return Current year (e.g., 2026)
     */
    fun getCurrentYear(): Int {
        return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).year
    }

    fun getYear(epochMillis: Long):Int{
        return Instant.fromEpochMilliseconds(epochMillis).toLocalDateTime(TimeZone.currentSystemDefault()).year
    }
    /**
     * Get current time in epoch milliseconds
     * @return Current time as epoch millis
     */
    fun getCurrentEpochMillis(): Long {
        return Clock.System.now().toEpochMilliseconds()
    }

    /**
     * Get current time in epoch seconds
     * @return Current time as epoch seconds
     */
    fun getCurrentEpochSeconds(): Long {
        return Clock.System.now().epochSeconds
    }

    /**
     * Convert epoch milliseconds to LocalDateTime
     * @param epochMillis The epoch milliseconds to convert
     * @param timeZone The timezone (defaults to system default)
     * @return LocalDateTime representation
     */
    fun epochMillisToLocalDateTime(
        epochMillis: Long,
        timeZone: TimeZone = TimeZone.currentSystemDefault()
    ): LocalDateTime {
        return Instant.fromEpochMilliseconds(epochMillis)
            .toLocalDateTime(timeZone)
    }

    /**
     * Convert LocalDateTime to epoch milliseconds
     * @param dateTime The LocalDateTime to convert
     * @param timeZone The timezone (defaults to system default)
     * @return Epoch milliseconds
     */
    fun localDateTimeToEpochMillis(
        dateTime: LocalDateTime,
        timeZone: TimeZone = TimeZone.currentSystemDefault()
    ): Long {
        return dateTime.toInstant(timeZone).toEpochMilliseconds()
    }

    /**
     * Get the start of today in epoch milliseconds
     * @param timeZone The timezone (defaults to system default)
     * @return Epoch millis for start of today (00:00:00)
     */
    fun getStartOfTodayEpochMillis(timeZone: TimeZone = TimeZone.currentSystemDefault()): Long {
        val today = Clock.System.now().toLocalDateTime(timeZone).date
        val startOfDay = today.atStartOfDayIn(timeZone)
        return startOfDay.toEpochMilliseconds()
    }

    /**
     * Get the end of today in epoch milliseconds
     * @param timeZone The timezone (defaults to system default)
     * @return Epoch millis for end of today (23:59:59.999)
     */
    fun getEndOfTodayEpochMillis(timeZone: TimeZone = TimeZone.currentSystemDefault()): Long {
        val today = Clock.System.now().toLocalDateTime(timeZone).date
        val endOfDay = today.plus(1, DateTimeUnit.DAY).atStartOfDayIn(timeZone)
            .minus(1, DateTimeUnit.MILLISECOND)
        return endOfDay.toEpochMilliseconds()
    }

    /**
     * Add days to an epoch millis timestamp
     * @param epochMillis The base timestamp
     * @param days Number of days to add (can be negative)
     * @return New epoch millis after adding days
     */
    fun addDaysToEpochMillis(epochMillis: Long, days: Int): Long {
        val instant = Instant.fromEpochMilliseconds(epochMillis)
        return instant.plus(days.days).toEpochMilliseconds()
    }

    /**
     * Add hours to an epoch millis timestamp
     * @param epochMillis The base timestamp
     * @param hours Number of hours to add (can be negative)
     * @return New epoch millis after adding hours
     */
    fun addHoursToEpochMillis(epochMillis: Long, hours: Int): Long {
        val instant = Instant.fromEpochMilliseconds(epochMillis)
        return instant.plus(hours.hours).toEpochMilliseconds()
    }

    /**
     * Calculate difference between two epoch millis timestamps in days
     * @param startEpochMillis The earlier timestamp
     * @param endEpochMillis The later timestamp
     * @return Number of days between timestamps
     */
    fun getDaysBetween(startEpochMillis: Long, endEpochMillis: Long): Long {
        val start = Instant.fromEpochMilliseconds(startEpochMillis)
        val end = Instant.fromEpochMilliseconds(endEpochMillis)
        val duration = end - start
        return duration.inWholeDays
    }

    /**
     * Calculate difference between two epoch millis timestamps in hours
     * @param startEpochMillis The earlier timestamp
     * @param endEpochMillis The later timestamp
     * @return Number of hours between timestamps
     */
    fun getHoursBetween(startEpochMillis: Long, endEpochMillis: Long): Long {
        val start = Instant.fromEpochMilliseconds(startEpochMillis)
        val end = Instant.fromEpochMilliseconds(endEpochMillis)
        val duration = end - start
        return duration.inWholeHours
    }

    /**
     * Get year from epoch millis
     * @param epochMillis The timestamp
     * @param timeZone The timezone (defaults to system default)
     * @return Year as integer
     */
    fun getYearFromEpochMillis(
        epochMillis: Long,
        timeZone: TimeZone = TimeZone.currentSystemDefault()
    ): Int {
        return epochMillisToLocalDateTime(epochMillis, timeZone).year
    }

    /**
     * Check if a given epoch millis is today
     * @param epochMillis The timestamp to check
     * @param timeZone The timezone (defaults to system default)
     * @return True if the timestamp is today
     */
    fun isToday(epochMillis: Long, timeZone: TimeZone = TimeZone.currentSystemDefault()): Boolean {
        val date = epochMillisToLocalDateTime(epochMillis, timeZone).date
        val today = Clock.System.now().toLocalDateTime(timeZone).date
        return date == today
    }

    /**
     * Get current LocalDateTime
     * @param timeZone The timezone (defaults to system default)
     * @return Current LocalDateTime
     */
    fun now(timeZone: TimeZone = TimeZone.currentSystemDefault()): LocalDateTime {
        return Clock.System.now().toLocalDateTime(timeZone)
    }

    /**
     * Get current LocalDate
     * @param timeZone The timezone (defaults to system default)
     * @return Current LocalDate
     */
    fun today(timeZone: TimeZone = TimeZone.currentSystemDefault()): LocalDate {
        return Clock.System.now().toLocalDateTime(timeZone).date
    }
}
