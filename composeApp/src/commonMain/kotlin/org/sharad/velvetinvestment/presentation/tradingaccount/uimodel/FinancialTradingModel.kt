package org.sharad.velvetinvestment.presentation.tradingaccount.uimodel

import org.sharad.velvetinvestment.presentation.tradingaccount.compose.Holding

data class FinancialTradingDetailsModel (
    val occupation :String = "",
    val holderNature : Holding= Holding.SINGLE,
    val nominationOPT:String ="",
    val checked:Boolean = false,
    val nomineeAuthentication: String = "",
    val nomineeName: String = "",
    val nomineeRelation: String = "",
    val nomineeIdentityType: String = "",
    val nomineeAadhar: String = "",
    val nomineeEmail: String = "",
    val nomineeMobile: String = "",
    val nomineeAddress1: String = "",
    val nomineeAddress2: String = "",
    val nomineeAddress3: String = "",
    val nomineeCity: String = "",
    val nomineePincode: String = "",
    val nomineeCountry: String = "",
    val nomineeSOA: String = "",
    val nomineeOptRefNo: String = "",



    val jointFullName: String = "",
    val jointRelationship: String = "",
    val jointPan: String = "",
    val jointEmail: String = "",
    val jointMobile: String = "",
    val jointPercentage: String = "",
    val jointAddress1: String = "",
    val jointAddress2: String = "",
    val jointAddress3: String = "",
    val jointCity: String = "",
    val jointPincode: String = "",
    val jointCountry: String = ""
)
