package io.github.ronjunevaldoz.paymongo.ktor

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

/**
 * Verifies the `Paymongo-Signature` header PayMongo sends with every webhook delivery.
 * Algorithm confirmed against PayMongo's official Node SDK
 * (github.com/paymongo/paymongo-node, `WebhookService.prototype.constructEvent`):
 *
 * 1. The header is `t=<timestamp>,te=<test_mode_signature>,li=<live_mode_signature>`.
 * 2. The signed string is `"<timestamp>.<raw request body>"`.
 * 3. HMAC-SHA256 of that string, keyed with the webhook's secret key, hex-encoded.
 * 4. Compare against `li` if non-empty, else `te`.
 *
 * The reference implementation uses a plain string comparison; this uses a constant-time
 * comparison instead, since a timing side channel on a signature check is a real (if minor)
 * attack surface the reference doesn't need to worry about for a trusted SDK context.
 */
object PayMongoWebhookVerifier {
    /**
     * @param rawBody the exact, unparsed request body bytes as received -- any re-serialization
     * (even reformatting) breaks the signature
     * @param signatureHeader the raw `Paymongo-Signature` header value
     * @param webhookSecretKey the secret key PayMongo shows when the webhook endpoint was created
     * @throws PayMongoSignatureFormatException if the header doesn't have the expected `t=`/`te=`/`li=` parts
     * @throws PayMongoSignatureMismatchException if the computed signature doesn't match
     */
    fun verify(rawBody: String, signatureHeader: String, webhookSecretKey: String) {
        val parts = signatureHeader.split(",")
        if (parts.size < 3) {
            throw PayMongoSignatureFormatException(
                "Expected \"t=...,te=...,li=...\", got \"$signatureHeader\""
            )
        }

        val timestamp = parts[0].substringAfter("=", missingDelimiterValue = "")
        val testModeSignature = parts[1].substringAfter("=", missingDelimiterValue = "")
        val liveModeSignature = parts[2].substringAfter("=", missingDelimiterValue = "")
        val expectedSignature = liveModeSignature.ifEmpty { testModeSignature }

        val signedPayload = "$timestamp.$rawBody"
        val computedSignature = hmacSha256Hex(webhookSecretKey, signedPayload)

        if (!constantTimeEquals(computedSignature, expectedSignature)) {
            throw PayMongoSignatureMismatchException()
        }
    }

    private fun hmacSha256Hex(key: String, data: String): String {
        val mac = Mac.getInstance("HmacSHA256")
        mac.init(SecretKeySpec(key.encodeToByteArray(), "HmacSHA256"))
        return mac.doFinal(data.encodeToByteArray()).joinToString("") { "%02x".format(it) }
    }

    private fun constantTimeEquals(a: String, b: String): Boolean {
        if (a.length != b.length) return false
        var result = 0
        for (i in a.indices) {
            result = result or (a[i].code xor b[i].code)
        }
        return result == 0
    }
}

class PayMongoSignatureFormatException(message: String) : RuntimeException(message)
class PayMongoSignatureMismatchException : RuntimeException("Paymongo-Signature did not match the computed signature")
