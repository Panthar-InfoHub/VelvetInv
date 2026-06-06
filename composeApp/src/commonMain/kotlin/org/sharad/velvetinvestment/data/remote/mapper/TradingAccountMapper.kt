package org.sharad.velvetinvestment.data.remote.mapper

import org.sharad.velvetinvestment.data.remote.model.tradingaccount.prefilled.TradingAccountPrefilledResponseDto
import org.sharad.velvetinvestment.domain.models.tradingaccount.prefilled.TradingAccountPrefilledDomain

fun TradingAccountPrefilledResponseDto.toDomain(): TradingAccountPrefilledDomain {
    val prefilledData = this.data
    return TradingAccountPrefilledDomain(
        fullName = prefilledData.full_name,
        email = prefilledData.email,
        phoneNo = prefilledData.phone_no,
        dob = prefilledData.dob,
        gender = prefilledData.gender,
        panNo = prefilledData.pan_no,
        placeOfBirth = prefilledData.place_of_birth,
        fullAddress = prefilledData.full_address,
        uid = prefilledData.uid,
        pinCode = prefilledData.pin_code,
        city = prefilledData.city,
        district = prefilledData.district,
        state = prefilledData.state,
        country = prefilledData.country,
        martialStatus = prefilledData.martial_status,
        fatherName = prefilledData.father_name,
        motherName = prefilledData.mother_name
    )
}