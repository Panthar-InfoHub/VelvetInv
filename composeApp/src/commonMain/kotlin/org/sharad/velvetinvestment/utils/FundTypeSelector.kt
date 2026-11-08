package org.sharad.velvetinvestment.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.sharad.velvetinvestment.features.core.domain.models.PurchaseMode

/**
 * The buying mode picked on the investment-method screen (monthly SIP, daily SIP or lumpsum). It
 * is the default mode of every screen after it: the details screen's cart sheet, the cart's
 * opening tab and the purchase screen.
 */
object FundTypeSelector {
    private val _fundType  = MutableStateFlow<SelectedFundType>(SelectedFundType.SIP)
    val fundType = _fundType.asStateFlow()

    fun updateFundTypeToSIP() {
        _fundType.value = SelectedFundType.SIP
    }

    fun updateFundTypeToDailySIP() {
        _fundType.value = SelectedFundType.DAILY_SIP
    }

    fun updateFundTypeToLumpSum() {
        _fundType.value = SelectedFundType.LUMSUM
    }

    /** The picked mode as plans / cart / bundles know it. */
    val purchaseMode: PurchaseMode
        get() = _fundType.value.purchaseMode
}

enum class SelectedFundType(val purchaseMode: PurchaseMode) {
    /** A monthly SIP. */
    SIP(PurchaseMode.MONTHLY),
    DAILY_SIP(PurchaseMode.DAILY),
    LUMSUM(PurchaseMode.ONE_TIME);

    /** True for both SIP kinds — the cart files them under one SIP tab. */
    val isSip: Boolean
        get() = purchaseMode.isSip
}
