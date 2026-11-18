package org.sharad.velvetinvestment.utils.otpautofill.userconsent

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.IntentCompat
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import org.sharad.velvetinvestment.utils.otpautofill.userconsent.SmsUserConsent
import org.sharad.velvetinvestment.utils.otpautofill.userconsent.SmsUserConsentEvent
import com.google.android.gms.auth.api.phone.SmsRetriever as GoogleSmsRetriever

/**
 * SMS User Consent API. The consent dialog is an activity, so the hosting activity must call
 * [attach] from `onCreate`; the result launcher is registered there so a dialog result survives
 * configuration changes and still reaches the listener that asked for it.
 *
 * Everything here runs on the main thread (receiver callbacks, activity results, and the
 * ViewModel collecting the flow), so the mutable fields need no locking.
 */
class AndroidSmsUserConsent(
    private val context: Context
) : SmsUserConsent {

    private var consentLauncher: ActivityResultLauncher<Intent>? = null
    private var onConsentResult: ((ActivityResult) -> Unit)? = null

    fun attach(activity: ComponentActivity) {
        val launcher = activity.registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val callback = onConsentResult
            onConsentResult = null
            callback?.invoke(result)
        }
        consentLauncher = launcher

        activity.lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onDestroy(owner: LifecycleOwner) {
                if (consentLauncher === launcher) consentLauncher = null
            }
        })
    }

    override fun startListening(senderPhoneNumber: String?): Flow<SmsUserConsentEvent> =
        callbackFlow {
            var resultCallback: ((ActivityResult) -> Unit)? = null

            fun fail(message: String) {
                trySend(SmsUserConsentEvent.Error(IllegalStateException(message)))
                close()
            }

            fun showConsentDialog(consentIntent: Intent) {
                val launcher = consentLauncher
                    ?: return fail("No activity attached to show the SMS consent dialog")
                if (!isTrustedConsentIntent(consentIntent)) {
                    return fail("SMS consent intent does not point to Google Play services")
                }

                val callback: (ActivityResult) -> Unit = { result ->
                    val message = result.data
                        ?.getStringExtra(GoogleSmsRetriever.EXTRA_SMS_MESSAGE)
                    if (result.resultCode == Activity.RESULT_OK && message != null) {
                        trySend(SmsUserConsentEvent.SmsReceived(message))
                    } else {
                        trySend(SmsUserConsentEvent.ConsentDenied)
                    }
                    close()
                }
                resultCallback = callback
                onConsentResult = callback

                try {
                    launcher.launch(consentIntent)
                } catch (e: ActivityNotFoundException) {
                    onConsentResult = null
                    trySend(SmsUserConsentEvent.Error(e))
                    close()
                }
            }

            val receiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    if (intent.action != GoogleSmsRetriever.SMS_RETRIEVED_ACTION) return

                    val status = IntentCompat.getParcelableExtra(
                        intent,
                        GoogleSmsRetriever.EXTRA_STATUS,
                        Status::class.java
                    )

                    when (status?.statusCode) {
                        CommonStatusCodes.SUCCESS -> {
                            val consentIntent = IntentCompat.getParcelableExtra(
                                intent,
                                GoogleSmsRetriever.EXTRA_CONSENT_INTENT,
                                Intent::class.java
                            ) ?: return fail("SMS consent broadcast had no consent intent")
                            showConsentDialog(consentIntent)
                        }

                        CommonStatusCodes.TIMEOUT -> {
                            trySend(SmsUserConsentEvent.Timeout)
                            close()
                        }
                    }
                }
            }

            ContextCompat.registerReceiver(
                context,
                receiver,
                IntentFilter(GoogleSmsRetriever.SMS_RETRIEVED_ACTION),
                GoogleSmsRetriever.SEND_PERMISSION,
                null,
                ContextCompat.RECEIVER_EXPORTED
            )

            GoogleSmsRetriever.getClient(context)
                .startSmsUserConsent(senderPhoneNumber)
                .addOnSuccessListener {
                    trySend(SmsUserConsentEvent.ListeningStarted)
                }
                .addOnFailureListener { exception ->
                    trySend(SmsUserConsentEvent.Error(exception))
                    close()
                }

            awaitClose {
                runCatching { context.unregisterReceiver(receiver) }
                // A dialog still showing should not deliver into a closed flow.
                if (onConsentResult === resultCallback) onConsentResult = null
            }
        }

    /**
     * Guards against intent redirection: only launch the consent intent if it resolves to Google
     * Play services and does not try to grant URI permissions.
     */
    private fun isTrustedConsentIntent(intent: Intent): Boolean {
        val component = intent.resolveActivity(context.packageManager) ?: intent.component
        val grantFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION or
            Intent.FLAG_GRANT_WRITE_URI_PERMISSION
        return component?.packageName == GOOGLE_PLAY_SERVICES_PACKAGE &&
            intent.flags and grantFlags == 0
    }

    private companion object {
        const val GOOGLE_PLAY_SERVICES_PACKAGE = "com.google.android.gms"
    }
}
