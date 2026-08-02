package io.github.ronjunevaldoz.paymongo.models.resource

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Current Payment Links resource (`/v1/payment_links`).
 * Replaces the legacy [Link] resource (`/links`), which PayMongo has retired from its docs.
 * @see (https://docs.paymongo.com/reference/payment-links)
 */
@Serializable
data class CreatePaymentLinkInput(val data: PaymentLinkInput) {
    @Serializable
    data class PaymentLinkInput(
        val attributes: Attributes
    )

    @Serializable
    data class Attributes(
        val amount: Int,
        val currency: String,
        val description: String? = null,
        val remarks: String? = null,
        val metadata: Map<String, String>? = null
    )
}

@Serializable
data class UpdatePaymentLinkInput(val data: PaymentLinkInput) {
    @Serializable
    data class PaymentLinkInput(
        val attributes: Attributes
    )

    @Serializable
    data class Attributes(
        val archive: Boolean
    )
}

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
@SerialName("payment_link")
data class PaymentLink(
    val id: String,
    val attributes: Attributes
) : Resource() {
    @Serializable
    data class Attributes(
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
        val createdAt: Long,
        @SerialName("updated_at")
        val updatedAt: Long
    )

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
