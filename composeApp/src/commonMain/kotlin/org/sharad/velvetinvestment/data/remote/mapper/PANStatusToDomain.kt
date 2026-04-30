package org.sharad.velvetinvestment.data.remote.mapper

import org.sharad.velvetinvestment.data.remote.model.panverification.PANVerifyDto
import org.sharad.velvetinvestment.domain.models.user.PANVerifyDomain

fun PANVerifyDto.toDomain(): PANVerifyDomain{
    return PANVerifyDomain(
        status = this.data.pan_verified,
        message = this.message
    )
}