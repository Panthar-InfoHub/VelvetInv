package org.sharad.velvetinvestment.utils.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

import org.jetbrains.compose.resources.Font
import velvet.composeapp.generated.resources.Res
import velvet.composeapp.generated.resources.poppins_bold
import velvet.composeapp.generated.resources.poppins_medium
import velvet.composeapp.generated.resources.poppins_regular
import velvet.composeapp.generated.resources.poppins_semibold


val Poppins @Composable get() = FontFamily(
    Font(
        resource = Res.font.poppins_regular,
        weight = FontWeight.Normal
    ),
    Font(
        resource = Res.font.poppins_medium,
        weight = FontWeight.Medium
    ),
    Font(
        resource = Res.font.poppins_bold,
        weight = FontWeight.Bold
    ),
    Font(
        resource = Res.font.poppins_semibold,
        weight = FontWeight.SemiBold
    ),
    Font(
        resource = Res.font.poppins_medium,
        weight = FontWeight.Medium
    )
)

val Typography @Composable get() = Typography(
    ///////////////////////////
    headlineLarge = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp
    ),


    headlineMedium = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp
    ),

    headlineSmall = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    ),


    bodyMedium = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp
    ),

    //////////////////////////////////////////
    bodySmall = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),

    displayLarge = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Medium,
        fontSize = 16.sp,
        color = Color(0xFF909090)
    ),

    displayMedium = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        color = Color(0xFF909090)
    ),

    displaySmall = TextStyle(
        fontFamily = Poppins,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        color = Color(0xFF909090)
    ),

)

val buttonTextStyle @Composable get() = TextStyle(
    fontFamily = Poppins,
    fontWeight = FontWeight.Medium,
    fontSize = 18.sp,
)