package org.sharad.velvetinvestment.data.remote.model.mutualfundcombined

import kotlinx.serialization.Serializable

@Serializable
data class Data(
    val bundle_funds: BundleFunds,
    val normal_funds: NormalFunds
)
