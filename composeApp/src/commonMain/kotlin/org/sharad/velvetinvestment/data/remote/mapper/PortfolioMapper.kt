package org.sharad.velvetinvestment.data.remote.mapper

import org.sharad.velvetinvestment.data.remote.model.fdportfoliobyid.FDPortFolioById
import org.sharad.velvetinvestment.data.remote.model.portfolio.UserPortFolioDto
import org.sharad.velvetinvestment.domain.models.portfolio.FDStatus
import org.sharad.velvetinvestment.data.remote.model.portfolio.MutualFund as MutualFundDto
import org.sharad.velvetinvestment.data.remote.model.portfolio.FdTransaction as FdTransactionDto
import org.sharad.velvetinvestment.domain.models.portfolio.PortfolioDomain
import org.sharad.velvetinvestment.domain.models.portfolio.PortfolioDashboardDomain
import org.sharad.velvetinvestment.domain.models.portfolio.MutualFundPortfolioDomain
import org.sharad.velvetinvestment.domain.models.portfolio.FixedDepositPortfolioDomain
import org.sharad.velvetinvestment.domain.models.portfolio.FixedDepositTransactionDomain
import org.sharad.velvetinvestment.utils.PendingAction

fun UserPortFolioDto.toDomain(): PortfolioDomain {
    val investmentData = data.investment_data
    return PortfolioDomain(
        dashboard = PortfolioDashboardDomain(
            currentValue = investmentData.current_value,
            investedAmount = investmentData.invested_amount,
            totalReturns = investmentData.total_returns.toInt(),
            returnPercent = investmentData.return_percent ?: 0.0
        ),
        mutualFunds = data.mutual_funds.map { it.toDomain() },
        fixedDeposits = data.user_fd.fd_transactions.map { it.toDomain() }
    )
}

fun MutualFundDto.toDomain(): MutualFundPortfolioDomain {
    return MutualFundPortfolioDomain(
        id = id,
        title = title,
        category = category,
        amount = amount,
        isSip = is_sip,
        startDate = start_date,
        returnPercentage = return_percentage,
        returnAmount = `return`,
        xirr = xirr,
        currentNav = current_nav,
        avgNav = avg_nav,
        folio = folio,
        balanceUnits = balance_units,
        icon = img_url ?: ""
    )
}

fun FdTransactionDto.toDomain(): FixedDepositPortfolioDomain {
    return FixedDepositPortfolioDomain(
        id = id,
        amount = amount,
        roiAtBooking = roi_at_booking,
        tenureAtBooking = tenure_at_booking,
        fdIssuedAt = fd_issued_at,
        status = status,
        maturityAmount = maturity_amount,
        userId = user.id,
        userFullName = user.full_name,
        userEmail = user.email,
        issuerLogoUrl = product.issuer.logo_url,
        issuerDisplayName = product.issuer.display_name
    )
}

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
