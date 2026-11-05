package org.sharad.velvetinvestment.features.portfolio.data.mapper

import org.sharad.velvetinvestment.features.portfolio.domain.models.LumpSumAdd
import org.sharad.velvetinvestment.features.portfolio.ui.models.InvestMoreDto
import org.sharad.velvetinvestment.features.portfolio.ui.models.InvestMoreItemDto

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
