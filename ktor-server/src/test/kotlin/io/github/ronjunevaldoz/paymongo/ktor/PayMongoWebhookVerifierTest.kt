package io.github.ronjunevaldoz.paymongo.ktor

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import kotlin.test.Test
import kotlin.test.assertFailsWith

class PayMongoWebhookVerifierTest {
    private val secretKey = "whsec_test_123"

    private fun signatureHeaderFor(timestamp: String, body: String, live: Boolean = false): String {
        val mac = Mac.getInstance("HmacSHA256")
        mac.init(SecretKeySpec(secretKey.encodeToByteArray(), "HmacSHA256"))
        val signature = mac.doFinal("$timestamp.$body".encodeToByteArray())
            .joinToString("") { "%02x".format(it) }
        val te = if (live) "" else signature
        val li = if (live) signature else ""
        return "t=$timestamp,te=$te,li=$li"
    }

    @Test
    fun `accepts a correctly signed test-mode payload`() {
        val body = """{"data":{"id":"evt_1"}}"""
        val header = signatureHeaderFor("1700000000", body, live = false)
        PayMongoWebhookVerifier.verify(body, header, secretKey)
    }

    @Test
    fun `accepts a correctly signed live-mode payload`() {
        val body = """{"data":{"id":"evt_1"}}"""
        val header = signatureHeaderFor("1700000000", body, live = true)
        PayMongoWebhookVerifier.verify(body, header, secretKey)
    }

    @Test
    fun `rejects a tampered body`() {
        val header = signatureHeaderFor("1700000000", """{"data":{"id":"evt_1"}}""")
        assertFailsWith<PayMongoSignatureMismatchException> {
            PayMongoWebhookVerifier.verify("""{"data":{"id":"evt_2"}}""", header, secretKey)
        }
    }

    @Test
    fun `rejects the wrong secret key`() {
        val body = """{"data":{"id":"evt_1"}}"""
        val header = signatureHeaderFor("1700000000", body)
        assertFailsWith<PayMongoSignatureMismatchException> {
            PayMongoWebhookVerifier.verify(body, header, "whsec_wrong")
        }
    }

    @Test
    fun `rejects a malformed header`() {
        assertFailsWith<PayMongoSignatureFormatException> {
            PayMongoWebhookVerifier.verify("{}", "not-a-valid-header", secretKey)
        }
    }

    @Test
    fun `rejects a header missing the live and test parts`() {
        assertFailsWith<PayMongoSignatureFormatException> {
            PayMongoWebhookVerifier.verify("{}", "t=1700000000", secretKey)
        }
    }
}
