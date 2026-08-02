package io.github.ronjunevaldoz.paymongo.models.resource

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Same flat convention as [PaymentLink] -- no `data.attributes` wrapping, confirmed live
 * for the request body. The response shape below was not observed from a successful live
 * refund (the sandbox call returned a business-rule 500 on an already-settled test payment),
 * but follows the same flat family convention as its sibling endpoints.
 */
@Serializable
data class CreateRefundInput(
    val amount: Double,
    @SerialName("payment_id")
    val paymentId: String,
    val reason: String,
    val metadata: Map<String, String>? = null
)

@Serializable
data class RefundResponse(
    val data: Refund
)

@Serializable
data class Refund(
    val id: String,
    val amount: Int,
    val currency: String,
    val status: String,
    @SerialName("payment_id")
    val paymentId: String,
    val reason: String,
    @SerialName("livemode")
    val liveMode: Boolean,
    @SerialName("created_at")
    val createdAt: String,
    @SerialName("updated_at")
    val updatedAt: String
)
