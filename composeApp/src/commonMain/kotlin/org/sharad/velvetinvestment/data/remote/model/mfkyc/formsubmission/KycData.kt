package org.sharad.velvetinvestment.data.remote.model.mfkyc.formsubmission

import kotlinx.serialization.Serializable

@Serializable
data class KycData(
    val aadhaarNumber: String,
    val applicationStatusCode: String,
    val applicationStatusDescription: String,
    val citizenshipCountry: String,
    val citizenshipCountryCode: String,
    val communicationAddressCode: String,
    val communicationAddressType: String,
    val countryCode: Int,
    val dob: String,
    val emailId: String,
    val fatherName: String,
    val fatherTitle: String,
    val gender: String,
    val kycAccountCode: String,
    val kycAccountDescription: String,
    val maritalStatus: String,
    val mobileNumber: String,
    val motherName: String,
    val motherTitle: String,
    val name: String,
    val nomineeRelationShip: String,
    val occupationCode: String,
    val occupationDescription: String,
    val panNumber: String,
    val permanentAddressCode: String,
    val permanentAddressType: String,
    val placeOfBirth: String,
    val residentialStatus: String
)