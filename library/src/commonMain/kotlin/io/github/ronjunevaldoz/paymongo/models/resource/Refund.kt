package io.github.ronjunevaldoz.paymongo.models.resource

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateRefundInput(val data: RefundInput) {
    @Serializable
    data class RefundInput(
        val attributes: Attributes
    )

    @Serializable
    data class Attributes(
        val amount: Double,
        @SerialName("payment_id")
        val paymentId: String,
        val reason: String,
        val metadata: Map<String, String>? = null
    )
}

@Serializable
data class RefundResponse(
    val data: Refund
)

@Serializable
@SerialName("refund")
data class Refund(
    val id: String,
    val attributes: Attributes
) : Resource() {
    @Serializable
    data class Attributes(
        val amount: Int,
        val currency: String,
        val status: String,
        @SerialName("payment_id")
        val paymentId: String,
        val reason: String,
        @SerialName("livemode")
        val liveMode: Boolean,
        @SerialName("created_at")
        val createdAt: Long,
        @SerialName("updated_at")
        val updatedAt: Long
    )
}
