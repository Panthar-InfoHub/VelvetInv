package org.sharad.velvetinvestment.features.portfolio.data.mapper

import org.sharad.velvetinvestment.utils.isoUtcToDisplayDate
import org.sharad.velvetinvestment.features.portfolio.data.model.purchaseplan.MfPurchasePlanDto
import org.sharad.velvetinvestment.features.portfolio.data.model.purchaseplan.MfPurchasePlansDto
import org.sharad.velvetinvestment.features.portfolio.domain.models.ActiveSipDomain
import org.sharad.velvetinvestment.features.portfolio.domain.models.ActiveSipItemDomain

/**
 * The purchase plans behind the Active SIP tab, split by frequency.
 *
 * Nothing is filtered here: the request asks the server for `state=ACTIVE`, and every plan it
 * returns is shown.
 */
fun MfPurchasePlansDto.toActiveSipDomain(): ActiveSipDomain {
    val plans = data?.purchase_plans.orEmpty().map { it.toDomain() }

    val (daily, rest) = plans.partition { it.frequency.equals("daily", ignoreCase = true) }

    return ActiveSipDomain(
        totalInstallmentAmount = plans.sumOf { it.installmentAmount },
        monthlySips = rest,
        dailySips = daily
    )
}

fun MfPurchasePlanDto.toDomain(): ActiveSipItemDomain = ActiveSipItemDomain(
    id = id,
    fundName = mf_product?.name.orEmpty(),
    fundCategory = mf_product?.scheme_plan?.fund_category.orEmpty().toDisplayCase(),
    fundType = mf_product?.scheme_plan?.sub_category.orEmpty().toDisplayCase(),
    installmentAmount = amount?.toDoubleOrNull() ?: 0.0,
    frequency = frequency.orEmpty().toDisplayCase(),
    nextDueDate = next_installment_date?.takeIf { it.isNotBlank() }?.isoUtcToDisplayDate().orEmpty(),
    folioNumber = folio_number.orEmpty(),
    isin = mf_product?.isin ?: scheme.orEmpty(),
    totalInstallments = number_of_installments,
    remainingInstallments = remaining_installments,
    latestNav = mf_product?.latest_nav?.toDoubleOrNull() ?: 0.0,
    iconUrl = mf_product?.img_url.orEmpty()
)

/** "EQUITY FUND" and "monthly" both arrive from the gateway; the screen wants "Equity Fund". */
private fun String.toDisplayCase(): String = split(' ')
    .filter { it.isNotBlank() }
    .joinToString(" ") { word -> word.lowercase().replaceFirstChar { it.uppercase() } }
