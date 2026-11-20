package org.sharad.velvetinvestment.features.plans.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.sharad.velvetinvestment.features.core.domain.models.PurchaseMode
import org.sharad.velvetinvestment.features.plans.domain.usecases.GetMfPurchaseUseCase

/**
 * What the OTP screen hands on when a confirmed SIP takes its first installment now. The display
 * fields are already resolved, so the success screen shows exactly what the OTP screen would have.
 */
data class SipFirstPaymentHandoff(
    /** The first installment's gateway order id (`mfp_…`), polled on `GET /mf/purchase/{id}`. */
    val orderId: String,
    val paymentUrl: String,
    val mode: PurchaseMode,
    val schemeName: String,
    val amount: String,
    /** Zero when the mode has no debit day, which the success screen reads as "none". */
    val installmentDay: Int,
    val startDate: String
)

sealed interface SipFirstPaymentEffect {
    /** Open in the web view; call [SipFirstPaymentViewModel.onPaymentReturned] on the way back. */
    data class OpenPayment(val url: String) : SipFirstPaymentEffect

    data class PurchaseConfirmed(
        val mode: PurchaseMode,
        val schemeName: String,
        val amount: String,
        val installmentDay: Int,
        val startDate: String
    ) : SipFirstPaymentEffect

    data object Cancelled : SipFirstPaymentEffect
}

/**
 * Pays for a SIP's first installment, which the gateway places as a one-time order: open its
 * payment page, then read the order back until it is paid — the same check the lumpsum flow makes.
 *
 * The SIP is already confirmed by the time this screen opens, so nothing here goes back to the
 * OTP. A wait that runs out offers Check Status, which only reads the order again.
 */
class SipFirstPaymentViewModel(
    private val handoff: SipFirstPaymentHandoff,
    private val getMfPurchase: GetMfPurchaseUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        SipSetupUiState(stage = SipSetupStage.CONFIRMING_PAYMENT)
    )
    val uiState = _uiState.asStateFlow()

    private val _effect = Channel<SipFirstPaymentEffect>()
    val effect = _effect.receiveAsFlow()

    init {
        if (handoff.orderId.isBlank() || handoff.paymentUrl.isBlank()) {
            // The server always sends both when it asks for the first installment, so this is
            // only a fallback. Without them the installment can neither be paid nor checked.
            fail(PAYMENT_UNAVAILABLE_MESSAGE)
        } else {
            viewModelScope.launch {
                _effect.send(SipFirstPaymentEffect.OpenPayment(handoff.paymentUrl))
            }
        }
    }

    /** Coming back from the page proves nothing — the user may have closed it — so poll. */
    fun onPaymentReturned() = checkPayment()

    fun onCheckStatusClick() {
        if (!_uiState.value.canCheckStatus) return
        checkPayment()
    }

    fun onGoBackClick() {
        viewModelScope.launch { _effect.send(SipFirstPaymentEffect.Cancelled) }
    }

    private fun checkPayment() {
        if (handoff.orderId.isBlank()) return

        _uiState.update {
            it.copy(stage = SipSetupStage.CONFIRMING_PAYMENT, error = null, canCheckStatus = false)
        }

        viewModelScope.launch {
            when (val outcome = awaitPurchasePayment(getMfPurchase, handoff.orderId)) {
                is PurchasePaymentOutcome.Paid -> _effect.send(
                    SipFirstPaymentEffect.PurchaseConfirmed(
                        mode = handoff.mode,
                        schemeName = handoff.schemeName,
                        amount = handoff.amount,
                        installmentDay = handoff.installmentDay,
                        startDate = handoff.startDate
                    )
                )

                is PurchasePaymentOutcome.Failed ->
                    fail(outcome.reason ?: PAYMENT_FAILED_MESSAGE)

                // The order stays live on the server, so this is pending, not failed.
                is PurchasePaymentOutcome.Unsettled ->
                    fail(outcome.message ?: PAYMENT_PENDING_MESSAGE, canCheckStatus = true)
            }
        }
    }

    private fun fail(message: String, canCheckStatus: Boolean = false) {
        _uiState.update { it.copy(error = message, canCheckStatus = canCheckStatus) }
    }

    private companion object {
        const val PAYMENT_FAILED_MESSAGE =
            "Your SIP is set up, but the first installment's payment did not go through."
        const val PAYMENT_PENDING_MESSAGE =
            "We have not received your first installment's payment yet. Tap Check Status to " +
                "see if it has gone through."
        const val PAYMENT_UNAVAILABLE_MESSAGE =
            "We could not start the payment for your first installment. Please check your " +
                "orders in a moment."
    }
}
