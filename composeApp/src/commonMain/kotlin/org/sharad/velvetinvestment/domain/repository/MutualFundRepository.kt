package org.sharad.velvetinvestment.domain.repository

import org.sharad.velvetinvestment.data.remote.model.cartaddsip.AddCartSipRequest
import org.sharad.velvetinvestment.data.remote.model.fundredeem.FullRedemptionRequestDto
import org.sharad.velvetinvestment.data.remote.model.fundredeem.PartialRedemptionRequestDto
import org.sharad.velvetinvestment.domain.SIPStatus
import org.sharad.velvetinvestment.domain.models.PaginatedData
import org.sharad.velvetinvestment.domain.models.mutualfunds.CombinedFundsDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundDetailsDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundGraphDomain
import org.sharad.velvetinvestment.domain.models.mutualfunds.MutualFundPurchaseInitiateDomain
import org.sharad.velvetinvestment.domain.models.usercart.SipItemDomain
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


    suspend fun deleteCartItem(id: String) : NetworkResponse<Unit, ErrorDomain>

    suspend fun clearCart() : NetworkResponse<Unit, ErrorDomain>

    suspend fun addToCartLumSumFund(id: String, amount: Long, folioId: String?): NetworkResponse<Unit, ErrorDomain>
    suspend fun addToCartSipFund(request: AddCartSipRequest): NetworkResponse<Unit, ErrorDomain>

    suspend fun purchaseLumSumFund(): NetworkResponse<String, ErrorDomain>

    suspend fun initiateSipPurchase(sipData: List<SipItemDomain>): NetworkResponse<MutualFundPurchaseInitiateDomain, ErrorDomain>
    suspend fun checkSipPurchaseStatus(mandateId: String): NetworkResponse<SIPStatus, ErrorDomain>

    suspend fun purchaseSipFund(mandateId: String, sipItems: List<SipItemDomain>): NetworkResponse<String, ErrorDomain>

    suspend fun getCombinedCategoryMutualFunds(): NetworkResponse<CombinedFundsDomain, ErrorDomain>

    suspend fun redeemPartialFund(data: PartialRedemptionRequestDto): NetworkResponse<String, ErrorDomain>
    suspend fun redeemFullFund(data: FullRedemptionRequestDto): NetworkResponse<String, ErrorDomain>

    suspend fun cancelLumpSumOrder(orderId: String): NetworkResponse<Unit, ErrorDomain>
    suspend fun cancelSipOrder(xsipRegNo: String): NetworkResponse<Unit, ErrorDomain>

}

