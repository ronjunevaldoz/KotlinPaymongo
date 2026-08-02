package io.github.ronjunevaldoz.paymongo.models.resource

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Current Payment Links resource (`/v1/payment_links`).
 * Replaces the legacy [Link] resource (`/links`), which PayMongo has retired from its docs.
 * Unlike [Link] and other older resources, fields here are flat -- no `data.attributes`
 * wrapping on write, no `attributes` nesting or `type` discriminator on read, and
 * timestamps are ISO-8601 strings, not epoch seconds. Verified against a live sandbox call.
 * @see (https://docs.paymongo.com/reference/payment-links)
 */
@Serializable
data class CreatePaymentLinkInput(
    val amount: Int,
    val currency: String,
    val description: String? = null,
    val remarks: String? = null,
    val metadata: Map<String, String>? = null
)

@Serializable
data class UpdatePaymentLinkInput(
    val archive: Boolean
)

@Serializable
data class PaymentLinkResponse(
    val data: PaymentLink
)

@Serializable
data class PaymentLinksResponse(
    val data: List<PaymentLink>,
    @SerialName("has_more")
    val hasMore: Boolean = false
)

@Serializable
data class PaymentLink(
    val id: String,
    val amount: Int,
    val currency: String,
    val description: String? = null,
    val remarks: String? = null,
    val status: String,
    @SerialName("livemode")
    val liveMode: Boolean,
    val url: String,
    @SerialName("reference_number")
    val referenceNumber: String,
    val metadata: Map<String, String>? = null,
    val restrictions: Restrictions? = null,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String
) {
    @Serializable
    data class Restrictions(
        @SerialName("completed_sessions")
        val completedSessions: CompletedSessions? = null
    )

    @Serializable
    data class CompletedSessions(
        val count: Int,
        val limit: Int
    )
}

/**
 * Lightweight payment summary returned by `GET /v1/payment_links/{id}/payments`.
 * Distinct from and much narrower than [Payment] -- confirmed live, do not conflate the two.
 */
@Serializable
data class PaymentLinkPaymentsResponse(
    val data: List<PaymentLinkPayment>
)

@Serializable
data class PaymentLinkPayment(
    @SerialName("payment_id")
    val paymentId: String,
    val amount: Int,
    val currency: String,
    @SerialName("livemode")
    val liveMode: Boolean,
    val description: String? = null,
    val status: String,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String
)
