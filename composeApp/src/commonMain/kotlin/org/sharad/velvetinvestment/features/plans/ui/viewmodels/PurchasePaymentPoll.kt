package org.sharad.velvetinvestment.features.plans.ui.viewmodels

import kotlinx.coroutines.delay
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.features.plans.domain.model.MfPurchase
import org.sharad.velvetinvestment.features.plans.domain.usecases.GetMfPurchaseUseCase
import kotlin.time.Duration.Companion.milliseconds

/** Where a one-time order's payment stands once the user is back from the payment page. */
sealed interface PurchasePaymentOutcome {
    data class Paid(val purchase: MfPurchase) : PurchasePaymentOutcome

    /** The debit or the order failed outright; [reason] is the gateway's, when it gave one. */
    data class Failed(val reason: String?) : PurchasePaymentOutcome

    /**
     * Nothing settled inside the poll window, or the order could not be read. The money may
     * still land, so this is not a failure — [message] is set only when a read failed.
     */
    data class Unsettled(val message: String?) : PurchasePaymentOutcome
}

/**
 * Reads the order back on `GET /mf/purchase/{id}` until it reports `SUBMITTED` *and* its payment
 * reports `SUCCESS` — the same rule the lumpsum flow applies after its payment page. Coming back
 * from the page proves nothing on its own, since the user may simply have closed it.
 */
suspend fun awaitPurchasePayment(
    getMfPurchase: GetMfPurchaseUseCase,
    purchaseId: String,
    attempts: Int = PAYMENT_POLL_ATTEMPTS,
    intervalMs: Long = PAYMENT_POLL_INTERVAL_MS
): PurchasePaymentOutcome {
    repeat(attempts) { attempt ->
        if (attempt > 0) delay(intervalMs.milliseconds)

        when (val result = getMfPurchase(purchaseId)) {
            is NetworkResponse.Error ->
                return PurchasePaymentOutcome.Unsettled(result.error.message)

            is NetworkResponse.Success -> {
                val purchase = result.data

                if (purchase.isSubmitted) {
                    if (purchase.hasPaymentFailed) {
                        return PurchasePaymentOutcome.Failed(purchase.paymentFailureReason)
                    }
                    if (purchase.isPaymentSuccessful) {
                        return PurchasePaymentOutcome.Paid(purchase)
                    }
                    // Submitted with the debit still in flight — keep waiting.
                }

                if (purchase.hasFailed) return PurchasePaymentOutcome.Failed(null)
            }
        }
    }

    return PurchasePaymentOutcome.Unsettled(null)
}

/** Five reads gives four 5-second gaps — about 20 seconds of grace, as elsewhere in the flow. */
private const val PAYMENT_POLL_ATTEMPTS = 5
private const val PAYMENT_POLL_INTERVAL_MS = 5_000L
