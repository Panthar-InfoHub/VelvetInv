package org.sharad.velvetinvestment.data.remote.model.mfkyc.digilockerdetails

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val aadhaar_pdf_url: String,
    val address_line: String,
    val city: String,
    val contract_pdf_url: String?,
    val country: String,
    val createdAt: String,
    val digilocker_photo_url: String,
    val district: String,
    val dob: String,
    val full_address: String,
    val full_name: String,
    val gender: String,
    val id: String,
    val is_final_confirmed: Boolean,
    val land_mark: String,
    val pincode: String,
    val signature_url: String?,
    val state: String,
    val uid: String,
    val updatedAt: String,
    val user_id: String,
    val user_photo_url: String?,
    val verified_at: String?
)