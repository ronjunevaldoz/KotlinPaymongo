package com.ronjune.valdoz.kpaymongo

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.get
import io.ktor.http.URLProtocol
import io.ktor.http.headersOf
import io.ktor.http.path
import kotlinx.coroutines.runBlocking
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * PayMongoClientFactory sets a "v1/" base path via defaultRequest. A relative request path
 * (no leading slash) appends onto that base -- confirmed correct against a live sandbox call
 * for v1 endpoints. v2 endpoints (checkout sessions v2, customer payment methods) need a
 * leading "/" to escape the "v1/" base instead of nesting under it as "v1/v2/...". This test
 * locks that resolution behavior in place without hitting the network.
 */
class UrlResolutionTest {
    private fun mockClient(capturedUrl: (String) -> Unit) = HttpClient(MockEngine) {
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
                respond("{}", headers = headersOf())
            }
        }
    }

    @Test
    fun `relative path appends onto the v1 base`() = runBlocking {
        var captured = ""
        val client = mockClient { captured = it }
        client.get("customers")
        assertEquals("https://api.paymongo.com/v1/customers", captured)
    }

    @Test
    fun `leading slash escapes the v1 base for v2 endpoints`() = runBlocking {
        var captured = ""
        val client = mockClient { captured = it }
        client.get("/v2/customer_payment_methods/cus_123")
        assertEquals("https://api.paymongo.com/v2/customer_payment_methods/cus_123", captured)
    }
}
