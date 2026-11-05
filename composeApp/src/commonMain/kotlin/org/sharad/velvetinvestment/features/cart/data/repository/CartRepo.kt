package org.sharad.velvetinvestment.features.cart.data.repository

import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import org.sharad.velvetinvestment.utils.networking.ErrorDomain
import org.sharad.velvetinvestment.utils.networking.ErrorType
import org.sharad.velvetinvestment.utils.networking.NetworkResponse
import org.sharad.velvetinvestment.utils.networking.getUrl
import org.sharad.velvetinvestment.utils.networking.safeRequest
import org.sharad.velvetinvestment.utils.networking.safeUnitRequest
import org.sharad.velvetinvestment.utils.CartInfo
import org.sharad.velvetinvestment.features.cart.data.remote.mapper.toDomain
import org.sharad.velvetinvestment.features.cart.data.remote.mapper.toInitiateBodyDto
import org.sharad.velvetinvestment.features.cart.data.remote.model.bundlecart.AddBundleLumpsumRequest
import org.sharad.velvetinvestment.features.cart.data.remote.model.bundlecart.AddBundleSipRequest
import org.sharad.velvetinvestment.features.cart.data.remote.model.cartaddlumpsum.AddCartLumpSumRequest
import org.sharad.velvetinvestment.features.cart.data.remote.model.cartaddsip.AddCartSipRequest
import org.sharad.velvetinvestment.features.cart.data.remote.model.cartpurchase.CartPurchaseLumpSumDto
import org.sharad.velvetinvestment.features.cart.data.remote.model.cartpurchase.CartPurchaseSIPDto
import org.sharad.velvetinvestment.features.cart.data.remote.model.initiatemfpurchase.InitiateMFPurchaseDto
import org.sharad.velvetinvestment.features.cart.data.remote.model.mfpurchasemandatestatus.CheckMFPurchaseMandateStatusDto
import org.sharad.velvetinvestment.features.cart.data.remote.model.lumpsumcheckout.ConfirmLumpsumCheckoutRequest
import org.sharad.velvetinvestment.features.cart.data.remote.model.lumpsumcheckout.ConfirmLumpsumCheckoutResponseDto
import org.sharad.velvetinvestment.features.cart.data.remote.model.lumpsumcheckout.LumpsumCheckoutResponseDto
import org.sharad.velvetinvestment.features.cart.data.remote.model.payment.PaymentStatusDto
import org.sharad.velvetinvestment.features.cart.data.remote.model.sipcheckout.SipCheckoutRequest
import org.sharad.velvetinvestment.features.cart.data.remote.model.sipcheckout.SipCheckoutResponseDto
import org.sharad.velvetinvestment.features.cart.data.remote.model.sipcheckout.VerifySipCheckoutOtpRequest
import org.sharad.velvetinvestment.features.cart.data.remote.model.usercart.UserCartDto
import org.sharad.velvetinvestment.features.cart.domain.models.MutualFundPurchaseInitiateDomain
import org.sharad.velvetinvestment.features.cart.domain.models.SIPStatus
import org.sharad.velvetinvestment.features.cart.domain.models.CartCheckoutDomain
import org.sharad.velvetinvestment.features.cart.domain.models.CartPaymentStatusDomain
import org.sharad.velvetinvestment.features.cart.domain.models.CartType
import org.sharad.velvetinvestment.features.cart.domain.models.LumpsumCheckoutPaymentDomain
import org.sharad.velvetinvestment.features.cart.domain.models.SipItemDomain
import org.sharad.velvetinvestment.features.cart.domain.models.UserCartDomain
import org.sharad.velvetinvestment.features.cart.domain.repository.CartRepository

