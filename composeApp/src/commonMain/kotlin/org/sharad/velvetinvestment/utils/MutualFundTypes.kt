package org.sharad.velvetinvestment.utils

sealed class MutualFundTypes {
    data class LumSum(val totalAmount:Long): MutualFundTypes()
    data class Monthly(val monthlyAmount:Long, val dueDate:String):MutualFundTypes()
}