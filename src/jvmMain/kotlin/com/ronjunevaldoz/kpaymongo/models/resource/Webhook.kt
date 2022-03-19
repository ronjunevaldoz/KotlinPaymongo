package com.ronjunevaldoz.kpaymongo.models.resource

import com.ronjunevaldoz.kpaymongo.models.serializers.WebhookEventSerializer
import com.ronjunevaldoz.kpaymongo.models.serializers.WebhookStatusSerializer
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
        val url: String,
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

    @Serializable(with = WebhookEventSerializer::class)
    enum class Event(val value: String) {
        SourceChargeable("source.chargeable"),
        PaymentPaid("payment.paid"),
        PaymentFailed("payment.failed"),
        PaymentRefunded("payment.refunded"),
        PaymentRefundUpdated("payment.refund.updated");

        companion object {
            val All = listOf(
                SourceChargeable,
                PaymentPaid,
                PaymentFailed,
                PaymentRefunded,
                PaymentRefundUpdated
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
