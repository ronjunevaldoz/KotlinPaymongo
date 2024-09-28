package io.github.ronjunevaldoz.paymongo.models.resource

import io.github.ronjunevaldoz.paymongo.models.serializers.PaymentStatusSerializer
import kotlinx.serialization.Serializable

@Serializable(with = PaymentStatusSerializer::class)
enum class PaymentStatus(val value: String) {
    // Payment intent common
    AwaitingPaymentMethod("awaiting_payment_method"),
    AwaitingNextAction("awaiting_next_action"),
    Processing("processing"),
    Succeed("succeeded"),
    // Payments
    Pending("pending"),
    Failed("failed"),
    Paid("paid"),
    Cancelled("cancelled"),
    Chargeable("chargeable"),
    Expired("expired"),
    // Checkout session common
    Active("active"),
}