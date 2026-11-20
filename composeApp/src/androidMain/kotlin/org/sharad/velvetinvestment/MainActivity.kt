package org.sharad.velvetinvestment

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.ui.graphics.Color
import org.koin.android.ext.android.inject
import org.sharad.velvetinvestment.utils.otpautofill.userconsent.AndroidSmsUserConsent

// FragmentActivity (a ComponentActivity) because BiometricPrompt needs one — see rememberBiometricAuthenticator.
class MainActivity : FragmentActivity() {
    private val smsUserConsent: AndroidSmsUserConsent by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.light(
                scrim = Color.Transparent.hashCode(),
                darkScrim = Color.Transparent.hashCode()
            )
        )
        super.onCreate(savedInstanceState)
        // Must be registered before the activity is STARTED.
        smsUserConsent.attach(this)

        setContent {
            App()
        }
    }
}
