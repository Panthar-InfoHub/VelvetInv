package org.sharad.velvetinvestment.presentation.tradingaccount.uimodel

data class AddressDetailModel(
    val Address1: String = "",
    val Address2: String = "",
    val Address3: String = "",
    val City1: String = "",
    val states: String = "",
    val Pincode: String = "",
    val Country1: String = "",
    val phone1: String = "",
    val fax: String = "",
    val officeFax: String = "",
    val officePhoneNumber:String="",
    val email1: String = "",
    val checked: Boolean = false,

    val foreignAddress: String = "",
    val foreignAddressLine2: String = "",
    val foreignAddressLine3: String = "",
    val City2: String = "",
    val states2: String = "",
    val postalCode: String = "",
    val Country2: String = "",
    val fax2: String = "",
    val officeFax2: String = "",
    val officePhoneNumber2: String = "",
    val phone3 :String ="",

    val kycType: String = "",
    val ckycType: String = "",
    val panExempt: String = "",
    val adharUpdated: String = "",
    val mapin: String = "",
    val investor: String = "",
    val leiNumber: String = ""

)