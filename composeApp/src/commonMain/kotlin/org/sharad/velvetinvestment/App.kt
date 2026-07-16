package org.sharad.velvetinvestment

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import com.mmk.kmpnotifier.notification.NotifierManager
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.shared.Navigation.BaseNavigation
import org.sharad.velvetinvestment.shared.rememberWindowSize
import org.sharad.velvetinvestment.shared.theme.VelvetTheme
import org.sharad.velvetinvestment.shared.compose.AppSnackbarHost
import org.sharad.velvetinvestment.utils.AppEventsController
import org.sharad.velvetinvestment.utils.Log

@Composable
fun App() {

    val scope = rememberCoroutineScope()
    val size = rememberWindowSize()
    Log("size", size.toString())

    NotifierManager.addListener(
        object : NotifierManager.Listener {
            override fun onPushNotification(title: String?, body: String?) {
                super.onPushNotification(title, body)
                scope.launch {
                    AppEventsController.sendNotificationMarkEvent()
                }
            }
        }
    )

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