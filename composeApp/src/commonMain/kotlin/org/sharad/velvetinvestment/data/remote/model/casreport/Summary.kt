package org.sharad.velvetinvestment.data.remote.model.casreport

import kotlinx.serialization.Serializable

@Serializable
data class Summary(
    val total_value: Double,
    val accounts: Accounts
)

@Serializable
data class Accounts(
    val demat: AccountDetail,
    val mutual_funds: AccountDetail,
    val insurance: AccountDetail,
    val nps: AccountDetail
)

@Serializable
data class AccountDetail(
    val count: Int,
    val total_value: Double
)