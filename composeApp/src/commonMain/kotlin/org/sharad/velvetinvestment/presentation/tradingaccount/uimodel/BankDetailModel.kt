package org.sharad.velvetinvestment.presentation.tradingaccount.uimodel

data class BankDetailModel(
    val accountType:String="",
    val accountNumber:String ="",
    val ifscCode :String ="",
    val microNumber: String ="",
    val paymentMethod:String =""
)
