package org.sharad.velvetinvestment.shared.theme


import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

data class VelvetShapes(
    val roundedDp20: Shape = RoundedCornerShape(20.dp),
    val roundedDp15: Shape = RoundedCornerShape(15.dp),
    val roundedDp12: Shape = RoundedCornerShape(12.dp),
    val roundedDp10: Shape = RoundedCornerShape(10.dp),
    val roundedDp8: Shape = RoundedCornerShape(8.dp),
    val circle: Shape = CircleShape
)

val LocalVelvetShapes = staticCompositionLocalOf<VelvetShapes> {
    VelvetShapes()
}