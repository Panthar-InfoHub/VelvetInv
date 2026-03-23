package org.sharad.velvetinvestment.data.remote.model.mfkyc.formsubmission

import kotlinx.serialization.Serializable

@Serializable
data class FormSubmission(
    val kyc_data: KycData
)