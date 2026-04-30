package org.sharad.velvetinvestment.data.remote.model.portfolio

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val investment_data: InvestmentData,
    val mutual_funds: List<MutualFund>,
    val user_fd: UserFd
)