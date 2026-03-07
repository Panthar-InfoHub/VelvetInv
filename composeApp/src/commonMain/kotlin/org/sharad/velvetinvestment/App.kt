package org.sharad.velvetinvestment

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import org.sharad.velvetinvestment.presentation.tradingaccount.compose.TradingAccountScreen1
import org.sharad.velvetinvestment.presentation.tradingaccount.compose.TradingScreen2
import org.sharad.velvetinvestment.shared.Navigation.BaseNavigation
import org.sharad.velvetinvestment.shared.rememberWindowSize
import org.sharad.velvetinvestment.utils.Log
import org.sharad.velvetinvestment.utils.theme.VelvetTheme

@Composable
fun App() {

    val size=rememberWindowSize()

    Log("size",size.toString())
    VelvetTheme {
        Scaffold(
            containerColor = Color.White,
        ) {it->
//            BaseNavigation(windowSize=size,pv=it)
            TradingScreen2()
        }
    }
}
