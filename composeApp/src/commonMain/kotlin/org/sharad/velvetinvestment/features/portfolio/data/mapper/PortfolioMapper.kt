package org.sharad.velvetinvestment.features.portfolio.data.mapper

import org.sharad.velvetinvestment.utils.DateTimeUtils
import org.sharad.velvetinvestment.utils.trimDoubleTo
import org.sharad.velvetinvestment.features.portfolio.data.model.fdportfoliobyid.FDPortFolioById
import org.sharad.velvetinvestment.features.portfolio.data.model.pendingorders.PendingOrderDto
import org.sharad.velvetinvestment.features.portfolio.data.model.portfolio.FolioFundDataDto
import org.sharad.velvetinvestment.features.portfolio.domain.models.FDStatus
import org.sharad.velvetinvestment.features.portfolio.domain.models.FixedDepositTransactionDomain
import org.sharad.velvetinvestment.features.portfolio.domain.models.FolioFundDomain
import org.sharad.velvetinvestment.features.portfolio.domain.models.PendingAction
import org.sharad.velvetinvestment.features.portfolio.domain.models.PendingOrderDomain

fun FDPortFolioById.toDomain(): FixedDepositTransactionDomain {
    val data = this.data
    return FixedDepositTransactionDomain(
        id = data.id,
        userId = data.user_id,
        paymentCompletedAt = data.payment_completed_at,
        isVkycPending = data.is_vkyc_pending,
        amount = data.amount,
        roiAtBooking = data.roi_at_booking,
        tenureAtBooking = data.tenure_at_booking,
        payoutFrequency = data.payout_frequency,
        status = FDStatus.fromValue(data.status),
        maturityAmount = data.maturity_amount,
        maturityDate = data.maturity_date,
        maturityInstruction = data.maturity_instruction,
        paymentTxId = data.payment_tx_id,
        fdAccountNumber = data.fd_account_number,
        onboardedAt = data.onboarded_at,
        vkycCompletedAt = data.vkyc_completed_at,
        fdIssuedAt = data.fd_issued_at,
        refundDate = data.refund_date,
        vkycFailureReason = data.vkyc_failure_reason,
        failureReason = data.failure_reason,
        createdAt = data.createdAt,
        updatedAt = data.updatedAt,
        productId = data.product.id,
        issuerId = data.product.issuer.id,
        issuerFullName = data.product.issuer.full_name,
        issuerDisplayName = data.product.issuer.display_name,
        issuerType = data.product.issuer.issuer_type,
        issuerLogoUrl = data.product.issuer.logo_url,
        issuerBannerUrl = data.product.issuer.banner_url,
        issuerRatingText = data.product.issuer.rating_text,
        pendingAction = PendingAction.fromValue(data.pending_action)
    )
}

fun PendingOrderDto.toDomain(): PendingOrderDomain {
    return PendingOrderDomain(
        id = id ?: "",
        type = type ?: "",
        schemeName = scheme_name ?: "",
        amount = amount ?: 0.0,
        date = if (type == "SIP") {
            DateTimeUtils.formatDate(date ?: "")
        } else {
            date ?: ""
        },
        status = status ?: "",
        statusRemark = status_remark ?: "",
        amc = amc ?: "",
        frequency = frequency ?: "",
        startDate = start_date ?: "",
        icon = img_url ?: "",
    )
}

fun FolioFundDataDto.toDomain(): FolioFundDomain {
    return FolioFundDomain(
        id = id,
        mfHoldingId = mf_holding_id.orEmpty(),
        schemeId = scheme_id.orEmpty(),
        title = title.orEmpty(),
        category = category.orEmpty(),
        amount = amount ?: 0.0,
        currentValue = current_value ?: ((amount ?: 0.0) + (`return` ?: 0.0)),
        isSip = is_sip == true,
        startDate = start_date.orEmpty(),
        returnPercentage = return_percentage?.let { "${it.trimDoubleTo(2)}%" }.orEmpty(),
        `return` = `return` ?: 0.0,
        xirr = xirr?.let { "${it.trimDoubleTo(2)}%" }.orEmpty(),
        currentNav = current_nav ?: 0.0,
        avgNav = avg_nav ?: 0.0,
        folio = folio.orEmpty(),
        balanceUnits = balance_units ?: 0.0,
        imgUrl = img_url.orEmpty(),
        orderId = order_id.orEmpty(),
        actualFolio = actual_folio?.takeIf { it.isNotBlank() } ?: folio.orEmpty(),
        fpId = fp_id.orEmpty(),
        state = state.orEmpty()
    )
}
