package io.github.ronjunevaldoz.paymongo.models.resource

import io.github.ronjunevaldoz.paymongo.models.serializers.WebhookEventSerializer
import io.github.ronjunevaldoz.paymongo.models.serializers.WebhookStatusSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateWebhookInput(
    val data: WebhookInput
) {
    @Serializable
    data class WebhookInput(
        val attributes: AttributesInput
    )

    @Serializable
    data class AttributesInput(
        val url: String?,
        val events: List<WebhookEvent.Event>
    )
}

@Serializable
data class ReceiveWebhookEvent(
    val data: WebhookEvent
)

@Serializable
data class WebhookEvent(
    val id: String, // event id
    val type: String,
    val attributes: Attributes
) {
    @Serializable
    data class Attributes(
        val type: Event,
        @SerialName("livemode")
        val liveMode: Boolean,
        val data: Resource,
        @SerialName("pending_webhooks")
        val pendingWebhooks: Int? = null,
        @SerialName("created_at")
        val createdAt: Long,
        @SerialName("updated_at")
        val updatedAt: Long,
    )

    /**
     * Supported hooks
     * https://developers.paymongo.com/docs/webhooks
     */
    @Serializable(with = WebhookEventSerializer::class)
    enum class Event(val value: String) {
        SourceChargeable("source.chargeable"),
        PaymentPaid("payment.paid"),
        PaymentFailed("payment.failed"),
        LinkPaymentPaid("link.payment.paid"),
        PaymentRefunded("payment.refunded"),
        PaymentRefundUpdated("payment.refund.updated"),
        CheckoutSessionPaymentPaid("checkout_session.payment.paid"),
        QrPHExpired("qrph.expired")
        ;

        companion object {
            val All = listOf(
                SourceChargeable,
                PaymentPaid,
                PaymentFailed,
                LinkPaymentPaid,
                PaymentRefunded,
                PaymentRefundUpdated,
                CheckoutSessionPaymentPaid
            )
        }
    }
}

@Serializable
data class WebhookResponse(
    val data: Webhook
)

@Serializable
data class WebhooksResponse(
    val data: List<Webhook>
)

@Serializable
@SerialName("webhook")
data class Webhook(
    val id: String,
    val attributes: Attributes
) : Resource() {

    @Serializable
    data class Attributes(
        @SerialName("livemode")
        val liveMode: Boolean,
        @SerialName("secret_key")
        val secretKey: String,
        val url: String,
        val status: Status,
        val events: List<WebhookEvent.Event>,
        @SerialName("created_at")
        val createdAt: Long,
        @SerialName("updated_at")
        val updatedAt: Long
    )

    @Serializable(with = WebhookStatusSerializer::class)
    enum class Status(val value: String) {
        Enabled("enabled"),
        Disabled("disabled")
    }
}
