package org.sharad.velvetinvestment.features.plans.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.features.core.ui.otp.OtpController
import org.sharad.velvetinvestment.features.plans.domain.usecases.RequestPurchasePlanOtpUseCase
import org.sharad.velvetinvestment.features.plans.domain.model.PurchasePlanConfirmation
import org.sharad.velvetinvestment.features.plans.domain.usecases.VerifyPurchasePlanOtpUseCase
import org.sharad.velvetinvestment.features.plans.ui.viewmodels.OTP_LENGTH
import org.sharad.velvetinvestment.features.plans.ui.viewmodels.OTP_RESEND_SECONDS
import org.sharad.velvetinvestment.features.core.domain.models.PurchaseMode
import org.sharad.velvetinvestment.utils.SnackBarController
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.utils.otpautofill.OtpAutofill

sealed interface SipPurchaseOtpEffect {
    /** The SIP is registered; what the success screen shows comes from the gateway's echo. */
    data class PurchaseConfirmed(
        val mode: PurchaseMode,
        val schemeName: String,
        val amount: String,
        /** Zero when the mode has no debit day, which the success screen reads as "none". */
        val installmentDay: Int,
        val startDate: String
    ) : SipPurchaseOtpEffect

    /**
     * The SIP is registered but takes its first installment now. Paying for it — and every
     * retry of that — runs on its own screen, so the user never comes back to this one.
     */
    data class StartFirstPayment(val handoff: SipFirstPaymentHandoff) : SipPurchaseOtpEffect

    data object Cancelled : SipPurchaseOtpEffect
}

/**
 * Confirms a SIP that is already registered and reviewed. Everything it needs travels on the
 * route, so a resend or a retry runs from here without going back through the purchase form.
 *
 * A correct OTP always leaves this screen: straight to success, or — when the gateway takes the
 * first installment now — on to the screen that pays for it.
 */
class SipPurchaseOtpViewModel(
    /** The plan's gateway id; both OTP calls are keyed on it. */
    private val planId: String,
    private val schemeName: String,
    private val amount: Int,
    private val mode: PurchaseMode,
    private val installmentDay: Int,
    private val requestPurchasePlanOtp: RequestPurchasePlanOtpUseCase,
    private val verifyPurchasePlanOtp: VerifyPurchasePlanOtpUseCase,
    private val otpAutofill: OtpAutofill
) : ViewModel() {

    /** The OTP was sent by the screen before this one, so the cooldown starts already running. */
    val otp = OtpController(
        scope = viewModelScope,
        otpLength = OTP_LENGTH,
        resendCooldownSeconds = OTP_RESEND_SECONDS
    )

    private val _uiState = MutableStateFlow(SipPurchaseOtpUiState(schemeName = schemeName))
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<SipPurchaseOtpEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        // The OTP was requested on the previous screen; start listening before it arrives.
        startOtpAutofill()
    }

    fun onOtpChange(value: String) = otp.onOtpChange(value)

    fun onBackClick() {
        viewModelScope.launch { _effect.send(SipPurchaseOtpEffect.Cancelled) }
    }

    fun onVerifyClick() {
        val state = otp.state.value
        if (!state.isSubmitEnabled) return

        viewModelScope.launch {
            val result = otp.withLoading { verifyPurchasePlanOtp(planId, state.otpValue) }

            when (result) {
                // The screen stays put on a bad OTP so the user can correct it without starting
                // over — the plan is already registered and is still awaiting this code.
                is NetworkResponse.Error -> {
                    otp.clearOtp()
                    SnackBarController.showError(result.error.message)
                }

                is NetworkResponse.Success -> onPlanConfirmed(result.data)
            }
        }
    }

    /**
     * Without `generate_first_installment_now` the mandate carries every debit, so the SIP is
     * done. With it, the first installment's order still has to be paid; that screen is also
     * where a missing order or payment link is reported.
     */
    private suspend fun onPlanConfirmed(confirmation: PurchasePlanConfirmation) {
        val plan = confirmation.plan

        // The gateway echoes the amount back, but not always; the typed amount stands in.
        val shownAmount = plan.amount.takeIf { it.isNotBlank() } ?: amount.toString()
        val shownDay = if (mode.needsInstallmentDay) plan.installmentDay ?: installmentDay else 0
        val startDate = plan.startDate.orEmpty()

        if (!confirmation.generatesFirstInstallmentNow) {
            _effect.send(
                SipPurchaseOtpEffect.PurchaseConfirmed(
                    mode = mode,
                    schemeName = schemeName,
                    amount = shownAmount,
                    installmentDay = shownDay,
                    startDate = startDate
                )
            )
            return
        }

        _effect.send(
            SipPurchaseOtpEffect.StartFirstPayment(
                SipFirstPaymentHandoff(
                    orderId = confirmation.firstInstallment?.id.orEmpty(),
                    paymentUrl = confirmation.paymentUrl.orEmpty(),
                    mode = mode,
                    schemeName = schemeName,
                    amount = shownAmount,
                    installmentDay = shownDay,
                    startDate = startDate
                )
            )
        )
    }

    fun onResendClick() {
        if (!otp.state.value.canResend) return
        // Listening is one-shot, so re-arm it before the new SMS is sent.
        startOtpAutofill()

        viewModelScope.launch {
            when (val result = otp.withLoading { requestPurchasePlanOtp(planId) }) {
                is NetworkResponse.Error -> SnackBarController.showError(result.error.message)

                is NetworkResponse.Success -> {
                    otp.clearOtp()
                    otp.startResendTimer()
                }
            }
        }
    }

    /** Fills the field from the SMS and submits it; the user can still type the code if this fails. */
    private fun startOtpAutofill() {
        otp.startSmsAutofill(otpAutofill, onOtpFilled = ::onVerifyClick)
    }
}

data class SipPurchaseOtpUiState(
    val schemeName: String = ""
)
