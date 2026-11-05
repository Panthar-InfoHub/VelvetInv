package org.sharad.velvetinvestment.features.cart

import kotlinx.serialization.json.Json
import org.sharad.velvetinvestment.features.cart.data.remote.mapper.toDomain
import org.sharad.velvetinvestment.features.cart.data.remote.model.usercart.UserCartDto
import kotlin.test.Test
import kotlin.test.assertEquals

class UserCartDtoParseTest {
    private val json = Json { ignoreUnknownKeys = true }

    private fun item(id: String, amount: String, lumpMin: String?, dailyMax: String) = """
        {"id":"$id","user_id":"u","mf_product_id":"p$id","cart_type":"SIP","amount":"$amount",
         "frequency":"DAILY","installment_day":10,"createdAt":"2026-09-24T10:59:13.652Z",
         "updatedAt":"2026-09-24T10:59:13.652Z",
         "mf_product":{"id":"p$id","name":"Fund $id","isin":"INF","img_url":"https://x/y.png",
           "scheme_plan":{"lumpsum_amount_min":${lumpMin?.let { "\"$it\"" } ?: "null"},
             "lumpsum_amount_max":null,"lumpsum_amount_multiples":null,
             "sip_daily_amount_min":"20","sip_daily_amount_max":"$dailyMax","sip_daily_amount_multiples":"1",
             "sip_monthly_amount_min":"100","sip_monthly_amount_max":"999999999","sip_monthly_amount_multiples":"1",
             "sip_monthly_dates":[1,2,28]}}}
    """

    @Test
    fun parsesNewCartShape() {
        val body = """{"success":true,"message":"User cart fetched successfully","data":{"sip":[
            ${item("a", "1000", "5000", "999999999")},${item("b", "1500", null, "99999999999.99")}
        ],"lumpsum":[]}}"""
        val domain = json.decodeFromString<UserCartDto>(body).toDomain()
        assertEquals(2, domain.sipItems.size)
        assertEquals(0, domain.lumpSumItems.size)
        assertEquals(1500L, domain.sipItems[1].amount)
        assertEquals("DAILY", domain.sipItems[0].sipDetails.frequency)
        assertEquals(10, domain.sipItems[0].sipDetails.day)
    }
}
