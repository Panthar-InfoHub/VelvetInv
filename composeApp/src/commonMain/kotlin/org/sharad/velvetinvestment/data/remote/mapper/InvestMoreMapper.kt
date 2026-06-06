package org.sharad.velvetinvestment.data.remote.mapper

import org.sharad.velvetinvestment.data.remote.model.investmore.InvestMoreDto
import org.sharad.velvetinvestment.data.remote.model.investmore.InvestMoreItemDto
import org.sharad.velvetinvestment.presentation.portfolio.viewmodel.LumpSumAdd

fun List<LumpSumAdd>.toInvestMoreDto(): InvestMoreDto {
    return InvestMoreDto(
        type = "LUMPSUM",
        items = this.map {
            InvestMoreItemDto(
                scheme_id = it.prod_Id,
                amount = it.amount,
                folio = it.folio
            )
        }
    )
}
