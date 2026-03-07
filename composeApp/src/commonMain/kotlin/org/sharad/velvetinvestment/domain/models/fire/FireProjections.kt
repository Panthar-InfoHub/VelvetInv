package org.sharad.velvetinvestment.domain.models.fire

data class FireProjections(
    val year:Int,
    val age:Int,
    val firePercent: Double,
    val currentPortFolio:Long,
    val netOutflow:Long,
    val goals:Long,
    val fireNumber:Long
)