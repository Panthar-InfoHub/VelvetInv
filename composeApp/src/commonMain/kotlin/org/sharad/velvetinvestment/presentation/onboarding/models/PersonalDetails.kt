package org.sharad.velvetinvestment.presentation.onboarding.models

import org.sharad.velvetinvestment.utils.DateTimeUtils

data class PersonalDetails(
    val fullName: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    val city: String = "",
    val dob: Long? = null,
    val retirementYear: Int? = null
) {

    private val birthYear: Int?
        get() = dob?.let { DateTimeUtils.getYear(it) }

    val effectiveRetirementYear: Int?
        get() = retirementYear ?: birthYear?.plus(60)

    val retirementAge: Int?
        get() = birthYear?.let { by ->
            effectiveRetirementYear?.minus(by)
        }

    val savingYears: Int?
        get() = effectiveRetirementYear?.let {
            it - DateTimeUtils.getCurrentYear()
        }
}