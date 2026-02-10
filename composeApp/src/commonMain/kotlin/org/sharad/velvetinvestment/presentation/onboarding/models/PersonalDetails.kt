package org.sharad.velvetinvestment.presentation.onboarding.models

import org.sharad.velvetinvestment.utils.DateTimeUtils

data class PersonalDetails(
    val fullName: String="",
    val email: String="",
    val phoneNumber: String="",
    val city:String="",
    val dob:Long?= null,
    val retirementYear:Int= DateTimeUtils.getCurrentYear(),
    val retirementAge:Int?= dob?.let { retirementYear -  DateTimeUtils.getYear(it) },
    val savingYears:Int?= retirementYear-DateTimeUtils.getCurrentYear()
)
