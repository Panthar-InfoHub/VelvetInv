package org.sharad.velvetinvestment.domain.models.portfolio

import org.sharad.velvetinvestment.domain.TransactionStatus

data class TransactionHistoryDomain(
    val title:String,
    val date:String,
    val type: TransactionStatus
)
