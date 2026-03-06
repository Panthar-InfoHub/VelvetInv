package org.sharad.velvetinvestment.presentation.tradingaccount.uimodel

data class BasicDetailsUIModel(
    val clientId:String="",
    val firstName:String="",
    val middleName:String="",
    val lastName:String="",
    val taxStatus: String ="",
    val gender:String="",
    val dob:String="",
    val email:String="",
    val phone:String="",
)
