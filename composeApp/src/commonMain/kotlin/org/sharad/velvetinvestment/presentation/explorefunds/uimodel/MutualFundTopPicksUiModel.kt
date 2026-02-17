package org.sharad.velvetinvestment.presentation.explorefunds.uimodel

data class MutualFundTopPicksUiModel(
    val icon:String,
    val name:String,
    val metadata:String,
    val returnYears:Int,
    val percentage:Double,
    val id:String
)