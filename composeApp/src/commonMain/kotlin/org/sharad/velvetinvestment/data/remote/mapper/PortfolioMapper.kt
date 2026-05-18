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
import org.sharad.velvetinvestment.domain.models.portfolio.InvestedAmountBreakdownDomain
import org.sharad.velvetinvestment.domain.models.portfolio.PortfolioAllocationDomain
import org.sharad.velvetinvestment.domain.models.portfolio.PortfolioAllocationItemDomain
import org.sharad.velvetinvestment.domain.models.portfolio.TotalInvestmentsDomain
import org.sharad.velvetinvestment.utils.PendingAction

fun UserPortFolioDto.toDomain(): PortfolioDomain {

    val totalInvestments = data.total_investments
    val investedBreakdown = data.invested_amount_breakdown

    return PortfolioDomain(

        dashboard = PortfolioDashboardDomain(
            currentValue = totalInvestments.current_value,
            investedAmount = investedBreakdown.invested_amount,
            totalReturns = totalInvestments.total_returns.toInt(),
            returnPercent = totalInvestments.return_percent
        ),

        totalInvestments = TotalInvestmentsDomain(
            currentValue = totalInvestments.current_value,

            totalReturns = totalInvestments
                .total_returns,

            returnPercent = totalInvestments.return_percent,

            allocation = PortfolioAllocationDomain(

                mutualFunds = PortfolioAllocationItemDomain(
                    value = totalInvestments
                        .allocation
                        .mutual_funds
                        .value
                        .toDouble(),

                    percent = totalInvestments
                        .allocation
                        .mutual_funds
                        .percent
                ),

                fixedDeposits = PortfolioAllocationItemDomain(
                    value = totalInvestments
                        .allocation
                        .fixed_deposits
                        .value
                        .toDouble(),

                    percent = totalInvestments
                        .allocation
                        .fixed_deposits
                        .percent
                )
            )
        ),

        investedAmountBreakdown = InvestedAmountBreakdownDomain(
            investedAmount = investedBreakdown
                .invested_amount,

            investedItemsCount = investedBreakdown
                .invested_items_count,

            returnsAmount = investedBreakdown
                .returns_amount,

            returnsPercent = investedBreakdown
                .returns_percent
        ),

        mutualFunds = data.mutual_funds.map {
            it.toDomain()
        },

        fixedDeposits = data.fixed_deposits.map {
            it.toDomain()
        }
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
        amount = amount.toString(),
        roiAtBooking = roi.toString(),
        tenureAtBooking = tenure_days,
        fdIssuedAt = start_date,
        status = status,
        maturityAmount = maturity_amount.toString(),
        userId = "",
        userFullName = "",
        userEmail = "",
        issuerLogoUrl = issuer_logo,
        issuerDisplayName = title
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
