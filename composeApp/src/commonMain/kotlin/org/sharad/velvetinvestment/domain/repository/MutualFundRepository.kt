package org.sharad.velvetinvestment.domain.repository

import org.sharad.velvetinvestment.data.remote.model.cartaddsip.AddCartSipRequest
import org.sharad.velvetinvestment.data.remote.model.fundredeem.FullRedemptionRequestDto
import org.sharad.velvetinvestment.data.remote.model.fundredeem.PartialRedemptionRequestDto
import org.sharad.velvetinvestment.domain.SIPStatus
import org.sharad.velvetinvestment.domain.models.PaginatedData
import org.sharad.velvetinvestment.domain.models.bundle.BundleDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.BundledMutualFundDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.CombinedFundsDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundDetailsDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundGraphDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundPurchaseInitiateDomain
import org.sharad.velvetinvestment.domain.models.usercart.SipItemDomain
import org.sharad.velvetinvestment.domain.models.usercart.UserCartDomain
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.NetworkResponse

interface MutualFundRepository {
    suspend fun getMutualFundsBySearch(
        search: String?,
        page:Int?,
        limit:Int?,
        sort:String?,
        risk:Int?,
        category:String?,
        fundCategory:String?
    ): NetworkResponse<PaginatedData<MutualFundDomain>, ErrorDomain>


    suspend fun getMutualFundDetails(
        id: String
    ): NetworkResponse<MutualFundDetailsDomain, ErrorDomain>

    suspend fun getMutualFundGraph(
        id: String,
        period:String
    ): NetworkResponse<MutualFundGraphDomain, ErrorDomain>

    suspend fun getMutualFundCart() : NetworkResponse<UserCartDomain, ErrorDomain>

    suspend fun deleteCartItem(id: String) : NetworkResponse<Unit, ErrorDomain>

    suspend fun clearCart() : NetworkResponse<Unit, ErrorDomain>

    suspend fun addToCartLumSumFund(id: String, amount: Long, folioId: String?): NetworkResponse<Unit, ErrorDomain>
    suspend fun addToCartSipFund(request: AddCartSipRequest): NetworkResponse<Unit, ErrorDomain>

    suspend fun purchaseLumSumFund(): NetworkResponse<String, ErrorDomain>

    suspend fun initiateSipPurchase(sipData: List<SipItemDomain>): NetworkResponse<MutualFundPurchaseInitiateDomain, ErrorDomain>
    suspend fun checkSipPurchaseStatus(mandateId: String): NetworkResponse<SIPStatus, ErrorDomain>

    suspend fun purchaseSipFund(mandateId: String, sipItems: List<SipItemDomain>): NetworkResponse<String, ErrorDomain>

    suspend fun getCombinedCategoryMutualFunds(): NetworkResponse<CombinedFundsDomain, ErrorDomain>

    suspend fun getBundleFunds(id: String): NetworkResponse<BundleDomain, ErrorDomain>

    suspend fun getAllBundledFunds(
        page: Int?,
        limit: Int?
    ): NetworkResponse<PaginatedData<BundledMutualFundDomain>, ErrorDomain>

    suspend fun addBundleToCartLumpsum(
        bundleId: String,
        amount: Long,
        selections: List<org.sharad.velvetinvestment.data.remote.model.bundlecart.BundleSelection>
    ): NetworkResponse<Unit, ErrorDomain>

    suspend fun addBundleToCartSip(
        request: org.sharad.velvetinvestment.data.remote.model.bundlecart.AddBundleSipRequest
    ): NetworkResponse<Unit, ErrorDomain>

    suspend fun redeemPartialFund(data: PartialRedemptionRequestDto): NetworkResponse<String, ErrorDomain>
    suspend fun redeemFullFund(data: FullRedemptionRequestDto): NetworkResponse<String, ErrorDomain>

    suspend fun cancelLumpSumOrder(orderId: String): NetworkResponse<Unit, ErrorDomain>
    suspend fun cancelSipOrder(xsipRegNo: String): NetworkResponse<Unit, ErrorDomain>

}