class CartRepo(
    private val client: HttpClient
) : CartRepository {

    override suspend fun getCart(): NetworkResponse<UserCartDomain, ErrorDomain> {
        val response = safeRequest<UserCartDto> {
            client.get(getUrl("/user/cart"))
        }
        return when (response) {
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
            is NetworkResponse.Success -> {
                val domain = response.data.toDomain()
                CartInfo.updateFundAmount(domain.sipItems.size + domain.lumpSumItems.size)
                NetworkResponse.Success(domain)
            }
        }
    }

    override suspend fun addToCartLumpsum(
        mfProductId: String,
        amount: Long
    ): NetworkResponse<Unit, ErrorDomain> {
        return safeUnitRequest {
            client.post(getUrl("/mf/cart")) {
                setBody(
                    AddCartLumpSumRequest(
                        mf_product_id = mfProductId,
                        amount = amount
                    )
                )
            }
        }
    }

    override suspend fun addToCartSip(request: AddCartSipRequest): NetworkResponse<Unit, ErrorDomain> {
        return safeUnitRequest {
            client.post(getUrl("/mf/cart")) {
                setBody(request)
            }
        }
    }

    override suspend fun addBundleToCartLumpsum(request: AddBundleLumpsumRequest): NetworkResponse<Unit, ErrorDomain> {
        return safeUnitRequest {
            client.post(getUrl("/mf/cart/bundle")) {
                setBody(request)
            }
        }
    }

    override suspend fun addBundleToCartSip(request: AddBundleSipRequest): NetworkResponse<Unit, ErrorDomain> {
        return safeUnitRequest {
            client.post(getUrl("/mf/cart/bundle")) {
                setBody(request)
            }
        }
    }

    override suspend fun deleteCartItem(id: String): NetworkResponse<Unit, ErrorDomain> {
        return safeUnitRequest {
            client.delete(getUrl("/mf/cart/$id"))
        }
    }

    override suspend fun clearCart(): NetworkResponse<Unit, ErrorDomain> {
        return safeUnitRequest {
            client.delete(getUrl("/mf/cart/"))
        }
    }

    override suspend fun checkoutSip(mandateId: String): NetworkResponse<CartCheckoutDomain, ErrorDomain> {
        val response = safeRequest<SipCheckoutResponseDto> {
            client.post(getUrl("/mf/cart/checkout/sip")) {
                setBody(SipCheckoutRequest(mandate_id = mandateId))
            }
        }
        return when (response) {
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
            is NetworkResponse.Success -> {
                val data = response.data.data
                NetworkResponse.Success(
                    CartCheckoutDomain(
                        batchId = data.batch_id,
                        type = CartType.SIP,
                        itemsCount = data.plans_count,
                        totalAmount = data.total_amount.toLong()
                    )
                )
            }
        }
    }

    override suspend fun verifySipCheckoutOtp(batchId: String, otp: String): NetworkResponse<Unit, ErrorDomain> {
        return safeUnitRequest {
            client.post(getUrl("/mf/cart/checkout/sip/confirm")) {
                setBody(VerifySipCheckoutOtpRequest(batch_id = batchId, otp = otp))
            }
        }
    }

    override suspend fun checkoutLumpsum(): NetworkResponse<CartCheckoutDomain, ErrorDomain> {
        val response = safeRequest<LumpsumCheckoutResponseDto> {
            client.post(getUrl("/mf/cart/checkout/lumpsum"))
        }
        return when (response) {
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
            is NetworkResponse.Success -> {
                val data = response.data.data
                NetworkResponse.Success(
                    CartCheckoutDomain(
                        batchId = data.batch_id,
                        type = CartType.LUMPSUM,
                        itemsCount = data.orders_count,
                        totalAmount = data.total_amount.toLong()
                    )
                )
            }
        }
    }

    override suspend fun confirmLumpsumCheckout(
        batchId: String,
        otp: String,
        paymentPostbackUrl: String
    ): NetworkResponse<LumpsumCheckoutPaymentDomain, ErrorDomain> {
        val response = safeRequest<ConfirmLumpsumCheckoutResponseDto> {
            client.post(getUrl("/mf/cart/checkout/lumpsum/confirm")) {
                setBody(
                    ConfirmLumpsumCheckoutRequest(
                        batch_id = batchId,
                        otp = otp,
                        payment_postback_url = paymentPostbackUrl
                    )
                )
            }
        }
        return when (response) {
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
            is NetworkResponse.Success -> NetworkResponse.Success(
                LumpsumCheckoutPaymentDomain(
                    paymentUrl = response.data.data.payment_url,
                    paymentId = response.data.data.payment_id
                )
            )
        }
    }

    override suspend fun getPaymentStatus(paymentId: String): NetworkResponse<CartPaymentStatusDomain, ErrorDomain> {
        val response = safeRequest<PaymentStatusDto> {
            client.get(getUrl("/payment/$paymentId"))
        }
        return when (response) {
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
            is NetworkResponse.Success -> NetworkResponse.Success(
                CartPaymentStatusDomain(
                    status = response.data.data.status,
                    failedReason = response.data.data.failed_reason
                )
            )
        }
    }

    override suspend fun purchaseLumpSum(): NetworkResponse<String, ErrorDomain> {
        val response = safeRequest<CartPurchaseLumpSumDto> {
            client.post(getUrl("/mf/purchase-lumpsum"))
        }
        return when (response) {
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
            is NetworkResponse.Success -> NetworkResponse.Success(response.data.data)
        }
    }

    override suspend fun initiateSipPurchase(sipData: List<SipItemDomain>): NetworkResponse<MutualFundPurchaseInitiateDomain, ErrorDomain> {
        val response = safeRequest<InitiateMFPurchaseDto> {
            client.post(getUrl("/mf/initiate-sip")) {
                setBody(sipData.toInitiateBodyDto())
            }
        }
        return when (response) {
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
            is NetworkResponse.Success -> NetworkResponse.Success(response.data.data.toDomain())
        }
    }

    override suspend fun checkSipPurchaseStatus(mandateId: String): NetworkResponse<SIPStatus, ErrorDomain> {
        val response = safeRequest<CheckMFPurchaseMandateStatusDto> {
            client.get(getUrl("/mf/mandate-status")) {
                parameter("mandate_id", mandateId)
            }
        }
        return when (response) {
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
            is NetworkResponse.Success -> {
                val status = SIPStatus.getStatus(response.data.data.enach_status)
                if (status != null) {
                    NetworkResponse.Success(status)
                } else {
                    NetworkResponse.Error(ErrorDomain(0, response.data.data.enach_status, ErrorType.UNKNOWN))
                }
            }
        }
    }

    override suspend fun purchaseSip(mandateId: String, sipItems: List<SipItemDomain>): NetworkResponse<String, ErrorDomain> {
        val response = safeRequest<CartPurchaseSIPDto> {
            client.post(getUrl("/mf/purchase-sip")) {
                setBody(sipItems.toInitiateBodyDto())
            }
        }
        return when (response) {
            is NetworkResponse.Error -> NetworkResponse.Error(response.error)
            is NetworkResponse.Success -> NetworkResponse.Success(response.data.data.xsip_short_url)
        }
    }
}
