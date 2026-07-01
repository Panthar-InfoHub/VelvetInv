package org.sharad.velvetinvestment

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import org.sharad.velvetinvestment.shared.Navigation.BaseNavigation
import org.sharad.velvetinvestment.shared.rememberWindowSize
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import org.sharad.velvetinvestment.shared.compose.AppSnackbarHost
import org.sharad.velvetinvestment.utils.Log

@Composable
fun App() {

    val size = rememberWindowSize()
    Log("size", size.toString())

    VelvetTheme {
        Scaffold(
            containerColor = Color.White,
            snackbarHost = {
                AppSnackbarHost()
            }
        ) { paddingValues ->

            BaseNavigation(
                windowSize = size,
                pv = paddingValues
            )
        }
    }
}