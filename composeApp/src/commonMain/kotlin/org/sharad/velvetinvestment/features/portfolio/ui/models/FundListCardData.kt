package org.sharad.velvetinvestment.features.portfolio.ui.models

data class FundListCardData(
    val id: String,
    val icon:String,
    val fundName:String,
    val fundCategory:String,
    val amount:String,
    val fundRemark:String?=null,
    val fundType:String?=null
)