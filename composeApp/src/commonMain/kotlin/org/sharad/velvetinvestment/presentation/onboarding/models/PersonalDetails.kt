package org.sharad.velvetinvestment.presentation.onboarding.models

data class PersonalDetails(
    val fullName: String="",
    val email: String="",
    val phoneNumber: String="",
    val city:String="",
    val dob:Long?= null
)
