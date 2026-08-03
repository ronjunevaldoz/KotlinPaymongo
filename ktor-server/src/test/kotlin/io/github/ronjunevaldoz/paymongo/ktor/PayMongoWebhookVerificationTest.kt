package io.github.ronjunevaldoz.paymongo.ktor

import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.application.install
import io.ktor.server.response.respondText
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.test.Test
import kotlin.test.assertEquals

class PayMongoWebhookVerificationTest {
    private val secretKey = "whsec_test_123"
    private val eventJson = """{"data":{"id":"evt_wHb8c2V8TD6VWVAx3HctprNU","type":"event","attributes":{"type":"payment.paid","livemode":false,"data":{"id":"pay_1","type":"payment","attributes":{"amount":150000,"balance_transaction_id":"bal_txn_1","billing":{"address":{"city":"","country":"","line1":"","line2":"","postal_code":"","state":""},"email":"a@b.com","name":"A","phone":"1"},"currency":"PHP","disputed":false,"fee":3750,"livemode":false,"net_amount":146250,"origin":"api","source":{"id":"src_1","type":"gcash"},"status":"paid","refunds":[],"taxes":[],"available_at":1679562000,"created_at":1679383287,"credited_at":1680080400,"paid_at":1679383287}},"created_at":1647501632,"updated_at":1647501632}}}"""

    private fun signatureHeaderFor(timestamp: String, payload: String): String {
        val mac = Mac.getInstance("HmacSHA256")
        mac.init(SecretKeySpec(secretKey.encodeToByteArray(), "HmacSHA256"))
        val signature = mac.doFinal("$timestamp.$payload".encodeToByteArray())
            .joinToString("") { "%02x".format(it) }
        return "t=$timestamp,te=$signature,li="
    }

    @Test
    fun `verified request reaches the handler with a decoded event`() = testApplication {
        application {
            routing {
                route("/webhooks") {
                    install(PayMongoWebhookVerification) { secretKey = this@PayMongoWebhookVerificationTest.secretKey }
                    post {
                        call.respondText(call.payMongoEvent.data.attributes.type.value)
                    }
                }
            }
        }

        val response = client.post("/webhooks") {
            contentType(ContentType.Application.Json)
            header("Paymongo-Signature", signatureHeaderFor("1700000000", eventJson))
            setBody(eventJson)
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("payment.paid", response.bodyAsText())
    }

    @Test
    fun `tampered request never reaches the handler`() = testApplication {
        var handlerRan = false
        application {
            routing {
                route("/webhooks") {
                    install(PayMongoWebhookVerification) { secretKey = this@PayMongoWebhookVerificationTest.secretKey }
                    post {
                        handlerRan = true
                        call.respondText("should not get here")
                    }
                }
            }
        }

        val response = runCatching {
            client.post("/webhooks") {
                contentType(ContentType.Application.Json)
                header("Paymongo-Signature", signatureHeaderFor("1700000000", eventJson))
                setBody("""{"tampered":true}""")
            }
        }

        // the plugin throws PayMongoSignatureMismatchException with no StatusPages installed,
        // so Ktor's default behavior surfaces it as a 500 -- either way, the handler must not run
        response.getOrNull()?.let { assertEquals(HttpStatusCode.InternalServerError, it.status) }
        assertEquals(false, handlerRan)
    }

    @Test
    fun `request with no signature header never reaches the handler`() = testApplication {
        var handlerRan = false
        application {
            routing {
                route("/webhooks") {
                    install(PayMongoWebhookVerification) { secretKey = this@PayMongoWebhookVerificationTest.secretKey }
                    post {
                        handlerRan = true
                        call.respondText("should not get here")
                    }
                }
            }
        }

        val response = runCatching {
            client.post("/webhooks") {
                contentType(ContentType.Application.Json)
                setBody(eventJson)
            }
        }

        // PayMongoSignatureFormatException, uncaught with no StatusPages installed, surfaces as 500
        response.getOrNull()?.let { assertEquals(HttpStatusCode.InternalServerError, it.status) }
        assertEquals(false, handlerRan)
    }

    @Test
    fun `redelivered event is flagged as a duplicate on the second call`() = testApplication {
        var processedCount = 0
        application {
            routing {
                route("/webhooks") {
                    install(PayMongoWebhookVerification) {
                        secretKey = this@PayMongoWebhookVerificationTest.secretKey
                        dedupStore = InMemoryPayMongoWebhookDedupStore()
                    }
                    post {
                        if (!call.isDuplicatePayMongoEvent) processedCount++
                        call.respondText(call.isDuplicatePayMongoEvent.toString())
                    }
                }
            }
        }

        val header = signatureHeaderFor("1700000000", eventJson)
        val first = client.post("/webhooks") {
            contentType(ContentType.Application.Json)
            header("Paymongo-Signature", header)
            setBody(eventJson)
        }
        val second = client.post("/webhooks") {
            contentType(ContentType.Application.Json)
            header("Paymongo-Signature", header)
            setBody(eventJson)
        }

        assertEquals("false", first.bodyAsText())
        assertEquals("true", second.bodyAsText())
        assertEquals(1, processedCount)
    }

    @Test
    fun `no dedup store configured means never flagged as a duplicate`() = testApplication {
        application {
            routing {
                route("/webhooks") {
                    install(PayMongoWebhookVerification) { secretKey = this@PayMongoWebhookVerificationTest.secretKey }
                    post { call.respondText(call.isDuplicatePayMongoEvent.toString()) }
                }
            }
        }

        val header = signatureHeaderFor("1700000000", eventJson)
        repeat(2) {
            val response = client.post("/webhooks") {
                contentType(ContentType.Application.Json)
                header("Paymongo-Signature", header)
                setBody(eventJson)
            }
            assertEquals("false", response.bodyAsText())
        }
    }
}
