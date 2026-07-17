package org.sharad.velvetinvestment.data.remote.model.allbundles

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AllBundlesDto(
    val success: Boolean,
    val message: String,
    val data: AllBundlesDataDto
)

@Serializable
data class AllBundlesDataDto(
    val bundles: List<BundleDto>,
    val pagination: PaginationDto
)

@Serializable
data class BundleDto(
    val id: String,

    @SerialName("bundle_name")
    val bundleName: String,

    @SerialName("bundle_description")
    val bundleDescription: String,

    @SerialName("equity_percentage")
    val equityPercentage: Int,

    @SerialName("commodity_percentage")
    val commodityPercentage: Int,

    @SerialName("debt_percentage")
    val debtPercentage: Int,

    @SerialName("hybrid_percentage")
    val hybridPercentage: Int,

    @SerialName("meta_data")
    val metaData: BundleMetaDataDto,

    val img_url: String? = null
)

@Serializable
data class BundleMetaDataDto(

    @SerialName("risk_level")
    val riskLevel: String,

    @SerialName("investment_time")
    val investmentTime: String,

    @SerialName("investment_growth")
    val investmentGrowth: String
)

@Serializable
data class PaginationDto(
    val total: Int,
    val page: Int,
    val limit: Int,

    @SerialName("totalPages")
    val totalPages: Int
)