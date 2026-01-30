package org.sharad.velvetinvestment.presentation.LoginScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding

import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import org.sharad.velvetinvestment.utils.WindowSize

@Composable
fun LoginScreen(windowSize: WindowSize) {

    when(windowSize){
        WindowSize.PhoneLandscape -> {
            LoginScreenLandscape()
        }
        else -> {
            LoginScreenPortrait()
        }
    }
}

@Composable
fun LoginScreenPortrait() {
}

@Composable
fun LoginScreenLandscape() {
}

@Composable
fun CredentialBox(){
    Box(
        modifier = Modifier.widthIn(max=360.dp)
            .dropShadow(
                shape = RoundedCornerShape(20.dp),
                shadow = Shadow(
                    radius = 16.dp,
                    color = Color.Black.copy(25f),
                    offset = DpOffset(x = 0.dp, 4.dp)
                )
            )
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
    ){

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 20.dp, horizontal = 16.dp)
        ) {

            

        }
    }
}