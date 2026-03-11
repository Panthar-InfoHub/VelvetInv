package org.sharad.velvetinvestment.presentation.tradingaccount.uimodel

import androidx.compose.ui.graphics.Color
import org.jetbrains.compose.resources.DrawableResource
import org.sharad.emify.core.ui.theme.redColor
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.icon__2_

data class NotificationModel(
    val color: Color= redColor,
    val icon: DrawableResource= Res.drawable.icon__2_,
    val heading: String="",
    val body: String="",
    val time: String="",
    val extraText:String?=null
)
