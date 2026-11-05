package org.sharad.velvetinvestment.features.portfolio.domain.models

import org.sharad.velvetinvestment.domain.TransactionStatus

data class TransactionHistoryDomain(
    val title:String,
    val date:String,
    val type: TransactionStatus
)
