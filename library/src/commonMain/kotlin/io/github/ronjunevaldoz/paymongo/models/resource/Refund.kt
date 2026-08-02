package io.github.ronjunevaldoz.paymongo.models.resource

import io.github.ronjunevaldoz.paymongo.models.Amount
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Same flat convention as [PaymentLink] -- no `data.attributes` wrapping, confirmed live
 * for the request body. The response shape below was not observed from a successful live
 * refund (the sandbox call returned a business-rule 500 on an already-settled test payment),
 * but follows the same flat family convention as its sibling endpoints. `amount` is modeled
 * as the same integer-minor-unit [Amount] every other endpoint uses; a docs summary once
 * described this specific field as a decimal major-unit value, but that same summarization
 * pass produced a confirmed-wrong PaymentLink shape elsewhere, so it's not trusted here --
 * this follows the proven, live-verified convention instead. Confirm against a real refund
 * response before relying on this in production.
 */
@Serializable
data class CreateRefundInput(
    val amount: Amount,
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
    val amount: Amount,
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
