package org.sharad.velvetinvestment.data.remote.mapper

import org.sharad.velvetinvestment.data.remote.model.casreport.AccountDetail
import org.sharad.velvetinvestment.data.remote.model.casreport.Accounts
import org.sharad.velvetinvestment.data.remote.model.casreport.CASReport
import org.sharad.velvetinvestment.data.remote.model.casreport.CASResponseDto
import org.sharad.velvetinvestment.data.remote.model.casreport.Summary


fun CASReport.toCASResponseDto(): CASResponseDto {

    val mf = mutualFunds.summary

    return CASResponseDto(
        summary = Summary(
            total_value = mf.totalValuation,
            accounts = Accounts(
                mutual_funds = AccountDetail(
                    count = mf.totalSchemes,
                    total_value = mf.totalValuation
                ),
                demat = AccountDetail(
                    count = 0,
                    total_value = demat.summary.totalValue
                ),
                insurance = AccountDetail(
                    count = 0,
                    total_value = 0.0
                ),
                nps = AccountDetail(
                    count = 0,
                    total_value = 0.0
                )
            )
        )
    )
}