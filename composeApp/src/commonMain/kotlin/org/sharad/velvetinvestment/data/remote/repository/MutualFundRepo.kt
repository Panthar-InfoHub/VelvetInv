package org.sharad.velvetinvestment.data.remote.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.sharad.velvetinvestment.data.remote.mapper.toDomain
import org.sharad.velvetinvestment.data.remote.mapper.toInitiateBodyDto
import org.sharad.velvetinvestment.data.remote.mapper.toPaginatedDomain
import org.sharad.velvetinvestment.data.remote.model.allbundles.AllBundlesDto
import org.sharad.velvetinvestment.data.remote.model.allbundles.toDomain
import org.sharad.velvetinvestment.data.remote.model.bundledfundbyid.BundleByIdDto
import org.sharad.velvetinvestment.data.remote.model.cancelorder.CancelOrderRequestDto
import org.sharad.velvetinvestment.data.remote.model.cancelorder.CancelOrderResponseDto
import org.sharad.velvetinvestment.data.remote.model.cancelorder.CancelXsipRequestDto
import org.sharad.velvetinvestment.data.remote.model.cancelorder.CancelXsipResponseDto
import org.sharad.velvetinvestment.data.remote.model.cartaddlumpsum.AddCartLumpSumRequest
import org.sharad.velvetinvestment.data.remote.model.cartaddlumpsum.AddCartLumpSumResponseDto
import org.sharad.velvetinvestment.data.remote.model.cartaddsip.AddCartSipRequest
import org.sharad.velvetinvestment.data.remote.model.cartaddsip.AddCartSipResponseDto
import org.sharad.velvetinvestment.data.remote.model.cartpurchase.CartPurchaseLumpSumDto
import org.sharad.velvetinvestment.data.remote.model.cartpurchase.CartPurchaseSIPDto
import org.sharad.velvetinvestment.data.remote.model.fundredeem.FullRedemptionRequestDto
import org.sharad.velvetinvestment.data.remote.model.fundredeem.PartialRedemptionRequestDto
import org.sharad.velvetinvestment.data.remote.model.fundredeem.response.FundRedeemDto
import org.sharad.velvetinvestment.data.remote.model.getmf.MutualFundDto
import org.sharad.velvetinvestment.data.remote.model.initiatemfpurchase.InitiateMFPurchaseDto
import org.sharad.velvetinvestment.data.remote.model.mfdetails.MutualFundsDetailDto
import org.sharad.velvetinvestment.data.remote.model.mfgraph.MFGraphDto
import org.sharad.velvetinvestment.data.remote.model.mfpurchasemandatestatus.CheckMFPurchaseMandateStatusDto
import org.sharad.velvetinvestment.data.remote.model.mutualfundcombined.CombinedFundsDto
import org.sharad.velvetinvestment.data.remote.model.usercart.UserCartDto
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
import org.sharad.velvetinvestment.domain.repository.MutualFundRepository
import org.sharad.velvetinvestment.utils.CartInfo
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.ErrorType
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.utils.networking.getUrl
import org.sharad.velvetinvestment.utils.networking.safeRequest
import org.sharad.velvetinvestment.utils.networking.safeUnitRequest

