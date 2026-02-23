package org.sharad.velvetinvestment.domain.models.mutualfunds

data class MutualFundDomain(
    val id:String,
    val name:String,
    val icon:String,
    val category:String,
    val amount:String,
    val remark:String?=null,
    val rating:Int?=null,
    val returnYear:Int,
    val type:String,
    val percentage:Double
)
