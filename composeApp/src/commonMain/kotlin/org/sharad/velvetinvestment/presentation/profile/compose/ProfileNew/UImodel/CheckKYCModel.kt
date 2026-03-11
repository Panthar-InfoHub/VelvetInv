package org.sharad.velvetinvestment.presentation.profile.compose.ProfileNew.UImodel

import org.jetbrains.compose.resources.DrawableResource
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.upgrade

data class CheckKYCModel(
  val mutualFundKYC: Boolean =false,
    val fixedDepositKyc: Boolean=true
)
