package io.github.ronjunevaldoz.paymongo.models.resource

import io.github.ronjunevaldoz.paymongo.models.serializers.PaymentTypeSerializer
import kotlinx.serialization.Serializable

@Serializable(with = PaymentTypeSerializer::class)
enum class PaymentType(val value: String) {
    Atome("atome"),
    Card("card"),
    Dob("dob"),
    Maya("maya"),
    GCash("gcash"),
    GrabPay("grab_pay"),
    Billease("billease"),
    PayMaya("paymaya")
}