class MutualFundRepo(
    private val client: HttpClient
): MutualFundRepository {


    override suspend fun getMutualFundsBySearch(
        search: String?,
        page: Int?,
        limit: Int?,
        sort: String?,
        risk: Int?,
        category: String?,
        fundCategory: String?
    ):NetworkResponse<PaginatedData<MutualFundDomain>, ErrorDomain> {
        val response = safeRequest<MutualFundDto> {
            client.get(getUrl("/mf")) {
                search?.let { parameter("search", it) }
                page?.let { parameter("page", it) }
                limit?.let { parameter("limit", it) }
                sort?.let { parameter("sort_by", it) }
                risk?.let { parameter("risk", it) }
                category?.let { parameter("category", it) }
                fundCategory?.let { parameter("fund_category", it) }
            }
        }
        return when (response) {
            is NetworkResponse.Success -> {
                NetworkResponse.Success(response.data.toPaginatedDomain())
            }

            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
        }

    }

    override suspend fun getMutualFundDetails(id: String): NetworkResponse<MutualFundDetailsDomain, ErrorDomain> {
        val response = safeRequest< MutualFundsDetailDto> {
            client.get(getUrl("/mf/$id")) {
            }
        }
        return when (response) {
            is NetworkResponse.Success -> {
                NetworkResponse.Success(response.data.toDomain())
            }

            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
        }
    }

    override suspend fun getMutualFundGraph(
        id: String,
        period: String,
    ): NetworkResponse<MutualFundGraphDomain, ErrorDomain> {
        val response = safeRequest<MFGraphDto> {
            client.get(getUrl("/mf/history/${id}")) {
                parameter("period", period)
            }
        }
        return when (response) {
            is NetworkResponse.Success -> {
                NetworkResponse.Success(response.data.toDomain())
            }

            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
        }
    }

    override suspend fun getMutualFundCart(): NetworkResponse<UserCartDomain, ErrorDomain> {
        val response= safeRequest<UserCartDto> {
            client.get(getUrl("/user/cart"))
        }

        return when(response){
            is NetworkResponse.Error-> {
                NetworkResponse.Error(response.error)
            }

            is NetworkResponse.Success -> {
                val domain= response.data.toDomain()
                CartInfo.updateFundAmount(domain.sipItems.size+ domain.lumpSumItems.size)
                NetworkResponse.Success(domain)
            }
        }
    }

    override suspend fun deleteCartItem(id: String): NetworkResponse<Unit, ErrorDomain> {
        val response= safeUnitRequest {
            client.delete(getUrl("/mf/remove-cart-item")) {
                parameter("cart_item_id", id)
            }
        }
        return when(response){
            is NetworkResponse.Error-> {
                NetworkResponse.Error(response.error)
            }
            is NetworkResponse.Success -> {
                NetworkResponse.Success(Unit)
            }
        }
    }

    override suspend fun clearCart(): NetworkResponse<Unit, ErrorDomain> {
        val response = safeUnitRequest {
            client.delete(getUrl("/mf/clear-cart"))
        }
        return when (response) {
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
            is NetworkResponse.Success -> {
                NetworkResponse.Success(Unit)
            }
        }
    }

    override suspend fun addToCartLumSumFund(
        id: String,
        amount: Long,
        folioId: String?,
    ): NetworkResponse<Unit, ErrorDomain> {
        val response= safeRequest<AddCartLumpSumResponseDto> {
            client.post(getUrl("/mf/lumpsum-cart")){
                setBody(
                    AddCartLumpSumRequest(
                        amount = amount,
                        mf_product_id = id,
                        folio= folioId?:""
                    )
                )
            }
        }

        return when(response){
            is NetworkResponse.Error-> {
                NetworkResponse.Error(response.error)
            }

            is NetworkResponse.Success -> {
                NetworkResponse.Success(Unit)
            }
        }
    }

    override suspend fun addToCartSipFund(request: AddCartSipRequest): NetworkResponse<Unit, ErrorDomain> {
        val response= safeRequest<AddCartSipResponseDto> {
            client.post(getUrl("/mf/sip-cart")){
                setBody(request)
            }
        }

        return when(response){
            is NetworkResponse.Error-> {
                NetworkResponse.Error(response.error)
            }

            is NetworkResponse.Success -> {
                NetworkResponse.Success(Unit)
            }
        }
    }

    override suspend fun purchaseLumSumFund(): NetworkResponse<String, ErrorDomain> {
        val response=safeRequest<CartPurchaseLumpSumDto> {
            client.post(getUrl("/mf/purchase-lumpsum"))
        }
        return when(response){
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
            is NetworkResponse.Success -> {
                NetworkResponse.Success(response.data.data)
            }
        }
    }

    override suspend fun initiateSipPurchase(sipData: List<SipItemDomain>): NetworkResponse<MutualFundPurchaseInitiateDomain, ErrorDomain> {
        val response = safeRequest<InitiateMFPurchaseDto> {
            client.post(getUrl("/mf/initiate-sip")){
                setBody(
                    sipData.toInitiateBodyDto()
                )
            }
        }
        return when(response){
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
            is NetworkResponse.Success -> {
                NetworkResponse.Success(response.data.data.toDomain())
            }
        }
    }

    override suspend fun checkSipPurchaseStatus(mandateId: String): NetworkResponse<SIPStatus, ErrorDomain> {
        val response=safeRequest<CheckMFPurchaseMandateStatusDto> {
            client.get(getUrl("/mf/mandate-status")){
                parameter("mandate_id", mandateId)
            }
        }
        return when(response){
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
            is NetworkResponse.Success -> {
                val status= SIPStatus.getStatus(response.data.data.enach_status)
                if (status != null){
                    NetworkResponse.Success(status)
                }else {
                    NetworkResponse.Error(ErrorDomain(0, response.data.data.enach_status, ErrorType.UNKNOWN))
                }
            }
        }
    }

    override suspend fun purchaseSipFund(mandateId: String, sipItems: List<SipItemDomain>): NetworkResponse<String, ErrorDomain> {
        val response=safeRequest<CartPurchaseSIPDto> {
            client.post(getUrl("/mf/purchase-sip")){
                setBody(
                    sipItems.toInitiateBodyDto()
                )
            }
        }
        return when(response){
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
            is NetworkResponse.Success -> {
                NetworkResponse.Success(response.data.data.xsip_short_url)
            }
        }
    }

    override suspend fun getCombinedCategoryMutualFunds(): NetworkResponse<CombinedFundsDomain, ErrorDomain> {
        val response = safeRequest<CombinedFundsDto> {
            client.get(getUrl("/frontend/mf-data"))
        }

        return when (response) {
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }

            is NetworkResponse.Success -> {
                NetworkResponse.Success(response.data.toDomain())
            }
        }
    }

    override suspend fun getBundleFunds(id: String): NetworkResponse<BundleDomain, ErrorDomain> {
        val response = safeRequest<BundleByIdDto> {
            client.get(getUrl("/bundles/$id"))
        }

        return when (response) {
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }

            is NetworkResponse.Success -> {
                NetworkResponse.Success(response.data.toDomain())
            }
        }
    }

    override suspend fun getAllBundledFunds(
        page: Int?,
        limit: Int?
    ): NetworkResponse<PaginatedData<BundledMutualFundDomain>, ErrorDomain> {
        val response = safeRequest<AllBundlesDto> {
            client.get(getUrl("/bundles")){
                parameter("page",page?:1)
                parameter("limit", limit?:20)
            }
        }
        return when (response) {
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
            is NetworkResponse.Success->{
                NetworkResponse.Success(response.data.toDomain())
            }
        }
    }

    override suspend fun addBundleToCartLumpsum(
        bundleId: String,
        amount: Long,
        selections: List<org.sharad.velvetinvestment.data.remote.model.bundlecart.BundleSelection>
    ): NetworkResponse<Unit, ErrorDomain> {
        val response = safeUnitRequest {
            client.post(getUrl("/mf/bundle-cart")) {
                setBody(
                    org.sharad.velvetinvestment.data.remote.model.bundlecart.AddBundleLumpsumRequest(
                        bundle_id = bundleId,
                        amount = amount,
                        selections = selections
                    )
                )
            }
        }
        return when (response) {
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
            is NetworkResponse.Success -> {
                NetworkResponse.Success(Unit)
            }
        }
    }

    override suspend fun addBundleToCartSip(
        request: org.sharad.velvetinvestment.data.remote.model.bundlecart.AddBundleSipRequest
    ): NetworkResponse<Unit, ErrorDomain> {
        val response = safeUnitRequest {
            client.post(getUrl("/mf/bundle-cart")) {
                setBody(request)
            }
        }
        return when (response) {
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
            is NetworkResponse.Success -> {
                NetworkResponse.Success(Unit)
            }
        }
    }

    override suspend fun redeemPartialFund(data: PartialRedemptionRequestDto): NetworkResponse<String, ErrorDomain> {
        val response = safeRequest<FundRedeemDto> {
            client.post(getUrl("/mf/redeem")) { setBody(data) }
        }

        return when (response) {
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
            is NetworkResponse.Success -> {
                NetworkResponse.Success(response.data.data.payment_link)
            }
        }
    }

    override suspend fun redeemFullFund(data: FullRedemptionRequestDto): NetworkResponse<String, ErrorDomain> {
        val response = safeRequest<FundRedeemDto> {
            client.post(getUrl("/mf/redeem")) { setBody(data) }
        }

        return when (response) {
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
            is NetworkResponse.Success -> {
                NetworkResponse.Success(response.data.data.payment_link)
            }
        }
    }

    override suspend fun cancelLumpSumOrder(orderId: String): NetworkResponse<Unit, ErrorDomain> {
        val response = safeRequest<CancelOrderResponseDto> {
            client.post(getUrl("/mf/cancel-order")) {
                setBody(CancelOrderRequestDto(order_no = orderId))
            }
        }
        return when (response) {
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
            is NetworkResponse.Success -> {
                NetworkResponse.Success(Unit)
            }
        }
    }

    override suspend fun cancelSipOrder(xsipRegNo: String): NetworkResponse<Unit, ErrorDomain> {
        val response = safeRequest<CancelXsipResponseDto> {
            client.post(getUrl("/mf/cancel-xsip")) {
                setBody(CancelXsipRequestDto(xsip_reg_no = xsipRegNo))
            }
        }
        return when (response) {
            is NetworkResponse.Error -> {
                NetworkResponse.Error(response.error)
            }
            is NetworkResponse.Success -> {
                NetworkResponse.Success(Unit)
            }
        }
    }
}
