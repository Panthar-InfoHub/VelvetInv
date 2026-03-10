package org.sharad.velvetinvestment.domain.models.user

data class LoginResponseDomain(
    val onBoardedComplete:Boolean,
    val onBoardingStep: Int,
    val bearerToken:String,
    val refreshToken:String,
    val userId:String
)