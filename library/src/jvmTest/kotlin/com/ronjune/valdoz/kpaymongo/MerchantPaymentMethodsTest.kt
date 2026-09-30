package com.ronjune.valdoz.kpaymongo

import io.github.ronjunevaldoz.paymongo.PayMongo
import io.github.ronjunevaldoz.paymongo.models.resource.PaymentType
import io.github.ronjunevaldoz.paymongo.serialization.PayMongoJson
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.HttpHeaders
import io.ktor.http.URLProtocol
import io.ktor.http.headersOf
import io.ktor.http.path
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.builtins.ListSerializer
import kotlin.test.Test
import kotlin.test.assertEquals

class MerchantPaymentMethodsTest {
    private fun payMongo(body: String, capturedUrl: (String) -> Unit = {}) = PayMongo(
        PayMongo.Config(secretKey = "sk_test_x"),
        HttpClient(MockEngine) {
            install(ContentNegotiation) { json(PayMongoJson) }
            defaultRequest {
                url {
                    protocol = URLProtocol.HTTPS
                    host = "api.paymongo.com"
                    path("v1/")
                }
            }
            engine {
                addHandler { request ->
                    capturedUrl(request.url.toString())
                    respond(body, headers = headersOf(HttpHeaders.ContentType, "application/json"))
                }
            }
        }
    )

    @Test
    fun `merchant payment methods decodes the documented bare array`() = runBlocking {
        var url = ""
        val methods = payMongo("""["card","gcash","qrph"]""") { url = it }.getMerchantPaymentMethods()
        assertEquals("https://api.paymongo.com/v1/merchants/capabilities/payment_methods", url)
        assertEquals(listOf("card", "gcash", "qrph"), methods)
    }

    @Test
    fun `merchant payment methods also accepts a data envelope`() = runBlocking {
        val methods = payMongo("""{"data":["qrph","brankas_rcbc"]}""").getMerchantPaymentMethods()
        assertEquals(listOf("qrph", "brankas_rcbc"), methods)
    }

    @Test
    fun `checkout payment types round trip through their wire values`() {
        val checkoutTypes = listOf(
            "shopee_pay", "qrph", "billease", "card", "dob", "dob_ubp", "brankas_bdo",
            "brankas_landbank", "brankas_metrobank", "gcash", "grab_pay", "paymaya",
        )
        val serializer = ListSerializer(PaymentType.serializer())
        val json = checkoutTypes.joinToString(",", "[", "]") { "\"$it\"" }
        val decoded = PayMongoJson.decodeFromString(serializer, json)
        assertEquals(checkoutTypes, decoded.map { it.value })
        assertEquals(decoded, PayMongoJson.decodeFromString(serializer, PayMongoJson.encodeToString(serializer, decoded)))
        assertEquals(PaymentType.QrPh, PaymentType.fromValue("qrph"))
        assertEquals(null, PaymentType.fromValue("brankas_rcbc"))
    }
}
