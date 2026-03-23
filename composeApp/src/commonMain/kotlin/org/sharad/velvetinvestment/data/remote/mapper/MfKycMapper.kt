package org.sharad.velvetinvestment.data.remote.mapper

import org.sharad.velvetinvestment.data.remote.model.mfkyc.digilockerdetails.DigiLockerDetailsDto
import org.sharad.velvetinvestment.data.remote.model.mfkyc.formsubmission.FormSubmission
import org.sharad.velvetinvestment.data.remote.model.mfkyc.formsubmission.KycData
import org.sharad.velvetinvestment.data.remote.model.mfkyc.init.InitiateKycDto
import org.sharad.velvetinvestment.domain.models.mfkyc.DigiLockerDetailsDomain
import org.sharad.velvetinvestment.domain.models.mfkyc.KYCInitInfo
import org.sharad.velvetinvestment.domain.models.mfkyc.KycFormDataDomain

fun DigiLockerDetailsDto.toDomain(): DigiLockerDetailsDomain {
    val data = this.data

    return DigiLockerDetailsDomain(
        aadhaar_pdf_url = data.aadhaar_pdf_url,
        address_line = data.address_line,
        city = data.city,
        contract_pdf_url = data.contract_pdf_url,
        country = data.country,
        createdAt = data.createdAt,
        digilocker_photo_url = data.digilocker_photo_url,
        district = data.district,
        dob = data.dob,
        full_address = data.full_address,
        full_name = data.full_name,
        gender = data.gender,
        id = data.id,
        is_final_confirmed = data.is_final_confirmed,
        land_mark = data.land_mark,
        pincode = data.pincode,
        signature_url = data.signature_url,
        state = data.state,
        uid = data.uid,
        updatedAt = data.updatedAt,
        user_id = data.user_id,
        user_photo_url = data.user_photo_url,
        verified_at = data.verified_at
    )
}

fun InitiateKycDto.toDomain(): KYCInitInfo {
    val data = this.data
    return KYCInitInfo(
        digilocker_url = data.digilocker_url,
        kyc_access_token = data.kyc_access_token,
        kyc_type_id = data.kyc_type_id,
        session_id = data.session_id,
        user_id = data.user_id
    )
}

fun KycFormDataDomain.toDto(): FormSubmission {
    return FormSubmission(
        kyc_data = KycData(
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
            kycAccountCode = kycAccountCode,
            kycAccountDescription = kycAccountDescription,
            maritalStatus = maritalStatus,
            mobileNumber = mobileNumber,
            motherName = motherName,
            motherTitle = motherTitle,
            name = name,
            nomineeRelationShip = nomineeRelationShip,
            occupationCode = occupationCode,
            occupationDescription = occupationDescription,
            panNumber = panNumber,
            permanentAddressCode = permanentAddressCode,
            permanentAddressType = permanentAddressType,
            placeOfBirth = placeOfBirth,
            residentialStatus = residentialStatus
        )
    )
}