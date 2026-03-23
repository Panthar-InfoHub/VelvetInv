package org.sharad.velvetinvestment.presentation.kyc.uistate

import org.sharad.velvetinvestment.domain.models.mfkyc.DigiLockerDetailsDomain
import org.sharad.velvetinvestment.domain.models.mfkyc.KycFormDataDomain

fun DigiLockerDetailsDomain.toFormState(): KycFormUiState {
    return KycFormUiState(
        gender = if (gender == "MALE") "M" else "F",
        aadhaarNumber = uid,
        name = full_name,
        dob = dob
    )
}

fun KycFormUiState.toDomain(): KycFormDataDomain {
    return KycFormDataDomain(
        aadhaarNumber = aadhaarNumber,
        applicationStatusCode = applicationStatusCode,
        applicationStatusDescription = applicationStatusDescription,
        citizenshipCountry = citizenshipCountry,
        citizenshipCountryCode = citizenshipCountryCode,
        communicationAddressCode = communicationAddressCode,
        communicationAddressType = communicationAddressType,
        countryCode = countryCode,
        dob = dob,
        emailId = emailId,
        fatherName = fatherName,
        fatherTitle = fatherTitle,
        gender = gender,
        kycAccountCode = kycAccountCode ?: "",
        kycAccountDescription = kycAccountDescription,
        maritalStatus = maritalStatus,
        mobileNumber = mobileNumber,
        motherName = motherName,
        motherTitle = motherTitle,
        name = name,
        nomineeRelationShip = nomineeRelationShip,
        occupationCode = occupationCode ?: "",
        occupationDescription = occupationDescription,
        panNumber = panNumber,
        permanentAddressCode = permanentAddressCode,
        permanentAddressType = permanentAddressType,
        placeOfBirth = placeOfBirth,
        residentialStatus = residentialStatus
    )
}