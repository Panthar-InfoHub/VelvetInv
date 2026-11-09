package org.sharad.velvetinvestment.features.profile.ui.model

data class PersonalInfoUiData(
    val fullName: String = "",
    val dob: String = "",
    val mobileNumber: String = "",
    val email: String = "",
    val city: String = "",
    val kycStatusMf: String = "",
    val kycStatusTrading: String = "",
)